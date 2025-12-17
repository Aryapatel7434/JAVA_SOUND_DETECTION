import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * STUDENT 2 WORK:
 * Handles all networking logic.
 * Connects to OpenRouter/Grok API.
 * Parses the manual JSON response.
 */
public class MusicAPIService {

    // NOTE: Keep your key secure. 
    private static final String API_KEY = "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";
    private static final String TARGET_URL = "https://openrouter.ai/api/v1/chat/completions";

    public List<String> fetchRecommendations(String artist, String mood) throws Exception {
        String prompt = "List 10 songs by " + artist + " (only this artist songs should be given in output) suitable for a " + mood + " mood. Output format: [Song1, Song2, ...]. Return only the array string, no other text.";
        // Escape quotes to prevent JSON breakage
        prompt = prompt.replace("\"", "\\\"");

        String jsonPayload = "{"
                + "\"model\": \"x-ai/grok-4.1-fast\","
                + "\"messages\": ["
                + "  {\"role\": \"user\", \"content\": \"" + prompt + "\"}"
                + "]"
                + "}";

        URL url = new URL(TARGET_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int status = conn.getResponseCode();
        BufferedReader br;
        if (status >= 200 && status < 300) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        } else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
        }

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line.trim());
        }
        br.close();

        if (status != 200) {
            throw new Exception("API Error: " + status);
        }

        return parseCustomResponse(response.toString());
    }

    // Manual Parsing Logic (extracted for clarity)
    private List<String> parseCustomResponse(String jsonResponse) {
        List<String> result = new ArrayList<>();
        try {
            // Find content inside the JSON structure
            String marker = "\"content\":";
            int contentIndex = jsonResponse.indexOf(marker);
            if (contentIndex == -1) return result;

            int startIndex = jsonResponse.indexOf("\"", contentIndex + marker.length()) + 1;
            
            // Extract the actual message string
            StringBuilder contentBuilder = new StringBuilder();
            boolean isEscaped = false;
            // A simple state machine to read the string until the closing quote
            for (int i = startIndex; i < jsonResponse.length(); i++) {
                char c = jsonResponse.charAt(i);
                if (isEscaped) {
                    contentBuilder.append(c);
                    isEscaped = false;
                } else {
                    if (c == '\\') {
                        isEscaped = true;
                    } else if (c == '"') {
                        break; // End of content string
                    } else {
                        contentBuilder.append(c);
                    }
                }
            }
            
            String rawText = contentBuilder.toString();
            // Cleanup brackets and split by comma
            rawText = rawText.replace("[", "").replace("]", "");
            String[] items = rawText.split(",");
            
            for (String item : items) {
                String cleanItem = item.trim().replace("\"", "").replace("'", "");
                if (!cleanItem.isEmpty()) {
                    result.add(cleanItem);
                }
            }
        } catch (Exception e) {
            result.add("Error parsing API response");
        }
        return result;
    }
}
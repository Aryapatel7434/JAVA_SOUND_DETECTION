import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * STUDENT 3 WORK:
 * Handles Data Persistence.
 * Saves and Loads Favorites and History to local text files.
 * This simulates a database connection.
 */
public class DataManager {

    private static final String HISTORY_FILE = "history.txt";
    private static final String FAVORITES_FILE = "favorites.txt";

    public static void saveHistory(String searchLog) {
        try (FileWriter fw = new FileWriter(HISTORY_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(searchLog);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> loadHistory() {
        return loadFile(HISTORY_FILE);
    }

    public static void saveFavorite(String song) {
        // Prevent duplicates
        List<String> current = loadFavorites();
        if(current.contains(song)) return;

        try (FileWriter fw = new FileWriter(FAVORITES_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(song);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> loadFavorites() {
        return loadFile(FAVORITES_FILE);
    }

    private static List<String> loadFile(String filename) {
        List<String> lines = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) return lines;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
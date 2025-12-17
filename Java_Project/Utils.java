import javax.swing.*;
import java.awt.*;
import java.net.URI;

/**
 * STUDENT 6 WORK:
 * General Utility functions used across the app.
 * Handles styling constants (Colors, Fonts) and Browser opening logic.
 */
public class Utils {

    // Global Styles for consistency
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Color DARK_BG = new Color(40, 44, 52);
    public static final Color LIGHT_BG = new Color(240, 240, 245);
    public static final Color ACCENT_COLOR = new Color(65, 105, 225); // Royal Blue

    public static void styleButton(JButton btn) {
        btn.setBackground(ACCENT_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    // Function to open browser
    public static void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URI(urlString));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Could not open browser.");
        }
    }
}
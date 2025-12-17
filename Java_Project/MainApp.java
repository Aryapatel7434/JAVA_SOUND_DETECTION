import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * STUDENT 1 WORK:
 * Entry point of the application.
 * Handles the Login Screen and transitions to the Main Dashboard.
 */
public class MainApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DashboardUI dashboard;

    public MainApp() {
        setTitle("SurSagar");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add Login Panel
        mainPanel.add(createLoginPanel(), "LOGIN");

        // Add Dashboard Panel (Lazy loaded usually, but initialized here for simplicity)
        dashboard = new DashboardUI(this);
        mainPanel.add(dashboard, "DASHBOARD");

        add(mainPanel);
        
        // Start at Login
        cardLayout.show(mainPanel, "LOGIN");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Utils.DARK_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("SurSagar Login");
        title.setFont(Utils.HEADER_FONT);
        title.setForeground(Color.WHITE);

        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JButton loginBtn = new JButton("Login");
        Utils.styleButton(loginBtn);

        // Layout
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel uLabel = new JLabel("Username:");
        uLabel.setForeground(Color.WHITE);
        panel.add(uLabel, gbc);
        gbc.gridx = 1;
        panel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel pLabel = new JLabel("Password:");
        pLabel.setForeground(Color.WHITE);
        panel.add(pLabel, gbc);
        gbc.gridx = 1;
        panel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);

        // Login Logic (Simple simulation)
        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            // In a real app, check DB. Here we simulate.
            if (!user.isEmpty() && !pass.isEmpty()) {
                dashboard.setCurrentUser(user);
                cardLayout.show(mainPanel, "DASHBOARD");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter any username/password");
            }
        });

        return panel;
    }

    public void logout() {
        cardLayout.show(mainPanel, "LOGIN");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
    }
}
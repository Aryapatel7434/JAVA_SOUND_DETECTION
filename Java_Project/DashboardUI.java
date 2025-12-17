import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * STUDENT 4 WORK:
 * The Main UI Container.
 * Uses JTabbedPane to separate Search, Favorites, and History.
 * Coordinates between UI and Backend Services.
 */
public class DashboardUI extends JPanel {

    private MainApp mainApp;
    private MusicAPIService musicService;
    private JTextField artistField;
    private JComboBox<String> moodComboBox;
    private JPanel resultsContainer; // Holds the result blocks
    private DefaultListModel<String> historyModel;
    private DefaultListModel<String> favModel;
    private String currentUser;

    public DashboardUI(MainApp app) {
        this.mainApp = app;
        this.musicService = new MusicAPIService();
        setLayout(new BorderLayout());

        // Navigation Bar (Tabs)
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Utils.NORMAL_FONT);

        // 1. Search Panel
        tabbedPane.addTab("🎵 Search Music", createSearchPanel());

        // 2. Favorites Panel
        tabbedPane.addTab("❤️ Favorites", createFavoritesPanel());

        // 3. History Panel
        tabbedPane.addTab("🕒 History", createHistoryPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Logout Button at bottom
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(Color.RED);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.addActionListener(e -> mainApp.logout());
        add(logoutBtn, BorderLayout.SOUTH);
    }

    public void setCurrentUser(String user) {
        this.currentUser = user;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Input Area
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBackground(Utils.LIGHT_BG);
        
        artistField = new JTextField(15);
        String[] moods = {"Happy", "Sad", "Energetic", "Relaxed", "Study", "Workout"};
        moodComboBox = new JComboBox<>(moods);
        JButton searchBtn = new JButton("Get Recommendations");
        Utils.styleButton(searchBtn);

        inputPanel.add(new JLabel("Artist:"));
        inputPanel.add(artistField);
        inputPanel.add(new JLabel("Mood:"));
        inputPanel.add(moodComboBox);
        inputPanel.add(searchBtn);

        // Results Area (Scrollable)
        resultsContainer = new JPanel();
        resultsContainer.setLayout(new BoxLayout(resultsContainer, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultsContainer);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Search Action
        searchBtn.addActionListener(e -> performSearch());

        return panel;
    }

    private JPanel createFavoritesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        favModel = new DefaultListModel<>();
        JList<String> favList = new JList<>(favModel);
        favList.setFont(Utils.NORMAL_FONT);
        
        JButton refreshBtn = new JButton("Refresh Favorites");
        refreshBtn.addActionListener(e -> loadFavorites());

        panel.add(new JScrollPane(favList), BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        historyModel = new DefaultListModel<>();
        JList<String> histList = new JList<>(historyModel);
        
        JButton refreshBtn = new JButton("Refresh History");
        refreshBtn.addActionListener(e -> loadHistory());

        panel.add(new JScrollPane(histList), BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);
        return panel;
    }

    // Logic to call API and Update UI
    private void performSearch() {
        String artist = artistField.getText();
        String mood = (String) moodComboBox.getSelectedItem();

        if (artist.isEmpty()) return;

        resultsContainer.removeAll();
        resultsContainer.add(new JLabel("Loading..."));
        resultsContainer.revalidate();
        resultsContainer.repaint();

        // Save to History
        DataManager.saveHistory(currentUser + " searched: " + artist + " (" + mood + ")");

        SwingWorker<List<String>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<String> doInBackground() throws Exception {
                return musicService.fetchRecommendations(artist, mood);
            }

            @Override
            protected void done() {
                resultsContainer.removeAll();
                try {
                    List<String> songs = get();
                    for (String song : songs) {
                        // Use the Custom Component created by Student 5
                        resultsContainer.add(new ResultPanel(song));
                        resultsContainer.add(Box.createRigidArea(new Dimension(0, 5))); // Spacer
                    }
                } catch (Exception ex) {
                    resultsContainer.add(new JLabel("Error: " + ex.getMessage()));
                }
                resultsContainer.revalidate();
                resultsContainer.repaint();
            }
        };
        worker.execute();
    }

    private void loadFavorites() {
        favModel.clear();
        List<String> favs = DataManager.loadFavorites();
        for(String s : favs) favModel.addElement(s);
    }

    private void loadHistory() {
        historyModel.clear();
        List<String> hist = DataManager.loadHistory();
        for(String s : hist) historyModel.addElement(s);
    }
}
import javax.swing.*;
import java.awt.*;

/**
 * STUDENT 5 WORK:
 * A custom UI component representing a single song result.
 * Includes buttons to "Like" or "Open in YouTube".
 */
public class ResultPanel extends JPanel {

    public ResultPanel(String songName) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setBackground(Color.WHITE);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Full width, fixed height

        JLabel nameLabel = new JLabel("🎵 " + songName);
        nameLabel.setFont(Utils.NORMAL_FONT);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton favBtn = new JButton("❤️");
        favBtn.setToolTipText("Add to Favorites");
        favBtn.setBackground(Color.PINK);
        
        JButton playBtn = new JButton("▶");
        playBtn.setToolTipText("Find on YouTube");
        playBtn.setBackground(new Color(200, 255, 200));

        buttonPanel.add(favBtn);
        buttonPanel.add(playBtn);

        add(nameLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);

        // Action Listeners
        favBtn.addActionListener(e -> {
            DataManager.saveFavorite(songName);
            JOptionPane.showMessageDialog(this, "Added to Favorites!");
        });

        playBtn.addActionListener(e -> {
            Utils.openWebpage("https://www.youtube.com/results?search_query=" + songName.replace(" ", "+"));
        });
    }
}
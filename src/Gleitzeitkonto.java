import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Gleitzeitkonto extends JFrame {

    private Font customFont;
    private JLabel stundenLabel;
    private JLabel imageLabel;
    private int stunden; // Aktuelle Stunden
    private int minuten; // Aktuelle Minuten
    private ResourceBundle bundle; // ResourceBundle für die Mehrsprachigkeit

    public Gleitzeitkonto(Locale locale) {
        setTitle("Gleitzeitkonto");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Fenster zentrieren

        // Benutzerdefinierte Schriftart laden
        loadCustomFont();

        // ResourceBundle laden
        loadBundle(locale); // Lade das ResourceBundle hier

        // Hintergrundpanel mit Bild
        JPanel backgroundPanel = createBackgroundPanel();
        setContentPane(backgroundPanel);
        backgroundPanel.setLayout(new BorderLayout());

        // Hauptinhalt hinzufügen
        addMainContent(backgroundPanel);

        setVisible(true);
    }

    private void loadBundle(Locale locale) {
        bundle = ResourceBundle.getBundle("ressourcen/messages", locale); // Stellen Sie sicher, dass der Pfad zu den Ressourcen korrekt ist.
    }

    private void loadCustomFont() {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/ressourcen/KGDoYouLoveMe.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            System.err.println("Fehler beim Laden der Schriftart: " + e.getMessage());
            customFont = new Font("Arial", Font.PLAIN, 16); // Fallback-Schriftart
        }
    }

    private JPanel createBackgroundPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("src/ressourcen/hintergrundBBQ-3.jpg");
                Image img = icon.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
    }

    private void addMainContent(JPanel backgroundPanel) {
        // Label für die Stunden
        stundenLabel = new JLabel(String.format("+ %02d:%02d", stunden, minuten), SwingConstants.CENTER);
        stundenLabel.setFont(customFont.deriveFont(45f));
        stundenLabel.setForeground(Color.WHITE);
        stundenLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Label für das Bild
        imageLabel = new JLabel();
        updateImage(); // Bild anfangs setzen

        // Panel für Inhalt
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false); // Setze das Panel auf transparent
        contentPanel.add(stundenLabel);
        contentPanel.add(imageLabel);

        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        // Button zum Aktualisieren der Stunden
        JButton updateButton = new JButton(bundle.getString("button.update")); // "Stunden aktualisieren"
        updateButton.setFont(customFont.deriveFont(20f));
        updateButton.addActionListener(e -> updateHours());

        // Füge den Button zum Hintergrund-Panel hinzu
        backgroundPanel.add(updateButton, BorderLayout.SOUTH);
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(0, 80, 10, 80));
    }

    private void updateImage() {
        String imagePath;
        if (stunden < 4) {
            imagePath = "src/ressourcen/roteAmpel.png";
        } else if (stunden < 8) {
            imagePath = "src/ressourcen/gelbeAmpel.png";
        } else {
            imagePath = "src/ressourcen/grüneAmpel-2.png";
        }
        ImageIcon icon = new ImageIcon(imagePath);
        imageLabel.setIcon(icon);
    }

    // Methode zum Aktualisieren der Stunden
    private void updateHours() {
        // Stunden und Minuten aus der Dummy-Methode abrufen
        int[] timeFromDatabase = getHoursFromDatabase();
        stunden = timeFromDatabase[0];
        minuten = timeFromDatabase[1];

        // Update das Bild nach Zeitänderung
        SwingUtilities.invokeLater(this::updateImage);
        // Update die Stundenanzeige
        SwingUtilities.invokeLater(() -> stundenLabel.setText(String.format("+ %02d:%02d", stunden, minuten)));
    }

    // Dummy-Methode zur Simulation des Abrufs von Stunden und Minuten aus einer Datenbank
    private int[] getHoursFromDatabase() {
        // Aktuell simulieren wir die Rückgabe von zufälligen Stunden und Minuten.
        stunden = (int) (Math.random() * 10);
        minuten = (int) (Math.random() * 60);
        return new int[]{stunden, minuten};
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Gleitzeitkonto(new Locale("de"))); // Standardmäßig Deutsch
    }
}

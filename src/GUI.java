import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;

public class GUI extends JFrame {
    private JButton kommenButton;
    private JButton gehenButton;
    private JLabel gearbeiteteStunden;
    private JButton benutzer;
    private Font customFont;
    private JButton gleitzeitkonto;
    private ResourceBundle bundle;
    private Locale currentLocale;
    private Timer timer; // Timer für den Countdown
    private int elapsedSeconds = 0; // Zeit in Sekunden
    private JLabel countdown; // Countdown-Label
    private String email;

    private Datenbank datenbank = new Datenbank();
    private Arbeitszeitgesetz arbeitszeitgesetz = new Arbeitszeitgesetz();
    private LocalTime startTime; // Startzeit der Arbeitszeit

    public GUI(Locale locale, String email) {
        this.email = email;
        this.currentLocale = locale;
        loadBundle(currentLocale);

        // GUI-Setup
        setTitle(bundle.getString("title"));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Font-Setup
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/ressourcen/KGDoYouLoveMe.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.PLAIN, 16);
        }

        // Hintergrundbild-Panel
        Hintergrund backgroundPanel = new Hintergrund("/ressourcen/hintergrundBBQ-3.jpg");
        setContentPane(backgroundPanel);
        backgroundPanel.setLayout(new BorderLayout()); // Layout für das Hintergrundpanel setzen

        // UI-Komponenten erstellen
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false); // Panel transparent machen
        topPanel.setBorder(BorderFactory.createEmptyBorder(35, 10, 10, 10));

        JPanel arbeitszeitPanel = new JPanel(new BorderLayout());
        arbeitszeitPanel.setOpaque(false); // Panel transparent machen
        arbeitszeitPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        gearbeiteteStunden = new JLabel(bundle.getString("worked.hours"), SwingConstants.CENTER);
        gearbeiteteStunden.setFont(customFont.deriveFont(40f));
        gearbeiteteStunden.setForeground(Color.WHITE);
        arbeitszeitPanel.add(gearbeiteteStunden, BorderLayout.NORTH);

        // Countdown Label
        countdown = new JLabel("00:00:00", SwingConstants.CENTER);
        countdown.setFont(new Font("Abadi MS", Font.PLAIN, 45));
        countdown.setForeground(Color.white);
        arbeitszeitPanel.add(countdown, BorderLayout.CENTER);
        topPanel.add(arbeitszeitPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false); // Panel transparent machen

        // Kommen-Button
        kommenButton = new JButton(bundle.getString("button.come"));
        kommenButton.setFont(customFont.deriveFont(20f));
        buttonPanel.add(kommenButton);

        // Gehen-Button
        gehenButton = new JButton(bundle.getString("button.go"));
        gehenButton.setFont(customFont.deriveFont(20f));
        buttonPanel.add(gehenButton);

        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Gleitzeitkonto Panel
        JPanel gleitzeitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        gleitzeitPanel.setOpaque(false); // Panel transparent machen
        gleitzeitPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 270, 0));
        gleitzeitkonto = new JButton(bundle.getString("button.flexitime"));
        gleitzeitkonto.setFont(customFont.deriveFont(25f));
        gleitzeitkonto.addActionListener(e -> new Gleitzeitkonto(currentLocale, email));
        gleitzeitPanel.add(gleitzeitkonto);
        add(gleitzeitPanel, BorderLayout.SOUTH);

        // Benutzer-Button
        ImageIcon benutzerIcon = new ImageIcon(getClass().getResource("/ressourcen/userIcon-2.png"));
        Image image = benutzerIcon.getImage();
        Image scaledImage = image.getScaledInstance(130, 130, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        benutzer = new JButton(scaledIcon);

        JButton deutsch = createFlagButton("/ressourcen/deutscheFlagge.png", new Locale("de", "DE"));
        JButton english = createFlagButton("/ressourcen/UK-Flagge.png", new Locale("en", "UK"));

        // Panel für den Benutzer-Button
        JPanel buttonPanelRechtsOben = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanelRechtsOben.setBorder(BorderFactory.createEmptyBorder(48, 30, 0, 83));
        buttonPanelRechtsOben.add(benutzer);
        buttonPanelRechtsOben.setOpaque(false); // Panel transparent machen
        buttonPanelRechtsOben.add(deutsch);
        buttonPanelRechtsOben.add(english);

        benutzer.addActionListener(e -> new BenutzerMenu(currentLocale, email));

        topPanel.add(buttonPanelRechtsOben, BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        // Timer konfigurieren
        timer = new Timer(1000, e -> {
            elapsedSeconds++;
            updateCountdownLabel();
        });

        // ActionListener für den Kommen-Button (Arbeitsbeginn speichern)
        kommenButton.addActionListener(e -> {
            if (!arbeitszeitgesetz.pruefeKommen()){
                JOptionPane.showMessageDialog(this, "Sie können nicht kommen, weil Sie Sich außerhalb der gesetzlichen Arbeitszeiten befinden!");
                return;
            }
            startTime = LocalTime.now(); // Arbeitsbeginn speichern
            timer.start();
            try {
                datenbank.starten();
                datenbank.mitarbeiterKommt(email); // Arbeitsbeginn speichern
                datenbank.schliessen();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Arbeitszeit: " + ex.getMessage());
            }
        });

        // ActionListener für den Gehen-Button (Arbeitsende speichern)
        gehenButton.addActionListener(e -> {
            if (!arbeitszeitgesetz.pruefeGehen(email)){
                JOptionPane.showMessageDialog(this, "Sie können nicht gehen, weil Sie sich heute noch nicht eingestempelt haben!");
                return;
            }
            timer.stop();
            LocalTime endTime = LocalTime.now(); // Arbeitsende speichern
            int totalSeconds = elapsedSeconds;

            // Pausenzeiten abziehen
            int pauseDuration = calculatePauseDuration(totalSeconds);
            totalSeconds -= pauseDuration;

            try {
                datenbank.starten();
                datenbank.mitarbeiterGeht(email); // Arbeitsende und gesamte Arbeitszeit speichern
                datenbank.schliessen();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Arbeitszeit: " + ex.getMessage());
            }

            elapsedSeconds = 0; // Zurücksetzen des Timers
            updateCountdownLabel(); // Countdown-Label zurücksetzen
        });

        setVisible(true);
    }

    // Methode, um das Countdown-Label zu aktualisieren
    private void updateCountdownLabel() {
        int hours = elapsedSeconds / 3600;
        int minutes = (elapsedSeconds % 3600) / 60;
        int seconds = elapsedSeconds % 60;
        countdown.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    // Berechnung der Pausenzeiten
    private int calculatePauseDuration(int totalSeconds) {
        if (totalSeconds > 9 * 3600) {
            return 45 * 60; // 45 Minuten Pause abziehen
        } else if (totalSeconds > 6 * 3600) {
            return 30 * 60; // 30 Minuten Pause abziehen
        }
        return 0; // Keine Pausenzeiten abziehen
    }

    private JButton createFlagButton(String path, Locale locale) {
        JButton button = new JButton();
        try {
            Image img = ImageIO.read(getClass().getResourceAsStream(path));
            Image scaledImg = img.getScaledInstance(32, 19, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImg));
        } catch (IOException e) {
            System.err.println("Fehler beim Laden des Bildes: " + path);
        }
        button.setPreferredSize(new Dimension(32, 19));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        // ActionListener für Sprachwechsel
        button.addActionListener(e -> {
            currentLocale = locale;
            updateLabels();
        });

        return button;
    }

    // ResourceBundle basierend auf Locale laden
    private void loadBundle(Locale locale) {
        bundle = ResourceBundle.getBundle("ressourcen.messages", locale);
    }

    // Methode, um die Beschriftungen zu aktualisieren
    private void updateLabels() {
        loadBundle(currentLocale);
        kommenButton.setText(bundle.getString("button.come"));
        gehenButton.setText(bundle.getString("button.go"));
        gearbeiteteStunden.setText(bundle.getString("worked.hours"));
        gleitzeitkonto.setText(bundle.getString("button.flexitime"));
        setTitle(bundle.getString("title"));
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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

    Datenbank datenbank = new Datenbank();
    Benutzer klasseBenutzer = new Benutzer();
    Arbeitszeitgesetz arbeitszeitgesetz = new Arbeitszeitgesetz(klasseBenutzer);

    public GUI(Locale locale, String email) {
        this.email = email;
        this.currentLocale = locale;
        loadBundle(currentLocale);

        setTitle(bundle.getString("title"));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/ressourcen/KGDoYouLoveMe.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.PLAIN, 16);
        }

        // Hintergrundbild-Panel
        Hintergrund backgroundPanel = new Hintergrund("src/ressourcen/hintergrundBBQ-3.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(35, 10, 10, 10));

        JPanel arbeitszeitPanel = new JPanel(new BorderLayout());
        arbeitszeitPanel.setOpaque(false);
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
        buttonPanel.setOpaque(false);

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
        gleitzeitPanel.setOpaque(false);
        gleitzeitPanel.setBorder(BorderFactory.createEmptyBorder(0,0,270,0));
        gleitzeitkonto = new JButton(bundle.getString("button.flexitime"));
        gleitzeitkonto.setFont(customFont.deriveFont(25f));
        gleitzeitkonto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Gleitzeitkonto(currentLocale);
            }
        });
        gleitzeitPanel.add(gleitzeitkonto);
        add(gleitzeitPanel, BorderLayout.SOUTH);

        // Benutzer-Button
        ImageIcon benutzerIcon = new ImageIcon("src/ressourcen/userIcon-2.png");
        Image image = benutzerIcon.getImage();
        Image scaledImage = image.getScaledInstance(130, 130, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        benutzer = new JButton(scaledIcon);

        JButton deutsch = createFlagButton("src/ressourcen/deutscheFlagge.png", new Locale("de", "DE"));
        JButton english = createFlagButton("src/ressourcen/UK-Flagge.png", new Locale("en", "UK"));

        // Panel für den Benutzer-Button
        JPanel buttonPanelRechtsOben = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanelRechtsOben.setBorder(BorderFactory.createEmptyBorder(48, 30, 0, 83));
        buttonPanelRechtsOben.add(benutzer);
        buttonPanelRechtsOben.setOpaque(false);
        buttonPanelRechtsOben.add(deutsch);
        buttonPanelRechtsOben.add(english);

        /*benutzer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Benutzer-Menü öffnen
                new benutzerMenu(currentLocale);
            }
        });

         */

        topPanel.add(buttonPanelRechtsOben, BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        // Timer konfigurieren
        timer = new Timer(1000, new ActionListener() { // 1000 ms = 1 Sekunde
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedSeconds++;
                updateCountdownLabel();
            }
        });

        // ActionListener für den Kommen-Button (Timer starten)
        kommenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.start();
            }
        });

        // ActionListener für den Gehen-Button (Timer stoppen)
        gehenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
            }
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

    private JButton createFlagButton(String path, Locale locale) {
        JButton button = new JButton();
        try {
            Image img = ImageIO.read(new File(path));
            Image scaledImg = img.getScaledInstance(32, 19, Image.SCALE_SMOOTH); // Größe anpassen
            button.setIcon(new ImageIcon(scaledImg));
        } catch (IOException e) {
            System.err.println("Fehler beim Laden des Bildes: " + path);
        }
        button.setPreferredSize(new Dimension(32, 19)); // Breite und Höhe der Flaggen in Pixel
        button.setContentAreaFilled(false); // Hintergrund des Buttons transparent machen
        button.setBorderPainted(false); // Rahmen des Buttons entfernen

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

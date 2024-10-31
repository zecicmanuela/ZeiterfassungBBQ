import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Gleitzeitkonto extends JFrame {

    private enum Zeitraum {WOCHE, MONAT, JAHR}

    private Zeitraum aktuellerZeitraum = Zeitraum.WOCHE;
    private Font customFont;
    private JLabel stundenLabel;
    private JLabel imageLabel;
    private int stunden; // Aktuelle Stunden
    private int minuten; // Aktuelle Minuten
    private ResourceBundle bundle;
    private Datenbank datenbank = new Datenbank();
    private String email;

    public Gleitzeitkonto(Locale locale, String email) {
        this.email = email;
        setTitle("Gleitzeitkonto");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        loadCustomFont();
        loadBundle(locale);

        JPanel backgroundPanel = createBackgroundPanel();
        setContentPane(backgroundPanel);
        backgroundPanel.setLayout(new BorderLayout());

        addMainContent(backgroundPanel);

        setVisible(true);
    }

    private void loadBundle(Locale locale) {
        bundle = ResourceBundle.getBundle("ressourcen/messages", locale);
    }

    private void loadCustomFont() {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/ressourcen/KGDoYouLoveMe.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            System.err.println("Fehler beim Laden der Schriftart: " + e.getMessage());
            customFont = new Font("Arial", Font.PLAIN, 16);
        }
    }

    private JPanel createBackgroundPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon(getClass().getResource("/ressourcen/hintergrundBBQ-3.jpg"));
                Image img = icon.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
    }

    private void addMainContent(JPanel backgroundPanel) {
        stundenLabel = new JLabel(String.format(String.valueOf(stunden), minuten), SwingConstants.CENTER);
        stundenLabel.setFont(customFont.deriveFont(45f));
        stundenLabel.setForeground(Color.WHITE);
        stundenLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        imageLabel = new JLabel();

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.add(stundenLabel);
        contentPanel.add(imageLabel);

        // RadioButtons für "Woche", "Monat" und "Jahr"
        JRadioButton wocheRadioButton = new JRadioButton(bundle.getString("radio.week"));
        JRadioButton monatRadioButton = new JRadioButton(bundle.getString("radio.month"));
        JRadioButton jahrRadioButton = new JRadioButton(bundle.getString("radio.year"));

        wocheRadioButton.setFont(customFont.deriveFont(21f));
        wocheRadioButton.setForeground(Color.WHITE);
        monatRadioButton.setFont(customFont.deriveFont(21f));
        monatRadioButton.setForeground(Color.WHITE);
        jahrRadioButton.setFont(customFont.deriveFont(21f));
        jahrRadioButton.setForeground(Color.WHITE);

        // ActionListener für die RadioButtons hinzufügen
        wocheRadioButton.addActionListener(e -> {
            aktuellerZeitraum = Zeitraum.WOCHE;
            updateHours();
        });
        monatRadioButton.addActionListener(e -> {
            aktuellerZeitraum = Zeitraum.MONAT;
            updateHours();
        });
        jahrRadioButton.addActionListener(e -> {
            aktuellerZeitraum = Zeitraum.JAHR;
            updateHours();
        });

        ButtonGroup zeitraumGroup = new ButtonGroup();
        zeitraumGroup.add(wocheRadioButton);
        zeitraumGroup.add(monatRadioButton);
        zeitraumGroup.add(jahrRadioButton);

        wocheRadioButton.setSelected(true); // Standardwert

        JPanel radioPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        radioPanel.setOpaque(false);
        radioPanel.add(wocheRadioButton);
        radioPanel.add(monatRadioButton);
        radioPanel.add(jahrRadioButton);

        backgroundPanel.add(radioPanel, BorderLayout.NORTH);
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        JButton updateButton = new JButton(bundle.getString("button.update"));
        updateButton.setFont(customFont.deriveFont(20f));
        updateButton.addActionListener(e -> updateHours());

        backgroundPanel.add(updateButton, BorderLayout.SOUTH);
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(0, 80, 10, 80));
    }


    private void updateHours() {
        double gleitzeit = 0;
        int mitarbeiterID;

        try {
            datenbank.starten();
            mitarbeiterID = datenbank.findeMitarbeiterID(email);

            // Gleitzeit abrufen basierend auf dem ausgewählten Zeitraum
            switch (aktuellerZeitraum) {
                case WOCHE:
                    gleitzeit = datenbank.getGleitzeitWoche(mitarbeiterID);
                    break;
                case MONAT:
                    gleitzeit = datenbank.getGleitzeitMonat(mitarbeiterID);
                    break;
                case JAHR:
                    gleitzeit = datenbank.getGleitzeitJahr(mitarbeiterID);
                    break;
            }

            datenbank.schliessen();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Berechnung der Stunden und Minuten aus der Gleitzeit
        stunden = (int) Math.abs(gleitzeit);
        minuten = (int) ((Math.abs(gleitzeit) - stunden) * 60);
        String vorzeichen = gleitzeit >= 0 ? "+" : "-";

        SwingUtilities.invokeLater(() -> stundenLabel.setText(String.format("%s %02d:%02d", vorzeichen, stunden, minuten)));

        // Übergabe der tatsächlichen Gleitzeit an updateImage für Ampel-Logik
        final double finalGleitzeit = gleitzeit; // Machen Sie gleitzeit final
        SwingUtilities.invokeLater(() -> updateImage(finalGleitzeit));
    }

    private void updateImage(double gleitzeit) {
        String imagePath;
        gleitzeit = Math.abs(gleitzeit);
        double gleitzeitwarnung;
        try {
            datenbank.starten();
            gleitzeitwarnung = datenbank.getGleitzeitwarnung(email);
            datenbank.schliessen();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Ampel-Bedingungen für Gleitzeit
        if (gleitzeit> (gleitzeitwarnung*1.5)) { // Rot bei mehr als 4 Stunden
            imagePath = "/ressourcen/roteAmpel.png";
        } else if (gleitzeit > gleitzeitwarnung) { // Gelb bei 0 bis -4 Stunden
            imagePath = "/ressourcen/gelbeAmpel.png";
        } else { // Grün für positive Werte über 0 Stunden
            imagePath = "/ressourcen/grüneAmpel-2.png";
        }

        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        imageLabel.setIcon(icon);
    }



}

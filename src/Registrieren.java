import javax.swing.*;
import java.awt.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;
public class Registrieren extends JFrame {

    private JTextField vornameField;
    private JTextField nameField = new JTextField(20);
    private JTextField emailField = new JTextField(20);
    private JPasswordField passwortField = new JPasswordField(20);
    private JPasswordField passwortBestätigung = new JPasswordField(20);
    private JComboBox<String> zeitmodellComboBox = new JComboBox<>();
    private JComboBox<String> sicherheitsfragenComboBox = new JComboBox<>();
    private JTextField antwortFeld = new JTextField();
    Datenbank datenbank = new Datenbank();
    private final Font customFont;
    private final ResourceBundle messages;

    public Registrieren(Font customFont) {
        this.customFont = customFont;
        this.messages = ResourceBundle.getBundle("ressourcen.messages", Locale.getDefault()); // Sprachbundle laden
        setTitle(messages.getString("register.title"));
        setSize(900, 800);
        setLayout(new BorderLayout());

        JPanel backgroundPanel = createBackgroundPanel();
        setupComponents(backgroundPanel);

        add(backgroundPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
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

    private void setupComponents(JPanel backgroundPanel) {
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        createInputFields(backgroundPanel, gbc);
        createSpeichernButton(backgroundPanel, gbc);
    }

    private void createInputFields(JPanel backgroundPanel, GridBagConstraints gbc) {
        JLabel nameLabel = new JLabel(messages.getString("register.name"));
        nameLabel.setFont(customFont);
        nameLabel.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        backgroundPanel.add(nameLabel, gbc);

        nameField.setFont(customFont);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        backgroundPanel.add(nameField, gbc);

        JLabel vornameLabel = new JLabel(messages.getString("register.vorname"));
        vornameLabel.setFont(customFont);
        vornameLabel.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 1;
        backgroundPanel.add(vornameLabel, gbc);

        vornameField = new JTextField(20);
        vornameField.setFont(customFont);
        gbc.gridx = 1;
        gbc.gridy = 1;
        backgroundPanel.add(vornameField, gbc);

        JLabel emailLabel = new JLabel("E-Mail");
        emailLabel.setFont(customFont);
        emailLabel.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 2;
        backgroundPanel.add(emailLabel, gbc);


        emailField.setFont(customFont);
        gbc.gridx = 1;
        gbc.gridy = 2;
        backgroundPanel.add(emailField, gbc);

        JLabel geburtsdatumLabel = new JLabel(messages.getString("register.geburtsdatum"));
        geburtsdatumLabel.setFont(customFont);
        geburtsdatumLabel.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 3;
        backgroundPanel.add(geburtsdatumLabel, gbc);

        JTextField geburtsdatumField = new JTextField(20);
        geburtsdatumField.setFont(customFont);
        gbc.gridx = 1;
        gbc.gridy = 3;
        backgroundPanel.add(geburtsdatumField, gbc);

        JLabel zeitmodellLabel = new JLabel(messages.getString("register.zeitmodell"));
        zeitmodellLabel.setFont(customFont);
        zeitmodellLabel.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 4;
        backgroundPanel.add(zeitmodellLabel, gbc);

        String[] zeitmodelle = {messages.getString("zeitmodell.vollzeit"), messages.getString("zeitmodell.teilzeit"), messages.getString("zeitmodell.minijob")};
        JComboBox<String> zeitmodellComboBox = new JComboBox<>(zeitmodelle);
        zeitmodellComboBox.setFont(customFont);
        gbc.gridx = 1;
        gbc.gridy = 4;
        backgroundPanel.add(zeitmodellComboBox, gbc);

        JLabel behinderungsgradLabel = new JLabel(messages.getString("register.behinderungsgrad"));
        behinderungsgradLabel.setFont(customFont);
        behinderungsgradLabel.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 5;
        backgroundPanel.add(behinderungsgradLabel, gbc);

        JSlider behinderungsgradSlider = new JSlider(0, 100);
        behinderungsgradSlider.setPaintLabels(true);
        behinderungsgradSlider.setPaintTicks(true);
        behinderungsgradSlider.setMajorTickSpacing(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        backgroundPanel.add(behinderungsgradSlider, gbc);

        JLabel passwortLabel = new JLabel(messages.getString("register.passwort"));
        passwortLabel.setFont(customFont);
        passwortLabel.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 6;
        backgroundPanel.add(passwortLabel, gbc);

        JLabel bestätigungPasswortLabel = new JLabel(messages.getString("register.bestätigung"));
        bestätigungPasswortLabel.setFont(customFont);
        bestätigungPasswortLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 7;
        backgroundPanel.add(bestätigungPasswortLabel, gbc);


        gbc.gridx = 1;
        gbc.gridy = 6;
        backgroundPanel.add(passwortField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        backgroundPanel.add(passwortBestätigung, gbc);

        JLabel sicherheitsfrage = new JLabel(messages.getString("register.sicherheitsfrage"));
        sicherheitsfrage.setFont(customFont);
        sicherheitsfrage.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 8;
        backgroundPanel.add(sicherheitsfrage, gbc);

        String[] sicherheitsfragen = {
                messages.getString("sicherheitsfrage.1"),
                messages.getString("sicherheitsfrage.2"),
                messages.getString("sicherheitsfrage.3")
        };
        JComboBox<String> sicherheitsfragenComboBox = new JComboBox<>(sicherheitsfragen);
        sicherheitsfragenComboBox.setFont(customFont);
        gbc.gridx = 1;
        gbc.gridy = 8;
        backgroundPanel.add(sicherheitsfragenComboBox, gbc);

        JLabel antwort = new JLabel(messages.getString("register.antwort"));
        antwort.setFont(customFont);
        antwort.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 9;
        backgroundPanel.add(antwort, gbc);

        JTextField antwortFeld = new JTextField(20);
        antwortFeld.setFont(customFont);
        gbc.gridx = 1;
        gbc.gridy = 9;
        backgroundPanel.add(antwortFeld, gbc);

    }

    private void createSpeichernButton(JPanel backgroundPanel, GridBagConstraints gbc) {
        JButton speichernButton = new JButton(messages.getString("register.speichern"));
        speichernButton.setFont(customFont);
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.EAST;
        backgroundPanel.add(speichernButton, gbc);

        speichernButton.addActionListener(e -> {
            String vorname = vornameField.getText();
            JOptionPane.showMessageDialog(this, messages.getString("register.welcome") + " " + vorname + "!", messages.getString("register.title"), JOptionPane.INFORMATION_MESSAGE);
            speichernMitarbeiter();
            dispose();
        });
    }
    private void speichernMitarbeiter() {
        System.out.println("Wir waren hier");
        try {
            // Benutzereingaben aus den Feldern holen
            String vorname = vornameField.getText();
            String nachname = nameField.getText();
            String email = emailField.getText();
            String passwort = new String(passwortField.getPassword());
            String passwortBestätigungText = new String(passwortBestätigung.getPassword());

            // Überprüfen, ob Passwort und Bestätigung übereinstimmen
            if (!passwort.equals(passwortBestätigungText)) {
                JOptionPane.showMessageDialog(this, messages.getString("register.passwort_mismatch"), messages.getString("error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Passwort hashen
            //String passwortHash = hashPasswort(passwort);

            int wochenstunden = 40;

            // Zeitmodell (Vollzeit, Teilzeit, Minijob)
            /*
            String zeitmodell = (String) zeitmodellComboBox.getSelectedItem();
            int wochenstunden = zeitmodell.equals(messages.getString("zeitmodell.vollzeit")) ? 40 :
                    zeitmodell.equals(messages.getString("zeitmodell.teilzeit")) ? 20 : 10;
                    */

            // Standardwert für gleitzeitWarnungGrenze (kann angepasst werden)
            double gleitzeitWarnungGrenze = 5.0;
            datenbank.starten();
            datenbank.addMitarbeiter(vorname, nachname, email, passwort, Locale.getDefault().getLanguage(), wochenstunden, gleitzeitWarnungGrenze);
            datenbank.schliessen();
            // Erfolgsmeldung anzeigen
            JOptionPane.showMessageDialog(this, messages.getString("register.success"), messages.getString("register.title"), JOptionPane.INFORMATION_MESSAGE);
            dispose();  // Fenster schließen

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, messages.getString("register.db_error"), messages.getString("error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private String hashPasswort(String passwort) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(passwort.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}

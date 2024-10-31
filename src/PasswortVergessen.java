import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PasswortVergessen extends JDialog {

    private ResourceBundle messages;
    private Font customFont;
    private JTextField emailField;
    private JLabel sicherheitsfrageLabel;
    private JTextField sicherheitsfrageAntwort;
    private JPasswordField neuesPasswort;
    private JComboBox<String> sicherheitsfragenComboBox; // JComboBox für Sicherheitsfragen
    private Datenbank datenbank;

    // Konstruktor, der die Datenbank-Instanz erhält und korrekt initialisiert
    public PasswortVergessen(Frame owner, ResourceBundle messages, Font customFont, Datenbank datenbank) {
        super(owner, messages.getString("passwort.vergessen"), true);
        this.messages = messages;
        this.customFont = customFont;
        this.datenbank = datenbank; // Datenbank-Instanz korrekt zuweisen

        setSize(500, 400);
        setLocationRelativeTo(owner);
        setLayout(new GridBagLayout());

        JPanel backgroundPanel = createBackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setOpaque(false);
        setContentPane(backgroundPanel);

        initializeComponents(backgroundPanel);
    }

    // Hintergrund-Panel mit Bild erstellen
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

    // Komponenten initialisieren und hinzufügen
    private void initializeComponents(JPanel backgroundPanel) {
        emailField = new JTextField(20);

        sicherheitsfrageLabel = new JLabel();
        sicherheitsfrageLabel.setFont(customFont.deriveFont(16f));
        sicherheitsfrageLabel.setForeground(Color.WHITE);

        sicherheitsfrageAntwort = new JTextField(20);
        JLabel antwortLabel = new JLabel(messages.getString("register.antwort"));
        antwortLabel.setFont(customFont.deriveFont(16f));
        antwortLabel.setForeground(Color.WHITE);

        JLabel neuesPasswortLabel = new JLabel(messages.getString("label.newPassword"));
        neuesPasswortLabel.setFont(customFont.deriveFont(16f));
        neuesPasswortLabel.setForeground(Color.WHITE);

        JLabel emailLabel = new JLabel("E-Mail");
        emailLabel.setForeground(Color.white);
        emailLabel.setFont(customFont.deriveFont(16f));

        neuesPasswort = new JPasswordField(20);

        // JComboBox für Sicherheitsfragen
        String[] sicherheitsfragen = {
                messages.getString("sicherheitsfrage.1"),
                messages.getString("sicherheitsfrage.2"),
                messages.getString("sicherheitsfrage.3")
        };
        sicherheitsfragenComboBox = new JComboBox<>(sicherheitsfragen);
        sicherheitsfragenComboBox.setFont(customFont);

        JButton passwortZurücksetzen = new JButton(messages.getString("passwort.zurücksetzen"));
        passwortZurücksetzen.setFont(customFont.deriveFont(18f));
        passwortZurücksetzen.addActionListener(e -> {
            try {
                resetPassword();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // Layout-Management mit GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // E-Mail-Feld
        gbc.gridx = 1;
        gbc.gridy = 1;
        backgroundPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        backgroundPanel.add(emailLabel, gbc);

        // Sicherheitsfrage Auswahl
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        backgroundPanel.add(sicherheitsfragenComboBox, gbc);

        // Sicherheitsfragen Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        backgroundPanel.add(sicherheitsfrageLabel, gbc);

        // Antwort-Feld
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        backgroundPanel.add(antwortLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        backgroundPanel.add(sicherheitsfrageAntwort, gbc);

        // Neues Passwort Label
        gbc.gridx = 0;
        gbc.gridy = 5;
        backgroundPanel.add(neuesPasswortLabel, gbc);

        // Neues Passwort Feld
        gbc.gridx = 1;
        gbc.gridy = 5;
        backgroundPanel.add(neuesPasswort, gbc);

        // Passwort zurücksetzen Button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(passwortZurücksetzen, gbc);
    }

    // Methode zur Passwort-Zurücksetzung
    private void resetPassword() throws SQLException {
        datenbank.starten();
        String email = emailField.getText();

        // Frage und Antwort aus der Datenbank abrufen
        String benutzerFrage = (String) sicherheitsfragenComboBox.getSelectedItem(); // Ausgewählte Sicherheitsfrage
        String gespeicherteAntwort = datenbank.getSicherheitsantwort(email);

        // Sicherheitsfrage anzeigen
        sicherheitsfrageLabel.setText(benutzerFrage);

        String eingabeAntwort = sicherheitsfrageAntwort.getText();
        String neuesPasswortText = new String(neuesPasswort.getPassword());

        // Überprüfen, ob die eingegebene Antwort korrekt ist
        if (eingabeAntwort.equals(gespeicherteAntwort) && benutzerFrage.equals(datenbank.getSicherheitsfrage(email))) {
            datenbank.updatePasswort(email, neuesPasswortText); // Passwort in der Datenbank aktualisieren
            JOptionPane.showMessageDialog(this, messages.getString("passwort.erfolgreich"));
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, messages.getString("passwort.falscheAntwort"), messages.getString("error"), JOptionPane.ERROR_MESSAGE);
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class PasswortVergessen extends JDialog {

    private ResourceBundle messages;
    private Font customFont;

    public PasswortVergessen(Frame owner, ResourceBundle messages, Font customFont) {
        super(owner, messages.getString("passwort.vergessen"), true);
        this.messages = messages;
        this.customFont = customFont;

        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new GridBagLayout());

        JPanel backgroundPanel = createBackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setOpaque(false);
        setContentPane(backgroundPanel);

        initializeComponents(backgroundPanel);
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

    private void initializeComponents(JPanel backgroundPanel) {
        JLabel neuesPasswortLabel = new JLabel(messages.getString("label.newPassword"));
        neuesPasswortLabel.setFont(customFont.deriveFont(16f));
        neuesPasswortLabel.setForeground(Color.WHITE);

        JPasswordField neuesPasswort = new JPasswordField(20);
        neuesPasswort.setPreferredSize(new Dimension(200, 25));

        JButton passwortZurücksetzen = new JButton(messages.getString("passwort.zurücksetzen"));
        passwortZurücksetzen.setFont(customFont.deriveFont(18f));

        JLabel sicherheitsfrageLabel = new JLabel(getRandomSecurityQuestion());
        sicherheitsfrageLabel.setFont(customFont.deriveFont(16f));
        sicherheitsfrageLabel.setForeground(Color.WHITE);

        JTextField sicherheitsfrageAntwort = new JTextField(20);
        JLabel antwortLabel = new JLabel(messages.getString("register.antwort"));
        antwortLabel.setFont(customFont.deriveFont(16f));
        antwortLabel.setForeground(Color.WHITE);

        // GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(sicherheitsfrageLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        backgroundPanel.add(antwortLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        backgroundPanel.add(sicherheitsfrageAntwort, gbc);

        // Neues Passwort Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        backgroundPanel.add(neuesPasswortLabel, gbc);

        // Neues Passwort Feld
        gbc.gridx = 1;
        gbc.gridy = 2;
        backgroundPanel.add(neuesPasswort, gbc);


        // Passwort zurücksetzen Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 0.1;
        backgroundPanel.add(passwortZurücksetzen, gbc);
    }

    private String getRandomSecurityQuestion() {
        String[] fragen = {
                messages.getString("sicherheitsfrage.1"),
                messages.getString("sicherheitsfrage.2"),
                messages.getString("sicherheitsfrage.3")
        };

        int randomIndex = (int) (Math.random() * fragen.length); // Zufälligen Index wählen
        return fragen[randomIndex]; // Rückgabe der zufälligen Sicherheitsfrage
    }
}

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class GUIAnmeldung extends JFrame {

    private JTextField benutzernameField;
    private JPasswordField passwortField;
    private JButton loginButton;
    private JButton deutsch;
    private JButton englisch;
    private JLabel benutzernameLabel;
    private JLabel passwortLabel;
    private JButton registrierenButton;
    private JButton passwortVergessen;
    private Font customFont;
    private ResourceBundle messages;

    public GUIAnmeldung() {
        setTitle("Login");
        setSize(700, 500);
        setLayout(new BorderLayout());

        JPanel backgroundPanel = createBackgroundPanel();
        setupComponents(backgroundPanel);

        add(backgroundPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        changeLanguage(Locale.getDefault());
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

        loadCustomFont();

        createUserInputFields(backgroundPanel, gbc);
        createLoginButton(backgroundPanel, gbc);
        createRegisterButton(backgroundPanel, gbc);
        createFlagButtons(backgroundPanel, gbc);
        createPasswortVergessen(backgroundPanel, gbc);
    }

    private void loadCustomFont() {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/ressourcen/KGDoYouLoveMe.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.PLAIN, 16);
        }
    }

    private void createUserInputFields(JPanel backgroundPanel, GridBagConstraints gbc) {
        benutzernameLabel = new JLabel("Benutzername:");
        benutzernameLabel.setFont(customFont);
        benutzernameLabel.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        backgroundPanel.add(benutzernameLabel, gbc);

        benutzernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        backgroundPanel.add(benutzernameField, gbc);

        passwortLabel = new JLabel("Passwort:");
        passwortLabel.setFont(customFont);
        passwortLabel.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        backgroundPanel.add(passwortLabel, gbc);

        passwortField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        backgroundPanel.add(passwortField, gbc);
    }

    private void createLoginButton(JPanel backgroundPanel, GridBagConstraints gbc) {
        loginButton = new JButton("Login");
        loginButton.setFont(customFont);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        backgroundPanel.add(loginButton, gbc);

        ActionListener loginActionListener = e -> {
            if (authenticate(benutzernameField.getText(), new String(passwortField.getPassword()))) {
                new GUI(Locale.getDefault()); // Hier wird das GUI-Fenster geöffnet
                dispose(); // Schließt das Login-Fenster
            } else {
                JOptionPane.showMessageDialog(GUIAnmeldung.this, messages.getString("login.error"), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        };

        loginButton.addActionListener(loginActionListener);
        benutzernameField.addActionListener(loginActionListener);
        passwortField.addActionListener(loginActionListener);
    }

    private void createFlagButtons(JPanel backgroundPanel, GridBagConstraints gbc) {
        JPanel flagPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        flagPanel.setOpaque(false);

        deutsch = createFlagButton("src/ressourcen/deutscheFlagge.png", Locale.GERMAN);
        englisch = createFlagButton("src/ressourcen/UK-Flagge.png", Locale.ENGLISH);

        flagPanel.add(deutsch);
        flagPanel.add(englisch);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(flagPanel, gbc);
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

        button.addActionListener(e -> {
            changeLanguage(locale);
        });

        return button;
    }

    private void createRegisterButton(JPanel backgroundPanel, GridBagConstraints gbc) {
        registrierenButton = new JButton("Registrieren");
        registrierenButton.setFont(customFont);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(registrierenButton, gbc);

        registrierenButton.addActionListener(e -> new Registrieren(customFont));
    }

    private void createPasswortVergessen(JPanel backgroundPanel, GridBagConstraints gbc) {
        passwortVergessen = new JButton("Passwort vergessen");
        passwortVergessen.setFont(customFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(passwortVergessen, gbc);

        passwortVergessen.addActionListener(e -> {
            PasswortVergessen dialog = new PasswortVergessen(this, messages, customFont);
            dialog.setVisible(true);
        });
    }

    private void changeLanguage(Locale locale) {
        Locale.setDefault(locale);
        messages = ResourceBundle.getBundle("ressourcen.messages", locale);
        updateLabels();
    }

    private void updateLabels() {
        setTitle(messages.getString("title"));
        loginButton.setText(messages.getString("button.login"));
        benutzernameLabel.setText(messages.getString("label.username"));
        passwortLabel.setText(messages.getString("label.password"));
        registrierenButton.setText(messages.getString("button.register"));
        passwortVergessen.setText(messages.getString("passwort.vergessen"));
        benutzernameField.setText("");
        passwortField.setText("");
    }

    private boolean authenticate(String username, String password) {
        // Dummy-Authentifizierung
        return username.equals("user") && password.equals("password");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUIAnmeldung::new);
    }
}

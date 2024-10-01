import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class benutzerMenu extends JFrame {
    private ResourceBundle messages;
    private Font customFont;
    private JButton passwortÄnderung;
    private JButton datenÄnderung;

    public benutzerMenu(Locale locale) {
        // Setze das Locale und lade die entsprechenden Nachrichten
        Locale.setDefault(locale);
        messages = ResourceBundle.getBundle("ressourcen.messages", locale);

        setTitle(messages.getString("menu.title")); // Setze den Fenstertitel
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Erstelle ein Hintergrund-Panel
        JPanel backgroundPanel = createBackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());

        // Lade die benutzerdefinierte Schriftart
        loadCustomFont();

        // Schaltflächen hinzufügen
        passwortÄnderung = new JButton(messages.getString("button.changePassword"));
        datenÄnderung = new JButton(messages.getString("button.changeData"));

        // Schriftart für die Schaltflächen anwenden
        applyFontToButton(passwortÄnderung);
        applyFontToButton(datenÄnderung);

        // Aktionen für die Schaltflächen
        passwortÄnderung.addActionListener(e -> showPasswortÄnderung());
        datenÄnderung.addActionListener(e -> showDataChangeDialog());

        // Schaltflächen zum Hintergrund-Panel hinzufügen
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        backgroundPanel.add(passwortÄnderung, gbc);

        gbc.gridy = 1;
        backgroundPanel.add(datenÄnderung, gbc);

        // Füge das Panel zum Frame hinzu
        add(backgroundPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    private void applyFontToButton(JButton button) {
        button.setFont(customFont.deriveFont(20f));
    }

    // Methode, um das Dialogfenster für die Passwortänderung zu zeigen
    private void showPasswortÄnderung() {
        JDialog passwortDialog = createDialog(messages.getString("dialog.changePassword"));

        // Felder für Passwortänderung
        JLabel currentPasswordLabel = new JLabel(messages.getString("label.currentPassword"));
        currentPasswordLabel.setFont(customFont.deriveFont(18f));
        currentPasswordLabel.setForeground(Color.white);
        JPasswordField currentPasswordField = new JPasswordField(15);

        JLabel newPasswordLabel = new JLabel(messages.getString("label.newPassword"));
        newPasswordLabel.setFont(customFont.deriveFont(18f));
        newPasswordLabel.setForeground(Color.WHITE);
        JPasswordField newPasswordField = new JPasswordField(15);

        JButton submitButton = new JButton(messages.getString("button.submit"));
        applyFontToButton(submitButton);

        // GridBagLayout für den Dialog
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Elemente hinzufügen
        passwortDialog.add(currentPasswordLabel, gbc);
        gbc.gridx = 1;
        passwortDialog.add(currentPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        passwortDialog.add(newPasswordLabel, gbc);
        gbc.gridx = 1;
        passwortDialog.add(newPasswordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        passwortDialog.add(submitButton, gbc);

        passwortDialog.setVisible(true);
    }

    // Methode, um das Dialogfenster für die Datenänderung zu zeigen
    private void showDataChangeDialog() {
        JDialog dataDialog = createDialog(messages.getString("dialog.changeData"));

        // Felder für Datenänderung
        JLabel nameLabel = new JLabel(messages.getString("label.name"));
        nameLabel.setFont(customFont.deriveFont(18f));
        nameLabel.setForeground(Color.WHITE);
        JTextField nameField = new JTextField(15);
        nameField.setFont(customFont);

        JLabel timeModelLabel = new JLabel(messages.getString("label.timeModel"));
        timeModelLabel.setFont(customFont.deriveFont(18f));
        timeModelLabel.setForeground(Color.WHITE);
        String[] timeModels = { messages.getString("zeitmodell.vollzeit"),
                messages.getString("zeitmodell.teilzeit"),
                messages.getString("zeitmodell.minijob") } ;
        JComboBox<String> timeModelComboBox = new JComboBox<>(timeModels);
        timeModelComboBox.setFont(customFont);

        JButton submitButton = new JButton(messages.getString("button.submit"));
        applyFontToButton(submitButton);

        // GridBagLayout für den Dialog
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Elemente hinzufügen
        dataDialog.add(nameLabel, gbc);
        gbc.gridx = 1;
        dataDialog.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dataDialog.add(timeModelLabel, gbc);
        gbc.gridx = 1;
        dataDialog.add(timeModelComboBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        dataDialog.add(submitButton, gbc);

        dataDialog.setVisible(true);
    }

    private JDialog createDialog(String title) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());

        // Erstelle ein Hintergrund-Panel für den Dialog
        JPanel backgroundPanel = createBackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        dialog.setContentPane(backgroundPanel);

        return dialog;
    }

    public static void main(String[] args) {
        // Beispiel für den Aufruf des benutzerMenu mit deutschem Locale
        SwingUtilities.invokeLater(() -> new benutzerMenu(new Locale("de", "DE")));
    }
}

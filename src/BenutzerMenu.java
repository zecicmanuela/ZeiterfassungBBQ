import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class BenutzerMenu extends JFrame {
    private ResourceBundle messages;
    private Font customFont;
    private JButton passwortÄnderung;

    private String email;
    Datenbank datenbank = new Datenbank();

    public BenutzerMenu(Locale locale, String email) {
        this.email = email;
        Locale.setDefault(locale);
        messages = ResourceBundle.getBundle("ressourcen.messages", locale);

        setTitle(messages.getString("menu.title"));
        setSize(800, 600);
        setLayout(new BorderLayout());

        JPanel backgroundPanel = createBackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());

        loadCustomFont();

        passwortÄnderung = new JButton(messages.getString("button.changePassword"));

        applyFontToButton(passwortÄnderung);

        passwortÄnderung.addActionListener(e -> showPasswortÄnderung());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        backgroundPanel.add(passwortÄnderung, gbc);

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

    private void showPasswortÄnderung() {
        JDialog passwortDialog = createDialog(messages.getString("dialog.changePassword"));

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

        submitButton.addActionListener(e -> {
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());

            try {
                datenbank.starten();
                // Validierung des aktuellen Passworts über die E-Mail
                if (datenbank.mitarbeiterAnmelden(email, currentPassword) != null) {
                    // Hier aktualisieren wir das Passwort über die E-Mail anstelle einer ID
                    datenbank.updatePasswort(email, newPassword);
                    JOptionPane.showMessageDialog(this, "Passwort erfolgreich geändert!");
                } else {
                    JOptionPane.showMessageDialog(this, "Aktuelles Passwort ist falsch.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Fehler bei der Passwortänderung.");
            } finally {
                datenbank.schliessen();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
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

    private JDialog createDialog(String title) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());

        JPanel backgroundPanel = createBackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        dialog.setContentPane(backgroundPanel);

        return dialog;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BenutzerMenu(new Locale("de", "DE"), "beispiel@domain.de"));
    }
}

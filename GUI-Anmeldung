import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GUIAnmeldung extends JFrame {

    private JTextField benutzernameField;
    private JPasswordField passwortField;
    private JButton loginButton;

    public GUIAnmeldung() {
        setTitle("Login");
        setSize(400, 300);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Benutzername Label und Textfeld
        JLabel benutzernameLabel = new JLabel("Benutzername:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(benutzernameLabel, gbc);

        benutzernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(benutzernameField, gbc);

        // Passwort Label und Textfeld
        JLabel passwortLabel = new JLabel("Passwort:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwortLabel, gbc);

        passwortField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(passwortField, gbc);

        // Login Button
        loginButton = new JButton("Login");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        add(loginButton, gbc);


        ActionListener loginActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (authenticate(benutzernameField.getText(), new String(passwortField.getPassword()))) {
                    new GUI();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(GUIAnmeldung.this, "Ungültiger Benutzername oder Passwort!", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        loginButton.addActionListener(loginActionListener);

        benutzernameField.addActionListener(loginActionListener);
        passwortField.addActionListener(loginActionListener);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // Dummy-Authentifizierungsmethode
    private boolean authenticate(String username, String password) {
        return username.equals("user") && password.equals("password");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUIAnmeldung().setVisible(true);
            }
        });
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class GUI extends JFrame {
    private JButton kommenButton;
    private JButton gehenButton;
    private JLabel gearbeiteteStunden;
    private Font customFont;
    private ResourceBundle bundle;
    private Locale currentLocale;
    private Timer timer;
    private int elapsedSeconds = 0;
    private JLabel countdown;
    private String mitarbeiterEmail;

    Datenbank datenbank = new Datenbank();

    public GUI(Locale locale, String email) {
        this.currentLocale = locale;
        this.mitarbeiterEmail = email;
        loadBundle(currentLocale);

        datenbank.starten(); // Verbindet die Datenbank

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

        countdown = new JLabel("00:00:00", SwingConstants.CENTER);
        countdown.setFont(new Font("Abadi MS", Font.PLAIN, 45));
        countdown.setForeground(Color.white);
        arbeitszeitPanel.add(countdown, BorderLayout.CENTER);
        topPanel.add(arbeitszeitPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        kommenButton = new JButton(bundle.getString("button.come"));
        kommenButton.setFont(customFont.deriveFont(20f));
        buttonPanel.add(kommenButton);

        gehenButton = new JButton(bundle.getString("button.go"));
        gehenButton.setFont(customFont.deriveFont(20f));
        buttonPanel.add(gehenButton);

        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ActionListener für den Kommen-Button
        kommenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.start();
                String datum = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String arbeitsbeginn = new SimpleDateFormat("HH:mm:ss").format(new Date());

                try {
                    datenbank.mitarbeiterKommt(mitarbeiterEmail, datum, arbeitsbeginn);
                    JOptionPane.showMessageDialog(null, "Kommen-Zeit erfasst: " + arbeitsbeginn);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Fehler beim Erfassen der Kommen-Zeit: " + ex.getMessage());
                }
            }
        });

        // ActionListener für den Gehen-Button
        gehenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                String datum = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String arbeitsende = new SimpleDateFormat("HH:mm:ss").format(new Date());

                try {
                    datenbank.mitarbeiterGeht(mitarbeiterEmail, datum, arbeitsende);
                    JOptionPane.showMessageDialog(null, "Gehen-Zeit erfasst: " + arbeitsende);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Fehler beim Erfassen der Gehen-Zeit: " + ex.getMessage());
                }
            }
        });

        add(topPanel, BorderLayout.NORTH);
        setVisible(true);
    }

    private void loadBundle(Locale locale) {
        bundle = ResourceBundle.getBundle("ressourcen.messages", locale);
    }

    public static void main(String[] args) {
        Locale locale = new Locale("de", "DE"); // Standardmäßig Deutsch
        Datenbank datenbank = new Datenbank();
        datenbank.starten();

        // Beispiel für Anmeldeversuch
        try {
            String email = "beispiel@domain.de"; // Beispiel E-Mail
            String passwortHash = "hashedPasswort"; // Beispiel Passwort-Hash

            String angemeldeteEmail = datenbank.mitarbeiterAnmelden(email, passwortHash);

            if (angemeldeteEmail != null) {
                SwingUtilities.invokeLater(() -> new GUI(locale, angemeldeteEmail));
            } else {
                JOptionPane.showMessageDialog(null, "Anmeldung fehlgeschlagen.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Fehler bei der Anmeldung: " + ex.getMessage());
        }
    }
}

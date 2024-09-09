import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

    private JButton kommenButton;
    private JButton gehenButton;
    private JLabel gearbeiteteStunden;

    public GUI() {
        setTitle("Zeiterfassungsprogramm");

        setLayout(new BorderLayout());

        // Anzeige gearbeitete Stunden
        JPanel countdownPanel = new JPanel();
        countdownPanel.setLayout(new BorderLayout());
        countdownPanel.setBorder(BorderFactory.createEmptyBorder(85, 10, 10, 10));

        JLabel heuteGearbeitetLabel = new JLabel("HEUTE GEARBEITET", SwingConstants.CENTER);
        heuteGearbeitetLabel.setFont(new Font("Abadi MS", Font.BOLD, 16));
        countdownPanel.add(heuteGearbeitetLabel, BorderLayout.NORTH);

        gearbeiteteStunden = new JLabel("00:00:00", SwingConstants.CENTER);
        gearbeiteteStunden.setFont(new Font("Abadi MS", Font.BOLD, 24));
        gearbeiteteStunden.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        gearbeiteteStunden.setPreferredSize(new Dimension(100, 50));
        countdownPanel.add(gearbeiteteStunden, BorderLayout.CENTER);

        add(countdownPanel, BorderLayout.NORTH);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        ImageIcon kommenIcon = new ImageIcon("/Users/manuelazecic/Downloads/soldier-2676585_1280.png");
        ImageIcon gehenIcon = new ImageIcon("/Users/manuelazecic/Downloads/soldier-2676586_1280.png");
        Image scaledKommenIcon = kommenIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        Image scaledGehenIcon = gehenIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);

        // Kommen
        kommenButton = new JButton("Kommen", new ImageIcon(scaledKommenIcon));
        kommenButton.setFont(new Font("Abadi MS", Font.PLAIN, 16));
        buttonPanel.add(kommenButton);
        kommenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(GUI.this,"Kommen-Button geklickt.");
            }
        });

        // Gehen
        gehenButton = new JButton("Gehen", new ImageIcon(scaledGehenIcon));
        gehenButton.setFont(new Font("Abadi MS", Font.PLAIN, 16));
        buttonPanel.add(gehenButton);
        gehenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(GUI.this,"Gehen-Button geklickt.");
            }
        });


        add(buttonPanel, BorderLayout.CENTER);

        // Tabelle
        String[] spalten = {"Datum", "Gearbeitet von-bis", "Saldo"};
        DefaultTableModel model = new DefaultTableModel(spalten, 0);
        JTable gleitzeitkonto = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(gleitzeitkonto);

        scrollPane.setPreferredSize(new Dimension(800, 150));
        add(scrollPane, BorderLayout.SOUTH);

        gleitzeitkonto.setShowGrid(true);
        gleitzeitkonto.setGridColor(Color.BLACK);


        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new GUI();
    }
}

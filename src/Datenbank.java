import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.JOptionPane;
public class Datenbank {
    Connection connection;
    Statement statement;
    ResultSet result;
    public void starten() {
        if (connection != null) {
            //Fehlerbehebung
            System.out.println("DOPPELT gestartet du Depp.");
            return;
        }
        String url = "jdbc:mysql://sql7.freesqldatabase.com:3306/sql7741225";
        String name = "sql7741225";
        String password = "tirDqa4QMX";

        try {
            connection = DriverManager.getConnection(url, name, password);
            statement = connection.createStatement();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "datenstarten fehler: " + e.toString());
        }
    }

    public void addMitarbeiter(String vorname, String nachname, String email, String passwortHash, String sprache, int wochenstunden, double gleitzeitWarnungGrenze, String sicherheitsfrage, String antwort) throws SQLException {
        // Aktualisierte SQL-Abfrage, um Passwort_hash zu verwenden
        String query = "INSERT INTO mitarbeiter (vorname, nachname, email, Passwort_hash, sprache, wochenstunden, gleitzeit_warnung_grenze, sicherheitsfrage, antwort) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, vorname);
            pstmt.setString(2, nachname);
            pstmt.setString(3, email);
            pstmt.setString(4, passwortHash); // Hier bleibt der Hash (oder Klartext, je nach Bedarf)
            pstmt.setString(5, sprache);
            pstmt.setInt(6, wochenstunden);
            pstmt.setDouble(7, gleitzeitWarnungGrenze);
            pstmt.setString(8, sicherheitsfrage);
            pstmt.setString(9, antwort);

            pstmt.executeUpdate();
        }
    }






    public void addArbeitszeit(int mitarbeiterId, String datum, String arbeitsbeginn, String arbeitsende, boolean istUeberzeit) throws SQLException {
        String query = "INSERT INTO arbeitszeiten (mitarbeiter_id, datum, arbeitsbeginn, arbeitsende, ist_ueberzeit) " +
                "VALUES(" + mitarbeiterId + ", '" + datum + "', '" + arbeitsbeginn + "', '" + arbeitsende + "', " + istUeberzeit + ")";

        statement.execute(query);
    }

    public void addGleitzeit(int mitarbeiterId, String monat, double gleitzeitStunden, String quartal, int jahr) throws SQLException {
        String query = "INSERT INTO gleitzeit (mitarbeiter_id, monat, gleitzeit_stunden, quartal, jahr) " +
                "VALUES(" + mitarbeiterId + ", '" + monat + "', " + gleitzeitStunden + ", '" + quartal + "', " + jahr + ")";

        statement.execute(query);
    }


    public void updatePasswort(String email, String neuesPasswort) throws SQLException {
        String query = "UPDATE mitarbeiter SET Passwort_hash = ? WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, neuesPasswort); // Klartext-Passwort setzen
            pstmt.setString(2, email); // E-Mail-Adresse setzen
            pstmt.executeUpdate();
        }
    }


    public String getSicherheitsfrage(String email) throws SQLException {
        String query = "SELECT sicherheitsfrage FROM mitarbeiter WHERE email = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString("sicherheitsfrage");
        } else {
            throw new SQLException("Benutzer nicht gefunden");
        }
    }

    // Methode, um die Sicherheitsantwort eines Benutzers abzurufen
    public String getSicherheitsantwort(String email) throws SQLException {
        String query = "SELECT antwort FROM mitarbeiter WHERE email = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString("antwort");
        } else {
            throw new SQLException("Benutzer nicht gefunden");
        }
    }


    public void updateSprache(int mitarbeiterId, String neueSprache) throws SQLException {
        String query = "UPDATE einstellungen SET sprache = '" + neueSprache + "' WHERE mitarbeiter_id = " + mitarbeiterId;

        statement.executeUpdate(query);
    }


    public double getGleitzeitWoche(int mitarbeiterId) throws SQLException {
        double wochenstunden = 0, gleitzeit;
        String query = "SELECT wochenstunden FROM mitarbeiter WHERE mitarbeiter_id = '" + mitarbeiterId + "'";
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            wochenstunden = resultSet.getDouble("wochenstunden");
            gleitzeit = getArbeitszeit(mitarbeiterId, 7) - wochenstunden;
            return gleitzeit;
        }
        return 0;
    }

    public double getGleitzeitMonat(int mitarbeiterId) throws SQLException {
        double wochenstunden = 0, gleitzeit;
        String query = "SELECT wochenstunden FROM mitarbeiter WHERE mitarbeiter_id = '" + mitarbeiterId + "'";
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            wochenstunden = resultSet.getDouble("wochenstunden");
            gleitzeit = getArbeitszeit(mitarbeiterId, 30) - ((wochenstunden/7)*30);
            return gleitzeit;
        }
        return 0;
    }

    public double getGleitzeitJahr(int mitarbeiterId) throws SQLException {
        double wochenstunden = 0, gleitzeit;
        String query = "SELECT wochenstunden FROM mitarbeiter WHERE mitarbeiter_id = '" + mitarbeiterId + "'";
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            wochenstunden = resultSet.getDouble("wochenstunden");
            gleitzeit = getArbeitszeit(mitarbeiterId, 365) - ((wochenstunden/7)*365);
            return gleitzeit;
        }
        return 0;
    }

    public double getArbeitszeit(int mitarbeiterId, int tageAnzahl) throws SQLException {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(tageAnzahl);

        String query = "SELECT mitarbeiter_id AS woche, SUM(TIME_TO_SEC(TIMEDIFF(arbeitsende, arbeitsbeginn))/ 3600) AS gleitzeit " +
                "FROM arbeitszeiten " +
                "WHERE mitarbeiter_id = " + mitarbeiterId + " " +
                "AND datum BETWEEN '" + startDate + "' AND '" + endDate + "' " +
                "GROUP BY mitarbeiter_id";

        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            double gleitzeit = resultSet.getDouble("gleitzeit");
            return gleitzeit;
        }
        return 0;
    }


    public void updateGleitzeitWarnungGrenze(int mitarbeiterId, double neueGrenze) throws SQLException {
        String query = "UPDATE einstellungen SET gleitzeit_grenze = " + neueGrenze + " WHERE mitarbeiter_id = " + mitarbeiterId;

        statement.executeUpdate(query);
    }

    public int findeMitarbeiterID(String email) throws SQLException {
        String checkQuery = "SELECT mitarbeiter_id FROM mitarbeiter WHERE email = '" + email + "'";
        ResultSet resultSet = statement.executeQuery(checkQuery);
        String mitarbeiterID = null;
        if (resultSet.next()) {
            mitarbeiterID = resultSet.getString("mitarbeiter_id");
            return Integer.parseInt(mitarbeiterID);
        }
        return 0;
    }


    public void mitarbeiterKommt(String email) throws SQLException {
        // Prüfen, ob für den aktuellen Tag bereits ein Arbeitsbeginn existiert
        LocalDate datum = LocalDate.now();
        LocalTime arbeitsbeginn = LocalTime.now().withNano(0);
        int mitarbeiterID = findeMitarbeiterID(email);
        String checkQuery = "SELECT * FROM arbeitszeiten WHERE mitarbeiter_id = " + mitarbeiterID + " AND datum = '" + datum + "'";
        ResultSet resultSet = statement.executeQuery(checkQuery);

        if (!resultSet.next()) {
            // Wenn es noch keinen Eintrag gibt, füge eine neue Arbeitszeit ohne Arbeitsende ein
            String insertQuery = "INSERT INTO arbeitszeiten (mitarbeiter_id, datum, arbeitsbeginn) " +
                    "VALUES(" + mitarbeiterID + ", '" + datum + "', '" + arbeitsbeginn + "')";
            statement.execute(insertQuery);
        }
    }

    public void mitarbeiterGeht(String email) throws SQLException {
        LocalDate datum = LocalDate.now();
        LocalTime arbeitsende = LocalTime.now().withNano(0);
        int mitarbeiterId = findeMitarbeiterID(email);
        // Prüfen, ob der Mitarbeiter an diesem Tag bereits Arbeitsbeginn hat
        String checkQuery = "SELECT * FROM arbeitszeiten WHERE mitarbeiter_id = " + mitarbeiterId + " AND datum = '" + datum + "'";
        ResultSet resultSet = statement.executeQuery(checkQuery);

        if (resultSet.next()) {
            // Wenn Arbeitsbeginn existiert, aktualisiere den Arbeitsende-Eintrag
            String updateQuery = "UPDATE arbeitszeiten SET arbeitsende = '" + arbeitsende + "' WHERE mitarbeiter_id = " + mitarbeiterId + " AND datum = '" + datum + "'";
            statement.executeUpdate(updateQuery);
        }
    }

    public String mitarbeiterAnmelden(String email, String passwort) throws SQLException {
        // SQL-Abfrage zur Überprüfung von E-Mail und Passwort
        String query = "SELECT * FROM mitarbeiter WHERE email = ? AND Passwort_hash = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, passwort); // Verwenden Sie das Klartext-Passwort
            ResultSet resultSet = pstmt.executeQuery();

            // Wenn ein Datensatz zurückgegeben wird, ist die Anmeldung erfolgreich
            if (resultSet.next()) {
                return email;
            } else {
                return null; // Anmeldung fehlgeschlagen
            }
        }
    }





    public void loescheMitarbeiter(int mitarbeiterId) throws SQLException {
        String query = "DELETE FROM mitarbeiter WHERE mitarbeiter_id = " + mitarbeiterId;

        statement.executeUpdate(query);
    }

    private String hashPasswort(String passwort) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(passwort.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }


    public void schliessen() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Fehler beim Schließen der Verbindung: " + e.getMessage());
        }
    }
}
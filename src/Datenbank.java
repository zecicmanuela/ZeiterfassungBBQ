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

    public void addMitarbeiter(String vorname, String nachname, String email, String passwortHash, String sprache, int wochenstunden, double gleitzeitWarnungGrenze) throws SQLException {
        String query = "INSERT INTO mitarbeiter (vorname, nachname, email, passwort_hash, sprache, wochenstunden, gleitzeit_warnung_grenze) " +
                "VALUES(\"" + vorname + "\", \"" + nachname + "\", \"" + email + "\", \"" + passwortHash + "\", \"" + sprache + "\", " + wochenstunden + ", " + gleitzeitWarnungGrenze + ")";

        statement.execute(query);
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
        String query = "UPDATE mitarbeiter SET passwort_hash = '" + neuesPasswort + "' WHERE email = '" + email + "'";
        statement.executeUpdate(query);
    }


    public void updateSprache(int mitarbeiterId, String neueSprache) throws SQLException {
        String query = "UPDATE einstellungen SET sprache = '" + neueSprache + "' WHERE mitarbeiter_id = " + mitarbeiterId;

        statement.executeUpdate(query);
    }

    public ResultSet getArbeitszeiten(int mitarbeiterId) throws SQLException {
        String query = "SELECT * FROM arbeitszeiten WHERE mitarbeiter_id = " + mitarbeiterId;

        return statement.executeQuery(query);
    }

    public ResultSet getGleitzeit(int mitarbeiterId) throws SQLException {
        String query = "SELECT * FROM gleitzeit WHERE mitarbeiter_id = " + mitarbeiterId;

        return statement.executeQuery(query);
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
        }
        return Integer.parseInt(mitarbeiterID);
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

    public String mitarbeiterAnmelden(String email, String passwortHash) throws SQLException {
        // SQL-Abfrage zur Überprüfung von E-Mail und Passwort-Hash
        String query = "SELECT * FROM mitarbeiter WHERE email = '" + email + "' AND passwort_hash = '" + passwortHash + "'";
        ResultSet resultSet = statement.executeQuery(query);

        // Wenn ein Datensatz zurückgegeben wird, ist die Anmeldung erfolgreich
        if (resultSet.next()) {
            return email;
        } else {
            return null; // Anmeldung fehlgeschlagen
        }
    }




    public void loescheMitarbeiter(int mitarbeiterId) throws SQLException {
        String query = "DELETE FROM mitarbeiter WHERE mitarbeiter_id = " + mitarbeiterId;

        statement.executeUpdate(query);
    }



    public void schliessen() {
        try {
            connection.close();
            connection = null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "datenschließen");
        }
    }
}
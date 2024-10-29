import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;


class DatenbankAnmeldenTest {

    private Datenbank datenbank;

    @BeforeEach
    void setUp() {
        datenbank = new Datenbank();
        datenbank.starten();
        try {
            // Testbenutzer anlegen, falls nicht vorhanden
            datenbank.addMitarbeiter("Max", "Mustermann", "test@example.com", "passwort123", "DE", 40, 8.0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        try {
            int mitarbeiterId = datenbank.findeMitarbeiterID("test@example.com");
            datenbank.loescheMitarbeiter(mitarbeiterId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        datenbank.schliessen();
    }


    @Test
    void mitarbeiterAnmelden() {
        try {
            String result = datenbank.mitarbeiterAnmelden("test@example.com", "passwort123");
            assertEquals("test@example.com", result, "Die Anmeldung sollte erfolgreich sein.");
        } catch (SQLException e) {
            fail("Datenbankfehler bei der Anmeldung: " + e.getMessage());
        }
    }

    @Test
    void mitarbeiterAnmeldenFehlgeschlagen() {
        try {
            String result = datenbank.mitarbeiterAnmelden("test@example.com", "falschesPasswort");
            assertNull(result, "Die Anmeldung sollte fehlschlagen, da das Passwort falsch ist.");
        } catch (SQLException e) {
            fail("Datenbankfehler bei der Anmeldung: " + e.getMessage());
        }
    }
}
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
            datenbank.addMitarbeiter("Test", "Test", "test@BBQ.de", "passwort", "DE", 40, 8.0,"Wann haben Ihre Eltern geheiratet? ","01.01.2000");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        try {
            int mitarbeiterId = datenbank.findeMitarbeiterID("test@BBQ.de");
            datenbank.loescheMitarbeiter(mitarbeiterId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        datenbank.schliessen();
    }


    @Test
    void mitarbeiterAnmelden() {
        try {
            String result = datenbank.mitarbeiterAnmelden("test@BBQ.de", "passwort");
            assertEquals("test@BBQ.de", result, "Die Anmeldung sollte erfolgreich sein.");
        } catch (SQLException e) {
            fail("Datenbankfehler bei der Anmeldung: " + e.getMessage());
        }
    }

    @Test
    void mitarbeiterAnmeldenFehlgeschlagen() {
        try {
            String result = datenbank.mitarbeiterAnmelden("test@BBQ.de", "falschesPasswort");
            assertNull(result, "Die Anmeldung sollte fehlschlagen, da das Passwort falsch ist.");
        } catch (SQLException e) {
            fail("Datenbankfehler bei der Anmeldung: " + e.getMessage());
        }
    }
}
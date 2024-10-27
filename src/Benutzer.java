// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Benutzer {

    // Attribute der Klasse
    private String passwort;
    private int alter;
    private int id;
    private String vorname;
    private String nachname;
    private String arbeitszeitRegelung;
    private int behinderungsGrad;
    private boolean schwangerschaft;
    private String sicherheitsAntwort;  // Antwort auf die Frage "Was ist deine Lieblingsfarbe?"
    private String email;

    //Konstruktor
    public Benutzer() {
        this.passwort = "";
        this.alter = 0;
        this.vorname = "";
        this.nachname = "";
        this.arbeitszeitRegelung = "";
        this.behinderungsGrad = 0;
        this.schwangerschaft = false;
        this.sicherheitsAntwort = "";
        this.email = "";
    }

    //Konstruktor mit Parameter
    public Benutzer(String passwort, int alter, int id, String vorname, String nachname, String arbeitszeitRegelung, int behinderungsGrad, boolean schwangerschaft, String sicherheitsAntwort, String email) {
        this.passwort = passwort;
        this.alter = alter;
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.arbeitszeitRegelung = arbeitszeitRegelung;
        this.behinderungsGrad = behinderungsGrad;
        this.schwangerschaft = schwangerschaft;
        this.sicherheitsAntwort = sicherheitsAntwort;
        setEmail(email);
    }

    public String getPasswort() {
        return passwort;
    }

    // Setter mit Validierung für Passwort (muss mindestens 8 Zeichen lang sein, 1 Großbuchstabe, 1 Sonderzeichen)
    public void setPasswort(String passwort) {
        if (istPasswortGueltig(passwort)) {
            this.passwort = passwort;
        } else {
            throw new IllegalArgumentException("Das Passwort muss mindestens 8 Zeichen lang sein und mindestens einen Großbuchstaben sowie ein Sonderzeichen enthalten.");
        }
    }

    // Methode zur Überprüfung, ob das Passwort gültig ist
    private boolean istPasswortGueltig(String passwort) {
        if (passwort == null || passwort.length() < 8) {
            return false;
        }
        // Regex für mindestens 1 Großbuchstabe und 1 Sonderzeichen, mindestens 8 Zeichen
        String regex = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
        return passwort.matches(regex);
    }

    // Methode zum Zurücksetzen des Passworts mit Sicherheitsfrage
    public boolean passwortZuruecksetzen(String antwort, String neuesPasswort) {
        if (this.sicherheitsAntwort.equalsIgnoreCase(antwort)) {
            setPasswort(neuesPasswort);  // Neues Passwort setzen, falls Antwort korrekt ist
            return true;
        } else {
            System.out.println("Falsche Antwort auf die Sicherheitsfrage.");
            return false;
        }
    }

    public int getAlter() {
        return alter;
    }

    public void setAlter(int alter) {
        this.alter = alter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getArbeitszeitRegelung() {
        return arbeitszeitRegelung;
    }

    public void setArbeitszeitRegelung(String arbeitszeitRegelung) {
        this.arbeitszeitRegelung = arbeitszeitRegelung;

    }

    public int getBehinderungsGrad() {
        return behinderungsGrad;
    }

    public void setBehinderungsGrad(int behinderungsGrad) {
        this.behinderungsGrad = behinderungsGrad;
    }

    public boolean isSchwangerschaft() {
        return schwangerschaft;
    }

    public void setSchwangerschaft(boolean schwangerschaft) {
        this.schwangerschaft = schwangerschaft;
    }

    public String getSicherheitsAntwort() {
        return sicherheitsAntwort;
    }

    public void setSicherheitsAntwort(String sicherheitsAntwort) {
        this.sicherheitsAntwort = sicherheitsAntwort;
    }

    // Setter mit Validierung für die E-Mail-Adresse
    public void setEmail(String email) {
        if (istEmailGueltig(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Ungültige E-Mail-Adresse.");
        }
    }

    // Methode zur Überprüfung, ob die E-Mail-Adresse gültig ist
    private boolean istEmailGueltig(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // Regex für die Überprüfung einer einfachen E-Mail-Adresse
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }
}


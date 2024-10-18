// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class KlasseBenutzer {

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
    

    //Konstruktor
    public KlasseBenutzer() {
        this.passwort = "";
        this.alter = 0;
        this.name = "";
        this.arbeitszeitRegelung = "";
        this.behinderungsGrad = 0;
        this.schwangerschaft = false;
        this.sicherheitsAntwort = "";

    }

    //Konstruktor mit Parameter
    public KlasseBenutzer(String passwort, int alter, int id, String vorname, String nachname, String arbeitszeitRegelung, int behinderungsGrad, boolean schwangerschaft, String sicherheitsAntwort) {
        this.passwort = passwort;
        this.alter = alter;
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname,
        this.arbeitszeitRegelung = arbeitszeitRegelung;
        this.behinderungsGrad = behinderungsGrad;
        this.schwangerschaft = schwangerschaft;
        this.sicherheitsAntwort = sicherheitsAntwort;
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

    public void getId() {
        this.id = id;
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

}




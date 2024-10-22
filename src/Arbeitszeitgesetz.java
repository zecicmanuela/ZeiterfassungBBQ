import java.time.DayOfWeek;
import java.time.LocalDate;

public class Arbeitszeitgesetz {

    KlasseBenutzer benutzer = new KlasseBenutzer();
    public Arbeitszeitgesetz(KlasseBenutzer benutzer) {
        this.benutzer = benutzer;
    }

    public boolean MaximaleArbeitszeit(double arbeitszeit){
        if (benutzer.getAlter()<18){
            if (arbeitszeit>8)
                return false;
            return true;
        }
        if (arbeitszeit>10)
            return false;
        return true;
    }
    public boolean Ruhezeiten(double pause){
        if(pause>11)
            return true;
        return false;
    }

    public boolean SonnUndFeiertags(LocalDate datum){
        if (datum.getDayOfWeek() == DayOfWeek.SUNDAY)
            return false;
        return true;
    }

    public boolean PausenRegelungen(double pausenZeit, double arbeitsZeit){
        if (arbeitsZeit>9){
            if (pausenZeit>=45){
                return true;
            }
            else return false;
        } else if (arbeitsZeit>6) {
            if (pausenZeit>=30){
                return true;
            }
            else return false;
        }
        return true;
    }

    public boolean Nachtarbeit(){
        return true;
    }

}

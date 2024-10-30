import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class Arbeitszeitgesetz {
Datenbank datenbank = new Datenbank();



    public boolean pruefeKommen(){
        if (SonnUndFeiertags() && Nachtarbeit()){
            return true;
        }
        return false;
    }

    public boolean pruefeGehen(String email){
        int mitarbeitderID;
        double arbeitszeit;
        try {
            datenbank.starten();
            mitarbeitderID = datenbank.findeMitarbeiterID(email);
            arbeitszeit = datenbank.getArbeitszeit(mitarbeitderID, 1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (MaximaleArbeitszeit(arbeitszeit)){
            return true;
        }
        return false;
    }

    public boolean MaximaleArbeitszeit(double arbeitszeit){
        if (20<18){
            if (arbeitszeit>9)
                return false;
            return true;
        }
        if (arbeitszeit>10.45)
            return false;
        return true;
    }

    public boolean SonnUndFeiertags(){
        LocalDate datum = LocalDate.now();
        if (datum.getDayOfWeek() == DayOfWeek.SUNDAY)
            return false;
        return true;
    }


    public boolean Nachtarbeit(){
        LocalTime arbeitsbeginn = LocalTime.now().withNano(0);
        LocalTime fruehZeit = LocalTime.of(6, 0); // 6:00 Uhr
        LocalTime spaetZeit = LocalTime.of(22, 0); // 22:00 Uhr
        if (arbeitsbeginn.isBefore(fruehZeit) || arbeitsbeginn.isAfter(spaetZeit)){
            return false;
        }
        return true;
    }

}
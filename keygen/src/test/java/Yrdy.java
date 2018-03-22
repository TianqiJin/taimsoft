import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;


public class Yrdy {
    @Test
    public void testDateParser(){
        String timeStr = "2018-Jan-15 08:00:00 UTC";
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MMM-dd HH:mm:ss z");
        dtf.parseDateTime(timeStr);
        System.out.println(1);

    }

}

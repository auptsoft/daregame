package weplay.auptsoft.daregame.services;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.callback.Callback;

/**
 * Created by Andrew on 15.3.19.
 */

public class Utility {

    public static GregorianCalendar toGregorianCalender(String dateTimeString) {
        try {
            String[] dateTime = dateTimeString.split(" ");
            String[] dateArray = dateTime[0].split("-");
            String[] timeArray = dateTime[1].split(":");

            GregorianCalendar gregorianCalendar = new GregorianCalendar(
                    Integer.parseInt(dateArray[0]),
                    Integer.parseInt(dateArray[1]),
                    Integer.parseInt(dateArray[2]),

                    Integer.parseInt(timeArray[0]),
                    Integer.parseInt(timeArray[1]),
                    Integer.parseInt(timeArray[2])
            );

            return gregorianCalendar;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String fromGregorianCalender(GregorianCalendar gregorianCalendar) {
        int year = gregorianCalendar.get(Calendar.YEAR);
        int month = gregorianCalendar.get(Calendar.MONTH);
        int day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);

        int hour = gregorianCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = gregorianCalendar.get(Calendar.MINUTE);
        int seconds = gregorianCalendar.get(Calendar.SECOND);

         return  ""+year+"-" +
                month +
                '-' +
                day +
                ' ' +
                hour +
                ':' +
                minutes +
                ':' +
                seconds;
    }
}

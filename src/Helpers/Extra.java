package Helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Extra {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public static boolean IsAssessmentNew(String assessmentDate) {
        boolean ret = false;
        try {
            Date date1 = sdf.parse(assessmentDate);
            Date dateNow = new Date();

            if (date1.compareTo(dateNow) > 0) {
                ret = true;
            }
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        }
        return ret;
    }

    public static String GetDifferenceInTime(String dateStart, String dateStop) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        String result = "";

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = diff / daysInMilli;
            diff = diff % daysInMilli;

            long elapsedHours = diff / hoursInMilli;
            diff = diff % hoursInMilli;

            if (elapsedDays > 1) {
                if (elapsedHours == 0) {
                    result = String.format(" (%d days left)", elapsedDays);
                } else {
                    result = String.format(" (%d days, %d hours left)", elapsedDays, elapsedHours);
                }
            } else {
                result = String.format(" (%d hours left)", elapsedHours);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}

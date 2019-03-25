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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date d1 = null;
        Date d2 = null;

        String result = "";

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);

            result += " (" + diffDays + " days left)";

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}

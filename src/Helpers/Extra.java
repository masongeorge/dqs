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
}

package gmedia.net.id.osbondmerchant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConvertDate {
    public static String parseDateToyyyyMMdd(String time) {
        String inputPattern = "dd-MMM-yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}

package cn.szlee.mail.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * <b><code>CalenderUtil</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019-04-23 14:53.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
public class CalenderUtil {

    public static Date twoWeeksBefore() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 14);
        return calendar.getTime();
    }

    public static Date now() {
        return Calendar.getInstance().getTime();
    }
}

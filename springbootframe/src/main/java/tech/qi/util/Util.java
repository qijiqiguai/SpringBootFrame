package tech.qi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.keygen.KeyGenerators;
import tech.qi.core.Constants;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *
 * @author wangqi
 * @date 2017/12/28 下午4:44
 */
public class Util {
    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    private static Hashids hashids_alphabet = new Hashids(Constants.HASHID_SALT, Constants.HASHID_MIN_LENGTH, Constants.HASHID_ALPHABET);

    private static DecimalFormat df = new DecimalFormat("#");

    public static boolean isNull(Object obj) {
        if (obj == null) {
            return true;
        } else {
            return isNull(obj.toString());
        }
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static String encodePhone(String phone) {
        return phone.substring(0,phone.length()-(phone.substring(3)).length())+"****"+phone.substring(7);
    }

//    /**
//     * 计算年龄
//     */
//    public static int getAge(Date birthDay) throws Exception {
//        Calendar cal = Calendar.getInstance();
//
//        if (cal.before(birthDay)) {
//            throw new IllegalArgumentException( "生日必须早于当前日期!" );
//        }
//
//        int yearNow = cal.get(Calendar.YEAR);
//        int monthNow = cal.get(Calendar.MONTH)+1;
//        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
//
//        cal.setTime(birthDay);
//        int yearBirth = cal.get(Calendar.YEAR);
//        int monthBirth = cal.get(Calendar.MONTH);
//        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
//
//        int age = yearNow - yearBirth;
//
//        if (monthNow <= monthBirth) {
//            if (monthNow == monthBirth) {
//                //monthNow==monthBirth
//                if (dayOfMonthNow < dayOfMonthBirth) {
//                    age--;
//                }
//            } else {
//                //monthNow>monthBirth
//                age--;
//            }
//        }
//        return age;
//    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return true：为空； false：非空
     */
    public static boolean isNull(String str) {
        if (str != null && !str.trim().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * MD5 加密
     */
    public static String encryptMD5(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            //System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }
    /**
     * Generate Random Passcode
     *
     * @return
     */
    public static String generatePasscode() {
        return encryptMD5(KeyGenerators.string().generateKey());
    }

    /**
     * 得到抛异常的信息
     *
     * @param t
     * @return
     */
    public static String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    public static Date string2Date(String str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date string2Date(String date,String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }


    public static Date object2Date(Object date,String format) {
        try {
            if(date==null){
                return null;
            }
            return new SimpleDateFormat(format).parse(date.toString());
        } catch (ParseException e) {
            return null;
        }
    }

    public static String date2String(Date date) {
        return date2String(date, "yyyy-MM-dd");
    }

    public static String date2StringMDH(Date date) {
        return date2String(date, "M月d日H点");
    }

    public static String date2StringMDHM(Date date) {
        return date2String(date, "M月d日 H:mm");
    }

    public static String date2String(Date date, String dateFormat) {
        return date == null ? " " : new SimpleDateFormat(dateFormat).format(date);
    }

    public static Date today() {
        return string2Date(date2String(new Date()));
    }

    public static java.sql.Date dateOfFirstDayOfWeek() {
        return java.sql.Date.valueOf(LocalDate.now().with(java.time.DayOfWeek.MONDAY));
    }

    /**
     * Convert from Date to LocalDate
     *
     * @param input
     * @return
     */
    public static LocalDate date2LocalDate(Date input) {
        return input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime date2LocalDateTime(Date input) {
        return input.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date localDateTime2Date(LocalDateTime input) {
        return Date.from(input.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date localDate2Date(LocalDate input) {
        return Date.from(LocalDateTime.of(input, LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date sqlDateTime2Date(java.sql.Date sqlDate, java.sql.Time sqlTime) {
        return Date.from(LocalDateTime.of(sqlDate.toLocalDate(), sqlTime.toLocalTime()).atZone(ZoneId.systemDefault()).toInstant());
    }


    public static boolean isMobileNumber(String mobiles) {
        Pattern p = Pattern.compile("^1[3-9]\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean regexpMatcher(String value, String regexp) {
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(value);
        return m.matches();
    }

    public static Date getLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        return cal.getTime();
    }

    public static Date getLastMonth() {
        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.MONTH, false);
        return cal.getTime();
    }

    public static Date getLast3Month() {
        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.MONTH, false);
        cal.roll(Calendar.MONTH, false);
        cal.roll(Calendar.MONTH, false);
        return cal.getTime();
    }

    public static Date getLastYear() {
        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.YEAR, false);
        return cal.getTime();
    }

    public static String generateValidateCode() {
        Random rand = new Random();
        int tmp = Math.abs(rand.nextInt());
        return String.valueOf(tmp % (9999 - 1000 + 1) + 1000);
    }

    public static Date getDateOffDay(Date date,int offset){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_WEEK,offset);
        return cal.getTime();
    }

    /**
     * Return unguessable string value of id
     *
     * @param id
     * @return
     */
    public static String alphabetHashidEncode(long id) {
        return hashids_alphabet.encode(id);
    }

    /**
     * Decode encoded hashid
     *
     * @param hash
     * @return decoded long value, or -1L if invalid hash passed in
     */
    public static long alphabetHashidDecode(String hash) {
        try {
            long[] ids = hashids_alphabet.decode(hash);
            if (ids.length == 0) {
                return -1L;
            } else {
                return ids[0];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return -1L;
        }
    }

    public static Object[] arrayConcat(Object[] a, Object[] b) {
        return Stream.concat(Arrays.stream(a), Arrays.stream(b)).toArray(Object[]::new);
    }

    /**
     * windows平台返回的uri中的分隔符是"\\",需统一转换为unix平台的"/"
     * @Auth wdwei
     * @Date 2015-08-04
     */
    public static String getImageUri(String first, String... more) {
        String uri = Paths.get(first,more).toString();
        if (File.separator.equals("\\")) {
            uri = uri.replace("\\","/");
        }
        return uri;
    }

    /**
     * 私有文件保存的时候截取？之前的url
     * @param materialUrl
     * @return
     */
    public static String getProtectedMaterialUrl (String materialUrl) {
        return materialUrl.substring(0, materialUrl.indexOf("?") == -1 ? materialUrl.length() : materialUrl.indexOf("?"));
    }

    /**
     * 通过userAgent比较版本
     * @param userAgent
     * @param version 目标版本
     * @return
     */
    public static int versionCompareByUserAgent(String userAgent,String version){
        try {
            if(userAgent==null||userAgent.isEmpty()){
                logger.warn("userAgent为空,版本不比较");
                return 0;
            }else{
                int start = userAgent.indexOf("SKWY/");
                if (start<0){
                    logger.warn("userAgent找不到版本标记位");
                    return 0;
                }
                int end = userAgent.indexOf(" ",start);
                String versionInfo = userAgent.substring(start,end);
                String[] ss = versionInfo.split("/");
                return versionCompare(ss[1],version);
            }
        } catch (Exception e) {
            logger.error("通过userAgent比较版本异常",e);
            return 0;
        }
    }

    /**
     * Compares two version strings.
     *
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     *
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     *
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2.
     *         The result is a positive integer if str1 is _numerically_ greater than str2.
     *         The result is zero if the strings are _numerically_ equal.
     */
    public static Integer versionCompare(String str1, String str2)
    {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i]))
        {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length)
        {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        else
        {
            return Integer.signum(vals1.length - vals2.length);
        }
    }

    /**
     * 前面补0
     * @param s
     * @param size
     * @return
     */
    public static String appendPreZero(String s,int size){
        if(s==null||s.length()>=size){
            return s;
        }else{
            int n = size-s.length();
            for(int i=0;i<n;i++){
                s="0"+s;
            }
            return s;
        }
    }
}

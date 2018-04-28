/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis.api.common;


import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
/**
 *
 * @author huynct
 */
public class CommonFunction {

    public static String getCurrentDate() {

       
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        fmt.setCalendar(cal);
        String currDate = fmt.format(cal.getTimeInMillis());

        return currDate;
    }

 
         public static String getCurrentDateTime(long time) {

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmt.setCalendar(cal);
        String currDateTime = fmt.format(time);

        return currDateTime;
    }

    public static String getCurrentDateTime() {

    
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmt.setCalendar(cal);
        String currDateTime = fmt.format(cal.getTimeInMillis());

        return currDateTime;
    }

    public static String getCurrentYYMMdd() {

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat("yyMMdd");
        fmt.setCalendar(cal);
        String currDateTime = fmt.format(cal.getTimeInMillis());

        return currDateTime;
    }

    public static String getCurrentYYYYMMdd() {

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        fmt.setCalendar(cal);
        String currDateTime = fmt.format(cal.getTimeInMillis());

        return currDateTime;
    }

    public static String quoteSQL(String input) {

        //String output = input.replace("\\", "\\\\");
        //output = output.replace("'", "\\'");
        String output = input.replace("\\", "");
        output = output.replace("'", "");
        return output;
    }

    public static String quoteDataToInsertToDatabase(String input) {

        //String output = input.replace("\\", "\\\\");
        //output = output.replace("'", "\\'");
        String output = input.replace("\\", "\\\\\\");
      
        return output;
    }
    public static String getStringCurrentTimeMillis() {

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        return Long.toString(cal.getTimeInMillis());
    }

    public static long getCurrentTimeMillis() {

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        return cal.getTimeInMillis();
    }


    public static void convertResultSetToObjectList(ResultSet resultSet, List<Map<String, Object>> objectList) {

        try {
            while (resultSet.next()) {

                Map<String, Object> obj = new HashMap<>();
                int colCount = resultSet.getMetaData().getColumnCount();

                for (int i = 0; i < colCount; i++) {

                    String colTypeName = resultSet.getMetaData().getColumnTypeName(i + 1);
                    Object colVale = resultSet.getObject(i + 1);

                    if (colVale == null) {
                        colVale = "null";
                    } else {
                        String colStringVale = colVale.toString();
                        if (colTypeName.equalsIgnoreCase("DATETIME")) {
                            if (colStringVale.length() >= 2 && colStringVale.charAt(colStringVale.length() - 2) == '.') {
                                colVale = colStringVale.substring(0, colStringVale.length() - 2);
                            }
                        } else if (colStringVale.equals("true") || colStringVale.equals("false")) {
                            colVale = convertBooleanStringIntoInt(colStringVale);
                        }
                    }

                    obj.put(resultSet.getMetaData().getColumnLabel(i + 1), colVale);
                }
                objectList.add(obj);
            }
        } catch (Exception ex) {

        }
    }

    public static void convertResultToObject(ResultSet resultSet, Map<String, Object> obj) {

        try {

            //Map<String, Object> obj = new HashMap<>();
            int colCount = resultSet.getMetaData().getColumnCount();

            for (int i = 0; i < colCount; i++) {

                String colTypeName = resultSet.getMetaData().getColumnTypeName(i + 1);
                Object colVale = resultSet.getObject(i + 1);

                if (colVale == null) {
                    colVale = "null";
                } else {
                    String colStringVale = colVale.toString();
                    if (colTypeName.equalsIgnoreCase("DATETIME")) {
                        if (colStringVale.length() >= 2 && colStringVale.charAt(colStringVale.length() - 2) == '.') {
                            colVale = colStringVale.substring(0, colStringVale.length() - 2);
                        }
                    } else if (colStringVale.equals("true") || colStringVale.equals("false")) {
                        colVale = convertBooleanStringIntoInt(colStringVale);
                    }
                }

                obj.put(resultSet.getMetaData().getColumnLabel(i + 1), colVale);
            }
        } catch (Exception ex) {

        }
    }

    private static int convertBooleanStringIntoInt(String bool) {

        if (bool.equals("true")) {
            return 1;
        }

        return 0;
    }

}

package kz.beeset.med.admin.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс содержит различные методы конвертации
 */
public class Convertors {

    /**
     * Метод конвертирует строку в java.util.Date и выбрасывает ошибку, если возникнет
     */
    public static java.util.Date stringToDate(String date, String format) throws ParseException {
        DateFormat frmt = new SimpleDateFormat(format);
        java.util.Date utilDate = frmt.parse(date);
        return utilDate;
    }

    /**
     * Метод конвертирует строку в java.util.Date, если возникнет ошибка, то возвращает null, без выброса ошибки
     */
    public static java.util.Date stringToDateSafe(String date, String format){
        DateFormat frmt = new SimpleDateFormat(format);
        java.util.Date utilDate;
        try {
            utilDate = frmt.parse(date);
        } catch (ParseException e) {
            return null;
        }
        return utilDate;
    }


    /**
     * Метод конвертирует строку в java.sql.Date и выбрасывает ошибку, если возникнет
     */
    public static java.sql.Date stringToSqlDate(String date, String format) throws ParseException {
        DateFormat frmt = new SimpleDateFormat(format);
        java.util.Date utilDate = frmt.parse(date);
        return new java.sql.Date(utilDate.getTime());
    }

    /**
     * Метод конвертирует строку в java.sql.Date, если возникнет ошибка, то возвращает null, без выброса ошибки
     */
    public static java.sql.Date stringToSqlDateSafe(String date, String format){
        DateFormat frmt = new SimpleDateFormat(format);
        java.util.Date utilDate;
        try {
            utilDate = frmt.parse(date);
        } catch (ParseException e) {
            return null;
        }
        return new java.sql.Date(utilDate.getTime());
    }

    /**
     * Метод конвертирует строку формата yyyy-MM-dd'T'HH:mm:ss.SSSZ в ZonedDateTime
     */
    public static ZonedDateTime stringToZanedDate(String date) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        DateTimeFormatter Parser = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault());
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(date,Parser);
        return zonedDateTime;
    }
}

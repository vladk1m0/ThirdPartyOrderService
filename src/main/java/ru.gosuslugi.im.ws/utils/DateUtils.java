package ru.gosuslugi.im.ws.utils;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    public static XMLGregorianCalendar getCalendarNow() {
        try {
            final GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(new Date());
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

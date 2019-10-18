package kz.beeset.med.device.utils;

import com.thoughtworks.xstream.XStream;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

public class MySerialization {

    public static String exceptionToString(Exception e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        ps.println("Error message: " + e.getMessage());
        ps.println("Cause: " + e.getCause());
        e.printStackTrace(ps);
        String error = baos.toString();
        return error;
    }

    public static <T> String OToXml(T object, String alias) {
        try {
            if (object != null) {
                XStream xstream = new XStream();
                xstream.alias(alias, object.getClass());
                String xml = xstream.toXML(object);
                return xml;
            } else {
                return "null";
            }
        } catch (Exception ex) {
            return "Exception [" + ex.getMessage() + "] for alias [" + alias + "] when " + Thread.currentThread().getStackTrace()[1];
        }
    }

    public static <T> T XmlToO(String xml) {
        XStream xstream = new XStream();
        T object = (T) xstream.fromXML(xml);
        return object;
    }
    /*
    static public String OToS(Object obj) throws Exception {
        BASE64Encoder encode = new BASE64Encoder();
        long start = System.currentTimeMillis();
        String out = null;
        if (obj != null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                oos.close();
                out = encode.encode(baos.toByteArray());
            } catch (IOException e) {
                //System.out.println("e:" + e.getMessage());
                throw e;
            }
        }
        long end = System.currentTimeMillis();
        //System.out.println("Encode:" + (end - start));
        return out;
    }

    static public Object SToO(String str) throws Exception {
        BASE64Decoder decode = new BASE64Decoder();
        long start = System.currentTimeMillis();
        Object out = null;
        if (str != null) {
            try {

                ByteArrayInputStream bios = new ByteArrayInputStream(decode.decodeBuffer(str));
                ObjectInputStream ois = new ObjectInputStream(bios);
                out = ois.readObject();
                ois.close();
            } catch (IOException e) {
                throw e;
            } catch (ClassNotFoundException e) {
                throw e;
            }
        }
        long end = System.currentTimeMillis();
        //System.out.println("Decode:" + (end - start));
        return out;
    }*/
}

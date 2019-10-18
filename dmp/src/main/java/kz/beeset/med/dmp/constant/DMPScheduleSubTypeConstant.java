package kz.beeset.med.dmp.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DMPScheduleSubTypeConstant {

    public final static DMPScheduleSubTypeObject SINGLE = new DMPScheduleSubTypeObject("Разовый","single");
    public final static DMPScheduleSubTypeObject MULTIPLE = new DMPScheduleSubTypeObject("Множественный", "multiple");
    public final static DMPScheduleSubTypeObject CYCLIC = new DMPScheduleSubTypeObject("Цикличный", "cyclic");


    public static Map<String, DMPScheduleSubTypeObject> getMap(){
        Map<String, DMPScheduleSubTypeObject> map = new HashMap<>();
        map.put(SINGLE.code, SINGLE);
        map.put(MULTIPLE.code, MULTIPLE);
        map.put(CYCLIC.code, CYCLIC);
        return map;
    }

    public static List<DMPScheduleSubTypeObject> getList(){
        List<DMPScheduleSubTypeObject> list = new ArrayList<>();
        list.add(DMPScheduleSubTypeConstant.SINGLE);
        list.add(DMPScheduleSubTypeConstant.MULTIPLE);
        list.add(DMPScheduleSubTypeConstant.CYCLIC);
        return list;
    }

    public static class DMPScheduleSubTypeObject{
        public String code;
        public String name;

        public DMPScheduleSubTypeObject(String name, String code){
            this.name = name;
            this.code = code;
        }
    }

}

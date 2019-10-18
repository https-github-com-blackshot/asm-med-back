package kz.beeset.med.dmp.constant;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class DMPPatientAppealConstants {

    @Getter
    @Setter
    public static class Status {

        public String code;
        public String nameRu;
        public String nameEn;
        public String nameKz;

    }

    @Getter
    @Setter
    public static class EventType {

        public String code;
        public String nameRu;
        public String nameEn;
        public String nameKz;

    }

    public static List<Status> getStatusList(){
        List<Status> statusList = new ArrayList<>();

        Status status1 = new Status();
        status1.setCode("active");
        status1.setNameEn("Active");
        status1.setNameRu("Активный");
        status1.setNameKz("Белсенді");

        statusList.add(status1);

        Status status2 = new Status();
        status2.setCode("completed");
        status2.setNameEn("Completed");
        status2.setNameRu("Завершенный");
        status2.setNameKz("Аяқталған");

        statusList.add(status2);

        return statusList;
    }

    public static List<EventType> getEventTypeList() {
        List<EventType> eventTypeList = new ArrayList<>();

        EventType eventType1 = new EventType();
        eventType1.setCode("operation_performed");
        eventType1.setNameEn("Operation was performed");
        eventType1.setNameRu("Проведена операция");
        eventType1.setNameKz("Операция жасалды");

        eventTypeList.add(eventType1);

        EventType eventType2 = new EventType();
        eventType2.setCode("in_hospital");
        eventType2.setNameEn("In hospital");
        eventType2.setNameRu("В больнице");
        eventType2.setNameKz("Ауруханада");

        eventTypeList.add(eventType2);

        return eventTypeList;
    }

}

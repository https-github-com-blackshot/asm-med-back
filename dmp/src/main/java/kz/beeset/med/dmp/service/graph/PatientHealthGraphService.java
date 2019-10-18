package kz.beeset.med.dmp.service.graph;

import com.google.gson.Gson;
import kz.beeset.med.dmp.model.BaseAuditable;
import kz.beeset.med.dmp.model.DMPDeviceStat;
import kz.beeset.med.dmp.model.DMPDeviceStatConfig;
import kz.beeset.med.dmp.model.graph.PatientHealthGraph;
import kz.beeset.med.dmp.model.graph.components.Color;
import kz.beeset.med.dmp.model.graph.components.DeviceParameters;
import kz.beeset.med.dmp.model.graph.components.Options;
import kz.beeset.med.dmp.repository.DMPDeviceStatConfigRepository;
import kz.beeset.med.dmp.repository.DMPDeviceStatRepository;
import kz.beeset.med.dmp.utils.error.ErrorCode;
import kz.beeset.med.dmp.utils.error.InternalException;
import kz.beeset.med.dmp.utils.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class PatientHealthGraphService implements IPatientHealthGraphService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientHealthGraphService.class);
    private final InternalExceptionHelper IE_HELPER = new InternalExceptionHelper(this.toString());

    @Autowired
    private DMPDeviceStatRepository dmpDeviceStatRepository;
    @Autowired
    private DMPDeviceStatConfigRepository dmpDeviceStatConfigRepository;

    @Override
    public Object getGraphByDMPPatientId(String dmpPatientId) throws InternalException {
        try {
            List<DMPDeviceStat> dmpDeviceStats = dmpDeviceStatRepository.findAllByDmpPatientIdAndStateAndCheckDateBetweenOrderByCheckDateAsc(dmpPatientId, 1, LocalDateTime.now().minusDays(7), LocalDateTime.now());

            List<DMPDeviceStat> dmpDeviceStatList = dmpDeviceStats.stream().sorted(Comparator.comparing(DMPDeviceStat::getCheckDate)).collect(Collectors.toList());

            DMPDeviceStatConfig dmpDeviceStatConfig = dmpDeviceStatConfigRepository.getByDMPPatientId(dmpPatientId);

            LinkedHashMap<String, LinkedHashMap<String, List<Object>>> dateParamValueListMap = new LinkedHashMap<>();
            LinkedHashMap<String, List<String>> dateLabelsMap = new LinkedHashMap<>();

            dmpDeviceStatList.forEach(dmpDeviceStat -> {
                LocalDateTime date = dmpDeviceStat.getCheckDate();
                String createDate = date.getDayOfMonth() + " " + date.getMonth().getDisplayName(TextStyle.FULL, new Locale("ru")) + ", " + date.getYear() + "г.";
                String time = date.getHour() + ":" + date.getMinute();

                LOGGER.info("Created Date: " + createDate);

                if (dateParamValueListMap.get(createDate) == null || dateParamValueListMap.get(createDate).size() == 0) {
                    LinkedHashMap<String, List<Object>> paramValueListMap = new LinkedHashMap<>();
                    dmpDeviceStat.getParameterMap().keySet().forEach(key -> {
                        List<Object> newList = new ArrayList<>();
                        newList.add(dmpDeviceStat.getParameterMap().get(key));
                        paramValueListMap.put(key, newList);
                    });

                    List<String> labels = new ArrayList<>();
                    labels.add(time);

                    dateLabelsMap.put(createDate, labels);

                    dateParamValueListMap.put(createDate, paramValueListMap);
                } else {
                    LinkedHashMap<String, List<Object>> paramValueListMap = dateParamValueListMap.get(createDate);

                    dmpDeviceStat.getParameterMap().keySet().forEach(key -> {
                        List<Object> valuelist = paramValueListMap.get(key);
                        valuelist.add(dmpDeviceStat.getParameterMap().get(key));
                        paramValueListMap.put(key, valuelist);
                    });

                    List<String> labels = dateLabelsMap.get(createDate);
                    labels.add(time);

                    dateLabelsMap.put(createDate, labels);

                    dateParamValueListMap.put(createDate, paramValueListMap);
                }
            });

            List<String> staticColors = new ArrayList<>();
            staticColors.add("#FC5A52");
            staticColors.add("#FCA752");
            staticColors.add("#FCD852");
            staticColors.add("#FCFC52");
            staticColors.add("#B6FC52");
            staticColors.add("#52FCED");
            staticColors.add("#527BFC");
            staticColors.add("#9552FC");

            List<Color> colors = new ArrayList<>();

            staticColors.forEach(colorText -> {
                Color color = new Color();
                color.setBackgroundColor("transparent");
                color.setBorderColor(colorText);
                color.setPointBackgroundColor(colorText);
                color.setPointHoverBackgroundColor(colorText);
                color.setPointBorderColor("#ffffff");
                color.setPointHoverBorderColor("#ffffff");
                colors.add(color);
            });

            LinkedHashMap<String, List<DeviceParameters>> datasets = new LinkedHashMap<>();
            AtomicInteger i = new AtomicInteger();
            dateParamValueListMap.keySet().forEach(date -> {
                List<DeviceParameters> deviceParametersList = new ArrayList<>();

                LinkedHashMap<String, List<Object>> paramValueListMap = dateParamValueListMap.get(date);
                paramValueListMap.keySet().forEach(param -> {
                    DeviceParameters deviceParameters = new DeviceParameters();
                    deviceParameters.setLabel(param.equals("pulse")?"Пульс":"Шаги");
                    deviceParameters.setYAxisID(param.equals("pulse")?"A":"B");
                    deviceParameters.setLineTension(0);
                    deviceParameters.setData(paramValueListMap.get(param));
                    deviceParameters.setFill("start");

                    deviceParametersList.add(deviceParameters);
                });

                datasets.put(date, deviceParametersList);
            });

            PatientHealthGraph patientHealthGraph = new PatientHealthGraph();

            patientHealthGraph.setChartType("line");
            patientHealthGraph.setDatasets(datasets);
            patientHealthGraph.setLabels(dateLabelsMap);
            patientHealthGraph.setColors(colors);
            patientHealthGraph.setOptions(Options.getDefauleOptions());

            return patientHealthGraph;

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw IE_HELPER.generate(ErrorCode.ErrorCodes.SYSTEM_ERROR, "Exception:-" +
                    "PatientHealthGraph getGraphByDMPPatientId(String dmpPatientId)" + "-", e);
        }
    }

}

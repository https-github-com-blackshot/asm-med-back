package kz.beeset.med.device.service.implementation;

import kz.beeset.med.device.constant.DefaultConstant;
import kz.beeset.med.device.model.DeviceStat;
import kz.beeset.med.device.model.DeviceStatConfig;
import kz.beeset.med.device.model.graph.PatientHealthGraph;
import kz.beeset.med.device.model.graph.components.Color;
import kz.beeset.med.device.model.graph.components.DeviceParameters;
import kz.beeset.med.device.model.graph.components.Options;
import kz.beeset.med.device.repository.DeviceStatConfigRepository;
import kz.beeset.med.device.repository.DeviceStatRepository;
import kz.beeset.med.device.service.interfaces.IPatientHealthGraphService;
import kz.beeset.med.device.utils.error.ErrorCode;
import kz.beeset.med.device.utils.error.InternalException;
import kz.beeset.med.device.utils.error.InternalExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private DeviceStatRepository deviceStatRepository;
    @Autowired
    private DeviceStatConfigRepository deviceStatConfigRepository;

    @Override
    public Object getGraphByPatientUserId(String patientUserId) throws InternalException {
        try {
            List<DeviceStat> deviceStats = deviceStatRepository.findAllByUserIdAndStateAndCheckDateBetweenOrderByCheckDateAsc(patientUserId, DefaultConstant.STATUS_ACTIVE, LocalDateTime.now().minusDays(7), LocalDateTime.now());

            List<DeviceStat> deviceStatList = deviceStats.stream().sorted(Comparator.comparing(DeviceStat::getCheckDate)).collect(Collectors.toList());

            DeviceStatConfig deviceStatConfig = deviceStatConfigRepository.getByUserId(patientUserId);

            LinkedHashMap<String, LinkedHashMap<String, List<Object>>> dateParamValueListMap = new LinkedHashMap<>();
            LinkedHashMap<String, List<String>> dateLabelsMap = new LinkedHashMap<>();

            deviceStatList.forEach(deviceStat -> {
                LocalDateTime date = deviceStat.getCheckDate();
                String createDate = date.getDayOfMonth() + " " + date.getMonth().getDisplayName(TextStyle.FULL, new Locale("ru")) + ", " + date.getYear() + "г.";
                String time = date.getHour() + ":" + date.getMinute();

                LOGGER.info("Created Date: " + createDate);

                if (dateParamValueListMap.get(createDate) == null || dateParamValueListMap.get(createDate).size() == 0) {
                    LinkedHashMap<String, List<Object>> paramValueListMap = new LinkedHashMap<>();
                    deviceStat.getParameterMap().keySet().forEach(key -> {
                        List<Object> newList = new ArrayList<>();
                        newList.add(deviceStat.getParameterMap().get(key));
                        paramValueListMap.put(key, newList);
                    });

                    List<String> labels = new ArrayList<>();
                    labels.add(time);

                    dateLabelsMap.put(createDate, labels);

                    dateParamValueListMap.put(createDate, paramValueListMap);
                } else {
                    LinkedHashMap<String, List<Object>> paramValueListMap = dateParamValueListMap.get(createDate);

                    deviceStat.getParameterMap().keySet().forEach(key -> {
                        List<Object> valuelist = paramValueListMap.get(key);
                        valuelist.add(deviceStat.getParameterMap().get(key));
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
                    "PatientHealthGraph getGraphByPatientUserId(String dmpPatientId)" + "-", e);
        }
    }

}

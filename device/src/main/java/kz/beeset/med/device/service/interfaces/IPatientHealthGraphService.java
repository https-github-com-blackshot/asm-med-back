package kz.beeset.med.device.service.interfaces;


import kz.beeset.med.device.utils.error.InternalException;

public interface IPatientHealthGraphService {

    Object getGraphByPatientUserId(String patientUserId) throws InternalException;

}

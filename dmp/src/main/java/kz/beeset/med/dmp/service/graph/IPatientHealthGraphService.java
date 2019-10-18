package kz.beeset.med.dmp.service.graph;

import kz.beeset.med.dmp.model.graph.PatientHealthGraph;
import kz.beeset.med.dmp.utils.error.InternalException;

public interface IPatientHealthGraphService {

    Object getGraphByDMPPatientId(String dmpPatientId) throws InternalException;

}

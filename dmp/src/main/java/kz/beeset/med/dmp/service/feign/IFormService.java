package kz.beeset.med.dmp.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
@FeignClient(name = "med-constructor")
public interface IFormService {

    @RequestMapping(method = RequestMethod.GET, value = "/forms/read/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<?> get(@PathVariable("id") String id);

}

package kz.beeset.med.dmp.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Service
@FeignClient(name = "med-admin")
public interface IAdminService {

    @RequestMapping(method = RequestMethod.GET, value = "/users/read/{userId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<?> getUser(@PathVariable("userId") String userId);

    @RequestMapping(method = RequestMethod.GET, value = "/users/search", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<?> searchUsers(@RequestParam Map<String, String> allRequestParams);


}

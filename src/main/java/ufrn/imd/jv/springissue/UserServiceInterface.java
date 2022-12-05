package ufrn.imd.jv.springissue;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient("SPRINGUSER")
public interface UserServiceInterface {
    @RequestMapping(method = RequestMethod.GET, value = "/users/{id}")
    ResponseEntity<Map<String, String>> getUser(@PathVariable Long id);
}

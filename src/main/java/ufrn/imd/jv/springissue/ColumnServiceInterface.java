package ufrn.imd.jv.springissue;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient("SPRINGCOLUMN")
public interface ColumnServiceInterface {
    @RequestMapping(method = RequestMethod.GET, value = "/columns/{id}")
    ResponseEntity<Map<String, String>> getColumn(@PathVariable Long id);
}

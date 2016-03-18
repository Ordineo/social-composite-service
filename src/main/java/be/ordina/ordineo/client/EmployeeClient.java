package be.ordina.ordineo.client;

import be.ordina.ordineo.resource.EmployeeResource;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Hans on 17/03/16.
 */
@FeignClient("https://employee-ordineo")
public interface EmployeeClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/employees/search/employee{username}")
    EmployeeResource  getEmployee(@PathVariable("username") String username);



}

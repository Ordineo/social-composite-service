package be.ordina.ordineo.service.impl;

import be.ordina.ordineo.client.EmployeeClient;
import be.ordina.ordineo.resource.EmployeeResource;
import be.ordina.ordineo.service.LinkedInService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Hans on 17/03/16.
 */
@Service
public class LinkedInServiceImpl implements LinkedInService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private EmployeeClient employeeClient;

    @Override
    public EmployeeResource applyLinkedInDataToEmployee(String username, LinkedIn linkedIn) {
        LinkedInProfileFull profile = linkedIn.profileOperations().getUserProfileFull();
        EmployeeResource employee = employeeClient.getEmployee(username);
        employee.setFirstName(profile.getFirstName());
        employee.setLastName(profile.getLastName());
        try {
            getUserPhotoUrl(linkedIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        employee.setDescription(profile.getSummary());
        employee.setLinkedin(profile.getPublicProfileUrl());
        employee.setFunction(profile.getHeadline());

        employeeClient.synchronizeEmployee(employee);

        return employee;
    }

    public String getUserPhotoUrl(LinkedIn linkedIn) throws IOException {
        String json = linkedIn.restOperations().getForObject(URIBuilder.fromUri("https://api.linkedin.com/v1/people/~/picture-urls::(original)").build(), String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree( json );

        return jsonNode.get("values").get(0).asText();
    }

}

package be.ordina.ordineo.service.impl;

import be.ordina.ordineo.client.EmployeeClient;
import be.ordina.ordineo.client.ImageClient;
import be.ordina.ordineo.resource.EmployeeResource;
import be.ordina.ordineo.service.LinkedInService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;

/**
 * Created by Hans on 17/03/16.
 */
@Service
@Slf4j
public class LinkedInServiceImpl implements LinkedInService {

    @Autowired
    private EmployeeClient employeeClient;

    @Autowired
    private ImageClient imageClient;

    @Override
    public void synchronizeEmployee(String username, LinkedIn linkedIn) {
        try {
            this.applyLinkedInDataToEmployee(username, linkedIn);
        } catch(Exception e) {
            log.error("Error when syncing basic employee data", e);
        }

        try {
            this.applyUserProfilePicture(username, linkedIn);
        } catch(Exception e) {
            log.error("Error when syncing profile picture", e);
        }
    }

    protected void applyLinkedInDataToEmployee(String username, LinkedIn linkedIn) {
        LinkedInProfileFull profile = linkedIn.profileOperations().getUserProfileFull();
        EmployeeResource employee = employeeClient.getEmployee(username);
        employee.setFirstName(profile.getFirstName());
        employee.setLastName(profile.getLastName());
        employee.setDescription(profile.getSummary());
        employee.setLinkedin(profile.getPublicProfileUrl());
        employee.setFunction(profile.getHeadline());

        employeeClient.synchronizeEmployee(employee);
    }

    protected void applyUserProfilePicture(String username, LinkedIn linkedIn) throws IOException {
        URI uri = URIBuilder.fromUri("https://api.linkedin.com/v1/people/~/picture-urls::(original)").build();
        String json = linkedIn.restOperations().getForObject(uri, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree( json );

        String profilePictureUrl = jsonNode.get("values").get(0).asText();

        imageClient.synchronizeProfilePicture(username, profilePictureUrl);
    }

}

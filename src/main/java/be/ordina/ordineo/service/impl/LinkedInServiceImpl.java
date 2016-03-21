package be.ordina.ordineo.service.impl;

import be.ordina.ordineo.client.EmployeeClient;
import be.ordina.ordineo.resource.EmployeeResource;
import be.ordina.ordineo.service.LinkedInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.stereotype.Service;

/**
 * Created by Hans on 17/03/16.
 */
@Service
public class LinkedInServiceImpl implements LinkedInService {

    @Autowired
    private EmployeeClient employeeClient;


    @Override
    public EmployeeResource ApplyLinkedInDataToEmployee(String username, LinkedIn linkedIn) {
        LinkedInProfile profile = linkedIn.profileOperations().getUserProfileFull();
        EmployeeResource employee = employeeClient.getEmployee(username);
        employee.setFirstName(profile.getFirstName());
        employee.setLastName(profile.getLastName());
        employee.setEmail(profile.getEmailAddress());
        employee.setProfilePicture(profile.getProfilePictureUrl());
        employee.setDescription(profile.getSummary());
        employee.setLinkedin(profile.getPublicProfileUrl());
        employee.setFunction(profile.getHeadline());

        employeeClient.synchronizeEmployee(username,employee);

        return employee;
    }
}

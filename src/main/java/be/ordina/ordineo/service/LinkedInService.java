package be.ordina.ordineo.service;

import be.ordina.ordineo.resource.EmployeeResource;
import org.springframework.social.linkedin.api.LinkedIn;

/**
 * Created by Hans on 17/03/16.
 */
public interface LinkedInService
{
    public EmployeeResource ApplyLinkedInDataToEmployee (String username , LinkedIn linkedIn);



}

package be.ordina.ordineo.service;

import be.ordina.ordineo.client.EmployeeClient;
import be.ordina.ordineo.resource.EmployeeResource;
import be.ordina.ordineo.resource.Gender;
import be.ordina.ordineo.resource.Unit;
import be.ordina.ordineo.service.impl.LinkedInServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.social.linkedin.api.*;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by gide on 22/03/16.
 */
public class LinkedInServiceTest {

    @Mock
    EmployeeClient employeeClient;

    @InjectMocks
    LinkedInServiceImpl linkedInService;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void applyLinkedInDataToEmployee_success() {
        String username = "test";

        LinkedIn linkedin = mock(LinkedIn.class);
        ProfileOperations profileOperations = mock(ProfileOperations.class);
        EmployeeResource resource = new EmployeeResource();
        resource.setUnit(new Unit((long) 1,"Jworks"));
        resource.setGender(Gender.MALE);
        resource.setHireDate(LocalDate.of(2015, Month.AUGUST, 14));
        resource.setStartDate(LocalDate.of(2015, Month.AUGUST, 14));
        resource.setResignationDate(LocalDate.of(2016,Month.MARCH,22));

        resource.setUsername(username);
        resource.setEmail("test email");
        resource.setPhoneNumber("test phoneNumber");
        LinkedInProfileFull profile = new LinkedInProfileFull(username, "Craig", "Walls", "Spring Guy", "Software", "http://www.linkedin.com/in/habuma",
                new UrlResource(null, "http://www.linkedin.com/profile?viewProfile=&key=3630172&authToken=0IpZ&authType=name&trk=api*a121026*s129482*"), "http://media.linkedin.com/mpr/mprx/0_9-Hjc8b0ViE1gGElNtdCcGh0s3pjxbRlNzpCciT05XHD8i2Asq4AM_zAN7yGp8VgcAoi4k1faewD");

        when(linkedin.profileOperations()).thenReturn(profileOperations);
        when(profileOperations.getUserProfileFull()).thenReturn(profile);
        when(employeeClient.getEmployee(username)).thenReturn(resource);

        linkedInService.applyLinkedInDataToEmployee(username, linkedin);

        assertEquals(username, resource.getUsername());
        assertEquals(profile.getFirstName(), resource.getFirstName());
        assertEquals(profile.getLastName(), resource.getLastName());
        assertEquals(profile.getPublicProfileUrl(), resource.getLinkedin());
        assertEquals("test email", resource.getEmail());
        assertEquals("test phoneNumber", resource.getPhoneNumber());
        assertEquals(profile.getHeadline(), resource.getFunction()); //TODO: do we really want to take this from linked in?
        assertEquals(profile.getSummary(), resource.getDescription());
        assertEquals(profile.getProfilePictureUrl(), resource.getProfilePicture());
        assertEquals(profile.getDateOfBirth(), resource.getBirthDate());
        assertEquals(Gender.MALE, resource.getGender());
        assertEquals(LocalDate.of(2015, Month.AUGUST, 14), resource.getStartDate());
        assertEquals(LocalDate.of(2015, Month.AUGUST, 14), resource.getHireDate());
        assertEquals(LocalDate.of(2016, Month.MARCH, 22), resource.getResignationDate());
        assertEquals(Unit.class, resource.getUnit().getClass());

        verify(employeeClient).getEmployee(username);
        verify(employeeClient).synchronizeEmployee(resource);
        verifyNoMoreInteractions(employeeClient);
    }

}

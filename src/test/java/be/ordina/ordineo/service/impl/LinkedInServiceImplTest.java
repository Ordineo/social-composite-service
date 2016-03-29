package be.ordina.ordineo.service.impl;

import be.ordina.ordineo.client.EmployeeClient;
import be.ordina.ordineo.client.ImageClient;
import be.ordina.ordineo.resource.EmployeeResource;
import be.ordina.ordineo.resource.Gender;
import be.ordina.ordineo.resource.Unit;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.linkedin.api.*;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by gide on 22/03/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({URIBuilder.class, LoggerFactory.class})
public class LinkedInServiceImplTest {

    @Mock
    EmployeeClient employeeClient;

    @Mock
    ImageClient imageClient;

    @InjectMocks
    LinkedInServiceImpl linkedInService;

    static Logger mockLog;
    String username;
    LinkedIn linkedIn;

    @BeforeClass
    public static void preSetup() {
        mockStatic(LoggerFactory.class);
        mockLog = mock(Logger.class);
        when(LoggerFactory.getLogger(LinkedInServiceImpl.class)).thenReturn(mockLog);
    }

    @Before
    public void setup() {
        initMocks(this);

        username = "test";
        linkedIn = mock(LinkedIn.class);
    }

    @Test
    public void synchronizeEmployee() throws Exception {
        linkedInService.synchronizeEmployee(username, linkedIn);

        verify(mockLog, times(2)).error(anyString(), any(NullPointerException.class));
    }

    @Test
    public void applyLinkedInDataToEmployee_success() {
        Unit unit = new Unit();
        unit.setId(1L);
        unit.setName("JWorks");

        EmployeeResource resource = new EmployeeResource();
        resource.setId(1L);
        resource.setUnit(unit);
        resource.setGender(Gender.MALE);
        resource.setHireDate(LocalDate.of(2015, Month.AUGUST, 14));
        resource.setStartDate(LocalDate.of(2015, Month.AUGUST, 14));
        resource.setResignationDate(LocalDate.of(2016,Month.MARCH,22));

        resource.setUsername(username);
        resource.setEmail("test email");
        resource.setPhoneNumber("test phoneNumber");
        LinkedInProfileFull profile = new LinkedInProfileFull(username, "Craig", "Walls", "Spring Guy", "Software", "http://www.linkedin.com/in/habuma",
                new UrlResource(null, "http://www.linkedin.com/profile?viewProfile=&key=3630172&authToken=0IpZ&authType=name&trk=api*a121026*s129482*"), "http://media.linkedin.com/mpr/mprx/0_9-Hjc8b0ViE1gGElNtdCcGh0s3pjxbRlNzpCciT05XHD8i2Asq4AM_zAN7yGp8VgcAoi4k1faewD");

        ProfileOperations profileOperations = mock(ProfileOperations.class);
        when(linkedIn.profileOperations()).thenReturn(profileOperations);
        when(profileOperations.getUserProfileFull()).thenReturn(profile);
        when(employeeClient.getEmployee(username)).thenReturn(resource);

        linkedInService.applyLinkedInDataToEmployee(username, linkedIn);

        assertEquals(1, resource.getId().intValue());
        assertEquals(1, resource.getUnit().getId().intValue());
        assertEquals("JWorks", resource.getUnit().getName());

        assertEquals(username, resource.getUsername());
        assertEquals(profile.getFirstName(), resource.getFirstName());
        assertEquals(profile.getLastName(), resource.getLastName());
        assertEquals(profile.getPublicProfileUrl(), resource.getLinkedin());
        assertEquals("test email", resource.getEmail());
        assertEquals("test phoneNumber", resource.getPhoneNumber());
        assertEquals(profile.getHeadline(), resource.getFunction());
        assertEquals(profile.getSummary(), resource.getDescription());
        assertEquals(profile.getDateOfBirth(), resource.getBirthDate());
        assertEquals(Gender.MALE, resource.getGender());
        assertEquals(LocalDate.of(2015, Month.AUGUST, 14), resource.getStartDate());
        assertEquals(LocalDate.of(2015, Month.AUGUST, 14), resource.getHireDate());
        assertEquals(LocalDate.of(2016, Month.MARCH, 22), resource.getResignationDate());
        assertEquals(Unit.class, resource.getUnit().getClass());
        assertEquals(Long.valueOf(1), resource.getId());

        verify(employeeClient).getEmployee(username);
        verify(employeeClient).synchronizeEmployee(resource);
        verifyNoMoreInteractions(employeeClient);
        verifyNoMoreInteractions(imageClient);
    }

    @Test
    public void applyUserProfilePicture_success() throws Exception {
        String baseUri = "https://api.linkedin.com/v1/people/~/picture-urls::(original)";
        String profilePictureUrl = "http://test.com";
        String json = "{\"values\":[\"" + profilePictureUrl + "\"]}";

        RestOperations restOperations = mock(RestOperations.class);

        mockStatic(URIBuilder.class);
        URIBuilder uriBuilder = mock(URIBuilder.class);
        URI uri = new URI(baseUri);

        when(linkedIn.restOperations()).thenReturn(restOperations);

        when(URIBuilder.fromUri(baseUri)).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(uri);
        when(restOperations.getForObject(uri, String.class)).thenReturn(json);

        linkedInService.applyUserProfilePicture(username, linkedIn);

        verify(uriBuilder).build();
        verify(imageClient).synchronizeProfilePicture(username, profilePictureUrl);
        verifyNoMoreInteractions(uriBuilder);
        verifyNoMoreInteractions(employeeClient);
        verifyNoMoreInteractions(imageClient);
    }

}

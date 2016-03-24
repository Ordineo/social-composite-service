package be.ordina.ordineo.rest;

import be.ordina.ordineo.service.LinkedInService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.linkedin.api.LinkedIn;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by gide on 24/03/16.
 */
public class LinkedInRestControllerTest {

    @Mock
    LinkedIn linkedIn;

    @Mock
    LinkedInService linkedInService;

    @InjectMocks
    LinkedInRestController controller;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void syncProfileOk() {
        ResponseEntity result = controller.syncProfile("test");

        assertEquals(HttpStatus.OK, result.getStatusCode());

        verify(linkedInService).applyLinkedInDataToEmployee("test", linkedIn);
    }

}
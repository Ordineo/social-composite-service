package be.ordina.ordineo.client.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by gide on 30/03/16.
 */
public class ImageClientImplTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    ImageClientImpl imageClient;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void synchronizeProfilePicture() throws Exception {
        String username = "test";
        String profilePictureUrl = "http://test.com";

        imageClient.synchronizeProfilePicture(username, profilePictureUrl);

        verify(restTemplate).postForObject(eq("https://image-ordineo/api/images/{username}"), argThat(new HeaderArgumentMatcher(profilePictureUrl)), eq(ResponseEntity.class), eq(username));
    }

    private class HeaderArgumentMatcher extends ArgumentMatcher<String> {
        String profilePictureUrl;

        public HeaderArgumentMatcher(String profilePictureUrl) {
            this.profilePictureUrl = profilePictureUrl;
        }

        @Override
        public boolean matches(Object argument) {
            if (argument instanceof HttpEntity) {
                HttpEntity entity = (HttpEntity) argument;
                return profilePictureUrl.equals(entity.getBody());
            }

            return false;
        }
    }
}
package etf.unsa.ba.nwt.emajstor.review.controller;

import com.jayway.jsonpath.JsonPath;
import etf.unsa.ba.nwt.emajstor.review.dto.User;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class RestTemplateTest {
    private static JSONObject UserJsonObject;
    private static HttpHeaders headers ;
    private User userTest1;
    private User userTest2;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RestTemplate restTemplate;

    @BeforeAll
    public static void runBeforeAllTestMethods() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void addUserTest() throws Exception {
        userTest2 = addUser2();
        userTest1 = addUser1();

        assertNotNull(userTest2);
        assertNotNull(userTest1);
    }

    @Test
    public void addReviewOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"numStars\" : 5  \n," +
                        "\n \"comment\" : \"TEST\"  \n," +
                        "\n \"user\" : \""+userTest1.getId()+"\"  \n," +
                        "\n \"worker\" : \""+userTest2.getId()+"\"  \n}");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void addReviewBadRequest() {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"numStars\" : 5  \n," +
                        "\n \"comment\" : \"TEST\"  \n," +
                        "\n \"user\" : \"f6027b5a-1ac4-4781-9361-d2d0659d09a5\"  \n," +
                        "\n \"worker\" : \""+userTest2.getId()+"\"  \n}");
        try {
            MvcResult result = mockMvc.perform(request)
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String message = JsonPath.read(result.getResponse().getErrorMessage(), "$.message");
            assertEquals(message,"User does not exist.");

        } catch (Exception exception) {

        }
    }

    @Test
    public void addReviewBadRequest1() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"numStars\" : 5  \n," +
                        "\n \"comment\" : \"TEST\"  \n," +
                        "\n \"user\" : \""+userTest1.getId()+"\"  \n," +
                        "\n \"worker\" : \"f6027b5a-1ac4-4781-9361-d2d0659d09a5\"  \n}");
        try {
            MvcResult result = mockMvc.perform(request)
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String message = JsonPath.read(result.getResponse().getErrorMessage(), "$.message");
            assertEquals(message,"User does not exist.");

        } catch (Exception exception) {

        }
    }

    @AfterAll
    public void DeleteUsers() {

        Map< String, String > params = new HashMap<>();
        params.put("id", userTest1.getId().toString());

        restTemplate.delete( "http://user/api/users/{id}", params);

        params = new HashMap<>();
        params.put("id", userTest2.getId().toString());

        restTemplate.delete( "http://user/api/users/{id}", params);
    }


    private User addUser2() throws Exception {
        UserJsonObject = new JSONObject("{\n" +
                "\"username\": \"TEST\",\n" +
                "\"password\": \"test123\",\n" +
                "\"city\": \"Sarajevo\",\n" +
                "\"locationLongitude\": 18.4131,\n"+
                "\"locationLatitude\": 43.8563,\n"+
                "\"dateCreated\": \"2021-03-24T11:08:46.299Z\",\n" +
                "\"role\": \"ROLE_TEST\",\n" +
                "\"contactInfo\":"+
                "{\n" +
                "  \"firstName\": \"TEST\",\n" +
                "  \"lastName\": \"TEST\",\n" +
                "  \"email\": \"test@emajstor.com\",\n" +
                "  \"number\": \"063456245\" \n" +
                "}\n"+
                "}");

        HttpEntity<String> request =
                new HttpEntity<String>(UserJsonObject.toString(), headers);

        User user = restTemplate.postForObject( "http://user/api/users/", request, User.class);

        return user;
    }

    private User addUser1() throws Exception {
        UserJsonObject = new JSONObject("{\n" +
                "\"username\": \"TEST1\",\n" +
                "\"password\": \"test123\",\n" +
                "\"city\": \"Sarajevo\",\n" +
                "\"locationLongitude\": 18.4131,\n"+
                "\"locationLatitude\": 43.8563,\n"+
                "\"dateCreated\": \"2021-03-24T11:08:46.299Z\",\n" +
                "\"role\": \"ROLE_TEST\",\n" +
                "\"contactInfo\":"+
                "{\n" +
                "  \"firstName\": \"TEST1\",\n" +
                "  \"lastName\": \"TEST1\",\n" +
                "  \"email\": \"test1@emajstor.com\",\n" +
                "  \"number\": \"063456245\" \n" +
                "}\n"+
                "}");

        HttpEntity<String> request =
                new HttpEntity<String>(UserJsonObject.toString(), headers);

        User user = restTemplate.postForObject( "http://user/api/users/", request, User.class);

        return user;
    }
}

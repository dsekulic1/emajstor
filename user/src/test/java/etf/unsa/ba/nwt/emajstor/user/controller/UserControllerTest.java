package etf.unsa.ba.nwt.emajstor.user.controller;

import com.jayway.jsonpath.JsonPath;
import etf.unsa.ba.nwt.emajstor.user.model.ContactInfo;
import etf.unsa.ba.nwt.emajstor.user.model.Role;
import etf.unsa.ba.nwt.emajstor.user.model.User;
import etf.unsa.ba.nwt.emajstor.user.repositories.ContactInfoRepository;
import etf.unsa.ba.nwt.emajstor.user.repositories.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static User user1;
    private static User user2;
    private static ContactInfo contactInfo1;
    private static ContactInfo contactInfo2;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  ContactInfoRepository contactInfoRepository;

    private ContactInfo createContactInfo(String firstName, String lastName, String email, String number) {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setFirstName(firstName);
        contactInfo.setLastName(lastName);
        contactInfo.setEmail(email);
        contactInfo.setNumber(number);
        contactInfo = contactInfoRepository.save(contactInfo);
        return contactInfo;
    }

    private User createUser(String username, String password, String city, double locationLongitude, double locationLatitude, Role role, ContactInfo contactInfo) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setCity(city);
        user.setLocationLongitude(locationLongitude);
        user.setLocationLatitude(locationLatitude);
        user.setRole(role);
        user.setDateCreated(LocalDateTime.now());
        user.setContactInfo(contactInfo);
        user = userRepository.save(user);
        System.out.printf("Created user with UUID: %s\n", user.getId().toString());
        return user;
    }

    @BeforeAll
    private void generateDatabase(){
        contactInfo1 = createContactInfo("John", "Smith", "test567@emajstor.ba", "033235884");
        user1 = createUser("john", "test123", "Sarajevo",18.4131,43.8563, Role.ROLE_WORKER, contactInfo1);

        contactInfo2 = createContactInfo("Didier", "Testic", "test1239@emajstor.ba", "033235983");
        user2 = createUser("didier", "test123", "Sarajevo",18.4131,43.8563, Role.ROLE_USER, contactInfo2);
    }

    @AfterAll
    void afterAll() {
        userRepository.deleteById(user1.getId());
        userRepository.deleteById(user2.getId());

        contactInfoRepository.deleteById(contactInfo1.getId());
        contactInfoRepository.deleteById(contactInfo2.getId());
    }



    @Test
    public void getAllUsersOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users/all")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getAllReviewsOkRequest2() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users/all")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))))
                .andReturn();
    }

    @Test
    public void addUserOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"username\": \"muharem\",\n" +
                        "\"password\": \"muharem123\",\n" +
                        "\"city\": \"Sarajevo\",\n" +
                        "\"locationLongitude\": 18.4131,\n"+
                        "\"locationLatitude\": 43.8563,\n"+
                        "\"dateCreated\": \"2021-03-24T11:08:46.299Z\",\n" +
                        "\"role\": \"ROLE_WORKER\",\n" +
                        "\"contactInfo\":"+
                                "{\n" +
                                "  \"firstName\": \"Devon\",\n" +
                                "  \"lastName\": \"Sarajalic\",\n" +
                                "  \"email\": \"zenica@emajstor.com\",\n" +
                                "  \"number\": \"063456245\" \n" +
                                "}\n"+
                        "}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        String contactid = JsonPath.read(result.getResponse().getContentAsString(), "$['contactInfo'].id");

        userRepository.deleteById(UUID.fromString(id));
        contactInfoRepository.deleteById(UUID.fromString(contactid));

    }

    @Test
    public void addUserBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"username\": \"muharem\",\n" +
                        "\"password\": \"muharem123\",\n" +
                        "\"city\": \"Sarajevo\",\n" +
                        "\"locationLongitude\": 18.4131,\n"+
                        "\"locationLatitude\": 43.8563,\n"+
                        "\"dateCreated\": \"2021-03-24T11:08:46.299Z\",\n" +
                        "\"role\": \"ROLE_WORKER\",\n" +
                        "\"contactInfo\":"+
                        "{\n" +
                        "  \"firstName\": \"Devon\",\n" +
                        "  \"lastName\": \"Sarajalic\",\n" +
                        "  \"email\": \"zenica@emajstor.com\",\n" +
                        "  \"number\": \063456245\" \n" +
                        "}\n"+
                        "}");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void getUserByUsernameOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users/username/"+user2.getUsername())
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{" +
                        "\n \"username\" : \"didier\"  ,\n" +
                        "\n \"city\" : \"Sarajevo\"  \n," +
                        "\n \"role\" : \"ROLE_USER\"  \n}"));
    }

    @Test
    public void getUserByUsernameBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users/username/didierdrogba")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void getUserByIdOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users/"+user2.getId())
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{" +
                        "\n \"username\" : \"didier\"  ,\n" +
                        "\n \"city\" : \"Sarajevo\"  \n," +
                        "\n \"role\" : \"ROLE_USER\"  \n}"));

    }

    @Test
    public void getUserByIdBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users/"+user2.getId()+"234")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void deleteUserOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"username\": \"bakir\",\n" +
                        "\"password\": \"muharem123\",\n" +
                        "\"city\": \"Sarajevo\",\n" +
                        "\"locationLongitude\": 18.4131,\n"+
                        "\"locationLatitude\": 43.8563,\n"+
                        "\"dateCreated\": \"2021-03-24T11:08:46.299Z\",\n" +
                        "\"role\": \"ROLE_WORKER\",\n" +
                        "\"contactInfo\":"+
                        "{\n" +
                        "  \"firstName\": \"Bakir\",\n" +
                        "  \"lastName\": \"Izetbegovic\",\n" +
                        "  \"email\": \"bakebake@emajstor.com\",\n" +
                        "  \"number\": \"063156245\" \n" +
                        "}\n"+
                        "}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        request = MockMvcRequestBuilders
                .delete("/api/users/"+id)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void deleteUserBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .delete("/api/users/"+"nekirandomide")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void updateUserBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/users/"+"nekidi123")
                .contentType(MediaType.APPLICATION_JSON)
               .content("{\n" +
                       "\"username\": \"zeljkec123\",\n" +
                       "\"password\": \"muharem123\",\n" +
                       "\"city\": \"Sarajevo\",\n" +
                       "\"locationLongitude\": 18.4131,\n"+
                       "\"locationLatitude\": 43.8563,\n"+
                       "\"role\": \"ROLE_WORKER\",\n" +
                       "\"contactInfo\":"+
                       "{\n" +
                       "  \"firstName\": \"Bakir\",\n" +
                       "  \"lastName\": \"Izetbegovic\",\n" +
                       "  \"email\": \"bakebake123@emajstor.com\",\n" +
                       "  \"number\": \"063156245\" \n" +
                       "}\n"+
                       "}");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();


    }

}

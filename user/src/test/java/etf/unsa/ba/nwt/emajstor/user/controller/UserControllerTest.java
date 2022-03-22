package etf.unsa.ba.nwt.emajstor.user.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getUsers() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users/all")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getUserByID() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users/dd633aee-bf0b-417e-a78e-6be04c75ab05")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\n \"id\" : \"dd633aee-bf0b-417e-a78e-6be04c75ab05\",  \n" +
                        "\n \"username\" : \"tarik\"  ,\n" +
                        "\n \"city\" : \"Tarčin\"  \n," +
                        "\n \"locationLongitude\" : 18.0921  \n," +
                        "\n \"locationLatitude\" : 43.7937  \n," +
                        "\n \"dateCreated\" : \"2022-03-21T21:02:25.078767\"  \n," +
                        "\n \"role\" : \"ROLE_ADMIN\"  \n}"));
    }
    @Test
    public void getUserByUsername() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users/username/tarik")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\n \"id\" : \"dd633aee-bf0b-417e-a78e-6be04c75ab05\",  \n" +
                        "\n \"username\" : \"tarik\"  ,\n" +
                        "\n \"city\" : \"Tarčin\"  \n," +
                        "\n \"locationLongitude\" : 18.0921  \n," +
                        "\n \"locationLatitude\" : 43.7937  \n," +
                        "\n \"dateCreated\" : \"2022-03-21T21:02:25.078767\"  \n," +
                        "\n \"role\" : \"ROLE_ADMIN\"  \n}"));
    }

    @Test
    public void addUser() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\n" +
                        "  \"username\": \"John\",\n" +
                        "  \"password\": \"naprednewebtehnologije\",\n" +
                        "  \"city\": \"Zenica\",\n" +
                        "  \"password\": \"John12!\",\n" +
                        "   \"locationLatitude\" : 50  \n," +
                        "   \"locationLatitude\" : 70  \n," +
                        "   \"dateCreated\": \"2021-03-24T11:08:46.299Z\",\n" +
                        "    \"role\": \"ROLE_ADMIN\",\n" +
                        "}");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void updateUser() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/users/f64cb9a4-b3e8-49d2-bd73-454a75f11d71")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"id\":\"" + "f64cb9a4-b3e8-49d2-bd73-454a75f11d71" + "\", \"username\":\"test data\"}");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();


    }


}

package etf.unsa.ba.nwt.emajstor.review.controller;


import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getReviews() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/review/all")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getReviewByID() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/review/2a21627c-f36b-4955-bc1d-88178cd3835f")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\n \"id\" : \"2a21627c-f36b-4955-bc1d-88178cd3835f\",  \n" +
                        "\n \"numStars\" : 3  \n," +
                        "\n \"comment\" : \"dobar majstor\"  \n," +
                        "\n \"user\" : \"f64cb9a4-b3e8-49d2-bd73-454a75f11d71\"  \n," +
                        "\n \"worker\" : \"50bfde38-7058-4d82-9854-ecc4609ae742\"  \n}"));
    }

    @Test
    public void addReview() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"numStars\" : 3  \n," +
                        "\n \"comment\" : \"dobar majstor\"  \n," +
                        "\n \"user\" : \"f64cb9a4-b3e8-49d2-bd73-454a75f11d71\"  \n," +
                        "\n \"worker\" : \"50bfde38-7058-4d82-9854-ecc4609ae742\"  \n}");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void updateUser() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/review/25facf32-2567-4cd0-b740-b354e45f2e3f")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"numStars\" : 18.0921  \n," +
                        "\n \"comment\" : \"f64cb9a4-b3e8-49d2-bd73-454a75f11d7\"  \n," +
                        "\n \"user\" : \"f64cb9a4-b3e8-49d2-bd73-454a75f11d71\"  \n," +
                        "\n \"worker\" : \"50bfde38-7058-4d82-9854-ecc4609ae742\"  \n}");


        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

}

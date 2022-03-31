package etf.unsa.ba.nwt.emajstor.review.controller;


import com.jayway.jsonpath.JsonPath;
import etf.unsa.ba.nwt.emajstor.review.model.Review;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import etf.unsa.ba.nwt.emajstor.review.repositories.ReviewRepository;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.json.JSONArray;

import java.util.UUID;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static  Review review;

    @Autowired
    private ReviewRepository reviewRepository;

    private Review createReview(int numStars, String comment, UUID user, UUID worker) {
        Review review = new Review();
        review.setNumStars(numStars);
        review.setComment(comment);
        review.setUser(user);
        review.setWorker(worker);
        review = reviewRepository.save(review);

        return review;
    }

    @BeforeAll
    private void generateDatabase(){
        review = createReview(2,"jako povrsan majstor, nije temeljit", UUID.fromString("a1788536-d2b8-4bc5-9609-45d33202058b"), UUID.fromString("8e1eb86e-66e9-4a5d-a75b-470ab4cb70c8"));
    }


    @Test
    public void getAllReviewsOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/review/all")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getAllReviewsOkRequest2() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/review/all")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))))
                .andReturn();
    }

    @Test
    public void addReviewOkRequest() throws Exception {
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
    public void addReviewBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"numStars\" : dobar  \n," +
                        "\n \"comment\" : \"dobar majstor\"  \n," +
                        "\n \"user\" : \"f64cb9a4-b3e8-49d2-bd73-454a75f11d71\"  \n," +
                        "\n \"worker\" : \"50bfde38-7058-4d82-9854-ecc4609ae742\"  \n}");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void getReviewByIDBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/review/2a2333")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void getReviewByIDOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/review/"+review.getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.comment");

        assertEquals(id,review.getComment());

    }

    @Test
    public void deleteReviewByIDOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"numStars\" : 1 \n," +
                        "\n \"comment\" : \"loš majstor\"  \n," +
                        "\n \"user\" : \"f64cb9a4-b3e8-49d2-bd73-454a75f11d71\"  \n," +
                        "\n \"worker\" : \"50bfde38-7058-4d82-9854-ecc4609ae742\"  \n}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        request = MockMvcRequestBuilders
                .delete("/api/review/"+id)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void deleteReviewByIDBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .delete("/api/review/neki423ID")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void updateReviewByIDBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/review/nekiIDpokusaj234")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"numStars\" : 18.0921  \n," +
                        "\n \"comment\" : \"f64cb9a4-b3e8-49d2-bd73-454a75f11d7\"  \n," +
                        "\n \"user\" : \"f64cb9a4-b3e8-49d2-bd73-454a75f11d71\"  \n," +
                        "\n \"worker\" : \"50bfde38-7058-4d82-9854-ecc4609ae742\"  \n}");


        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void updateReviewByIDOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"numStars\" : 5 \n," +
                        "\n \"comment\" : \"predobro uradjeno\"  \n," +
                        "\n \"user\" : \"f64cb9a4-b3e8-49d2-bd73-454a75f11d71\"  \n," +
                        "\n \"worker\" : \"50bfde38-7058-4d82-9854-ecc4609ae742\"  \n}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        request = MockMvcRequestBuilders
                .put("/api/review/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"numStars\" :2 \n," +
                        "\n \"comment\" : \"kriza uradjeno\"  \n," +
                        "\n \"user\" : \"f64cb9a4-b3e8-49d2-bd73-454a75f11d71\"  \n," +
                        "\n \"worker\" : \"50bfde38-7058-4d82-9854-ecc4609ae742\"  \n}");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void PatchReviewByIDOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"numStars\" : 4 \n," +
                        "\n \"comment\" : \"dodajemo usera da mu promijenimo komentar\"  \n," +
                        "\n \"user\" : \"f64cb9a4-b3e8-49d2-bd73-454a75f11d71\"  \n," +
                        "\n \"worker\" : \"50bfde38-7058-4d82-9854-ecc4609ae742\"  \n}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        request = MockMvcRequestBuilders
                .patch("/api/review/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{" +
                        "\n \"op\" :\"replace\"," +
                        "\"path\" : \"/comment\"," +
                        "\"value\" : \"mijenjamo komentar pomocu patch metode\"" +
                        "}]");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        request = MockMvcRequestBuilders
                .get("/api/review/"+id)
                .accept(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String komentar = JsonPath.read(result.getResponse().getContentAsString(), "$.comment");

        assertEquals(komentar,"mijenjamo komentar pomocu patch metode");
    }

    @Test
    public void PatchReviewByIDBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"numStars\" : 4 \n," +
                        "\n \"comment\" : \"dodajemo usera da mu promijenimo komentar\"  \n," +
                        "\n \"user\" : \"f64cb9a4-b3e8-49d2-bd73-454a75f11d71\"  \n," +
                        "\n \"worker\" : \"50bfde38-7058-4d82-9854-ecc4609ae742\"  \n}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        request = MockMvcRequestBuilders
                .patch("/api/review/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{" +
                        "\n \"op\" :\"delete\"," +
                        "\"path\" : \"/randompolje\"," +
                        "}]");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}

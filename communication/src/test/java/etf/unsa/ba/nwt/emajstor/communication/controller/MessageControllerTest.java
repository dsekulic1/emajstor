package etf.unsa.ba.nwt.emajstor.communication.controller;

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
public class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addMessage() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"text\": \"pokusaj testa\",\n" +
                        "\"sender\": \"f64cb9a4-b3e8-49d2-bd73-454a75f11d71\",\n" +
                        "\"receiver\": \"50bfde38-7058-4d82-9854-ecc4609ae742\"\n" +
                        "}");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getMessages() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/messages/all")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getMessageByID() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/messages/6bf5b5ef-d8aa-461a-b374-795f6024f93e")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\n \"id\" : \"6bf5b5ef-d8aa-461a-b374-795f6024f93e\",  \n" +
                        "\n \"text\" : \"Tekst prve poruke\"  ,\n" +
                        "\n \"sender\" : \"f64cb9a4-b3e8-49d2-bd73-454a75f11d71\"  \n," +
                        "\n \"receiver\" : \"50bfde38-7058-4d82-9854-ecc4609ae742\"  \n}"));
    }

    @Test
    public void findAllByReceiver() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/messages/receiver/f64cb9a4-b3e8-49d2-bd73-454a75f11d71")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{\n \"id\" : \"6ed6a7be-3a8c-408d-a033-48dcb459c325\",\n" +
                        "\n \"text\" : \"Tekst druge poruke\"  ,\n" +
                        "\n \"sender\" : \"50bfde38-7058-4d82-9854-ecc4609ae742\"  \n," +
                        "\n \"receiver\" : \"f64cb9a4-b3e8-49d2-bd73-454a75f11d71\"  \n}" +
                        ",{\n \"id\" : \"e2dcca86-bb53-49ed-9929-4798a7744218\",\n" +
                        "\n \"text\" : \"Tekst nte poruke\"  ,\n" +
                        "\n \"sender\" : \"50bfde38-7058-4d82-9854-ecc4609ae742\"  ,\n" +
                        "\n \"receiver\" :\"f64cb9a4-b3e8-49d2-bd73-454a75f11d71\"  \n}]"));
    }

    @Test
    public void findAllBySender() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/messages/sender/50bfde38-7058-4d82-9854-ecc4609ae742")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{\n \"id\" : \"6ed6a7be-3a8c-408d-a033-48dcb459c325\",\n" +
                        "\n \"text\" : \"Tekst druge poruke\"  ,\n" +
                        "\n \"sender\" : \"50bfde38-7058-4d82-9854-ecc4609ae742\"  \n," +
                        "\n \"receiver\" : \"f64cb9a4-b3e8-49d2-bd73-454a75f11d71\"  \n}" +
                        ",{\n \"id\" : \"e2dcca86-bb53-49ed-9929-4798a7744218\",\n" +
                        "\n \"text\" : \"Tekst nte poruke\"  ,\n" +
                        "\n \"sender\" : \"50bfde38-7058-4d82-9854-ecc4609ae742\"  ,\n" +
                        "\n \"receiver\" :\"f64cb9a4-b3e8-49d2-bd73-454a75f11d71\"  \n}]"));
    }

    @Test
    public void updateMessage() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/messages/6ed6a7be-3a8c-408d-a033-48dcb459c325")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"text\": \"mijenjamo tekst poruke\",\n" +
                        "\"sender\": \"f64cb9a4-b3e8-49d2-bd73-454a75f11d71\",\n" +
                        "\"receiver\": \"50bfde38-7058-4d82-9854-ecc4609ae742\"\n" +
                        "}");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }


}

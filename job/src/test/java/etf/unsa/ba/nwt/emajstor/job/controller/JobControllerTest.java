package etf.unsa.ba.nwt.emajstor.job.controller;

import com.jayway.jsonpath.JsonPath;
import etf.unsa.ba.nwt.emajstor.job.model.Business;
import etf.unsa.ba.nwt.emajstor.job.model.Gallery;
import etf.unsa.ba.nwt.emajstor.job.model.Job;
import etf.unsa.ba.nwt.emajstor.job.model.PriceType;
import etf.unsa.ba.nwt.emajstor.job.repositories.BusinessRepository;
import etf.unsa.ba.nwt.emajstor.job.repositories.GalleryRepository;
import etf.unsa.ba.nwt.emajstor.job.repositories.JobRepository;
import etf.unsa.ba.nwt.emajstor.job.service.JobService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class JobControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private GalleryRepository galleryRepository;

    @Autowired
    private BusinessRepository businessRepository;

    private static  Job job1;
    private static  Job job2;
    private static  Gallery gallery1;
    private static  Gallery gallery2;
    private static  Business business1;
    private static  Business business2;
    private static  Business business3;


    private Gallery createGallery(String imageUrl) {
        Gallery gallery = new Gallery();
        gallery.setImageUrl(imageUrl);
        gallery = galleryRepository.save(gallery);
        return gallery;
    }

    private Business createBusiness(String name) {
        Business business = new Business();
        business.setName(name);
        business = businessRepository.save(business);
        return business;
    }

    private Job createJob(UUID user, double price, PriceType priceType, Business business, Gallery gallery) {
        Job job = new Job();
        job.setUser(user);
        job.setPrice(price);
        job.setPriceType(priceType);
        job.setBusiness(business);
        job.setGallery(gallery);
        job = jobRepository.save(job);
        return job;
    }

    @BeforeAll
    private void generateDatabase(){
        gallery1 = createGallery("www.imgur.com");
        gallery2 = createGallery("www.zaslike.com");

        business1 = createBusiness("Vodoinstalater");
        business2 = createBusiness("Vrtlar");
        business3 = createBusiness("Zidar");

        job1 = createJob(UUID.fromString("1dc0bec3-260a-4a50-8916-384128e8a55c"), 3.5, PriceType.PER_HOUR, business1, gallery1);
        job2 = createJob(UUID.fromString("8a351bab-e704-4afb-ab3e-7b27a54168a5"), 80, PriceType.PER_DAY, business2, gallery2);
    }

    @AfterAll
    void afterAll() {
        jobRepository.deleteById(job1.getId());
        jobRepository.deleteById(job2.getId());

        galleryRepository.deleteById(gallery1.getId());
        galleryRepository.deleteById(gallery2.getId());

        businessRepository.deleteById(business1.getId());
        businessRepository.deleteById(business2.getId());
        businessRepository.deleteById(business3.getId());
    }


    @Test
    public void getAllJobsOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/job/all")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getAllJobsOkRequest2() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/job/all")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))))
                .andReturn();
    }

    @Test
    public void getAllGalleriesOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/gallery/all")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getAllGallerisOkRequest2() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/gallery/all")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))))
                .andReturn();
    }

    @Test
    public void getAllBusinessOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/business/all")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getAllBusinessOkRequest2() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/business/all")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(greaterThan(0))))
                .andReturn();
    }

    @Test
    public void addBusinessOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/business")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"name\" : \"Autoelektricar\"  \n}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        String nazivPosla = JsonPath.read(result.getResponse().getContentAsString(), "$.name");

        assertEquals(nazivPosla,"Autoelektricar");

        businessRepository.deleteById(UUID.fromString(id));
    }

    @Test
    public void addBusinessBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/business")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"names\" : \"Automehanicar\"  \n}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void getBusinessByNameOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/business/name/"+business1.getName())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String nazivPosla = JsonPath.read(result.getResponse().getContentAsString(), "$.name");

        assertEquals(nazivPosla,"Vodoinstalater");
    }

    @Test
    public void getBusinessByNameBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/business/name/"+"testniposao")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void getBusinessByIdOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/business/"+business2.getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String nazivPosla = JsonPath.read(result.getResponse().getContentAsString(), "$.name");

        assertEquals(nazivPosla,"Vrtlar");
    }

    @Test
    public void getBusinessByIdBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/business/"+"nekiid")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void updateBussinesOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/business/"+business2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"name\": \"Smecar\"\n" +
                        "}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String nazivPosla = JsonPath.read(result.getResponse().getContentAsString(), "$.name");
        String idPosla = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        assertEquals(nazivPosla,"Smecar");

        businessRepository.deleteById(UUID.fromString(idPosla));
    }

    @Test
    public void updateBussinesBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/business/"+business2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"namesss\": \"Smecar\"\n" +
                        "}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void deleteBusinessByIDBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .delete("/api/business/neki423ID")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void deleteBusinessByIDOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/business")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"name\" : \"Majstor\"  \n}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        request = MockMvcRequestBuilders
                .delete("/api/business/"+id)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void PatchBusinessByIDOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/business")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"name\" : \"Tapetar\"  \n}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        request = MockMvcRequestBuilders
                .patch("/api/business/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{" +
                        "\n \"op\" :\"replace\"," +
                        "\"path\" : \"/name\"," +
                        "\"value\" : \"mijenjamo ime pomocu patch metode\"" +
                        "}]");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        request = MockMvcRequestBuilders
                .get("/api/business/"+id)
                .accept(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String komentar = JsonPath.read(result.getResponse().getContentAsString(), "$.name");

        assertEquals(komentar,"mijenjamo ime pomocu patch metode");

        request = MockMvcRequestBuilders
                .delete("/api/business/"+id)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void PatchBusinessByIDBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .patch("/api/business/"+business3.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{" +
                        "\n \"op\" :\"delete\"," +
                        "\"path\" : \"/randompolje\"," +
                        "}]");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void addGalleryOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/gallery")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"imageUrl\" : \"www.mojeslike.ba\"  \n}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        String url = JsonPath.read(result.getResponse().getContentAsString(), "$.imageUrl");

        assertEquals(url,"www.mojeslike.ba");

        galleryRepository.deleteById(UUID.fromString(id));
    }

    @Test
    public void addGalleryBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/gallery")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"nekopolje\" : \"\"  \n}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void getGalleryIdOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/gallery/"+gallery1.getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String url = JsonPath.read(result.getResponse().getContentAsString(), "$.imageUrl");

        assertEquals(url,"www.imgur.com");
    }

    @Test
    public void getGalleryIdBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/gallery/"+"nekiid1234")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void updateGalleryOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/gallery/"+gallery2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"imageUrl\": \"noviUrl\"\n" +
                        "}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String url = JsonPath.read(result.getResponse().getContentAsString(), "$.imageUrl");
        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        assertEquals(url,"noviUrl");

        galleryRepository.deleteById(UUID.fromString(id));
    }

    @Test
    public void updateGalleryBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/gallery/"+gallery1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"property\": \"noviUrl\"\n" +
                        "}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void deleteGalleryByIDOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/gallery")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"imageUrl\" : \"www.uploadimage.com\"  \n}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        request = MockMvcRequestBuilders
                .delete("/api/gallery/"+id)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void deleteGalleryByIDBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .delete("/api/gallery/neki423ID")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void PatchGalleryByIDOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/gallery")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\n \"imageUrl\" : \"www.slike.hr\"  \n}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        request = MockMvcRequestBuilders
                .patch("/api/gallery/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{" +
                        "\n \"op\" :\"replace\"," +
                        "\"path\" : \"/imageUrl\"," +
                        "\"value\" : \"mijenjamo galeriju pomocu patch metode\"" +
                        "}]");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        request = MockMvcRequestBuilders
                .get("/api/gallery/"+id)
                .accept(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String url = JsonPath.read(result.getResponse().getContentAsString(), "$.imageUrl");

        assertEquals(url,"mijenjamo galeriju pomocu patch metode");

        request = MockMvcRequestBuilders
                .delete("/api/gallery/"+id)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void PatchGalleryByIDBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .patch("/api/gallery/"+gallery1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{" +
                        "\n \"op\" :\"delete\"," +
                        "\"path\" : \"/randompolje\"," +
                        "}]");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void getJobIdOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/job/"+job1.getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String priceType = JsonPath.read(result.getResponse().getContentAsString(), "$.priceType");

        assertEquals(priceType,"PER_HOUR");
    }

    @Test
    public void getJobIdBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/job/"+"nekiId")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void addJobOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/job")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"price\": 2,\n" +
                        "\"priceType\": \"PER_HOUR\",\n" +
                        "\"user\": \"94a9050e-c748-4f42-a4fc-03e968551ecb\",\n" +
                        "\"businessId\": \"fd527611-4a17-465e-a861-afad9c84e274\",\n" +
                        "\"galleryId\": \"f2daef35-098d-4f41-83c6-b4e35a98a68a\"\n" +
                        "}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        jobRepository.deleteById(UUID.fromString(id));
    }

    @Test
    public void addJobBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/job")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"price\": 2,\n" +
                        "\"priceType\": \"novienumtip\",\n" +
                        "\"user\": \"94a9050e-c748-4f42-a4fc-03e968551ecb\",\n" +
                        "\"businessId\": \"fd527611-4a17-465e-a861-afad9c84e274\",\n" +
                        "\"galleryId\": \"f2daef35-098d-4f41-83c6-b4e35a98a68a\"\n" +
                        "}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void updateJobOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/job/"+job1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"price\": 2,\n" +
                        "\"priceType\": \"PER_DAY\",\n" +
                        "\"user\": \"94a9050e-c748-4f42-a4fc-03e968551ecb\",\n" +
                        "\"businessId\": \"fd527611-4a17-465e-a861-afad9c84e274\",\n" +
                        "\"galleryId\": \"f2daef35-098d-4f41-83c6-b4e35a98a68a\"\n" +
                        "}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String priceType = JsonPath.read(result.getResponse().getContentAsString(), "$.priceType");

        assertEquals(priceType,"PER_DAY");
    }

    @Test
    public void updateJobBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/job/"+job1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"price\": 2,\n" +
                        "\"priceType\": \"PER_MONTH\",\n" +
                        "\"user\": \"94a9050e-c748-4f42-a4fc-03e968551ecb\",\n" +
                        "\"businessId\": \"fd527611-4a17-465e-a861-afad9c84e274\",\n" +
                        "\"galleryId\": \"f2daef35-098d-4f41-83c6-b4e35a98a68a\"\n" +
                        "}");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void PatchJobByIDOkRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/job")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"price\": 2,\n" +
                        "\"priceType\": \"PER_HOUR\",\n" +
                        "\"user\": \"94a9050e-c748-4f42-a4fc-03e968551ecb\",\n" +
                        "\"businessId\": \"fd527611-4a17-465e-a861-afad9c84e274\",\n" +
                        "\"galleryId\": \"f2daef35-098d-4f41-83c6-b4e35a98a68a\"\n" +
                        "}");
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        request = MockMvcRequestBuilders
                .patch("/api/job/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{" +
                        "\n \"op\" :\"replace\"," +
                        "\"path\" : \"/priceType\"," +
                        "\"value\" : \"PER_DAY\"" +
                        "}]");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        request = MockMvcRequestBuilders
                .get("/api/job/"+id)
                .accept(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String url = JsonPath.read(result.getResponse().getContentAsString(), "$.priceType");

        assertEquals(url,"PER_DAY");

        jobRepository.deleteById(UUID.fromString(id));
    }

    @Test
    public void PatchJobByIDBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .patch("/api/job/"+job2.getId())
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

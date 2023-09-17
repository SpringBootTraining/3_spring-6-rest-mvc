package guru.springframework.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Mohammad Obeidat
 * <p>
 * Here we are uisng the MockMVC framework to do mock tests on the controller by using @WebMvcTest annotation and giving it the Controller that we will test.
 * <p>
 * MockMVC:
 * => This is a part of the Spring Test suite which allows you to test MVC Controllers without starting a full HTTP server.
 * It generates "mock" HTTP requests and passes them to your controllers.
 * <p>
 * => This way, you can test all aspects of your web layer (like HTTP request mappings, view resolution, form binding, etc.)
 * without the cost of starting the whole context.
 * <p>
 * <p>
 * <p>
 * Mockito:
 * => Mockito is a mocking framework for unit tests in Java. With Mockito, you can mock dependencies, specify behaviors, and verify
 * interactions between your code and the mocked dependencies.
 * <p>
 * => This can be useful when you want to isolate a certain class and test its behavior independently of its dependencies.
 */
@WebMvcTest(BeerController.class)
class BeerControllerTest {


    /**
     * This brings in that MockMVC component that is going to get autowired by the spring framework,
     * and this is going to require a beer service in the context.
     */
    @Autowired
    MockMvc mockMvc;

    /**
     * MockBean annotation basically tells Mockito to provide a mock of this into the spring context, so they worked together quite nicely.
     * Without this MockBean annotation we would get an exception saying that we don't have that dependency there, we'd to provide it manually.
     * This it adds the BeerService into the Spring Context, but it will add it as a mockito mocks.
     */
    @MockBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    /**
     * Injecting the ObjectMapper that has been created by Spring Boot.
     * Prefer to use it because two reasons:
     * 1- Because it is going to test your configuration
     * 2- The web context actually has an object mapper in it
     * By using it we'll get Spring Boot defaults and as we get into more advanced configuration, there
     * are overrides that we can do in a Spring Boot context so, now our test and our controllers are gonna
     * to be using the same configuration; We don't have to do it in two places.
     */
    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<Beer> beerArgumentCaptor;


    @Test
    void getBeerById() throws Exception {

        /**
         *                                   ==> Mock test for getBeerById using the [ path variable ] <==
         * Here the MockMVC will perform a mock GET request on the provided URL and then the call will search for the matched
         * method in the Controller in our case the method is getBeerById using the [PATH VARIABLE], and finally the mock API request
         * is expecting an OK response without issues.
         */

        Beer testBeer = beerServiceImpl.listBeers().get(0);
        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

        mockMvc.perform(
                        get(BeerController.BEER_PATH_GET_BY_ID_PATH_VARIABLE, testBeer.getId())
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));

        /**
         *                                   ==> Mock test for getBeerById using the [ query param ] <==
         * Here the MockMVC will perform a mock GET request on the provided URL and then the call will search for the matched
         * method in the Controller in our case the method is getBeerById using the [QUERY PARAM], and finally the mock API request
         * is expecting an OK response without issues.
         */

        String url = UriComponentsBuilder.fromPath(BeerController.BEER_PATH_GET_BY_ID_QUERY_PARAM)
                .queryParam("beerId", testBeer.getId())
                .toUriString();

        mockMvc.perform(
                        get(url)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void testGetBeerByIdNotFound() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        String uri = UriComponentsBuilder.fromPath(BeerController.BEER_PATH_GET_BY_ID_QUERY_PARAM)
                        .queryParam("beerId", UUID.randomUUID())
                        .toUriString();

        mockMvc.perform(get(uri))
                .andExpect(status().isNotFound());

    }

    @Test
    void testListBeers() throws Exception {

        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        mockMvc.perform(
                        get(BeerController.BEER_PATH_GET_ALL)
                                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));


    }

    @Test
    void testCreateNewBeer() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
        /**
         * This tell Jackson, the Jackson ObjectMapper to go out on the class path and find different modules,
         * and one of the modules for is to handle the date time type
         */
//        objectMapper.findAndRegisterModules();

        Beer beer = beerServiceImpl.listBeers().get(0);
        beer.setVersion(null);
        beer.setId(null);

        given(beerService.saveNewBeer(any(Beer.class))).willReturn(beerServiceImpl.listBeers().get(1).getId());

        mockMvc.perform(post(BeerController.BEER_PATH_POST)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdateBeer() throws Exception {

        Beer beer = beerServiceImpl.listBeers().get(0);

        mockMvc.perform(
                put("/api/v1/beer/beer-update?beerId=".concat(beer.getId().toString()))
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)));

        verify(beerService).updateBeerById(any(UUID.class), any(Beer.class));
    }

    @Test
    void testDeleteBeer() throws Exception {
        Beer beer = beerServiceImpl.listBeers().get(0);

        String uri = UriComponentsBuilder.fromPath(BeerController.BEER_PATH_DELETE)
                        .queryParam("beerId", beer.getId())
                        .toUriString();
        mockMvc.perform(delete(uri)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testPatchBeer() throws Exception {

        Beer beer = beerServiceImpl.listBeers().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name");

        String uri = UriComponentsBuilder.fromPath(BeerController.BEER_PATH_PATCH)
                        .queryParam("beerId", beer.getId())
                                .toUriString();

        mockMvc.perform(patch(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
    }
}
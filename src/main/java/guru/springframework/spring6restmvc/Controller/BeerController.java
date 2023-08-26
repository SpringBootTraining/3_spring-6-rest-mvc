package guru.springframework.spring6restmvc.Controller;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.ResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
//@RequestMapping("/api/v1/beer")
// Setting the api base path as the class level to be global for all rest API we'll create
public class BeerController {

    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_GET_ALL = BEER_PATH + "/beer-list";
    public static final String BEER_PATH_GET_BY_ID_QUERY_PARAM = BEER_PATH + "/beer-by-id";
    public static final String BEER_PATH_GET_BY_ID_PATH_VARIABLE = BEER_PATH + "/{beerId}";
    public static final String BEER_PATH_POST = BEER_PATH + "/beer-create";
    public static final String BEER_PATH_PUT = BEER_PATH + "/beer-update";
    public static final String BEER_PATH_DELETE = BEER_PATH + "/beer-delete";
    public static final String BEER_PATH_PATCH = BEER_PATH + "/beer-patch";

    private final BeerService beerService;


    // @RequestMapping(method = GET)  => This works as well
    // [ Rest ] Getting A List Of Beers.
    @GetMapping(BEER_PATH_GET_ALL)
    @ResponseBody
    public List<Beer> listBeers() {
        return beerService.listBeers();
    }

    // [ GraphQL ] Getting A List Of Beers.
    @QueryMapping
    // We can omit the parameter from this annotation if the controller method is matched with the method name in GraphQL schema.
    public Iterable<Beer> getBeers() {
        log.debug("Getting Beers List using GraphQL");
        return beerService.listBeers();
    }

    // [ Rest ] Getting Beer By ID Using Query Param
    // @RequestMapping( value = "beer-by-id", method = GET)  => This works as well
    @GetMapping(BEER_PATH_GET_BY_ID_QUERY_PARAM)
    public Beer getBeerById(@RequestParam("beerId") UUID id) {
        return beerService.getBeerById(id).orElseThrow(NotFoundException::new);
    }

    // [ Rest ] Getting Beer By ID Using Path Param
    // @RequestMapping(value = "{beerId}", method = GET)  => This works as well
    @GetMapping(BEER_PATH_GET_BY_ID_PATH_VARIABLE)
    public Beer getBeerByIdV2(@PathVariable("beerId") UUID id) {
        return beerService.getBeerById(id).orElseThrow(NotFoundException::new);
    }

    // [ GraphQL ]  Getting A Bear By ID Using The GraphQL
    @QueryMapping("getBeerById")
    // => This should be the same name of the method in GraphQL Schema if the controller method didn't matched with it.
    public Beer getBeerByIdV3(@Argument UUID id) {
        log.debug("Getting Beer By ID using GraphQL");
        return beerService.getBeerById(id).orElseThrow(NotFoundException::new);
    }


    // [ Rest ] Create New Beer And Return A Custom Response Body
    // @RequestMapping(value = "create", method = POST) => This works as well
    @PostMapping(BEER_PATH_POST)
    public ResponseEntity createNewBeer(@RequestBody Beer beer) {

        UUID savedBeerId = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_PATH_POST + "/" + savedBeerId.toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    // [ Rest ] Update existing Beer
    @PutMapping(BEER_PATH_PUT)
    public ResponseEntity updateBeer(@RequestParam("beerId") UUID id, @RequestBody Beer beer) {

        /*UUID updatedBeerId = beerService.updateBeerById(id, beer);
        HttpStatus status;
        HttpHeaders headers;
        ResponseModel responseModel;

        if (updatedBeerId != null) {
            status = OK;
            headers = new HttpHeaders();
            headers.add("Location", "/api/v1/beer/beer-update/".concat(id.toString()));
            responseModel = new ResponseModel(true, "Beer with ID: ".concat(id.toString().concat(" updated successfully")));
        } else {
            status = NOT_FOUND;
            headers = new HttpHeaders();
            headers.add("Location", "/api/v1/beer/beer-update/");
            responseModel = new ResponseModel(false, "Beer with ID: ".concat(id.toString().concat(" not found")));
        }
        return new ResponseEntity<>(responseModel, headers, status);*/

        beerService.updateBeerById(id, beer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }


    // [ Rest ] Delete Beer By ID.
    @DeleteMapping(BEER_PATH_DELETE)
    public ResponseEntity deleteBeerById(@RequestParam("beerId") UUID id){

        /*HttpStatus status;
        HttpHeaders headers;
        ResponseModel responseModel;

        if (beerService.deleteBeerById(id) != null) {
            status = OK;
            headers = new HttpHeaders();
            headers.add("Location", "/api/v1/beer/beer-delete/".concat(id.toString()));
            responseModel = new ResponseModel(true, "Beer with ID: ".concat(id.toString().concat(" deleted successfully")));
        } else {
            status = NOT_FOUND;
            headers = new HttpHeaders();
            headers.add("Location", "/api/v1/beer/beer-update/");
            responseModel = new ResponseModel(false, "Beer with ID: ".concat(id.toString().concat(" not found")));
        }
        return new ResponseEntity<>(responseModel, headers, status);*/

        beerService.deleteBeerById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // [ Rest ] Patch Beer
    @PatchMapping(BEER_PATH_PATCH)
    public ResponseEntity<ResponseModel> updateBeerPatchById(@RequestParam("beerId") UUID id, @RequestBody Beer beer){
        /*HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/beer-patch");
        beerService.patchBeerById(id, beer);
        return new ResponseEntity<>(new ResponseModel(true, "Beer with ID: ".concat(id.toString()).concat(" patched successfully")), headers, NO_CONTENT);*/

        beerService.patchBeerById(id, beer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
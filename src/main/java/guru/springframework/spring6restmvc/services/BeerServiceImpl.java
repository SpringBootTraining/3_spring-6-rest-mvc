package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Beer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static guru.springframework.spring6restmvc.model.BeerStyle.IPA;
import static guru.springframework.spring6restmvc.model.BeerStyle.PALE_ALE;

@Slf4j
@Component
public class BeerServiceImpl implements BeerService {
    private Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        log.debug("BeerServiceImpl initialized");
        this.beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(null)
                .build();
        log.debug("Beer 1 created, ID: ".concat(beer1.getId().toString()));

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(null)
                .build();
        log.debug("Beer 2 created, ID: ".concat(beer2.getId().toString()));

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(null)
                .build();
        log.debug("Beer 3 created, ID: ".concat(beer3.getId().toString()));

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
        log.debug("All 3 beers added to the beer map");
    }

    @Override
    public List<Beer> listBeers() {
        log.debug("Getting the list of beers");
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<Beer> getBeerById(UUID id) {
        log.debug("Get Beer by Id - in service. Id: " + id.toString());
        return Optional.of(beerMap.get(id));
    }


    @Override
    public UUID saveNewBeer(Beer beer) {

        Beer savedBeer = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .build();

        beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer.getId();
    }

    @Override
    public void updateBeerById(UUID id, Beer beer) {
            Beer updatedBeer = Beer.builder()
                    .id(id)
                    .version(1)
                    .beerStyle(beer.getBeerStyle())
                    .beerName(beer.getBeerName())
                    .price(beer.getPrice())
                    .upc(beer.getUpc())
                    .quantityOnHand(beer.getQuantityOnHand())
                    .updateDate(LocalDateTime.now())
                    .build();
            beerMap.put(id, updatedBeer);
    }

    @Override
    public void deleteBeerById(UUID id) {
            beerMap.remove(id);
    }


    @Override
    public void patchBeerById(UUID id, Beer beer) {

        Beer existing = beerMap.get(id);

        if (StringUtils.hasText(beer.getBeerName())){
            existing.setBeerName(beer.getBeerName());
        }

        if (beer.getBeerStyle() != null) {
            existing.setBeerStyle(beer.getBeerStyle());
        }

        if (beer.getPrice() != null) {
            existing.setPrice(beer.getPrice());
        }

        if (beer.getQuantityOnHand() != null){
            existing.setQuantityOnHand(beer.getQuantityOnHand());
        }

        if (StringUtils.hasText(beer.getUpc())) {
            existing.setUpc(beer.getUpc());
        }
    }
}

package guru.springframework.spring6restmvc.entities;

import guru.springframework.spring6restmvc.entities.enums.BeerStyle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "beer")
public class Beer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID id;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "beer_name")
    private String beerName;

    @Column(name = "beer_style")
    @Enumerated(EnumType.STRING)
    private BeerStyle beerStyle;

    @Column(name = "upc")
    private String upc;

    @Column(name = "quantity_on_hand")
    private Integer quantityOnHand;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "created_ts")
    private LocalDateTime createdDate;

    @Column(name = "updated_ts")
    private LocalDateTime updateDate;
}

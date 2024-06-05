package kz.sw_sku_service.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "brands")
@EqualsAndHashCode(callSuper = true)
public class BrandEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "is_made_in_kz", nullable = false)
    private Boolean isMadeInKz;
}

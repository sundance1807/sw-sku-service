package kz.sw_sku_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "brands")
@EqualsAndHashCode(callSuper = true)
public class BrandEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;
    @Column(length = 500)
    private String description;
    @Column(name = "is_made_in_kz", nullable = false)
    private Boolean isMadeInKz;
}

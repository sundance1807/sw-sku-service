package kz.sw_sku_service.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "skus")
@EqualsAndHashCode(callSuper = true)
public class SkuEntity extends BaseEntity {

    @Column(length = 120, unique = true, nullable = false)
    private String name;
    @Column(length = 500, nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandEntity brand;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;
}

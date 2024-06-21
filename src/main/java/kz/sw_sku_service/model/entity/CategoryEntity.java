package kz.sw_sku_service.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "categories")
@EqualsAndHashCode(callSuper = true)
public class CategoryEntity extends BaseEntity {

    @Column(nullable = false, length = 120, unique = true)
    private String name;
}

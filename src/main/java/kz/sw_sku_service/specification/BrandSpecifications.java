package kz.sw_sku_service.specification;

import kz.sw_sku_service.model.entity.BrandEntity;
import org.springframework.data.jpa.domain.Specification;

public class BrandSpecifications {

    public static Specification<BrandEntity> hasId() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("id"));
    }

    public static Specification<BrandEntity> nameLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                name.toLowerCase() + "%");
    }
}

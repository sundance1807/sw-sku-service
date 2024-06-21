package kz.sw_sku_service.specification;

import kz.sw_sku_service.model.entity.SkuEntity;
import org.springframework.data.jpa.domain.Specification;

public class SkuSpecifications {

    public static Specification<SkuEntity> hasId() {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("id")));
    }

    public static Specification<SkuEntity> nameLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                name.toLowerCase() + "%");
    }
}

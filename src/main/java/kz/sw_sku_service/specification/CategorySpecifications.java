package kz.sw_sku_service.specification;

import kz.sw_sku_service.model.entity.CategoryEntity;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecifications {

    public static Specification<CategoryEntity> hasId() {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("id")));
    }

    public static Specification<CategoryEntity> nameLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                name.toLowerCase() + "%");
    }
}

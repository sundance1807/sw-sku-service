package kz.sw_sku_service.mapper;

import kz.sw_sku_service.model.dto.CategoryDTO;
import kz.sw_sku_service.model.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryEntity toEntity(CategoryDTO dto);
    CategoryDTO toDTO(CategoryEntity entity);
}

package kz.sw_sku_service.mapper;

import kz.sw_sku_service.model.dto.BrandDTO;
import kz.sw_sku_service.model.entity.BrandEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    BrandEntity toEntity(BrandDTO dto);
    BrandDTO toDto(BrandEntity entity);
}

package kz.sw_sku_service.mapper;

import kz.sw_sku_service.model.dto.SkuDTO;
import kz.sw_sku_service.model.entity.SkuEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SkuMapper {

    SkuEntity toEntity(SkuDTO dto);

    SkuDTO toDTO(SkuEntity entity);
}

package kz.sw_sku_service.service;

import kz.sw_sku_service.exception.CustomException;
import kz.sw_sku_service.model.dto.CategoryDTO;
import kz.sw_sku_service.model.entity.CategoryEntity;

public interface CategoryService extends BaseService<CategoryDTO, CategoryEntity> {
    CategoryEntity findById(Long id) throws CustomException;
}

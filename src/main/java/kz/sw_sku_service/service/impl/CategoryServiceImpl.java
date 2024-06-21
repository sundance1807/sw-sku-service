package kz.sw_sku_service.service.impl;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotNull;
import kz.sw_sku_service.exception.CustomException;
import kz.sw_sku_service.mapper.CategoryMapper;
import kz.sw_sku_service.model.dto.CategoryDTO;
import kz.sw_sku_service.model.dto.SearchDTO;
import kz.sw_sku_service.model.dto.response.DeleteResponseDTO;
import kz.sw_sku_service.model.entity.CategoryEntity;
import kz.sw_sku_service.repository.CategoryRepository;
import kz.sw_sku_service.service.CategoryService;
import kz.sw_sku_service.service.JwtService;
import kz.sw_sku_service.util.MessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static kz.sw_sku_service.specification.CategorySpecifications.hasId;
import static kz.sw_sku_service.specification.CategorySpecifications.nameLike;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final JwtService jwtService;

    @Override
    public CategoryDTO saveOne(CategoryDTO categoryDTO) throws CustomException {
        String categoryName = categoryDTO.getName().trim();
        validateCategoryName(categoryName);
        String username = jwtService.getUsername();
        CategoryEntity entity = categoryMapper.toEntity(categoryDTO);
        entity.setName(categoryName);
        entity.setCreatedBy(username);
        entity.setUpdatedBy(username);
        entity = categoryRepository.save(entity);

        return categoryMapper.toDTO(entity);
    }

    @Override
    public CategoryDTO getOne(Long id) throws CustomException {
        return categoryMapper.toDTO(findById(id));
    }

    @Override
    public CategoryDTO updateOne(Long id, CategoryDTO categoryDTO) throws CustomException {
        String newCategoryName = categoryDTO.getName().trim();
        CategoryEntity categoryEntity = findById(id);
        validateCategoryName(newCategoryName);
        categoryEntity.setName(newCategoryName);
        categoryEntity.setUpdatedBy(jwtService.getUsername());
        categoryEntity = categoryRepository.save(categoryEntity);

        return categoryMapper.toDTO(categoryEntity);
    }

    @Override
    public DeleteResponseDTO deleteOne(Long id) throws CustomException {
        CategoryEntity entity = findById(id);
        categoryRepository.delete(entity);

        return new DeleteResponseDTO(id);
    }

    @Override
    public Page<CategoryDTO> search(SearchDTO searchDTO, Pageable pageable) {
        Specification<CategoryEntity> specification = getCategoryEntitySpecification(searchDTO);
        Page<CategoryEntity> categoryEntityPage = categoryRepository.findAll(specification, pageable);
        List<CategoryDTO> categoryDTOList = categoryEntityPage.getContent().stream()
                .map(categoryMapper::toDTO)
                .toList();

        return new PageImpl<>(categoryDTOList, pageable, categoryEntityPage.getTotalElements());
    }

    @NotNull
    private static Specification<CategoryEntity> getCategoryEntitySpecification(SearchDTO searchDTO) {
        Specification<CategoryEntity> specification = Specification.where(hasId());

        if (!StringUtils.isBlank(searchDTO.getName())) {
            specification = specification.and(nameLike(searchDTO.getName().trim()));
        }

        return specification;
    }

    private void validateCategoryName(String name) throws CustomException {
        name = name.trim();
        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findByNameIgnoreCase(name);

        if (optionalCategoryEntity.isPresent()) {
            throw CustomException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(MessageSource.CATEGORY_EXISTS.getText(name))
                    .build();
        }
    }

    @Override
    public CategoryEntity findById(Long id) throws CustomException {
        return categoryRepository.findById(id).orElseThrow(
                () -> CustomException.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .message(MessageSource.CATEGORY_NOT_FOUND.getText(id.toString()))
                        .build());
    }
}

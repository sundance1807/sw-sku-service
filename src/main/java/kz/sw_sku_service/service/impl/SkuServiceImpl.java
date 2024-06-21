package kz.sw_sku_service.service.impl;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotNull;
import kz.sw_sku_service.exception.CustomException;
import kz.sw_sku_service.mapper.SkuMapper;
import kz.sw_sku_service.model.dto.SearchDTO;
import kz.sw_sku_service.model.dto.SkuDTO;
import kz.sw_sku_service.model.dto.response.DeleteResponseDTO;
import kz.sw_sku_service.model.entity.BrandEntity;
import kz.sw_sku_service.model.entity.CategoryEntity;
import kz.sw_sku_service.model.entity.SkuEntity;
import kz.sw_sku_service.repository.SkuRepository;
import kz.sw_sku_service.service.JwtService;
import kz.sw_sku_service.service.SkuService;
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

import static kz.sw_sku_service.specification.SkuSpecifications.hasId;
import static kz.sw_sku_service.specification.SkuSpecifications.nameLike;

@Service
@RequiredArgsConstructor
public class SkuServiceImpl implements SkuService {

    private final SkuRepository skuRepository;
    private final BrandServiceImpl brandServiceImpl;
    private final CategoryServiceImpl categoryServiceImpl;
    private final SkuMapper skuMapper;
    private final JwtService jwtService;

    @Override
    public SkuDTO saveOne(SkuDTO skuDTO) throws CustomException {
        String skuName = skuDTO.getName().trim();
        validateSkuName(skuName);
        String username = jwtService.getUsername();
        BrandEntity brand = brandServiceImpl.findById(skuDTO.getBrandId());
        CategoryEntity category = categoryServiceImpl.findById(skuDTO.getCategoryId());
        SkuEntity entity = skuMapper.toEntity(skuDTO);
        entity.setName(skuName);
        entity.setBrand(brand);
        entity.setCategory(category);
        entity.setCreatedBy(username);
        entity.setUpdatedBy(username);
        entity.setDescription(skuDTO.getDescription());
        entity = skuRepository.save(entity);

        return skuMapper.toDTO(entity);
    }

    @Override
    public SkuDTO getOne(Long id) throws CustomException {
        return skuMapper.toDTO(findById(id));
    }

    @Override
    public SkuDTO updateOne(Long id, SkuDTO skuDTO) throws CustomException {
        SkuEntity entity = findById(id);
        String skuName = skuDTO.getName().trim();
        validateSkuName(skuName);
        entity.setName(skuName);
        entity.setUpdatedBy(jwtService.getUsername());

        if (!skuDTO.getBrandId().equals(entity.getBrand().getId())) {
            entity.setBrand(brandServiceImpl.findById(skuDTO.getBrandId()));
        }

        if (!skuDTO.getCategoryId().equals(entity.getCategory().getId())) {
            entity.setCategory(categoryServiceImpl.findById(skuDTO.getCategoryId()));
        }

        entity.setDescription(skuDTO.getDescription());
        entity = skuRepository.save(entity);

        return skuMapper.toDTO(entity);
    }

    @Override
    public DeleteResponseDTO deleteOne(Long id) throws CustomException {
        SkuEntity entity = findById(id);
        skuRepository.delete(entity);

        return new DeleteResponseDTO(id);

    }

    @Override
    public Page<SkuDTO> search(SearchDTO searchDTO, Pageable pageable) {
        Specification<SkuEntity> specification = getSkuEntitySpecification(searchDTO);
        Page<SkuEntity> skuEntityPage = skuRepository.findAll(specification, pageable);
        List<SkuDTO> categoryDTOList = skuEntityPage.getContent().stream()
                .map(skuMapper::toDTO)
                .toList();

        return new PageImpl<>(categoryDTOList, pageable, skuEntityPage.getTotalElements());
    }

    @NotNull
    private static Specification<SkuEntity> getSkuEntitySpecification(SearchDTO searchDTO) {
        Specification<SkuEntity> specification = Specification.where(hasId());

        if (!StringUtils.isBlank(searchDTO.getName())) {
            specification = specification.and(nameLike(searchDTO.getName().trim()));
        }

        return specification;
    }

    private void validateSkuName(String skuName) throws CustomException {
        Optional<SkuEntity> optionalSkuEntity = skuRepository.findByNameIgnoreCase(skuName);

        if (optionalSkuEntity.isPresent()) {
            throw CustomException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(MessageSource.SKU_EXISTS.getText(skuName))
                    .build();
        }
    }

    @Override
    public SkuEntity findById(Long id) throws CustomException {
        return skuRepository.findById(id).orElseThrow(
                () -> CustomException.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .message(MessageSource.SKU_NOT_FOUND.getText(id.toString()))
                        .build());
    }
}

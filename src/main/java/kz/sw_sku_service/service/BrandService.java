package kz.sw_sku_service.service;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.validation.constraints.NotNull;
import kz.sw_sku_service.exception.CustomException;
import kz.sw_sku_service.mapper.BrandMapper;
import kz.sw_sku_service.model.dto.BrandDTO;
import kz.sw_sku_service.model.dto.SearchDTO;
import kz.sw_sku_service.model.entity.BrandEntity;
import kz.sw_sku_service.repository.BrandRepository;
import kz.sw_sku_service.util.MessageSource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static kz.sw_sku_service.specification.BrandSpecifications.hasId;
import static kz.sw_sku_service.specification.BrandSpecifications.nameLike;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final JwtService jwtService;

    @Transactional(rollbackFor = CustomException.class)
    public BrandDTO saveOne(BrandDTO brandDTO) throws CustomException {
        String brandName = brandDTO.getName().trim();
        validateBrandName(brandName, null);
        BrandEntity entity = brandMapper.toEntity(brandDTO);
        entity = brandRepository.save(entity);

        return brandMapper.toDto(entity);
    }

    public BrandDTO getOne(Long id) throws CustomException {
        return brandMapper.toDto(findById(id));
    }

    @Transactional(rollbackFor = CustomException.class)
    public BrandDTO updateOne(Long id, BrandDTO dto) throws CustomException {
        BrandEntity existingBrandEntity = findById(id);
        String brandName = dto.getName();
        validateBrandName(brandName, id);
        existingBrandEntity.setName(brandName);
        existingBrandEntity.setIsMadeInKz(dto.getIsMadeInKz());
        existingBrandEntity.setUpdatedBy(jwtService.getUsername());
        existingBrandEntity = brandRepository.save(existingBrandEntity);

        return brandMapper.toDto(existingBrandEntity);
    }

    @Transactional(rollbackFor = CustomException.class)
    public String deleteOne(Long id) throws CustomException {
        BrandEntity entity = findById(id);
        brandRepository.delete(entity);

        return MessageSource.BRAND_DELETED.getMessage(entity.getName());
    }

    @Transactional(readOnly = true)
    public Page<BrandDTO> search(SearchDTO searchDTO, Pageable pageable) {
        Specification<BrandEntity> specification = getBrandEntitySpecification(searchDTO);
        Page<BrandEntity> brandEntityPage = brandRepository.findAll(specification, pageable);
        List<BrandDTO> brandDTOList = brandEntityPage.getContent().stream()
                .map(brandMapper::toDto)
                .toList();
        return new PageImpl<>(brandDTOList, pageable, brandEntityPage.getTotalElements());
    }

    @NotNull
    private static Specification<BrandEntity> getBrandEntitySpecification(SearchDTO searchDTO) {
        Specification<BrandEntity> specification = Specification.where(hasId());

        if (!StringUtils.isBlank(searchDTO.getName())) {
            specification = specification.and(nameLike(searchDTO.getName().trim()));
        }

        return specification;
    }

    private void validateBrandName(String brandName, Long brandId) throws CustomException {
        Optional<BrandEntity> optionalBrandEntity = brandRepository.findByNameIgnoreCase(brandName);

        if (optionalBrandEntity.isPresent() && (brandId == null || !optionalBrandEntity.get().getId().equals(brandId))) {
            throw CustomException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(MessageSource.DUPLICATED_BRAND.getMessage(brandName))
                    .build();
        }
    }

    private BrandEntity findById(Long id) throws CustomException {
        return brandRepository.findById(id).orElseThrow(
                () -> CustomException.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .message(MessageSource.BRAND_NOT_FOUND.getMessage(id.toString()))
                        .build());
    }
}

package kz.sw_sku_service.service.impl;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.validation.constraints.NotNull;
import kz.sw_sku_service.exception.CustomException;
import kz.sw_sku_service.mapper.BrandMapper;
import kz.sw_sku_service.model.dto.BrandDTO;
import kz.sw_sku_service.model.dto.SearchDTO;
import kz.sw_sku_service.model.dto.response.DeleteResponseDTO;
import kz.sw_sku_service.model.entity.BrandEntity;
import kz.sw_sku_service.repository.BrandRepository;
import kz.sw_sku_service.service.BrandService;
import kz.sw_sku_service.service.JwtService;
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

import java.util.List;
import java.util.Optional;

import static kz.sw_sku_service.specification.BrandSpecifications.hasId;
import static kz.sw_sku_service.specification.BrandSpecifications.nameLike;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final JwtService jwtService;

    @Override
    public BrandDTO saveOne(BrandDTO brandDTO) throws CustomException {
        String brandName = brandDTO.getName().trim();
        validateBrandName(brandName, null);
        String username = jwtService.getUsername();
        BrandEntity entity = brandMapper.toEntity(brandDTO);
        entity.setName(brandName);
        entity.setCreatedBy(username);
        entity.setUpdatedBy(username);
        entity = brandRepository.save(entity);

        return brandMapper.toDto(entity);
    }

    @Override
    public BrandDTO getOne(Long brandId) throws CustomException {
        return brandMapper.toDto(findById(brandId));
    }

    @Override
    public BrandDTO updateOne(Long brandId, BrandDTO brandDTO) throws CustomException {
        BrandEntity existingBrandEntity = findById(brandId);

        if (StringUtils.isNotBlank(brandDTO.getName())) {
            String brandName = brandDTO.getName().trim();
            validateBrandName(brandName, brandId);
            existingBrandEntity.setName(brandName);
        }

        existingBrandEntity.setIsMadeInKz(brandDTO.getIsMadeInKz());
        existingBrandEntity.setUpdatedBy(jwtService.getUsername());
        existingBrandEntity = brandRepository.save(existingBrandEntity);

        return brandMapper.toDto(existingBrandEntity);
    }

    @Override
    public DeleteResponseDTO deleteOne(Long id) throws CustomException {
        BrandEntity entity = findById(id);
        brandRepository.delete(entity);

        return new DeleteResponseDTO(id);
    }

    @Override
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

    private void validateBrandName(String name, Long brandId) throws CustomException {
        name = name.trim();
        Optional<BrandEntity> optionalBrandEntity = brandRepository.findByNameIgnoreCase(name);

        if (optionalBrandEntity.isPresent() && (brandId == null || !optionalBrandEntity.get().getId().equals(brandId))) {
            throw CustomException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(MessageSource.BRAND_EXISTS.getText(name))
                    .build();
        }
    }

    @Override
    public BrandEntity findById(Long id) throws CustomException {
        return brandRepository.findById(id).orElseThrow(
                () -> CustomException.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .message(MessageSource.BRAND_NOT_FOUND.getText(id.toString()))
                        .build());
    }
}




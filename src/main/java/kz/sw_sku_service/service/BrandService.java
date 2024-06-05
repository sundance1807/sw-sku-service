package kz.sw_sku_service.service;

import kz.sw_sku_service.exception.CustomException;
import kz.sw_sku_service.mapper.BrandMapper;
import kz.sw_sku_service.model.dto.BrandDTO;
import kz.sw_sku_service.model.entity.BrandEntity;
import kz.sw_sku_service.repository.BrandRepository;
import kz.sw_sku_service.util.MessageSource;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Transactional
    public BrandDTO saveOne(BrandDTO brandDTO) throws CustomException {
        String brandName = brandDTO.getName().trim();
        validateBrandName(brandName, null);
        BrandEntity entity = brandMapper.toEntity(brandDTO);
        entity =  brandRepository.save(entity);

        return brandMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public BrandDTO getOne(Long id) throws CustomException {
        return brandMapper.toDto(findById(id));
    }

    @Transactional
    public String deleteOne(Long id) throws CustomException {
        BrandEntity entity = findById(id);
        brandRepository.delete(entity);

        return MessageSource.BRAND_DELETED.getMessage(entity.getName());
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
                        .build()
        );
    }
}

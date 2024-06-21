package kz.sw_sku_service.controller;

import jakarta.validation.Valid;
import kz.sw_sku_service.exception.CustomException;
import kz.sw_sku_service.model.dto.SearchDTO;
import kz.sw_sku_service.model.dto.SkuDTO;
import kz.sw_sku_service.model.dto.response.DeleteResponseDTO;
import kz.sw_sku_service.service.impl.SkuServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/s")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SkuController {

    private final SkuServiceImpl skuServiceImpl;

    @PostMapping("/v1")
    public SkuDTO saveOne(@RequestBody @Valid SkuDTO dto) throws CustomException {
        log.info("Incoming request to save category: {}", dto.getName());
        return skuServiceImpl.saveOne(dto);
    }

    @GetMapping("/v1/{id}")
    public SkuDTO getOne(@PathVariable Long id) throws CustomException {
        log.info("Incoming request to get brand with id: {}", id);
        return skuServiceImpl.getOne(id);
    }

    @PutMapping("/v1/{id}")
    public SkuDTO updateOne(@PathVariable Long id, @RequestBody SkuDTO dto) throws CustomException {
        log.info("Incoming request to update brand with id: {}.", id);
        return skuServiceImpl.updateOne(id, dto);
    }

    @DeleteMapping("/v1/{id}")
    public DeleteResponseDTO deleteOne(@PathVariable Long id) throws CustomException {
        log.info("Incoming request to delete brand with id: {}", id);
        return skuServiceImpl.deleteOne(id);
    }

    @PostMapping("/v1/search")
    public Page<SkuDTO> search(@RequestBody SearchDTO searchDTO, Pageable pageable) {
        log.info("Incoming request to search brand: {}", searchDTO);
        return skuServiceImpl.search(searchDTO, pageable);
    }
}

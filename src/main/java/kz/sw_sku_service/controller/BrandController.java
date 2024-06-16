package kz.sw_sku_service.controller;

import jakarta.validation.Valid;
import kz.sw_sku_service.exception.CustomException;
import kz.sw_sku_service.model.dto.BrandDTO;
import kz.sw_sku_service.model.dto.SearchDTO;
import kz.sw_sku_service.service.BrandService;
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
@RequestMapping("/brands/v1")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public BrandDTO saveOne(@Valid @RequestBody BrandDTO dto) throws CustomException {
        log.info("Incoming request to save brand: {}", dto.toString());
        return brandService.saveOne(dto);
    }

    @GetMapping("/{id}")
    public BrandDTO getOne(@PathVariable Long id) throws CustomException {
        log.info("Incoming request to get brand with id: {}", id);
        return brandService.getOne(id);
    }

    @PutMapping("/{id}")
    public BrandDTO updateOne(@PathVariable Long id, @RequestBody BrandDTO dto) throws CustomException {
        log.info("Incoming request to update brand with id: {}.", id);
        return brandService.updateOne(id, dto);
    }

    @DeleteMapping("/{id}")
    public String deleteOne(@PathVariable Long id) throws CustomException {
        log.info("Incoming request to delete brand with id: {}", id);
        return brandService.deleteOne(id);
    }

    @PostMapping("/search")
    public Page<BrandDTO> search(@RequestBody SearchDTO searchDTO, Pageable pageable) {
        log.info("Incoming request to search brand: {}", searchDTO);
        return brandService.search(searchDTO, pageable);
    }
}

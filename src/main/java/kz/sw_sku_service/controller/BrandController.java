package kz.sw_sku_service.controller;

import jakarta.validation.Valid;
import kz.sw_sku_service.exception.CustomException;
import kz.sw_sku_service.model.dto.BrandDTO;
import kz.sw_sku_service.service.BrandService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/public/brands/v1")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public BrandDTO saveOne(@Valid @RequestBody BrandDTO brandDTO) throws CustomException {
        log.info("Incoming request to save brand: {}", brandDTO.toString());
        return brandService.saveOne(brandDTO);
    }

    @GetMapping("/{id}")
    public BrandDTO getOne(@PathVariable Long id) throws CustomException {
        log.info("Incoming request to get brand: {}", id);
        return brandService.getOne(id);
    }

    @DeleteMapping("/{id}")
    public void deleteOne(@PathVariable Long id) throws CustomException {
        log.info("Incoming request to delete brand: {}", id);
        brandService.deleteOne(id);
    }
}

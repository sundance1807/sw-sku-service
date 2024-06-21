package kz.sw_sku_service.controller;

import jakarta.validation.Valid;
import kz.sw_sku_service.exception.CustomException;
import kz.sw_sku_service.model.dto.CategoryDTO;
import kz.sw_sku_service.model.dto.SearchDTO;
import kz.sw_sku_service.model.dto.response.DeleteResponseDTO;
import kz.sw_sku_service.service.impl.CategoryServiceImpl;
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
@RequestMapping("/c")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    private final CategoryServiceImpl categoryServiceImpl;

    @PostMapping("/v1")
    public CategoryDTO saveOne(@RequestBody @Valid CategoryDTO dto) throws CustomException {
        log.info("Incoming request to save category: {}", dto.getName());
        return categoryServiceImpl.saveOne(dto);
    }

    @GetMapping("/v1/{id}")
    public CategoryDTO getOne(@PathVariable Long id) throws CustomException {
        log.info("Incoming request to get brand with id: {}", id);
        return categoryServiceImpl.getOne(id);
    }

    @PutMapping("/v1/{id}")
    public CategoryDTO updateOne(@PathVariable Long id, @RequestBody CategoryDTO dto) throws CustomException {
        log.info("Incoming request to update brand with id: {}.", id);
        return categoryServiceImpl.updateOne(id, dto);
    }

    @DeleteMapping("/v1/{id}")
    public DeleteResponseDTO deleteOne(@PathVariable Long id) throws CustomException {
        log.info("Incoming request to delete brand with id: {}", id);
        return categoryServiceImpl.deleteOne(id);
    }

    @PostMapping("/v1/search")
    public Page<CategoryDTO> search(@RequestBody SearchDTO searchDTO, Pageable pageable) {
        log.info("Incoming request to search brand: {}", searchDTO);
        return categoryServiceImpl.search(searchDTO, pageable);
    }
}

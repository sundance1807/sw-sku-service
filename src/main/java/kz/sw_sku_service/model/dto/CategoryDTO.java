package kz.sw_sku_service.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryDTO extends BaseDTO {
    @NotBlank(message = "Название категории не может быть пустым.")
    private String name;
}

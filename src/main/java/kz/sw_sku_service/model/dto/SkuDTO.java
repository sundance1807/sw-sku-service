package kz.sw_sku_service.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SkuDTO extends BaseDTO {

    @NotBlank(message = "Название товара не может быть пустым.")
    private String name;
    private String description;
    @NotNull(message = "ID бренда не может быть пустым.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long brandId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "ID категории не может быть пустым.")
    private Long categoryId;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BrandDTO brand;
    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CategoryDTO category;
}

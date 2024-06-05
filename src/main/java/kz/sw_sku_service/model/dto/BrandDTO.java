package kz.sw_sku_service.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BrandDTO extends BaseDTO{

    @NotBlank(message = "Наименование бренда должно быть заполнено")
    private String name;
    private Boolean isMadeInKz = false;
}

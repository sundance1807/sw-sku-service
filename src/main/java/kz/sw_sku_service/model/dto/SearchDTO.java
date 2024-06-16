package kz.sw_sku_service.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchDTO {

    private Long id;
    private String name;
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;
}

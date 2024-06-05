package kz.sw_sku_service.util;

public enum MessageSource {

    DUPLICATED_BRAND( "Duplicated brand: %s."),
    BRAND_NOT_FOUND("Brand not found: %s."),
    BRAND_DELETED("Brand deleted: %s."),
    ;

    private final String message;

   MessageSource(String text) {
        this.message = text;
    }

    public String getMessage(String... params) {
       return String.format(this.message, (Object) params);
    }
}


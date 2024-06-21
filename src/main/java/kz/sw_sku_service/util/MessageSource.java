package kz.sw_sku_service.util;

public enum MessageSource {

    BRAND_NOT_FOUND("Brand with id: '%s' not found."),
    BRAND_EXISTS( "Brand with name: '%s' already exists."),

    CATEGORY_NOT_FOUND("Category with id: '%s' not found."),
    CATEGORY_EXISTS("Category with name: '%s' already exists.")
    ;

    private final String text;

   MessageSource(String text) {
        this.text = text;
    }

    public String getText(String... params) {
       return String.format(this.text, params);
    }
}


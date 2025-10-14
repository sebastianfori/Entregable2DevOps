package com.devops.coffee_shop.coffee.domain;

/**
 * Enum que representa las categorías de productos del café
 */
public enum ProductCategory {
    COFFEE("COFFEE"),
    TEA("TEA"),
    PASTRY("PASTRY"),
    SANDWICH("SANDWICH"),
    BEVERAGE("BEVERAGE"),
    DESSERT("DESSERT");
    
    private final String displayName;
    
    ProductCategory(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}

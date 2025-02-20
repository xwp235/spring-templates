package jp.onehr.base.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Product {
    @NotBlank
    private String name;

    @Min(1)
    private int price;

    // 构造方法 & Getters/Setters
    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    @Min(1)
    public int getPrice() {
        return price;
    }

    public void setPrice(@Min(1) int price) {
        this.price = price;
    }
}

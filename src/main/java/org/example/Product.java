package org.example;

import java.util.Objects;

public class Product {
    private String category;
    private String vendor;
    private String name;
    private int price;

    public Product(String category, String vendor, String name, int price) {
        this.category = category;
        this.vendor = vendor;
        this.name = name;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public String getVendor() {
        return vendor;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(vendor, product.vendor) && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendor, name);
    }

    @Override
    public String toString() {
        return category + " / " + vendor + " / " + name + " / " + price;
    }
}

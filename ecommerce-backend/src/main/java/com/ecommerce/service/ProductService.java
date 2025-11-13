package com.ecommerce.service;

import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    // ------------------- Get All Products -------------------
    public List<Product> getAll() {
        List<Product> allProducts = repo.findAll();

        Set<String> seen = new HashSet<>();
        List<Product> uniqueProducts = new ArrayList<>();

        for (Product p : allProducts) {
            String key = p.getName() + "-" + p.getPrice();
            if (!seen.contains(key)) {
                seen.add(key);
                assignImage(p); // ensure correct image assigned
                uniqueProducts.add(p);
            }
        }

        return uniqueProducts;
    }

    // ------------------- Get by ID -------------------
    public Product getById(Long id) {
        Product p = repo.findById(id).orElse(null);
        if (p != null) assignImage(p); // assign image if missing
        return p;
    }

    // ------------------- Save Product -------------------
    public Product save(Product p) {
        assignImage(p); // assign correct image before saving
        return repo.save(p);
    }

    // ------------------- Delete Product -------------------
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ------------------- Sample Products -------------------
    public Product createSampleProduct() {
        Product p = new Product();
        p.setName("Phone");
        p.setBrand("Apple");
        p.setModel("iPhone 15");
        p.setDescription("Apple iPhone 15 (128 GB) - Black");
        p.setPrice(47999);
        p.setMrp(67690);
        p.setDiscountPercent(calculateDiscountPercent(47999, 67690));
        p.setRating(4.5);
        p.setExchangeAvailable(true);
        p.setTag("Great Indian Festival");
        p.setStock(10);
        p.setImageUrl("/images/mobile_iphone15.jpg");
        return repo.save(p);
    }

    public Product createSampleLaptop() {
        Product p = new Product();
        p.setName("Laptop");
        p.setBrand("Lenovo");
        p.setModel("ThinkPad E14 Gen 5");
        p.setDescription("Lenovo ThinkPad E14 Gen 5 with AMD Ryzen 7, 16GB RAM, 512GB SSD");
        p.setPrice(62999);
        p.setMrp(69999);
        p.setDiscountPercent(calculateDiscountPercent(62999, 69999));
        p.setRating(4.6);
        p.setExchangeAvailable(true);
        p.setStock(20);
        p.setTag("Diwali Deal");
        p.setImageUrl("/images/laptops_lenovo_thinkpad_e14.jpg");
        return repo.save(p);
    }

    // ------------------- Update Existing Data -------------------
    @Transactional
    public void enrichExistingProducts() {
        List<Product> products = repo.findAll();

        for (Product p : products) {
            String desc = p.getDescription() != null ? p.getDescription().toLowerCase() : "";

            if (desc.contains("hp pavilion")) {
                p.setBrand("HP");
                p.setModel("Pavilion 14");
                p.setPrice(65000);
                p.setMrp(76000);
                p.setDiscountPercent(calculateDiscountPercent(65000, 76000));
                p.setRating(4.2);
                p.setTag("Festival Deal");
            } else if (desc.contains("samsung") && desc.contains("galaxy")) {
                p.setBrand("Samsung");
                p.setModel("Galaxy S24");
                p.setPrice(95000);
                p.setMrp(114000);
                p.setDiscountPercent(calculateDiscountPercent(95000, 114000));
                p.setRating(4.4);
                p.setTag("Great Indian Festival");
            } else if (desc.contains("dell") && desc.contains("inspiron")) {
                p.setBrand("Dell");
                p.setModel("Inspiron 16");
                p.setPrice(55000);
                p.setMrp(66000);
                p.setDiscountPercent(calculateDiscountPercent(55000, 66000));
                p.setRating(4.3);
                p.setTag("Festival Deal");
            } else if (desc.contains("iphone")) {
                p.setBrand("Apple");
                p.setModel("iPhone 15");
                p.setPrice(47999);
                p.setMrp(67690);
                p.setDiscountPercent(calculateDiscountPercent(47999, 67690));
                p.setRating(4.5);
                p.setTag("Great Indian Festival");
            } else if (desc.contains("iqoo")) {
                p.setBrand("iQOO");
                p.setModel("Z10R 5G");
                p.setPrice(19498);
                p.setMrp(23988);
                p.setDiscountPercent(calculateDiscountPercent(19498, 23988));
                p.setRating(4.1);
                p.setTag("Festival Deal");
            } else if (desc.contains("watch") || desc.contains("smartwatch")) {
                p.setBrand("Noise");
                p.setModel("ColorFit Pro 5");
                p.setPrice(2999);
                p.setMrp(3999);
                p.setDiscountPercent(calculateDiscountPercent(2999, 3999));
                p.setRating(4.3);
                p.setTag("Deal of the Day");
            }

            assignImage(p); // assign correct image
            repo.save(p);   // save updates
        }
    }

    // ------------------- Utility -------------------
    private int calculateDiscountPercent(int price, int mrp) {
        if (mrp <= 0) return 0;
        double discount = ((double) (mrp - price) / mrp) * 100;
        return (int) Math.round(discount);
    }

    // ------------------- Smart Image Assignment -------------------
    private void assignImage(Product p) {
        String desc = (p.getDescription() != null) ? p.getDescription().toLowerCase() : "";
        String name = (p.getName() != null) ? p.getName().toLowerCase() : "";
        String brand = (p.getBrand() != null) ? p.getBrand().toLowerCase() : "";
        String model = (p.getModel() != null) ? p.getModel().toLowerCase() : "";

        // 1️⃣ Laptops
        if (brand.contains("dell") || model.contains("inspiron")) {
            p.setImageUrl("/images/laptops_dell_inspiron16.jpg");
        } else if (brand.contains("hp") || model.contains("pavilion")) {
            p.setImageUrl("/images/laptops_hp_pavilion14.jpg");
        } else if (brand.contains("lenovo") || model.contains("thinkpad")) {
            p.setImageUrl("/images/laptops_lenovo_thinkpad_e14.jpg");
        }

        // 2️⃣ Mobiles
        else if (brand.contains("apple") || model.contains("iphone")) {
            p.setImageUrl("/images/mobile_iphone15.jpg");
        } else if (brand.contains("iqoo") || model.contains("z10r")) {
            p.setImageUrl("/images/mobile_iqoo_z10r.jpg");
        } else if (brand.contains("samsung") || desc.contains("galaxy")) {
            p.setImageUrl("/images/mobile_samsung_galaxy.jpg");
        }

        // 3️⃣ Smartwatches
        else if (brand.contains("noise") || desc.contains("watch") || name.contains("watch")) {
            p.setImageUrl("/images/smartwatch_noise.jpg");
        }

        // 4️⃣ Generic fallback
        else if (name.contains("laptop") || desc.contains("laptop")) {
            p.setImageUrl("/images/laptop.jpg");
        } else if (name.contains("phone") || desc.contains("phone") || desc.contains("mobile")) {
            p.setImageUrl("/images/mobile.jpg");
        }

        // 5️⃣ Default
        else {
            p.setImageUrl("/images/default.jpg");
        }
    }

    // ------------------- Search -------------------
    public List<Product> searchByKeyword(String keyword) {
        String lower = keyword.toLowerCase();
        return repo.findAll().stream()
                .filter(p -> p.getDescription().toLowerCase().contains(lower)
                        || p.getName().toLowerCase().contains(lower)
                        || p.getBrand().toLowerCase().contains(lower))
                .toList();
    }
}

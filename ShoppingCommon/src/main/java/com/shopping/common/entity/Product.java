package com.shopping.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private String shortDescription;
    private String fullDescription;
    private String mainImage;
    private double price;
    private float discountPercent;
    private boolean enabled;
    private boolean inStock;
    private Date createdTime;
    private Date updatedTime;

    private String categoryId;

    @DBRef(lazy = false)
    private Set<ProductImage> extraImages = new HashSet<ProductImage>();

    @Transient
    public String getMainImagePath() {
        if(this.getId()==null || this.getMainImage().isEmpty()) {
            return "/images/img-thumbnail-default.jpg";
        }
        else {
            return "/product-images/"+this.getId()+"/"+this.getMainImage();
        }
    }
    public void addExtraImages(String nameExtraImage) {
        this.getExtraImages().add(new ProductImage(nameExtraImage, this));
    }

    public boolean containsExtraImage(String imageFileName) {
        for(ProductImage item : this.getExtraImages()) {
            if(item.getName().equals(imageFileName))
                return true;
        }
        return false;
    }

    // get Discount Price :
    @Transient
    public double getDiscountPrice() {
        double discountPrice;
        if(this.getDiscountPercent()>0)
        {
            discountPrice = this.getPrice() * ((100-this.getDiscountPercent())/100);
            // làm tròn 2 số thập phân
            return (double) (Math.round(discountPrice * 100.0) / 100.0);
        }
        else {
            return this.getPrice();
        }
    }

}

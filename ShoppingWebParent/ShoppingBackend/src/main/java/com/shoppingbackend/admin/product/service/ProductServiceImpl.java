package com.shoppingbackend.admin.product.service;

import com.shopping.common.entity.Category;
import com.shopping.common.entity.Product;
import com.shopping.common.entity.ProductImage;
import com.shoppingbackend.admin.product.repository.ProductRepository;
import com.shoppingbackend.admin.productImage.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService{

    public static final int ITEMS_NUMBER_PER_PAGE = 5;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(String id) {
        return productRepository.findById(id).get();
    }

    @Override
    public Page<Product> getProductsByPage(Integer pageNumber, String sortField, String sortDir, String keyword, String categoryId) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber-1, ITEMS_NUMBER_PER_PAGE, sort);

        if(keyword!=null && !keyword.isBlank())
        {
            // in case filter by category and search with keyword :
            if(categoryId!=null && !categoryId.equals("0"))
            {
                return productRepository.searchWithKeywordAndFilterByCategory(keyword.trim(), categoryId, pageable);
            }
            // else in case search with keyword only
            return productRepository.searchByKeyword(keyword.trim(), pageable);
        }

        // in case filter by category without search with keyword
        if(categoryId!=null && !categoryId.equals("0")) {
            return productRepository.findProductsByCategoryId(categoryId, pageable);
        }

        return productRepository.findAll(pageable);
    }

    @Override
    public Product saveProduct(Product product) {
        if (product.getId() == null) {
            // Nếu là tạo mới sản phẩm thì đặt thời gian tạo
            product.setCreatedTime(new Date());
        } else {
            // Nếu là chỉnh sửa, giữ nguyên thời gian tạo ban đầu
            Product existedProductInDB = productRepository.findById(product.getId()).orElse(null);
            if (existedProductInDB != null) {
                product.setCreatedTime(existedProductInDB.getCreatedTime());
            }
        }

        // Luôn cập nhật thời gian sửa
        product.setUpdatedTime(new Date());

        // 1. Loại bỏ các hình ảnh bổ sung khỏi sản phẩm trước khi lưu lần đầu
        Set<ProductImage> extraImages = product.getExtraImages();
        product.setExtraImages(new HashSet<>()); // Tạm thời bỏ các hình ảnh bổ sung ra

        // 2. Lưu sản phẩm trước để lấy ID
        Product savedProduct = productRepository.save(product);

        // 3. Lưu các hình ảnh bổ sung sau khi đã có ID sản phẩm
        Set<ProductImage> savedImages = new HashSet<>();
        for (ProductImage item : extraImages) {
            item.setProductId(savedProduct.getId());
            ProductImage savedImage = productImageRepository.save(item);
            savedImages.add(savedImage);
        }

        // 4. Cập nhật lại sản phẩm với các hình ảnh đã lưu
        savedProduct.setExtraImages(savedImages);
        return productRepository.save(savedProduct);
    }

    @Override
    public void deleteProduct(String id) {
        // delete productImage in advance :
        productImageRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }

}

package com.shopping.web.product.repository;

import com.shopping.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    @Query("{ $or: [ { name: { $regex: ?0, $options: 'i' } }, { price: { $regex: ?0, $options: 'i' } } ] }")
    public Page<Product> searchByKeyword(String keyword, Pageable pageable);

    public void deleteByCategoryId(String categoryId);

    // filter by category (include subcategory of that category):
    public Page<Product> findProductsByCategoryId(String categoryId, Pageable pageable);

    @Query("{ $and: [ " +
            "{ $or: [ { name: { $regex: ?0, $options: 'i' } }, { price: { $regex: ?0, $options: 'i' } } ] }, " +
            "{ categoryId: ?1 } " +
            "] }")
    public Page<Product> searchWithKeywordAndFilterByCategory(String keyword, String categoryId, Pageable pageable);

}

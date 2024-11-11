package com.shoppingbackend.admin.user.repository;

import com.shopping.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    public User findByEmail(String email);

    @Query("{ 'role': 'CUSTOMER' }")
    public Page<User> findByRoleCustomer(Pageable pageable);

    @Query("{ 'role': 'CUSTOMER' }")
    public List<User> findByRoleCustomer();



    @Query("{ $and: [ { role: 'CUSTOMER' }, { $or: [ { firstName: { $regex: ?0, $options: 'i' } }, { lastName: { $regex: ?0, $options: 'i' } }, { email: { $regex: ?0, $options: 'i' } } ] } ] }")
    public Page<User> searchByKeyword(String keyword, Pageable pageable);

}

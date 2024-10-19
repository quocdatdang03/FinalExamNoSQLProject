package com.shopping.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private boolean enabled;

    private String role;

    private Date createdTime;

}

package com.shopping.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection="categories")
public class Category {

    @Id
    private String id;

    private String name;

    private String description;

    private boolean enabled;
}

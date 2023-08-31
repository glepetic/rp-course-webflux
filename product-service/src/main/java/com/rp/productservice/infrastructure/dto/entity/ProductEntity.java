package com.rp.productservice.infrastructure.dto.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public record ProductEntity(@Id ObjectId id, String description, Integer price) {}

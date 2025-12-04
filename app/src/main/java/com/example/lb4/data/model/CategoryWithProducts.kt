package com.example.lb4.data.model

data class CategoryWithProducts(
    val category: CategoryEntity,
    val products: List<ProductEntity>
)
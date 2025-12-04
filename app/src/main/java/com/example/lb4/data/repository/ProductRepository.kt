package com.example.lb4.data.repository

import com.example.lb4.data.dao.CategoryDao
import com.example.lb4.data.dao.ProductDao
import com.example.lb4.data.model.CategoryEntity
import com.example.lb4.data.model.CategoryWithProducts
import com.example.lb4.data.model.ProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ProductRepository(
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao
) {

    fun getCategoriesWithProducts(): Flow<List<CategoryWithProducts>> {
        return combine(
            categoryDao.getAllCategories(),
            productDao.getAllProducts()
        ) { categories, products ->
            categories.map { category ->
                CategoryWithProducts(
                    category = category,
                    products = products.filter { it.categoryId == category.id }
                )
            }
        }
    }

    fun getAllCategories(): Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

    suspend fun getCategoryById(id: Int): CategoryEntity? = categoryDao.getCategoryById(id)

    suspend fun insertCategory(category: CategoryEntity): Long = categoryDao.insertCategory(category)

    suspend fun updateCategory(category: CategoryEntity) = categoryDao.updateCategory(category)

    suspend fun deleteCategory(category: CategoryEntity) = categoryDao.deleteCategory(category)

    fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()

    fun getProductsByCategory(categoryId: Int): Flow<List<ProductEntity>> =
        productDao.getProductsByCategory(categoryId)

    suspend fun getProductById(id: Int): ProductEntity? = productDao.getProductById(id)

    suspend fun insertProduct(product: ProductEntity): Long = productDao.insertProduct(product)

    suspend fun updateProduct(product: ProductEntity) = productDao.updateProduct(product)

    suspend fun deleteProduct(product: ProductEntity) = productDao.deleteProduct(product)
}
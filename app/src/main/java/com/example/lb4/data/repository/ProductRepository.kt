package com.example.lb4.data.repository

import com.example.lb4.data.dao.CategoryDao
import com.example.lb4.data.dao.ProductDao
import com.example.lb4.data.dao.PromotionDao
import com.example.lb4.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ProductRepository(
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao,
    private val promotionDao: PromotionDao
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

    fun getListItems(): Flow<List<ListItem>> {
        return combine(
            getCategoriesWithProducts(),
            promotionDao.getAllPromotions()
        ) { categoriesWithProducts, promotions ->
            val items = mutableListOf<ListItem>()

            promotions.forEach { promotion ->
                items.add(
                    ListItem.PromotionItem(
                        id = promotion.id + 10000,
                        promotion = promotion
                    )
                )
            }

            categoriesWithProducts.forEach { categoryWithProducts ->
                items.add(
                    ListItem.CategoryHeader(
                        id = categoryWithProducts.category.id * 1000,
                        categoryWithProducts = categoryWithProducts
                    )
                )
            }

            items
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

    fun getAllPromotions(): Flow<List<PromotionEntity>> = promotionDao.getAllPromotions()

    suspend fun insertPromotion(promotion: PromotionEntity): Long = promotionDao.insertPromotion(promotion)

    suspend fun updatePromotion(promotion: PromotionEntity) = promotionDao.updatePromotion(promotion)

    suspend fun deletePromotion(promotion: PromotionEntity) = promotionDao.deletePromotion(promotion)
}
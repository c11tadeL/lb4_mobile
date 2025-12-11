package com.example.lb4.data.model

sealed class ListItem {
    abstract val id: Int

    data class CategoryHeader(
        override val id: Int,
        val categoryWithProducts: CategoryWithProducts
    ) : ListItem()

    data class PromotionItem(
        override val id: Int,
        val promotion: PromotionEntity
    ) : ListItem()
}
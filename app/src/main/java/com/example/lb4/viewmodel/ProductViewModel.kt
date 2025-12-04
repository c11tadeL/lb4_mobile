package com.example.lb4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lb4.data.model.CategoryEntity
import com.example.lb4.data.model.CategoryWithProducts
import com.example.lb4.data.model.ProductEntity
import com.example.lb4.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _categoriesWithProducts = MutableStateFlow<List<CategoryWithProducts>>(emptyList())
    val categoriesWithProducts: StateFlow<List<CategoryWithProducts>> = _categoriesWithProducts.asStateFlow()

    private val _allCategories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val allCategories: StateFlow<List<CategoryEntity>> = _allCategories.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getCategoriesWithProducts().collect {
                _categoriesWithProducts.value = it
            }
        }
        viewModelScope.launch {
            repository.getAllCategories().collect {
                _allCategories.value = it
            }
        }
    }


    fun addCategory(name: String, icon: String) {
        viewModelScope.launch {
            repository.insertCategory(CategoryEntity(name = name, icon = icon))
        }
    }

    fun updateCategory(category: CategoryEntity) {
        viewModelScope.launch {
            repository.updateCategory(category)
        }
    }

    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }

    fun addProduct(name: String, price: Double, emoji: String, categoryId: Int) {
        viewModelScope.launch {
            repository.insertProduct(
                ProductEntity(
                    name = name,
                    price = price,
                    emoji = emoji,
                    categoryId = categoryId
                )
            )
        }
    }

    fun updateProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }
}

class ProductViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
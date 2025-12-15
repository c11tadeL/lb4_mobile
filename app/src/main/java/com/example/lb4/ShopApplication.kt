package com.example.lb4

import android.app.Application
import com.example.lb4.data.database.AppDatabase
import com.example.lb4.data.repository.ProductRepository

class ShopApplication : Application() {

    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    val repository: ProductRepository by lazy {
        ProductRepository(
            categoryDao = database.categoryDao(),
            productDao = database.productDao(),
            promotionDao = database.promotionDao()
        )
    }
}


package com.example.lb4.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.lb4.data.dao.CategoryDao
import com.example.lb4.data.dao.ProductDao
import com.example.lb4.data.dao.PromotionDao
import com.example.lb4.data.model.CategoryEntity
import com.example.lb4.data.model.ProductEntity
import com.example.lb4.data.model.PromotionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [CategoryEntity::class, ProductEntity::class, PromotionEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun promotionDao(): PromotionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "product_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(
                        database.categoryDao(),
                        database.productDao(),
                        database.promotionDao()
                    )
                }
            }
        }

        suspend fun populateDatabase(
            categoryDao: CategoryDao,
            productDao: ProductDao,
            promotionDao: PromotionDao
        ) {
            val fruitId = categoryDao.insertCategory(
                CategoryEntity(name = "Фрукти", icon = "🍎")
            ).toInt()

            val vegId = categoryDao.insertCategory(
                CategoryEntity(name = "Овочі", icon = "🥕")
            ).toInt()

            val meatId = categoryDao.insertCategory(
                CategoryEntity(name = "М'ясо", icon = "🍖")
            ).toInt()

            productDao.insertProducts(
                listOf(
                    ProductEntity(name = "Яблуко", price = 25.0, emoji = "🍎", categoryId = fruitId),
                    ProductEntity(name = "Банан", price = 30.0, emoji = "🍌", categoryId = fruitId),
                    ProductEntity(name = "Апельсин", price = 35.0, emoji = "🍊", categoryId = fruitId),
                    ProductEntity(name = "Морква", price = 20.0, emoji = "🥕", categoryId = vegId),
                    ProductEntity(name = "Огірок", price = 22.0, emoji = "🥒", categoryId = vegId),
                    ProductEntity(name = "Помідор", price = 28.0, emoji = "🍅", categoryId = vegId),
                    ProductEntity(name = "Курка", price = 120.0, emoji = "🍗", categoryId = meatId),
                    ProductEntity(name = "Свинина", price = 150.0, emoji = "🥓", categoryId = meatId)
                )
            )

            // Додаємо акції
            promotionDao.insertPromotion(
                PromotionEntity(
                    title = "Літній розпродаж!",
                    description = "Знижка на всі фрукти",
                    discount = 15,
                    emoji = "🔥"
                )
            )

            promotionDao.insertPromotion(
                PromotionEntity(
                    title = "Акція тижня",
                    description = "Овочі за пів ціни",
                    discount = 50,
                    emoji = "⭐"
                )
            )

            promotionDao.insertPromotion(
                PromotionEntity(
                    title = "Бонус",
                    description = "Купи 2 - отримай 1 у подарунок",
                    discount = 33,
                    emoji = "🎁"
                )
            )
        }
    }
}
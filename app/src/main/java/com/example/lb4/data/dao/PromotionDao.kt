package com.example.lb4.data.dao

import androidx.room.*
import com.example.lb4.data.model.PromotionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PromotionDao {

    @Query("SELECT * FROM promotions ORDER BY id ASC")
    fun getAllPromotions(): Flow<List<PromotionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPromotion(promotion: PromotionEntity): Long

    @Update
    suspend fun updatePromotion(promotion: PromotionEntity)

    @Delete
    suspend fun deletePromotion(promotion: PromotionEntity)

    @Query("DELETE FROM promotions")
    suspend fun deleteAllPromotions()
}
package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AdVariationEntity
import com.example.data.model.BrandSettingsEntity
import com.example.data.model.CampaignEntity
import com.example.data.model.ContentItemEntity
import com.example.data.model.CopilotMessageEntity
import com.example.data.model.EmailCampaignEntity
import com.example.data.model.LandingPageEntity
import com.example.data.model.MarketingStrategyEntity
import com.example.data.model.ProductEntity
import com.example.data.model.SocialPostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketDao {

    // --- Products ---
    @Query("SELECT * FROM products ORDER BY createdAt DESC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    fun getProductById(id: Long): Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getProductByIdDirect(id: Long): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProductById(id: Long)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int

    // --- Marketing Strategies ---
    @Query("SELECT * FROM marketing_strategies WHERE productId = :productId ORDER BY createdAt DESC LIMIT 1")
    fun getStrategyForProduct(productId: Long): Flow<MarketingStrategyEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStrategy(strategy: MarketingStrategyEntity): Long

    @Query("DELETE FROM marketing_strategies WHERE productId = :productId")
    suspend fun deleteStrategyForProduct(productId: Long)

    // --- Campaigns ---
    @Query("SELECT * FROM campaigns ORDER BY createdAt DESC")
    fun getAllCampaigns(): Flow<List<CampaignEntity>>

    @Query("SELECT * FROM campaigns WHERE productId = :productId ORDER BY createdAt DESC")
    fun getCampaignsForProduct(productId: Long): Flow<List<CampaignEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaign(campaign: CampaignEntity): Long

    @Update
    suspend fun updateCampaign(campaign: CampaignEntity)

    @Query("DELETE FROM campaigns WHERE id = :id")
    suspend fun deleteCampaignById(id: Long)

    // --- Content Items ---
    @Query("SELECT * FROM content_generations ORDER BY createdAt DESC")
    fun getAllContent(): Flow<List<ContentItemEntity>>

    @Query("SELECT * FROM content_generations WHERE productId = :productId ORDER BY createdAt DESC")
    fun getContentForProduct(productId: Long): Flow<List<ContentItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContent(contentItem: ContentItemEntity): Long

    @Update
    suspend fun updateContent(contentItem: ContentItemEntity)

    @Query("DELETE FROM content_generations WHERE id = :id")
    suspend fun deleteContentById(id: Long)

    // --- Ad Variations ---
    @Query("SELECT * FROM advertisements ORDER BY createdAt DESC")
    fun getAllAds(): Flow<List<AdVariationEntity>>

    @Query("SELECT * FROM advertisements WHERE productId = :productId ORDER BY createdAt DESC")
    fun getAdsForProduct(productId: Long): Flow<List<AdVariationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAd(ad: AdVariationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAds(ads: List<AdVariationEntity>)

    @Update
    suspend fun updateAd(ad: AdVariationEntity)

    @Query("DELETE FROM advertisements WHERE id = :id")
    suspend fun deleteAdById(id: Long)

    // --- Social Calendar & Posts ---
    @Query("SELECT * FROM social_posts ORDER BY scheduledDate ASC, scheduledTime ASC")
    fun getAllSocialPosts(): Flow<List<SocialPostEntity>>

    @Query("SELECT * FROM social_posts WHERE productId = :productId ORDER BY scheduledDate ASC")
    fun getSocialPostsForProduct(productId: Long): Flow<List<SocialPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSocialPost(post: SocialPostEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSocialPosts(posts: List<SocialPostEntity>)

    @Update
    suspend fun updateSocialPost(post: SocialPostEntity)

    @Query("DELETE FROM social_posts WHERE id = :id")
    suspend fun deleteSocialPostById(id: Long)

    // --- Email Campaigns ---
    @Query("SELECT * FROM email_campaigns ORDER BY createdAt DESC")
    fun getAllEmailCampaigns(): Flow<List<EmailCampaignEntity>>

    @Query("SELECT * FROM email_campaigns WHERE productId = :productId ORDER BY createdAt DESC")
    fun getEmailCampaignsForProduct(productId: Long): Flow<List<EmailCampaignEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmailCampaign(email: EmailCampaignEntity): Long

    @Update
    suspend fun updateEmailCampaign(email: EmailCampaignEntity)

    @Query("DELETE FROM email_campaigns WHERE id = :id")
    suspend fun deleteEmailCampaignById(id: Long)

    // --- Landing Pages ---
    @Query("SELECT * FROM landing_pages WHERE productId = :productId ORDER BY createdAt DESC LIMIT 1")
    fun getLandingPageForProduct(productId: Long): Flow<LandingPageEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLandingPage(landingPage: LandingPageEntity): Long

    @Update
    suspend fun updateLandingPage(landingPage: LandingPageEntity)

    // --- Brand Voice Settings ---
    @Query("SELECT * FROM brand_settings WHERE id = 1 LIMIT 1")
    fun getBrandSettings(): Flow<BrandSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrandSettings(brandSettings: BrandSettingsEntity)

    // --- Copilot Messages ---
    @Query("SELECT * FROM copilot_messages ORDER BY timestamp ASC")
    fun getCopilotMessages(): Flow<List<CopilotMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCopilotMessage(message: CopilotMessageEntity): Long

    @Query("DELETE FROM copilot_messages")
    suspend fun clearCopilotMessages()
}

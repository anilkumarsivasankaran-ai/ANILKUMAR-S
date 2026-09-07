package com.example.data.repository

import com.example.data.local.DemoData
import com.example.data.local.MarketDao
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
import com.example.data.remote.AiMarketingService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class MarketRepository(
    private val dao: MarketDao,
    private val aiService: AiMarketingService = AiMarketingService()
) {

    // --- Products ---
    val allProducts: Flow<List<ProductEntity>> = dao.getAllProducts()

    fun getProduct(id: Long): Flow<ProductEntity?> = dao.getProductById(id)

    suspend fun saveProduct(product: ProductEntity): Long {
        val id = dao.insertProduct(product)
        // Automatically generate initial marketing strategy in background for the new product
        val brand = dao.getBrandSettings().firstOrNull() ?: BrandSettingsEntity()
        val strategy = aiService.generateMarketingStrategy(product.copy(id = id), brand)
        dao.insertStrategy(strategy)
        return id
    }

    suspend fun updateProduct(product: ProductEntity) = dao.updateProduct(product)

    suspend fun deleteProduct(id: Long) {
        dao.deleteProductById(id)
        dao.deleteStrategyForProduct(id)
    }

    // --- Strategy ---
    fun getStrategy(productId: Long): Flow<MarketingStrategyEntity?> =
        dao.getStrategyForProduct(productId)

    suspend fun regenerateStrategy(productId: Long): MarketingStrategyEntity? {
        val product = dao.getProductByIdDirect(productId) ?: return null
        val brand = dao.getBrandSettings().firstOrNull() ?: BrandSettingsEntity()
        val strategy = aiService.generateMarketingStrategy(product, brand)
        dao.insertStrategy(strategy)
        return strategy
    }

    // --- Campaigns ---
    val allCampaigns: Flow<List<CampaignEntity>> = dao.getAllCampaigns()

    fun getCampaignsForProduct(productId: Long): Flow<List<CampaignEntity>> =
        dao.getCampaignsForProduct(productId)

    suspend fun saveCampaign(campaign: CampaignEntity) = dao.insertCampaign(campaign)

    suspend fun updateCampaign(campaign: CampaignEntity) = dao.updateCampaign(campaign)

    suspend fun deleteCampaign(id: Long) = dao.deleteCampaignById(id)

    // --- Content Studio ---
    val allContent: Flow<List<ContentItemEntity>> = dao.getAllContent()

    fun getContentForProduct(productId: Long): Flow<List<ContentItemEntity>> =
        dao.getContentForProduct(productId)

    suspend fun generateContent(
        productId: Long,
        contentType: String,
        targetAudience: String,
        objective: String,
        tone: String
    ): List<ContentItemEntity> {
        val product = dao.getProductByIdDirect(productId) ?: DemoData.sampleProduct
        val brand = dao.getBrandSettings().firstOrNull() ?: BrandSettingsEntity()
        val variations = aiService.generateContentVariations(
            product = product,
            brand = brand,
            contentType = contentType,
            targetAudience = targetAudience,
            objective = objective,
            customTone = tone
        )
        variations.forEach { dao.insertContent(it) }
        return variations
    }

    suspend fun saveContentItem(contentItem: ContentItemEntity) = dao.insertContent(contentItem)

    suspend fun deleteContentItem(id: Long) = dao.deleteContentById(id)

    // --- Advertisements ---
    val allAds: Flow<List<AdVariationEntity>> = dao.getAllAds()

    fun getAdsForProduct(productId: Long): Flow<List<AdVariationEntity>> =
        dao.getAdsForProduct(productId)

    suspend fun generateAds(
        productId: Long,
        platform: String,
        objective: String
    ): List<AdVariationEntity> {
        val product = dao.getProductByIdDirect(productId) ?: DemoData.sampleProduct
        val brand = dao.getBrandSettings().firstOrNull() ?: BrandSettingsEntity()
        val ads = aiService.generateAdVariations(product, brand, platform, objective)
        dao.insertAds(ads)
        return ads
    }

    suspend fun saveAd(ad: AdVariationEntity) = dao.insertAd(ad)

    suspend fun deleteAd(id: Long) = dao.deleteAdById(id)

    // --- Social Calendar ---
    val allSocialPosts: Flow<List<SocialPostEntity>> = dao.getAllSocialPosts()

    fun getSocialPostsForProduct(productId: Long): Flow<List<SocialPostEntity>> =
        dao.getSocialPostsForProduct(productId)

    suspend fun generateSocialCalendar(productId: Long): List<SocialPostEntity> {
        val product = dao.getProductByIdDirect(productId) ?: DemoData.sampleProduct
        val brand = dao.getBrandSettings().firstOrNull() ?: BrandSettingsEntity()
        val posts = aiService.generateSocialCalendar(product, brand)
        dao.insertSocialPosts(posts)
        return posts
    }

    suspend fun saveSocialPost(post: SocialPostEntity) = dao.insertSocialPost(post)

    suspend fun updateSocialPost(post: SocialPostEntity) = dao.updateSocialPost(post)

    suspend fun deleteSocialPost(id: Long) = dao.deleteSocialPostById(id)

    // --- Email Marketing ---
    val allEmailCampaigns: Flow<List<EmailCampaignEntity>> = dao.getAllEmailCampaigns()

    fun getEmailCampaignsForProduct(productId: Long): Flow<List<EmailCampaignEntity>> =
        dao.getEmailCampaignsForProduct(productId)

    suspend fun generateEmailCampaign(productId: Long, emailType: String): EmailCampaignEntity {
        val product = dao.getProductByIdDirect(productId) ?: DemoData.sampleProduct
        val brand = dao.getBrandSettings().firstOrNull() ?: BrandSettingsEntity()
        val email = aiService.generateEmailCampaign(product, brand, emailType)
        dao.insertEmailCampaign(email)
        return email
    }

    suspend fun deleteEmailCampaign(id: Long) = dao.deleteEmailCampaignById(id)

    // --- Landing Page ---
    fun getLandingPage(productId: Long): Flow<LandingPageEntity?> =
        dao.getLandingPageForProduct(productId)

    suspend fun generateLandingPage(productId: Long): LandingPageEntity {
        val product = dao.getProductByIdDirect(productId) ?: DemoData.sampleProduct
        val brand = dao.getBrandSettings().firstOrNull() ?: BrandSettingsEntity()
        val page = aiService.generateLandingPage(product, brand)
        dao.insertLandingPage(page)
        return page
    }

    suspend fun updateLandingPage(page: LandingPageEntity) = dao.updateLandingPage(page)

    // --- Brand Voice ---
    val brandSettings: Flow<BrandSettingsEntity?> = dao.getBrandSettings()

    suspend fun saveBrandSettings(settings: BrandSettingsEntity) =
        dao.insertBrandSettings(settings)

    // --- Copilot Messages ---
    val copilotMessages: Flow<List<CopilotMessageEntity>> = dao.getCopilotMessages()

    suspend fun askCopilot(userText: String, productId: Long?): String {
        dao.insertCopilotMessage(
            CopilotMessageEntity(
                productId = productId,
                isUser = true,
                message = userText
            )
        )
        val product = productId?.let { dao.getProductByIdDirect(it) }
        val brand = dao.getBrandSettings().firstOrNull()
        val campaigns = dao.getAllCampaigns().firstOrNull() ?: emptyList()

        val reply = aiService.chatWithCopilot(userText, product, brand, campaigns)
        dao.insertCopilotMessage(
            CopilotMessageEntity(
                productId = productId,
                isUser = false,
                message = reply
            )
        )
        return reply
    }

    suspend fun clearCopilotHistory() = dao.clearCopilotMessages()

    suspend fun resetToDemoData() {
        DemoData.sampleCampaigns.forEach { dao.deleteCampaignById(it.id) }
        dao.clearCopilotMessages()
        // Re-seed demo product and assets
        val productId = dao.insertProduct(DemoData.sampleProduct)
        dao.insertStrategy(DemoData.sampleStrategy.copy(productId = productId))
        DemoData.sampleCampaigns.forEach { dao.insertCampaign(it.copy(productId = productId)) }
        DemoData.sampleContentItems.forEach { dao.insertContent(it.copy(productId = productId)) }
        dao.insertAds(DemoData.sampleAds.map { it.copy(productId = productId) })
        dao.insertSocialPosts(DemoData.sampleSocialPosts.map { it.copy(productId = productId) })
        dao.insertEmailCampaign(DemoData.sampleEmailCampaign.copy(productId = productId))
        dao.insertLandingPage(DemoData.sampleLandingPage.copy(productId = productId))
        dao.insertBrandSettings(BrandSettingsEntity())
        DemoData.sampleCopilotMessages.forEach { dao.insertCopilotMessage(it.copy(productId = productId)) }
    }
}

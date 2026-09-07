package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.MarketDatabase
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
import com.example.data.repository.MarketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AnalyticsSummary(
    val totalRevenue: Double = 0.0,
    val totalSpend: Double = 0.0,
    val totalReach: Int = 0,
    val totalImpressions: Int = 0,
    val totalClicks: Int = 0,
    val totalConversions: Int = 0,
    val ctrPercent: Double = 0.0,
    val conversionRatePercent: Double = 0.0,
    val cpc: Double = 0.0,
    val cpa: Double = 0.0,
    val roas: Double = 0.0,
    val activeCampaignCount: Int = 0,
    val totalCampaignCount: Int = 0
)

class MarketViewModel(application: Application) : AndroidViewModel(application) {

    private val database = MarketDatabase.getDatabase(application, viewModelScope)
    private val repository = MarketRepository(database.marketDao())

    // Products State
    val products: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedProductId = MutableStateFlow<Long?>(null)
    val selectedProductId: StateFlow<Long?> = _selectedProductId.asStateFlow()

    // Active Product State
    val activeProduct: StateFlow<ProductEntity?> = combine(products, _selectedProductId) { list, id ->
        when {
            id != null -> list.find { it.id == id } ?: list.firstOrNull()
            list.isNotEmpty() -> list.first()
            else -> null
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Marketing Strategy for active product
    val currentStrategy: StateFlow<MarketingStrategyEntity?> = activeProduct.flatMapLatest { prod ->
        if (prod != null) repository.getStrategy(prod.id) else flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Campaigns State
    val campaigns: StateFlow<List<CampaignEntity>> = repository.allCampaigns
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Content Items State
    val contentItems: StateFlow<List<ContentItemEntity>> = repository.allContent
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Ad Variations State
    val adVariations: StateFlow<List<AdVariationEntity>> = repository.allAds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Social Posts State
    val socialPosts: StateFlow<List<SocialPostEntity>> = repository.allSocialPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Email Campaigns State
    val emailCampaigns: StateFlow<List<EmailCampaignEntity>> = repository.allEmailCampaigns
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Landing Page for active product
    val currentLandingPage: StateFlow<LandingPageEntity?> = activeProduct.flatMapLatest { prod ->
        if (prod != null) repository.getLandingPage(prod.id) else flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Brand Settings
    val brandSettings: StateFlow<BrandSettingsEntity> = repository.brandSettings.flatMapLatest {
        flowOf(it ?: BrandSettingsEntity())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BrandSettingsEntity())

    // Copilot Messages
    val copilotMessages: StateFlow<List<CopilotMessageEntity>> = repository.copilotMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI Loading & Action States
    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _userFeedbackMessage = MutableStateFlow<String?>(null)
    val userFeedbackMessage: StateFlow<String?> = _userFeedbackMessage.asStateFlow()

    // Computed Analytics
    val analytics: StateFlow<AnalyticsSummary> = campaigns.flatMapLatest { list ->
        val totalRevenue = list.sumOf { it.revenue }
        val totalSpend = list.sumOf { it.spend }
        val totalReach = list.sumOf { it.reach }
        val totalImpressions = list.sumOf { it.impressions }
        val totalClicks = list.sumOf { it.clicks }
        val totalConversions = list.sumOf { it.conversions }
        val activeCount = list.count { it.status.equals("Active", ignoreCase = true) }

        val ctr = if (totalImpressions > 0) (totalClicks.toDouble() / totalImpressions) * 100 else 0.0
        val cr = if (totalClicks > 0) (totalConversions.toDouble() / totalClicks) * 100 else 0.0
        val cpc = if (totalClicks > 0) totalSpend / totalClicks else 0.0
        val cpa = if (totalConversions > 0) totalSpend / totalConversions else 0.0
        val roas = if (totalSpend > 0) totalRevenue / totalSpend else 0.0

        flowOf(
            AnalyticsSummary(
                totalRevenue = totalRevenue,
                totalSpend = totalSpend,
                totalReach = totalReach,
                totalImpressions = totalImpressions,
                totalClicks = totalClicks,
                totalConversions = totalConversions,
                ctrPercent = ctr,
                conversionRatePercent = cr,
                cpc = cpc,
                cpa = cpa,
                roas = roas,
                activeCampaignCount = activeCount,
                totalCampaignCount = list.size
            )
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AnalyticsSummary())

    init {
        // Seed initial data if empty
        viewModelScope.launch {
            MarketDatabase.seedInitialData(database.marketDao())
        }
    }

    fun selectProduct(id: Long) {
        _selectedProductId.value = id
    }

    fun dismissFeedback() {
        _userFeedbackMessage.value = null
    }

    // --- Product Management ---
    fun addProduct(product: ProductEntity) {
        viewModelScope.launch {
            _isGenerating.value = true
            try {
                val newId = repository.saveProduct(product)
                _selectedProductId.value = newId
                _userFeedbackMessage.value = "Product created & AI Marketing Strategy generated!"
            } catch (e: Exception) {
                _userFeedbackMessage.value = "Error creating product: ${e.message}"
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            repository.deleteProduct(id)
            _userFeedbackMessage.value = "Product removed."
        }
    }

    fun regenerateStrategy() {
        val prod = activeProduct.value ?: return
        viewModelScope.launch {
            _isGenerating.value = true
            try {
                repository.regenerateStrategy(prod.id)
                _userFeedbackMessage.value = "AI Marketing Strategy regenerated!"
            } catch (e: Exception) {
                _userFeedbackMessage.value = "Generation failed: ${e.message}"
            } finally {
                _isGenerating.value = false
            }
        }
    }

    // --- Content Studio Generation ---
    fun generateContent(
        contentType: String,
        targetAudience: String,
        objective: String,
        tone: String
    ) {
        val prod = activeProduct.value ?: return
        viewModelScope.launch {
            _isGenerating.value = true
            try {
                val items = repository.generateContent(
                    productId = prod.id,
                    contentType = contentType,
                    targetAudience = targetAudience,
                    objective = objective,
                    tone = tone
                )
                _userFeedbackMessage.value = "Generated ${items.size} variations for $contentType!"
            } catch (e: Exception) {
                _userFeedbackMessage.value = "Error generating content: ${e.message}"
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun deleteContentItem(id: Long) {
        viewModelScope.launch {
            repository.deleteContentItem(id)
            _userFeedbackMessage.value = "Content item deleted."
        }
    }

    // --- Ad Generator ---
    fun generateAds(platform: String, objective: String) {
        val prod = activeProduct.value ?: return
        viewModelScope.launch {
            _isGenerating.value = true
            try {
                val ads = repository.generateAds(
                    productId = prod.id,
                    platform = platform,
                    objective = objective
                )
                _userFeedbackMessage.value = "Generated ${ads.size} A/B Ad variations for $platform!"
            } catch (e: Exception) {
                _userFeedbackMessage.value = "Error generating ads: ${e.message}"
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun deleteAd(id: Long) {
        viewModelScope.launch {
            repository.deleteAd(id)
            _userFeedbackMessage.value = "Ad deleted."
        }
    }

    // --- Social Calendar ---
    fun generateSocialCalendar() {
        val prod = activeProduct.value ?: return
        viewModelScope.launch {
            _isGenerating.value = true
            try {
                val posts = repository.generateSocialCalendar(prod.id)
                _userFeedbackMessage.value = "Generated ${posts.size} scheduled posts in Social Calendar!"
            } catch (e: Exception) {
                _userFeedbackMessage.value = "Calendar generation failed: ${e.message}"
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun addSocialPost(post: SocialPostEntity) {
        viewModelScope.launch {
            repository.saveSocialPost(post)
            _userFeedbackMessage.value = "Post scheduled successfully."
        }
    }

    fun deleteSocialPost(id: Long) {
        viewModelScope.launch {
            repository.deleteSocialPost(id)
            _userFeedbackMessage.value = "Post deleted."
        }
    }

    // --- Email Marketing ---
    fun generateEmailCampaign(emailType: String) {
        val prod = activeProduct.value ?: return
        viewModelScope.launch {
            _isGenerating.value = true
            try {
                repository.generateEmailCampaign(prod.id, emailType)
                _userFeedbackMessage.value = "$emailType email generated with A/B subject lines!"
            } catch (e: Exception) {
                _userFeedbackMessage.value = "Email generation failed: ${e.message}"
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun deleteEmailCampaign(id: Long) {
        viewModelScope.launch {
            repository.deleteEmailCampaign(id)
            _userFeedbackMessage.value = "Email campaign removed."
        }
    }

    // --- Landing Page Generator ---
    fun generateLandingPage() {
        val prod = activeProduct.value ?: return
        viewModelScope.launch {
            _isGenerating.value = true
            try {
                repository.generateLandingPage(prod.id)
                _userFeedbackMessage.value = "AI Landing Page copy generated!"
            } catch (e: Exception) {
                _userFeedbackMessage.value = "Landing page generation failed: ${e.message}"
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun updateLandingPage(page: LandingPageEntity) {
        viewModelScope.launch {
            repository.updateLandingPage(page)
            _userFeedbackMessage.value = "Landing page updated."
        }
    }

    // --- Campaigns Management ---
    fun addCampaign(campaign: CampaignEntity) {
        viewModelScope.launch {
            repository.saveCampaign(campaign)
            _userFeedbackMessage.value = "Campaign created."
        }
    }

    fun updateCampaign(campaign: CampaignEntity) {
        viewModelScope.launch {
            repository.updateCampaign(campaign)
            _userFeedbackMessage.value = "Campaign updated."
        }
    }

    fun deleteCampaign(id: Long) {
        viewModelScope.launch {
            repository.deleteCampaign(id)
            _userFeedbackMessage.value = "Campaign deleted."
        }
    }

    // --- Brand Voice Settings ---
    fun saveBrandSettings(settings: BrandSettingsEntity) {
        viewModelScope.launch {
            repository.saveBrandSettings(settings)
            _userFeedbackMessage.value = "Brand Voice settings saved."
        }
    }

    // --- Marketing Copilot Chat ---
    fun sendCopilotMessage(userPrompt: String) {
        if (userPrompt.isBlank()) return
        val prod = activeProduct.value
        viewModelScope.launch {
            _isGenerating.value = true
            try {
                repository.askCopilot(userPrompt, prod?.id)
            } catch (e: Exception) {
                _userFeedbackMessage.value = "Copilot failed to respond: ${e.message}"
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun clearCopilotChat() {
        viewModelScope.launch {
            repository.clearCopilotHistory()
            _userFeedbackMessage.value = "Copilot history cleared."
        }
    }

    // --- Demo Data Reset ---
    fun resetToDemoData() {
        viewModelScope.launch {
            _isGenerating.value = true
            try {
                repository.resetToDemoData()
                _selectedProductId.value = 1L
                _userFeedbackMessage.value = "Reset to pristine Demo data!"
            } catch (e: Exception) {
                _userFeedbackMessage.value = "Reset failed: ${e.message}"
            } finally {
                _isGenerating.value = false
            }
        }
    }
}

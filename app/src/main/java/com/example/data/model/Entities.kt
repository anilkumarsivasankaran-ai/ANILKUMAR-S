package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String,
    val description: String,
    val price: String = "$39.99",
    val features: String = "",
    val benefits: String = "",
    val targetLocation: String = "United States, Canada, Europe",
    val targetAgeGroup: String = "22-45",
    val targetGender: String = "All Genders",
    val targetCustomerType: String = "Eco-conscious professionals & outdoor enthusiasts",
    val painPoints: String = "Disposable plastics, bottles that sweat or leak, warm drinks after 2 hours",
    val usp: String = "Triple-insulated ocean-bound recycled stainless steel bottle keeping liquids cold for 36 hours",
    val competitors: String = "Hydro Flask, Yeti, Stanley",
    val brandName: String = "EcoBottle Co.",
    val brandTone: String = "Inspiring, Clean, Premium, Sustainable",
    val websiteUrl: String = "https://ecobottle.example.com",
    val imageUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "marketing_strategies")
data class MarketingStrategyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val primaryCustomer: String,
    val secondaryCustomer: String,
    val demographics: String,
    val interests: String,
    val behavior: String,
    val painPoints: String,
    val buyingMotivations: String,
    val uniqueValueProp: String,
    val brandPositioning: String,
    val productPositioning: String,
    val keySellingPoints: String,
    val competitiveAdvantages: String,
    val marketingObjectives: String,
    val recommendedChannels: String,
    val campaignIdeas: String,
    val contentStrategy: String,
    val customerAcquisitionStrategy: String,
    val customerRetentionStrategy: String,
    val recommendedBudgetAllocation: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "campaigns")
data class CampaignEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val name: String,
    val objective: String,
    val targetAudience: String,
    val marketingChannel: String,
    val startDate: String,
    val endDate: String,
    val budget: Double,
    val status: String = "Active", // Draft, Scheduled, Active, Paused, Completed
    val reach: Int = 15400,
    val impressions: Int = 42800,
    val clicks: Int = 2140,
    val conversions: Int = 186,
    val spend: Double = 850.0,
    val revenue: Double = 7440.0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "content_generations")
data class ContentItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val contentType: String,
    val variationTone: String,
    val title: String,
    val body: String,
    val callToAction: String,
    val hashtagsKeywords: String,
    val campaignName: String = "",
    val isSaved: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "advertisements")
data class AdVariationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val platform: String, // Instagram, Facebook, Google, LinkedIn, TikTok
    val headline: String,
    val primaryText: String,
    val description: String,
    val cta: String,
    val audienceSuggestion: String,
    val keywords: String,
    val creativeConcept: String,
    val variantLabel: String, // Variation A, Variation B, Variation C
    val impressions: Int = 12500,
    val clicks: Int = 620,
    val conversions: Int = 48,
    val isWinner: Boolean = false,
    val aiAnalysis: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "social_posts")
data class SocialPostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val platform: String, // Instagram, Facebook, LinkedIn, X/Twitter, YouTube
    val scheduledDate: String,
    val scheduledTime: String,
    val contentType: String,
    val campaignName: String,
    val caption: String,
    val hashtags: String,
    val status: String = "Scheduled", // Draft, Scheduled, Published
    val engagement: String = "Pending",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "email_campaigns")
data class EmailCampaignEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val emailType: String, // Promotional, Product Launch, Welcome, Discount, Abandoned Cart, Retention, Newsletter
    val subjectLineA: String,
    val subjectLineB: String,
    val previewText: String,
    val emailBody: String,
    val callToAction: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "landing_pages")
data class LandingPageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val heroHeadline: String,
    val heroSubheadline: String,
    val heroCta: String,
    val problemStatement: String,
    val benefits: String,
    val features: String,
    val socialProof: String,
    val testimonials: String,
    val pricing: String,
    val faqs: String,
    val ctaSection: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "brand_settings")
data class BrandSettingsEntity(
    @PrimaryKey val id: Long = 1,
    val brandName: String = "EcoBottle Co.",
    val brandDescription: String = "Sustainable hydration company committed to eliminating single-use plastics through innovative thermal drinkware.",
    val brandPersonality: String = "Modern, Eco-Conscious, Premium, Empowering",
    val tone: String = "Inspirational, Clear, Confident, Action-Oriented",
    val targetAudience: String = "Active professionals, conscious consumers, outdoor travelers",
    val preferredVocabulary: String = "Sustainable, Zero-Waste, Triple-Insulated, Eco-Warrior, Pure, Enduring",
    val wordsToAvoid: String = "Cheap, Disposable, Basic, Plastic, Ordinary",
    val brandColors: String = "#0F766E, #0284C7, #F59E0B",
    val logoUrl: String = ""
)

@Entity(tableName = "copilot_messages")
data class CopilotMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long? = null,
    val isUser: Boolean,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

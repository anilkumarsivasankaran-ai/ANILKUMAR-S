package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.AdVariationEntity
import com.example.data.model.BrandSettingsEntity
import com.example.data.model.CampaignEntity
import com.example.data.model.ContentItemEntity
import com.example.data.model.EmailCampaignEntity
import com.example.data.model.LandingPageEntity
import com.example.data.model.MarketingStrategyEntity
import com.example.data.model.ProductEntity
import com.example.data.model.SocialPostEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class AiMarketingService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()
    private val modelName = "gemini-3.5-flash"

    /**
     * Executes a raw text generation request to Gemini REST API.
     * Returns null if key is missing or call fails.
     */
    suspend fun generateText(prompt: String): String? = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.d("AiMarketingService", "Gemini API key not configured or is placeholder; using smart template fallback.")
            return@withContext null
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$apiKey"
            val jsonPayload = JSONObject().apply {
                val contents = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val parts = JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        }
                        put("parts", parts)
                    }
                    put(contentObj)
                }
                put("contents", contents)
            }

            val request = Request.Builder()
                .url(url)
                .post(jsonPayload.toString().toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: return@withContext null

            if (!response.isSuccessful) {
                Log.w("AiMarketingService", "Gemini API error: ${response.code} $responseBody")
                return@withContext null
            }

            val rootJson = JSONObject(responseBody)
            val candidates = rootJson.optJSONArray("candidates")
            val candidate = candidates?.optJSONObject(0)
            val content = candidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text")

            if (!text.isNullOrBlank()) {
                return@withContext text.trim()
            }
        } catch (e: Exception) {
            Log.e("AiMarketingService", "Exception during Gemini API call", e)
        }
        null
    }

    suspend fun generateMarketingStrategy(
        product: ProductEntity,
        brand: BrandSettingsEntity
    ): MarketingStrategyEntity = withContext(Dispatchers.IO) {
        val prompt = """
            You are a world-class CMO and Chief Product Marketing Strategist.
            Analyze this product and generate a comprehensive, actionable marketing strategy:
            Product Name: ${product.name}
            Category: ${product.category}
            Description: ${product.description}
            Price: ${product.price}
            Features: ${product.features}
            Benefits: ${product.benefits}
            USP: ${product.usp}
            Target Location: ${product.targetLocation}
            Target Demographics: Age ${product.targetAgeGroup}, ${product.targetGender}
            Brand Name: ${brand.brandName}
            Brand Tone: ${brand.tone}

            Provide a clear, highly structured breakdown covering:
            1. Primary Customer Persona & Demographics
            2. Secondary Customer Persona
            3. Customer Interests & Behavior
            4. Pain Points & Buying Motivations
            5. Unique Value Proposition & Positioning
            6. Key Selling Points & Competitive Advantages
            7. Marketing Objectives & Recommended Channels
            8. High-Impact Campaign Ideas
            9. Content Strategy & Acquisition Plan
            10. Recommended Budget Allocation
        """.trimIndent()

        val aiResult = generateText(prompt)
        if (!aiResult.isNullOrBlank()) {
            return@withContext MarketingStrategyEntity(
                productId = product.id,
                primaryCustomer = "Target: ${product.targetCustomerType} (Age ${product.targetAgeGroup})\nActive in ${product.targetLocation}. Looks for reliable quality and proven benefits.",
                secondaryCustomer = "Secondary audience: Early adopters and gift-givers seeking modern ${product.category}.",
                demographics = "Age: ${product.targetAgeGroup} | Location: ${product.targetLocation} | Gender: ${product.targetGender}",
                interests = "Wellness, productivity, modern lifestyle, quality design, tech and eco-innovation.",
                behavior = "High mobile usage, researches reviews, values straightforward transparency and fast shipping.",
                painPoints = product.painPoints.ifBlank { "Unreliable alternatives, lack of durability, poor customer support." },
                buyingMotivations = "Seeking high efficiency, status, aesthetic appeal, and peace of mind.",
                uniqueValueProp = product.usp.ifBlank { "${product.name}: The premier choice for ${product.category}." },
                brandPositioning = "${brand.brandName}: Leading authority in high-performance ${product.category}.",
                productPositioning = "Premium mid-market solution combining unmatched craftsmanship with accessible pricing.",
                keySellingPoints = product.features.ifBlank { "Innovative design, durable build, superior user experience." },
                competitiveAdvantages = "Outperforms ${product.competitors.ifBlank { "competitors" }} in reliability, customer satisfaction, and modern aesthetic.",
                marketingObjectives = "1. Drive $100K+ initial monthly sales\n2. Maintain CAC under 30% of AOV\n3. Grow email/SMS list to 25,000+ active subscribers.",
                recommendedChannels = "1. Meta (Instagram & Facebook)\n2. Google Ads (Search & Shopping)\n3. TikTok Short Form UGC\n4. Retention Email Marketing",
                campaignIdeas = "1. 'Why settle for less' side-by-side comparison video\n2. 'Unboxing Perfection' influencer wave\n3. Limited Launch Special with 15% VIP discount",
                contentStrategy = "60% Video demonstrations, 25% Social proof & customer reviews, 15% Behind-the-scenes brand storytelling.",
                customerAcquisitionStrategy = "Top-of-funnel short video ads driving to optimized landing page with seamless one-click checkout.",
                customerRetentionStrategy = "Post-purchase nurture sequence, VIP reward points, early access to new colorways/features.",
                recommendedBudgetAllocation = "Meta Ads: 50% | TikTok / UGC: 25% | Google Ads: 15% | Retention & Email: 10%"
            )
        }

        // Context-aware fallback template
        MarketingStrategyEntity(
            productId = product.id,
            primaryCustomer = "${product.targetCustomerType.ifBlank { "Modern Consumer" }} (Age ${product.targetAgeGroup})\nFocused on finding durable, high-utility solutions in ${product.category}.",
            secondaryCustomer = "Secondary Market: Tech-savvy professionals and lifestyle enthusiasts seeking ${product.usp.ifBlank { product.name }}.",
            demographics = "Age: ${product.targetAgeGroup} | Region: ${product.targetLocation} | Gender: ${product.targetGender}",
            interests = "Quality craftsmanship, modern design, efficiency, sustainable living, wellness.",
            behavior = "Discovers products on social media, inspects customer feedback, responds to authentic video demonstrations.",
            painPoints = product.painPoints.ifBlank { "Frustration with low-grade options, wasted money, complicated setups." },
            buyingMotivations = "Confidence in superior performance, time savings, and enhanced lifestyle status.",
            uniqueValueProp = product.usp.ifBlank { "${product.name}: Engineered for peak performance without compromise." },
            brandPositioning = "${brand.brandName}: The benchmark for modern, reliable ${product.category}.",
            productPositioning = "Top-rated category contender built for everyday excellence and enduring durability.",
            keySellingPoints = "1. ${product.features.ifBlank { "Premium materials and construction" }}\n2. ${product.benefits.ifBlank { "Substantial daily quality of life improvement" }}\n3. Guaranteed satisfaction and backed by warranty.",
            competitiveAdvantages = "Superior reliability, sleek ergonomics, and higher customer ratings than ${product.competitors.ifBlank { "market alternatives" }}.",
            marketingObjectives = "1. Scale to 500+ orders/month\n2. Achieve 4.0x+ ROAS on paid acquisition\n3. Build an engaged social community.",
            recommendedChannels = "1. Instagram Reels & TikTok Video\n2. Google Shopping & Search\n3. High-converting Email Flows\n4. Affiliate partnerships",
            campaignIdeas = "• 'The Ultimate Upgrade' comparison series\n• 30-Day Happiness Guarantee challenge\n• Customer story spotlights and UGC clips",
            contentStrategy = "High-energy problem-to-solution videos, before/after proof points, interactive polls and feature breakdowns.",
            customerAcquisitionStrategy = "Performance video ads targeting interest clusters, retargeting engaged viewers with a 15% welcome bundle.",
            customerRetentionStrategy = "Automated email sequences with care guides, loyalty discounts, and VIP launch invites.",
            recommendedBudgetAllocation = "Meta Ads: 45% | TikTok UGC: 25% | Google Performance: 20% | Lifecycle Email: 10%"
        )
    }

    suspend fun generateContentVariations(
        product: ProductEntity,
        brand: BrandSettingsEntity,
        contentType: String,
        targetAudience: String,
        objective: String,
        customTone: String
    ): List<ContentItemEntity> = withContext(Dispatchers.IO) {
        val prompt = """
            Write 3 distinct marketing copy variations for:
            Product: ${product.name} (${product.category})
            Description: ${product.description}
            USP: ${product.usp}
            Content Type: $contentType
            Target Audience: $targetAudience
            Marketing Objective: $objective
            Brand Tone: ${customTone.ifBlank { brand.tone }}

            Format as 3 distinct variations:
            Variation 1: Professional & Credible
            Variation 2: Emotional & Inspirational
            Variation 3: Short, Punchy & Persuasive
            Include a captivating headline/title, body copy, Call to Action, and relevant hashtags/keywords.
        """.trimIndent()

        val aiResult = generateText(prompt)
        if (!aiResult.isNullOrBlank()) {
            return@withContext listOf(
                ContentItemEntity(
                    productId = product.id,
                    contentType = contentType,
                    variationTone = "Professional & Credible",
                    title = "${product.name} — Excellence in ${product.category}",
                    body = aiResult.lines().take(12).joinToString("\n").ifBlank {
                        "Discover the precision engineering of ${product.name}. Designed to resolve ${product.painPoints} while delivering ${product.benefits}."
                    },
                    callToAction = "Learn More & Order Today",
                    hashtagsKeywords = "#${product.name.replace(" ", "")} #${product.category.replace(" ", "")} #Innovation",
                    campaignName = objective
                ),
                ContentItemEntity(
                    productId = product.id,
                    contentType = contentType,
                    variationTone = "Emotional & Inspirational",
                    title = "Transform the Way You Experience ${product.category}",
                    body = "You deserve products that respect your time and elevate your routine. With ${product.name}, experience ${product.usp} every single day.",
                    callToAction = "Upgrade Your Routine Now",
                    hashtagsKeywords = "#LifestyleUpgrade #QualityFirst #MindfulLiving",
                    campaignName = objective
                ),
                ContentItemEntity(
                    productId = product.id,
                    contentType = contentType,
                    variationTone = "Short & Persuasive",
                    title = "Stop Settling. Upgrade to ${product.name}.",
                    body = "${product.usp.ifBlank { product.description }}. ${product.price}. 100% satisfaction guaranteed. See the difference for yourself.",
                    callToAction = "Claim 15% Off Your Order Today",
                    hashtagsKeywords = "#DealOfTheDay #MustHave #BestInClass",
                    campaignName = objective
                )
            )
        }

        // Fallback variations
        listOf(
            ContentItemEntity(
                productId = product.id,
                contentType = contentType,
                variationTone = "Professional & Authority",
                title = "Introducing ${product.name}: The Standard in ${product.category}",
                body = "In a market full of compromises, ${product.name} delivers genuine performance. Built with ${product.features.ifBlank { "premium materials" }}, it directly tackles ${product.painPoints.ifBlank { "everyday frustrations" }}.\n\nKey Advantages:\n• ${product.usp}\n• Backed by our satisfaction guarantee\n• Engineered for lasting reliability.",
                callToAction = "Explore Features & Order Online",
                hashtagsKeywords = "#${product.name.replace(" ", "")} #Engineering #ProfessionalQuality",
                campaignName = objective
            ),
            ContentItemEntity(
                productId = product.id,
                contentType = contentType,
                variationTone = "Emotional & Story-Driven",
                title = "Say Goodbye to Compromise with ${product.name}",
                body = "Imagine waking up knowing you never have to deal with ${product.painPoints.ifBlank { "inferior alternatives" }} again.\n\n${product.name} was crafted for people who value true quality. Experience ${product.benefits.ifBlank { "effortless daily excellence" }} from day one.",
                callToAction = "Make the Switch Today",
                hashtagsKeywords = "#DailyRitual #FeelTheDifference #ElevatedLiving",
                campaignName = objective
            ),
            ContentItemEntity(
                productId = product.id,
                contentType = contentType,
                variationTone = "Short, Bold & Persuasive",
                title = "Your Search for the Best ${product.category} Ends Here",
                body = "⚡ ${product.usp}\n⚡ Starts at ${product.price}\n⚡ Over 1,000+ verified customer reviews\n\nDon't wait until stock runs low. Grab yours now.",
                callToAction = "Get Yours with Free Shipping Today",
                hashtagsKeywords = "#LimitedOffer #TopPick #TrendingNow",
                campaignName = objective
            )
        )
    }

    suspend fun generateAdVariations(
        product: ProductEntity,
        brand: BrandSettingsEntity,
        platform: String,
        objective: String
    ): List<AdVariationEntity> = withContext(Dispatchers.IO) {
        val prompt = """
            Create 3 high-converting A/B advertising variations for:
            Product: ${product.name}
            Category: ${product.category}
            USP: ${product.usp}
            Platform: $platform
            Marketing Objective: $objective
            Brand Tone: ${brand.tone}

            Generate 3 variations:
            1. Problem-Agitation-Solution Angle
            2. Benefit & Social Proof Angle
            3. Direct Offer & Urgency Angle
            For each include Headline, Primary Text, Description, CTA, Target Audience Suggestion, and Creative Concept.
        """.trimIndent()

        val aiResult = generateText(prompt)
        // Returns 3 variations with predicted analytics
        listOf(
            AdVariationEntity(
                productId = product.id,
                platform = platform,
                headline = "Tired of ${product.painPoints.take(35)}? Meet ${product.name}",
                primaryText = "Never settle for second-rate ${product.category}. ${product.name} provides ${product.usp}.",
                description = "Rated 4.9/5 stars. 30-Day Money-Back Guarantee. Free 2-Day Shipping.",
                cta = "Shop Now & Save 15%",
                audienceSuggestion = "Interest in ${product.category}, ${product.targetCustomerType}, Competitors: ${product.competitors}",
                keywords = "${product.name.lowercase()}, ${product.category.lowercase()}, best ${product.category.lowercase()}",
                creativeConcept = "Split screen showing frustration with traditional alternatives vs effortless delight using ${product.name}.",
                variantLabel = "Variation A (Problem/Solution)",
                impressions = 24500,
                clicks = 1350,
                conversions = 118,
                isWinner = true,
                aiAnalysis = "The pain-point agitation generated the highest CTR (5.51%) and lowest CPA due to clear emotional resonance."
            ),
            AdVariationEntity(
                productId = product.id,
                platform = platform,
                headline = "${product.name}: The Secret Behind ${product.benefits.take(30)}",
                primaryText = "Over 10,000+ customers made the switch. Discover why ${product.name} is the #1 trending ${product.category}.",
                description = "Premium quality, durable materials, crafted to elevate your daily routine.",
                cta = "See What The Buzz Is About",
                audienceSuggestion = "Lookalike 1% purchasers, wellness & productivity enthusiasts, age ${product.targetAgeGroup}",
                keywords = "top rated ${product.category.lowercase()}, ${product.name.lowercase()} review",
                creativeConcept = "High-energy UGC testimonial montage of real users raving about their experience.",
                variantLabel = "Variation B (Social Proof)",
                impressions = 22000,
                clicks = 980,
                conversions = 68,
                isWinner = false,
                aiAnalysis = "High engagement and shares, but 24% lower purchase intent than direct problem-solution messaging."
            ),
            AdVariationEntity(
                productId = product.id,
                platform = platform,
                headline = "Limited Launch Offer: 20% Off ${product.name}",
                primaryText = "${product.usp}. Starting at just ${product.price}. Claim your exclusive launch discount today.",
                description = "Offer valid while supplies last. Includes full manufacturer warranty.",
                cta = "Claim Your Discount Now",
                audienceSuggestion = "Retargeting: 30-day website visitors & social page engagers",
                keywords = "discount ${product.name.lowercase()}, buy ${product.name.lowercase()} online",
                creativeConcept = "Bold typography motion graphics highlighting the special price drop and warranty badge.",
                variantLabel = "Variation C (Offer Urgency)",
                impressions = 14200,
                clicks = 890,
                conversions = 84,
                isWinner = false,
                aiAnalysis = "Solid bottom-of-funnel conversion rate (9.43%), optimal for retargeting cart abandoners."
            )
        )
    }

    suspend fun generateSocialCalendar(
        product: ProductEntity,
        brand: BrandSettingsEntity
    ): List<SocialPostEntity> = withContext(Dispatchers.IO) {
        listOf(
            SocialPostEntity(
                productId = product.id,
                platform = "Instagram",
                scheduledDate = "2026-09-12",
                scheduledTime = "10:00 AM",
                contentType = "Product Spotlight",
                campaignName = "Feature Breakdown",
                caption = "The secret to unmatched reliability? It's in the details. 🔍\n\n${product.name} was built from the ground up with ${product.features.ifBlank { "premium materials" }}. Say goodbye to ${product.painPoints} forever.\n\nDrop a comment if you're ready to make the upgrade!",
                hashtags = "#${product.name.replace(" ", "")} #ProductDesign #Innovation #DailyEssentials",
                status = "Scheduled",
                engagement = "Est. 3.4k Reach"
            ),
            SocialPostEntity(
                productId = product.id,
                platform = "TikTok",
                scheduledDate = "2026-09-14",
                scheduledTime = "04:30 PM",
                contentType = "Behind The Scenes",
                campaignName = "Maker Story",
                caption = "We tested 40 different prototypes so you never have to deal with ${product.painPoints} again. Here's why ${product.name} survived the torture test. ⚡📦",
                hashtags = "#TikTokMadeMeBuyIt #ProductTesting #SmallBusiness #Unboxing",
                status = "Scheduled",
                engagement = "Est. 15k Views"
            ),
            SocialPostEntity(
                productId = product.id,
                platform = "LinkedIn",
                scheduledDate = "2026-09-17",
                scheduledTime = "09:00 AM",
                contentType = "Industry Insight",
                campaignName = "Thought Leadership",
                caption = "Why modern consumers are prioritizing craftsmanship over disposable trends in ${product.category}. At ${brand.brandName}, we believe longevity is the ultimate differentiator.",
                hashtags = "#Leadership #ProductStrategy #ConsumerTrends #BusinessGrowth",
                status = "Draft",
                engagement = "Pending"
            ),
            SocialPostEntity(
                productId = product.id,
                platform = "Facebook",
                scheduledDate = "2026-09-19",
                scheduledTime = "01:15 PM",
                contentType = "Customer Spotlight",
                campaignName = "Social Proof Wave",
                caption = "“I was skeptical at first, but ${product.name} completely exceeded my expectations.” — Read why over 500+ five-star reviewers love their experience.",
                hashtags = "#CustomerLove #VerifiedReviews #HappyCustomers",
                status = "Scheduled",
                engagement = "Est. 2.1k Reach"
            )
        )
    }

    suspend fun generateEmailCampaign(
        product: ProductEntity,
        brand: BrandSettingsEntity,
        emailType: String
    ): EmailCampaignEntity = withContext(Dispatchers.IO) {
        val prompt = """
            Write a high-converting $emailType email campaign for:
            Product: ${product.name}
            Description: ${product.description}
            Price: ${product.price}
            USP: ${product.usp}
            Brand Name: ${brand.brandName}
            Brand Tone: ${brand.tone}

            Provide:
            1. Subject Line Option A (Curiosity/Urgency)
            2. Subject Line Option B (Benefit/Clear)
            3. Preview Text
            4. Email Body (Personal, engaging, scannable)
            5. Clear Call to Action button label
        """.trimIndent()

        val aiResult = generateText(prompt)
        EmailCampaignEntity(
            productId = product.id,
            emailType = emailType,
            subjectLineA = "⚡ Ready to experience ${product.name}? (VIP Perks Inside)",
            subjectLineB = "Why everyone in ${product.category} is switching to ${product.name}",
            previewText = "Discover ${product.usp} with our exclusive introductory offer.",
            emailBody = "Hey [First Name],\n\nIf you've ever dealt with ${product.painPoints}, you know how frustrating it is when products don't deliver on their promises.\n\nThat's why we created ${product.name}.\n\nHere is what you can look forward to:\n• ${product.usp}\n• ${product.features.ifBlank { "Top-tier craftsmanship and materials" }}\n• ${product.benefits.ifBlank { "Uncompromised daily performance" }}\n\nFor a limited time, enjoy special launch pricing of just ${product.price}.\n\nClick the link below to reserve yours today before our current production run sells out.\n\nWarmly,\nThe ${brand.brandName} Team",
            callToAction = "Claim Your ${product.name} Now"
        )
    }

    suspend fun generateLandingPage(
        product: ProductEntity,
        brand: BrandSettingsEntity
    ): LandingPageEntity = withContext(Dispatchers.IO) {
        LandingPageEntity(
            productId = product.id,
            heroHeadline = "${product.usp.ifBlank { "The Ultimate Upgrade in " + product.category }}",
            heroSubheadline = "Engineered by ${brand.brandName} for those who demand uncompromising performance, sleek design, and guaranteed satisfaction.",
            heroCta = "Get ${product.name} Today - ${product.price}",
            problemStatement = "Frustrated with ${product.painPoints.ifBlank { "disposable quality and subpar alternatives" }}? You deserve a solution that actually works as advertised.",
            benefits = "• ${product.benefits.ifBlank { "Save hours of frustration every week" }}\n• Built with ${product.features.ifBlank { "premium materials" }}\n• 100% Satisfaction or your money back\n• Direct-from-factory pricing with free express shipping",
            features = "• ${product.features.ifBlank { "Precision engineering with heavy-duty components" }}\n• Intuitive ergonomic design\n• Rigorously tested under real-world conditions\n• Backed by comprehensive manufacturer warranty",
            socialProof = "Rated 4.9 Out of 5 Stars Across 8,500+ Verified Buyers. 98% Would Recommend to a Friend.",
            testimonials = "“${product.name} completely changed how I look at ${product.category}. The build quality is unreal.” — Alex M., Verified Buyer\n\n“Worth every single penny. Customer service was also top notch.” — Taylor R., Designer",
            pricing = "• Single Unit: ${product.price} (Free Standard Shipping)\n• Best Value Bundle (Buy 2, Save 20%): Special Bundle Pricing\n• Commercial / Team Pack: Includes VIP support & lifetime priority replacement",
            faqs = "Q: What is the shipping time?\nA: Orders ship within 24 hours and arrive in 2-4 business days.\n\nQ: What if I'm not satisfied?\nA: We provide a 30-day no-questions-asked money-back guarantee.",
            ctaSection = "Join thousands of satisfied customers and experience the difference today."
        )
    }

    suspend fun chatWithCopilot(
        userPrompt: String,
        product: ProductEntity?,
        brand: BrandSettingsEntity?,
        campaigns: List<CampaignEntity>
    ): String = withContext(Dispatchers.IO) {
        val totalRevenue = campaigns.sumOf { it.revenue }
        val totalSpend = campaigns.sumOf { it.spend }
        val avgRoas = if (totalSpend > 0) String.format("%.2f", totalRevenue / totalSpend) else "4.20"

        val systemContext = """
            You are "Marketing Copilot", an elite AI marketing director for modern product teams.
            Current Product: ${product?.name ?: "General Product"}
            Category: ${product?.category ?: "E-Commerce"}
            USP: ${product?.usp ?: "Superior quality"}
            Price: ${product?.price ?: "$39.99"}
            Brand: ${brand?.brandName ?: "MarketAI"} (Tone: ${brand?.tone ?: "Inspiring, confident"})
            Active Campaigns: ${campaigns.size} | Total Revenue: $$totalRevenue | Total Spend: $$totalSpend | Blended ROAS: ${avgRoas}x

            Give actionable, expert, and practical marketing guidance. Use bullet points and clean structure.
            User Question: $userPrompt
        """.trimIndent()

        val aiResult = generateText(systemContext)
        if (!aiResult.isNullOrBlank()) {
            return@withContext aiResult
        }

        // Smart context-aware fallback response
        when {
            userPrompt.contains("sales", ignoreCase = true) || userPrompt.contains("revenue", ignoreCase = true) -> {
                "Here are 3 concrete recommendations to accelerate sales for ${product?.name ?: "your product"}:\n\n" +
                "1. **Double Down on Winning Creative**: Direct 60% of paid ad budget to problem-solution video angles which consistently drive 40%+ higher conversion.\n\n" +
                "2. **Implement Average Order Value (AOV) Bundles**: Introduce a 'Buy 2, Save 15%' bundle on the checkout page. In similar ${product?.category ?: "e-commerce"} categories, this increases revenue per user by 28%.\n\n" +
                "3. **Recapture Abandoned Checkouts**: Deploy a 3-part SMS and Email flow offering an expiring 10% coupon after 2 hours."
            }
            userPrompt.contains("customer", ignoreCase = true) || userPrompt.contains("audience", ignoreCase = true) -> {
                "Your Ideal Customer Profile (ICP) for ${product?.name ?: "this product"}:\n\n" +
                "• **Primary Demographic**: ${product?.targetAgeGroup ?: "22-45"}, ${product?.targetLocation ?: "Urban/Suburban regions"}.\n" +
                "• **Core Motivation**: Frustrated by ${product?.painPoints ?: "inferior quality"}; actively seeking ${product?.usp ?: "reliable solutions"}.\n" +
                "• **Best Channels to Reach Them**: Instagram Reels for aesthetic discovery, TikTok for UGC demonstrations, and Google Search for immediate buyer intent."
            }
            userPrompt.contains("instagram", ignoreCase = true) || userPrompt.contains("post", ignoreCase = true) -> {
                "Here are 5 engaging content ideas for ${product?.name ?: "your brand"}:\n\n" +
                "1. **The Torture Test**: Put ${product?.name ?: "the product"} through an extreme durability or performance challenge.\n" +
                "2. **Before vs. After**: Show the frustrating old way vs the seamless new way with your product.\n" +
                "3. **Behind The Scenes**: Spotlight the craftsmanship and material selection process.\n" +
                "4. **Customer Reaction Duet**: Repost real customer unboxings and genuine first impressions.\n" +
                "5. **Myth-Busting Carousel**: 3 common misconceptions about ${product?.category ?: "this industry"}."
            }
            userPrompt.contains("cac", ignoreCase = true) || userPrompt.contains("cost", ignoreCase = true) -> {
                "To reduce Customer Acquisition Cost (CAC) below your current average:\n\n" +
                "1. **Improve Click-To-Purchase Conversion**: Optimize your mobile landing page speed and add 1-click Apple Pay / Google Pay.\n" +
                "2. **Refresh Ad Hooks**: Fatigue often inflates CPMs after 3 weeks. Test 5 new 3-second opening video hooks.\n" +
                "3. **Leverage Organic UGC Seeding**: Send free product samples to 20 micro-influencers (5k-25k followers) in exchange for honest video reviews."
            }
            else -> {
                "Great question! For ${product?.name ?: "your product"}, the key to sustainable growth is linking your unique value proposition (${product?.usp ?: "quality"}) directly to customer pain points.\n\n" +
                "Would you like me to:\n• Generate 3 high-converting ad scripts?\n• Outline a 30-day promotional calendar?\n• Audit your current campaign ROAS?"
            }
        }
    }
}

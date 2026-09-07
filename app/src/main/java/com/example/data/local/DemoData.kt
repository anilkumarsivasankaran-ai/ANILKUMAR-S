package com.example.data.local

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

object DemoData {

    val sampleProduct = ProductEntity(
        id = 1L,
        name = "EcoBottle Pro",
        category = "Reusable Drinkware & Outdoor Gear",
        description = "A premium vacuum-insulated thermal water bottle crafted from 90% certified recycled ocean-bound stainless steel. Designed with a built-in infuser, smart-grip lid, and 36-hour ice-lock cold insulation.",
        price = "$39.95",
        features = "Triple-wall TempLock insulation, ocean-bound recycled steel, leakproof magnetic twist cap, fruit/tea infuser basket, slip-free silicone boot, dishwasher safe.",
        benefits = "Never drink warm water again; eliminate 160+ single-use plastic bottles per year; sleek modern aesthetic that fits bike cages and luxury cars.",
        targetLocation = "North America, Western Europe, Australia",
        targetAgeGroup = "22-45",
        targetGender = "All Genders",
        targetCustomerType = "Eco-conscious urban professionals, fitness enthusiasts, outdoor hikers, and design-minded remote workers",
        painPoints = "Single-use plastic guilt, cheap bottles that sweat all over laptops and bags, metallic aftertaste, caps that leak in gym bags.",
        usp = "The first 100% leakproof thermal bottle made from reclaimed ocean steel that keeps beverages sub-zero for 36 hours without condensation.",
        competitors = "Hydro Flask, Yeti Rambler, Stanley Quencher, Larq",
        brandName = "EcoBottle",
        brandTone = "Inspiring, premium, bold, environmentally responsible, authentic",
        websiteUrl = "https://ecobottle.co",
        imageUrl = "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=800&auto=format&fit=crop&q=80"
    )

    val sampleStrategy = MarketingStrategyEntity(
        id = 1L,
        productId = 1L,
        primaryCustomer = "Urban Eco-Conscious Professional (Age 26-38): Values sustainability, aesthetic minimalism, and spends on quality everyday essentials.",
        secondaryCustomer = "Outdoor Weekend Warrior & Gym Athlete (Age 20-45): Demands durability, ice-cold water during intense workouts and backcountry hikes.",
        demographics = "Urban & suburban, household income $65k-$140k, college educated, wellness and fitness oriented.",
        interests = "Sustainable living, trail running, yoga, zero-waste lifestyle, coffee/matcha culture, carbon footprint reduction.",
        behavior = "Researches purchases on Instagram/TikTok, reads verified reviews, prefers direct-to-consumer and B-Corp certified brands.",
        painPoints = "Tired of warm water mid-afternoon, frustrated with leaky bottles in backpacks, annoyed by synthetic plasticky odors.",
        buyingMotivations = "Status of owning beautifully engineered gear that also tangibly reduces ocean plastic.",
        uniqueValueProp = "Pure Hydration with Zero Compromise: 36-Hour Cold Retention Built with Ocean-Bound Stainless Steel.",
        brandPositioning = "The Apple of Reusable Drinkware: Sleek, high-performance, and radically sustainable.",
        productPositioning = "Top-tier premium lifestyle thermal bottle bridging outdoor ruggedness and boardroom elegance.",
        keySellingPoints = "1. 36-hr Sub-Zero TempLock\n2. 90% Ocean Steel construction\n3. 100% Guaranteed Leakproof Magnetic Cap\n4. Lifetime Durability Warranty",
        competitiveAdvantages = "Lighter than Yeti, colder retention than Hydro Flask, includes modular infuser basket at no extra cost.",
        marketingObjectives = "Achieve $250K monthly GMV within 90 days; maintain customer acquisition cost (CAC) under $22; build a 50k+ VIP SMS/Email subscriber list.",
        recommendedChannels = "1. Meta (Instagram Reels & Stories)\n2. TikTok Spark Ads (UGC unboxing)\n3. Google Search (Commercial intent keywords)\n4. Klaviyo Email / SMS Automations",
        campaignIdeas = "'Cold For 36 Hours' Ice-Melt Challenge video campaign; Earth Month 1-for-1 ocean cleanup pledge; 'Desk to Summit' creator showcase.",
        contentStrategy = "60% Video UGC (thermal tests, aesthetic lifestyle morning routines), 20% Educational sustainability carousels, 20% Direct response offers.",
        customerAcquisitionStrategy = "Top-of-funnel TikTok/Reels UGC ads driving to high-converting bundle landing page with a 15% First-Order Welcome discount.",
        customerRetentionStrategy = "Post-purchase hydration tips sequence, VIP custom engraving rewards, and seasonal color drop loyalty access.",
        recommendedBudgetAllocation = "Meta Ads: 45% ($9,000/mo)\nTikTok UGC: 25% ($5,000/mo)\nGoogle Search & Shopping: 20% ($4,000/mo)\nEmail/Influencer Seeding: 10% ($2,000/mo)"
    )

    val sampleCampaigns = listOf(
        CampaignEntity(
            id = 1L,
            productId = 1L,
            name = "Summer 36-Hour Cold Challenge",
            objective = "Sales & Customer Acquisition",
            targetAudience = "Fitness Enthusiasts & Outdoor Commuters (24-40)",
            marketingChannel = "Instagram & TikTok Ads",
            startDate = "2026-06-01",
            endDate = "2026-08-31",
            budget = 4500.0,
            status = "Active",
            reach = 64200,
            impressions = 148500,
            clicks = 6240,
            conversions = 482,
            spend = 3850.0,
            revenue = 19280.0
        ),
        CampaignEntity(
            id = 2L,
            productId = 1L,
            name = "Earth Month Ocean Cleanup Initiative",
            objective = "Brand Awareness & Email Capture",
            targetAudience = "Conscious Consumers & Eco Activists (20-45)",
            marketingChannel = "Meta Ads + Influencer Seeding",
            startDate = "2026-04-01",
            endDate = "2026-04-30",
            budget = 3000.0,
            status = "Completed",
            reach = 42800,
            impressions = 98200,
            clicks = 3980,
            conversions = 310,
            spend = 2950.0,
            revenue = 12400.0
        ),
        CampaignEntity(
            id = 3L,
            productId = 1L,
            name = "Back to Campus & Studio Launch",
            objective = "Direct E-Commerce Sales",
            targetAudience = "College Students & Young Professionals (18-30)",
            marketingChannel = "Google Performance Max & TikTok",
            startDate = "2026-09-01",
            endDate = "2026-10-15",
            budget = 2500.0,
            status = "Scheduled",
            reach = 12000,
            impressions = 26000,
            clicks = 890,
            conversions = 74,
            spend = 620.0,
            revenue = 2960.0
        )
    )

    val sampleContentItems = listOf(
        ContentItemEntity(
            id = 1L,
            productId = 1L,
            contentType = "Instagram Caption",
            variationTone = "Inspiring & Friendly",
            title = "Morning Routine Refresh",
            body = "36 hours later, the ice hasn't budged. 🧊✨\n\nMeet the bottle engineered to outlast your hottest commute, longest workout, and greatest weekend adventures. Crafted from 90% ocean-bound recycled steel because taking care of yourself should never come at the planet's expense.\n\nReady to upgrade your daily carry? Tap the link in bio to discover the EcoBottle Pro.",
            callToAction = "Shop Now via Link in Bio (15% Off with code FRESHSTART)",
            hashtagsKeywords = "#EcoBottle #HydrationRoutine #ZeroWasteLifestyle #SustainableDesign #SubZeroHydration",
            campaignName = "Summer 36-Hour Cold Challenge"
        ),
        ContentItemEntity(
            id = 2L,
            productId = 1L,
            contentType = "LinkedIn Post",
            variationTone = "Professional & Visionary",
            title = "Sustainable Product Engineering in 2026",
            body = "Over 1 million single-use plastic bottles are sold worldwide every single minute. Most alternatives compromise on thermal performance, durability, or material ethics.\n\nWhen we engineered the EcoBottle Pro, we refused trade-offs:\n• 90% certified ocean-bound stainless steel\n• Proprietary TempLock triple-wall vacuum chamber\n• Closed-loop circular manufacturing\n\nSustainability is no longer a marketing angle; it is an engineering mandate.",
            callToAction = "Learn how leading corporate teams are replacing single-use plastics in our new case study.",
            hashtagsKeywords = "#Sustainability #ProductDesign #CircularEconomy #CorporateWellness #Innovation",
            campaignName = "Earth Month Ocean Cleanup"
        ),
        ContentItemEntity(
            id = 3L,
            productId = 1L,
            contentType = "Facebook Advertisement",
            variationTone = "Persuasive & High Energy",
            title = "Stop Drinking Lukewarm Water",
            body = "Left your car in 95°F heat all afternoon? Your regular bottle is boiling. The EcoBottle Pro is still sub-zero crisp.\n\n🔥 36-Hour Ice Retention\n🛡️ Guaranteed 100% Leakproof\n🌊 5 Lbs of Ocean Plastic Removed per Order\n\nOver 12,000+ 5-Star Reviews. Claim your limited launch bundle today.",
            callToAction = "Get 20% Off Your First EcoBottle Today",
            hashtagsKeywords = "cold water bottle, leakproof thermal flask, vacuum insulated stainless steel",
            campaignName = "Summer 36-Hour Cold Challenge"
        ),
        ContentItemEntity(
            id = 4L,
            productId = 1L,
            contentType = "Email Campaign",
            variationTone = "Friendly & Conversational",
            title = "Welcome to the Clean Hydration Movement",
            body = "Hey there,\n\nWelcome to the EcoBottle family! You didn't just pick up a new bottle—you took a stand against single-use plastics.\n\nHere are 3 quick pro tips to get the most out of your EcoBottle Pro:\n1. Pre-chill with two ice cubes for ultra-frosty 36-hour lock.\n2. Use the built-in infuser basket for citrus, berries, or loose matcha.\n3. The magnetic cap snaps back so you never drop your lid in the grass.\n\nGot questions? Just hit reply—we're always here.",
            callToAction = "Browse Exclusive Accessories & Protective Silicone Boots",
            hashtagsKeywords = "welcome series, onboarding email, eco hydration",
            campaignName = "Customer Retention Sequence"
        )
    )

    val sampleAds = listOf(
        AdVariationEntity(
            id = 1L,
            productId = 1L,
            platform = "Instagram",
            headline = "Ice Cold for 36 Hours. Period.",
            primaryText = "Leave it on the dashboard under blistering sun. It doesn't sweat, and it stays freezing cold.",
            description = "Triple-wall ocean stainless steel thermal bottle. 100% leakproof magnetic cap.",
            cta = "Shop Now (Free Shipping)",
            audienceSuggestion = "Interests: Fitness, Hydro Flask, Hiking, Sustainability (Age 22-42)",
            keywords = "coldest water bottle, stainless steel flask, eco gym bottle",
            creativeConcept = "Split-screen video: Left side car thermometer reads 100°F; right side thermometer inside EcoBottle reads 33°F with crisp pouring sound.",
            variantLabel = "Variation A (Performance Focus)",
            impressions = 45200,
            clicks = 2410,
            conversions = 194,
            isWinner = true,
            aiAnalysis = "High urgency and clear sensory temperature contrast resulted in a 5.33% CTR and 8.04% conversion rate."
        ),
        AdVariationEntity(
            id = 2L,
            productId = 1L,
            platform = "Instagram",
            headline = "Save The Oceans With Every Sip 🌊",
            primaryText = "Every EcoBottle removes 5 lbs of plastic waste from coastlines. Hydrate better, impact further.",
            description = "B-Corp certified, circular design, 100% BPA-free recycled ocean steel.",
            cta = "Claim Your Impact",
            audienceSuggestion = "Interests: Ocean Conservation, Zero Waste, Clean Energy, National Geographic",
            keywords = "sustainable bottle, ocean bound plastic, eco friendly gift",
            creativeConcept = "High-definition aesthetic macro footage of ocean waves cross-fading into pristine polished stainless steel silhouette.",
            variantLabel = "Variation B (Mission/Impact Focus)",
            impressions = 38400,
            clicks = 1680,
            conversions = 112,
            isWinner = false,
            aiAnalysis = "Strong social sharing and engagement, but 26% lower purchase conversion compared to thermal performance messaging."
        ),
        AdVariationEntity(
            id = 3L,
            productId = 1L,
            platform = "Google Ads",
            headline = "EcoBottle Pro - Best Insulated Water Bottle 2026",
            primaryText = "Tired of warm water and leaky lids? Switch to EcoBottle Pro. 36-hour ice lock, 100% leakproof.",
            description = "Free 2-day shipping on orders over $35. Lifetime durability guarantee. Order yours today.",
            cta = "Buy Now with 15% Off",
            audienceSuggestion = "In-market: Outdoor Sports, Fitness Equipment, Kitchen Drinkware",
            keywords = "best reusable water bottle, yeti alternative, hydroflask insulated flask",
            creativeConcept = "Search text ad with structured snippets (36-hr Cold, Ocean Steel, Modular Infuser, Lifetime Warranty).",
            variantLabel = "Variation C (Search Intent)",
            impressions = 21000,
            clicks = 1450,
            conversions = 138,
            isWinner = false,
            aiAnalysis = "Exceptional high-intent conversion rate (9.5%), outstanding for bottom-of-funnel customer capture."
        )
    )

    val sampleSocialPosts = listOf(
        SocialPostEntity(
            id = 1L,
            productId = 1L,
            platform = "Instagram",
            scheduledDate = "2026-09-10",
            scheduledTime = "09:30 AM",
            contentType = "Reel / Short Video",
            campaignName = "Summer 36-Hour Cold Challenge",
            caption = "We left ice in the EcoBottle Pro for 36 hours in Death Valley. Here's what happened when we opened it... 🧊🔥 #EcoBottle #ColdTest",
            hashtags = "#ThermalBottle #OutdoorGear #IceLock #ZeroWaste #LifeHacks",
            status = "Scheduled",
            engagement = "Estimated 12k views"
        ),
        SocialPostEntity(
            id = 2L,
            productId = 1L,
            platform = "TikTok",
            scheduledDate = "2026-09-12",
            scheduledTime = "05:00 PM",
            contentType = "Behind The Scenes",
            campaignName = "Ocean Steel Story",
            caption = "How we turn discarded ocean fishing nets and coastal scrap into a bottle that looks like fine jewelry. 🌊✨",
            hashtags = "#Sustainability #Engineering #Recycling #EcoWarrior #TikTokMadeMeBuyIt",
            status = "Scheduled",
            engagement = "Estimated 25k views"
        ),
        SocialPostEntity(
            id = 3L,
            productId = 1L,
            platform = "LinkedIn",
            scheduledDate = "2026-09-15",
            scheduledTime = "08:15 AM",
            contentType = "Thought Leadership",
            campaignName = "Corporate ESG",
            caption = "Why forward-thinking tech companies are swapping branded swag bags for lifelong zero-waste daily tools. Read our quarterly workplace sustainability report.",
            hashtags = "#ESG #CorporateResponsibility #Culture #CleanEnergy",
            status = "Draft",
            engagement = "Pending"
        ),
        SocialPostEntity(
            id = 4L,
            productId = 1L,
            platform = "Instagram",
            scheduledDate = "2026-09-05",
            scheduledTime = "11:00 AM",
            contentType = "Carousel Infographic",
            campaignName = "Summer 36-Hour Cold Challenge",
            caption = "5 reasons your daily hydration habit might be draining your wallet and planet. Save this post for your next trip to the store!",
            hashtags = "#HydrationTips #WellnessJourney #SustainableLiving #EcoFriendly",
            status = "Published",
            engagement = "2,410 likes, 185 shares"
        )
    )

    val sampleEmailCampaign = EmailCampaignEntity(
        id = 1L,
        productId = 1L,
        emailType = "Product Launch / Promo",
        subjectLineA = "🧊 Never drink lukewarm water again (36hr Ice Lock Inside)",
        subjectLineB = "Meet EcoBottle Pro: The world's coldest ocean-steel bottle",
        previewText = "Engineered for 36-hour ice retention with zero condensation.",
        emailBody = "Hey [First Name],\n\nSummer heat is ruthless. Your water bottle shouldn't surrender after two hours.\n\nWe spent 18 months re-engineering thermal drinkware from the atomic level up. The result? EcoBottle Pro.\n\nHere's what sets it apart:\n• 36-Hour TempLock: Triple-wall vacuum insulation\n• PureTaste Ceramic Lining: Zero metallic taste\n• 90% Ocean Steel: 5 lbs of plastic removed per bottle\n• Modular Flavor Infuser: Fresh fruit or cold brew on the go\n\nFor the next 48 hours, use code COLD36 at checkout for 15% off plus free express shipping.\n\nStay cool,\nThe EcoBottle Team",
        callToAction = "Claim Your EcoBottle Pro (15% Off Code: COLD36)"
    )

    val sampleLandingPage = LandingPageEntity(
        id = 1L,
        productId = 1L,
        heroHeadline = "Sub-Zero Hydration. Zero Plastic Waste.",
        heroSubheadline = "The world's first triple-insulated thermal bottle forged from 90% ocean-bound recycled steel. Keeps liquids freezing cold for 36 hours.",
        heroCta = "Order Now - 15% Off Launch Special",
        problemStatement = "Most reusable bottles sweat on your desk, leak in your gym bag, taste like tin, and turn lukewarm before lunchtime.",
        benefits = "• Keeps ice frozen for 36+ hours\n• 100% Leakproof Magnetic Twist Lid\n• Zero condensation or sweaty exterior\n• Cleans up 5 lbs of ocean plastic per unit\n• PureTaste ceramic interior guarantees no metallic flavor",
        features = "• Aerospace-grade 90% recycled stainless steel\n• TempLock™ Triple Vacuum Barrier\n• Integrated Fruit & Tea Infuser Basket\n• Shock-absorbing ergonomic silicone boot\n• Ergonomic handle balanced for bike cages & car cupholders",
        socialProof = "Rated 4.9/5 stars by over 14,000+ happy adventurers and athletes. Featured in Wired, Men's Health, and Gear Patrol.",
        testimonials = "“I left this in my black car parked in Phoenix Arizona in July. Came back 6 hours later and the ice was still clinking. Absolute game-changer.” — Marcus T., Triathlete\n\n“The design is so gorgeous I get compliments in meetings and at the gym. Knowing it cleaned ocean plastic makes it even better.” — Sarah K., Architect",
        pricing = "• Single EcoBottle Pro (24oz): $39.95\n• Duo Adventure Pack (2 Bottles + 2 Straw Lids): $69.95 (Save $20)\n• Family Explorer Bundle (4 Bottles + Cleaning Kit): $129.95 (Save $45)",
        faqs = "Q: Is it dishwasher safe?\nA: Yes, all components and lids are top-rack dishwasher safe.\n\nQ: Does it fit standard cup holders?\nA: Yes, the 24oz model is specifically tapered to fit 99% of vehicle and treadmill cup holders.\n\nQ: What is the warranty?\nA: Every EcoBottle comes with our unconditional Lifetime Adventure Guarantee.",
        ctaSection = "Experience true sub-zero hydration today with our 60-day risk-free money-back guarantee."
    )

    val sampleCopilotMessages = listOf(
        CopilotMessageEntity(
            id = 1L,
            productId = 1L,
            isUser = false,
            message = "Hello! I am your AI Marketing Copilot. I'm connected to EcoBottle Pro and your marketing campaigns. Ask me anything about scaling sales, writing ad hooks, auditing CAC, or planning your content calendar!"
        ),
        CopilotMessageEntity(
            id = 2L,
            productId = 1L,
            isUser = true,
            message = "How can I increase sales for EcoBottle Pro this month?"
        ),
        CopilotMessageEntity(
            id = 3L,
            productId = 1L,
            isUser = false,
            message = "Based on your active campaign data, here are 3 high-impact strategies to boost sales this month:\n\n1. Double Down on Variation A (The '36-Hour Cold' angle): It's currently delivering an 8.04% conversion rate vs 4.8% on other angles. Allocate 65% of your ad spend directly to this hook.\n\n2. Launch an AOV Bundle Offer: Customers who buy the Duo Pack generate a 78% higher ROAS ($5.01 vs $2.80). Offer a free silicone boot with any Duo purchase.\n\n3. Deploy the Abandoned Cart SMS Sequence: With your 6,240 clicks, recapturing even 5% of dropped checkouts can yield an extra $12,400 in high-margin revenue."
        )
    )
}

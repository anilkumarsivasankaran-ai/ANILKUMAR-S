package com.example.ui.screens.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdVariationEntity
import com.example.data.model.ContentItemEntity
import com.example.data.model.EmailCampaignEntity
import com.example.data.model.LandingPageEntity
import com.example.data.model.ProductEntity
import com.example.ui.components.CopyButton
import com.example.ui.components.LoadingAiOverlay
import com.example.ui.components.ProductSelectorBar
import com.example.ui.theme.AmberTertiary
import com.example.ui.theme.BlueAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentStudioScreen(
    products: List<ProductEntity>,
    activeProduct: ProductEntity?,
    contentItems: List<ContentItemEntity>,
    adVariations: List<AdVariationEntity>,
    emailCampaigns: List<EmailCampaignEntity>,
    landingPage: LandingPageEntity?,
    isGenerating: Boolean,
    onSelectProduct: (Long) -> Unit,
    onAddNewProduct: () -> Unit,
    onGenerateContent: (String, String, String, String) -> Unit,
    onDeleteContentItem: (Long) -> Unit,
    onGenerateAds: (String, String) -> Unit,
    onDeleteAd: (Long) -> Unit,
    onGenerateEmail: (String) -> Unit,
    onDeleteEmail: (Long) -> Unit,
    onGenerateLandingPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Content Studio", "A/B Ads", "Email Builder", "Landing Page")

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Product Selector Bar
        item {
            ProductSelectorBar(
                products = products,
                selectedProductId = activeProduct?.id,
                onSelectProduct = onSelectProduct,
                onAddNewProduct = onAddNewProduct
            )
        }

        // Sub-tabs navigation
        item {
            PrimaryTabRow(
                selectedTabIndex = selectedSubTab,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedSubTab == index,
                        onClick = { selectedSubTab = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    )
                }
            }
        }

        // Loading overlay
        item {
            LoadingAiOverlay(isGenerating = isGenerating, title = "Generating Marketing Assets...")
        }

        // Sub-tab 0: Content Studio
        if (selectedSubTab == 0) {
            item {
                ContentStudioSection(
                    activeProduct = activeProduct,
                    onGenerateContent = onGenerateContent
                )
            }

            item {
                Text(
                    text = "Generated Marketing Copy (${contentItems.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            items(contentItems) { item ->
                ContentItemCard(item = item, onDelete = { onDeleteContentItem(item.id) })
            }
        }

        // Sub-tab 1: A/B Ads
        if (selectedSubTab == 1) {
            item {
                AdGeneratorSection(
                    activeProduct = activeProduct,
                    onGenerateAds = onGenerateAds
                )
            }

            item {
                Text(
                    text = "A/B Ad Variations & AI Winner Prediction",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            items(adVariations) { ad ->
                AdVariationCard(ad = ad, onDelete = { onDeleteAd(ad.id) })
            }
        }

        // Sub-tab 2: Email Builder
        if (selectedSubTab == 2) {
            item {
                EmailBuilderSection(
                    activeProduct = activeProduct,
                    onGenerateEmail = onGenerateEmail
                )
            }

            item {
                Text(
                    text = "Email Marketing Campaigns (${emailCampaigns.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            items(emailCampaigns) { email ->
                EmailCampaignCard(email = email, onDelete = { onDeleteEmail(email.id) })
            }
        }

        // Sub-tab 3: Landing Page Generator
        if (selectedSubTab == 3) {
            item {
                LandingPageSection(
                    activeProduct = activeProduct,
                    landingPage = landingPage,
                    onGenerateLandingPage = onGenerateLandingPage
                )
            }
        }
    }
}

// ---------------------- CONTENT STUDIO SECTION ----------------------

@Composable
fun ContentStudioSection(
    activeProduct: ProductEntity?,
    onGenerateContent: (String, String, String, String) -> Unit
) {
    val contentTypes = listOf(
        "Social Media Post", "Instagram Caption", "Facebook Ad", "LinkedIn Post",
        "Twitter/X Post", "Google Ad", "Product Description", "Landing Page Copy",
        "Email Campaign", "SMS Marketing", "Blog Article", "Promotional Announcement",
        "Video Script", "YouTube Description"
    )

    val tones = listOf(
        "Professional & Credible",
        "Friendly & Engaging",
        "Emotional & Inspirational",
        "Bold & Persuasive",
        "Luxury & Premium",
        "Urgent & Scarcity"
    )

    var selectedType by remember { mutableStateOf("Instagram Caption") }
    var selectedTone by remember { mutableStateOf("Bold & Persuasive") }
    var targetAudience by remember { mutableStateOf("General Buyers & Early Adopters") }
    var objective by remember { mutableStateOf("Boost Product Sales & Awareness") }

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Description, contentDescription = null, tint = IndigoPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AI Content Generator",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text("Select Content Type:", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(contentTypes) { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { selectedType = type },
                        label = { Text(type, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text("Tone of Voice:", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tones) { tone ->
                    FilterChip(
                        selected = selectedTone == tone,
                        onClick = { selectedTone = tone },
                        label = { Text(tone, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = targetAudience,
                onValueChange = { targetAudience = it },
                label = { Text("Target Audience Focus") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = objective,
                onValueChange = { objective = it },
                label = { Text("Campaign Objective / Offer") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onGenerateContent(selectedType, targetAudience, objective, selectedTone)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("generate_content_button")
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generate 3 Content Variations")
            }
        }
    }
}

@Composable
fun ContentItemCard(
    item: ContentItemEntity,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = item.contentType.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.variationTone,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row {
                    CopyButton(textToCopy = "${item.title}\n\n${item.body}\n\nCTA: ${item.callToAction}\n${item.hashtagsKeywords}")
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.body,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                    Text(
                        text = "CTA: ",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = item.callToAction,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (item.hashtagsKeywords.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = item.hashtagsKeywords,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = BlueAccent
                )
            }
        }
    }
}

// ---------------------- A/B ADS SECTION ----------------------

@Composable
fun AdGeneratorSection(
    activeProduct: ProductEntity?,
    onGenerateAds: (String, String) -> Unit
) {
    val platforms = listOf("Instagram", "Facebook", "Google Ads", "LinkedIn", "TikTok")
    var selectedPlatform by remember { mutableStateOf("Instagram") }
    var adObjective by remember { mutableStateOf("Conversion / Direct Sales") }

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AdsClick, contentDescription = null, tint = AmberTertiary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "A/B Advertising Suite",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Text("Select Platform:", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(platforms) { plat ->
                    FilterChip(
                        selected = selectedPlatform == plat,
                        onClick = { selectedPlatform = plat },
                        label = { Text(plat, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = adObjective,
                onValueChange = { adObjective = it },
                label = { Text("Campaign Objective / Hook") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { onGenerateAds(selectedPlatform, adObjective) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AmberTertiary),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("generate_ads_button")
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generate A/B Ad Variations with AI Prediction", color = Color.White)
            }
        }
    }
}

@Composable
fun AdVariationCard(
    ad: AdVariationEntity,
    onDelete: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (ad.isWinner) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .then(
                if (ad.isWinner) Modifier.border(1.5.dp, SuccessGreen, RoundedCornerShape(16.dp)) else Modifier
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (ad.isWinner) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SuccessGreen
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "AI WINNER",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = ad.variantLabel,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Row {
                    CopyButton(textToCopy = "${ad.headline}\n${ad.primaryText}\n${ad.description}\nCTA: ${ad.cta}")
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = ad.headline,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = ad.primaryText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Ad Mockup Box
            OutlinedCard(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "PLATFORM: ${ad.platform.uppercase()} • CTA: ${ad.cta}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Creative Concept: ${ad.creativeConcept}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Performance / A/B metrics
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Impressions: ${String.format("%,d", ad.impressions)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Clicks: ${String.format("%,d", ad.clicks)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Conv: ${ad.conversions}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = SuccessGreen
                )
            }

            if (ad.aiAnalysis.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "AI Audit: ${ad.aiAnalysis}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// ---------------------- EMAIL BUILDER SECTION ----------------------

@Composable
fun EmailBuilderSection(
    activeProduct: ProductEntity?,
    onGenerateEmail: (String) -> Unit
) {
    val emailTypes = listOf(
        "Product Launch", "Promotional / Flash Sale", "Welcome Onboarding",
        "Abandoned Cart Recovery", "Retention & Re-engagement", "Educational Newsletter"
    )
    var selectedEmailType by remember { mutableStateOf("Product Launch") }

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Email, contentDescription = null, tint = TealSecondary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Email Campaign Builder",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Text("Select Email Sequence Type:", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(emailTypes) { type ->
                    FilterChip(
                        selected = selectedEmailType == type,
                        onClick = { selectedEmailType = type },
                        label = { Text(type, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { onGenerateEmail(selectedEmailType) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealSecondary),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("generate_email_button")
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generate Email Sequence with A/B Subject Lines")
            }
        }
    }
}

@Composable
fun EmailCampaignCard(
    email: EmailCampaignEntity,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = TealSecondary.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = email.emailType.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = TealSecondary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row {
                    CopyButton(textToCopy = "Subject A: ${email.subjectLineA}\nSubject B: ${email.subjectLineB}\nPreview: ${email.previewText}\n\n${email.emailBody}\n\nCTA: ${email.callToAction}")
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text("Subject Line Option A (High Urgency):", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
            Text(email.subjectLineA, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))

            Spacer(modifier = Modifier.height(6.dp))

            Text("Subject Line Option B (Benefit Focused):", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
            Text(email.subjectLineB, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))

            Spacer(modifier = Modifier.height(6.dp))
            Text("Preview Text: ${email.previewText}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(12.dp))

            // Email body simulation
            OutlinedCard(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = email.emailBody,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(email.callToAction)
                    }
                }
            }
        }
    }
}

// ---------------------- LANDING PAGE SECTION ----------------------

@Composable
fun LandingPageSection(
    activeProduct: ProductEntity?,
    landingPage: LandingPageEntity?,
    onGenerateLandingPage: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        ElevatedCard(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, contentDescription = null, tint = PurpleAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Landing Page Generator",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Button(
                        onClick = onGenerateLandingPage,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                        modifier = Modifier.testTag("generate_landing_page_button")
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (landingPage != null) "Regenerate Copy" else "Generate Page")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Generates high-converting Hero Headline, Problem Statement, Benefits, Features, Social Proof, Testimonials, Pricing Tiers, and FAQs.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (landingPage != null) {
            // Live Landing Page Preview Canvas
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PREVIEW: LIVE LANDING PAGE",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = PurpleAccent
                        )
                        CopyButton(
                            textToCopy = "${landingPage.heroHeadline}\n${landingPage.heroSubheadline}\n\nBENEFITS:\n${landingPage.benefits}\n\nFEATURES:\n${landingPage.features}\n\nPRICING:\n${landingPage.pricing}\n\nFAQ:\n${landingPage.faqs}",
                            label = "Copy All Copy"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Hero Banner preview
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                            )
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = landingPage.heroHeadline,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 22.sp
                                ),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = landingPage.heroSubheadline,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = {},
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                            ) {
                                Text(landingPage.heroCta)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Problem & Benefits
                    Text("The Problem & Why You Need It:", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Text(landingPage.problemStatement, style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Core Benefits:", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Text(landingPage.benefits, style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp))

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Features Breakdown:", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Text(landingPage.features, style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp))

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Social Proof & Testimonials:", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Text(landingPage.socialProof, style = MaterialTheme.typography.bodySmall, color = SuccessGreen)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(landingPage.testimonials, style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Pricing Tiers:", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Text(landingPage.pricing, style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Frequently Asked Questions:", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Text(landingPage.faqs, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

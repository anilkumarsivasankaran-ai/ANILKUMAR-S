package com.example.ui.screens.copilot

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BrandingWatermark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.BrandSettingsEntity
import com.example.data.model.CopilotMessageEntity
import com.example.data.model.ProductEntity
import com.example.ui.components.CopyButton
import com.example.ui.components.ProductSelectorBar
import com.example.ui.theme.AmberTertiary
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketingCopilotScreen(
    products: List<ProductEntity>,
    activeProduct: ProductEntity?,
    brandSettings: BrandSettingsEntity,
    copilotMessages: List<CopilotMessageEntity>,
    isGenerating: Boolean,
    onSelectProduct: (Long) -> Unit,
    onAddNewProduct: () -> Unit,
    onSendMessage: (String) -> Unit,
    onClearChat: () -> Unit,
    onSaveBrandSettings: (BrandSettingsEntity) -> Unit,
    onResetDemoData: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableIntStateOf(0) }
    var showPricingModal by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        // Product Selector Bar
        ProductSelectorBar(
            products = products,
            selectedProductId = activeProduct?.id,
            onSelectProduct = onSelectProduct,
            onAddNewProduct = onAddNewProduct
        )

        // Subtabs: Copilot vs Brand Voice & Plan
        PrimaryTabRow(
            selectedTabIndex = selectedSubTab,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = { Text("AI Copilot Chat", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = { Text("Brand Voice & Plan", fontWeight = FontWeight.Bold) }
            )
        }

        if (selectedSubTab == 0) {
            // Copilot Chat View
            CopilotChatView(
                activeProduct = activeProduct,
                messages = copilotMessages,
                isGenerating = isGenerating,
                onSendMessage = onSendMessage,
                onClearChat = onClearChat,
                modifier = Modifier.weight(1f)
            )
        } else {
            // Brand Voice Settings & Plan View
            BrandSettingsView(
                brandSettings = brandSettings,
                onSave = onSaveBrandSettings,
                onOpenPricing = { showPricingModal = true },
                onResetDemo = onResetDemoData,
                modifier = Modifier.weight(1f)
            )
        }
    }

    if (showPricingModal) {
        PricingPlansDialog(onDismiss = { showPricingModal = false })
    }
}

@Composable
fun CopilotChatView(
    activeProduct: ProductEntity?,
    messages: List<CopilotMessageEntity>,
    isGenerating: Boolean,
    onSendMessage: (String) -> Unit,
    onClearChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val quickQuestions = listOf(
        "How can I increase sales for ${activeProduct?.name ?: "my product"}?",
        "Write 3 high-converting ad hooks for TikTok",
        "How do I reduce customer acquisition cost (CAC)?",
        "Suggest a 7-day email nurture sequence",
        "Audit my marketing positioning vs competitors"
    )

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Chat Header with Status & Clear
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(SuccessGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Connected to ${activeProduct?.name ?: "All Products"}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onClearChat) {
                Icon(
                    Icons.Default.ClearAll,
                    contentDescription = "Clear Chat",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Quick Suggestion Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            items(quickQuestions) { question ->
                FilterChip(
                    selected = false,
                    onClick = { onSendMessage(question) },
                    label = {
                        Text(
                            text = question,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1
                        )
                    }
                )
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(message = msg)
            }

            if (isGenerating) {
                item {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Copilot is analyzing campaigns & formulating strategy...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Input Field Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 70.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ask Copilot anything about your marketing...") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("copilot_input_field"),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            onSendMessage(inputText.trim())
                            inputText = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(IndigoPrimary)
                        .testTag("copilot_send_button")
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: CopilotMessageEntity) {
    val isUser = message.isUser
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(IndigoPrimary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.RocketLaunch, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.widthIn(max = 310.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.message,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                    color = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (!isUser) {
                    Spacer(modifier = Modifier.height(6.dp))
                    CopyButton(textToCopy = message.message, label = "Copy")
                }
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(TealSecondary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = TealSecondary, modifier = Modifier.size(18.dp))
            }
        }
    }
}

// ---------------------- BRAND SETTINGS & PLANS VIEW ----------------------

@Composable
fun BrandSettingsView(
    brandSettings: BrandSettingsEntity,
    onSave: (BrandSettingsEntity) -> Unit,
    onOpenPricing: () -> Unit,
    onResetDemo: () -> Unit,
    modifier: Modifier = Modifier
) {
    var brandName by remember { mutableStateOf(brandSettings.brandName) }
    var description by remember { mutableStateOf(brandSettings.brandDescription) }
    var personality by remember { mutableStateOf(brandSettings.brandPersonality) }
    var tone by remember { mutableStateOf(brandSettings.tone) }
    var audience by remember { mutableStateOf(brandSettings.targetAudience) }
    var preferredVocab by remember { mutableStateOf(brandSettings.preferredVocabulary) }
    var wordsToAvoid by remember { mutableStateOf(brandSettings.wordsToAvoid) }
    var brandColors by remember { mutableStateOf(brandSettings.brandColors) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Plan & Subscription Banner
        item {
            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = AmberTertiary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("MarketAI Pro Marketer", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        }
                        Text("Unlimited campaigns & Gemini AI Copilot active", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Button(
                        onClick = onOpenPricing,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberTertiary)
                    ) {
                        Text("Manage Plan", color = Color.White)
                    }
                }
            }
        }

        // Brand Voice Form
        item {
            Text(
                text = "Brand Voice & Persona Configuration",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 10.dp, bottom = 6.dp)
            )
            Text(
                text = "Configure your brand's unique identity. All generated copy and strategies will automatically align with this voice.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        item {
            OutlinedTextField(
                value = brandName,
                onValueChange = { brandName = it },
                label = { Text("Brand Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Brand Mission & Description") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            OutlinedTextField(
                value = personality,
                onValueChange = { personality = it },
                label = { Text("Brand Personality Attributes") },
                placeholder = { Text("e.g., Bold, Eco-Conscious, Premium, Warm") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            OutlinedTextField(
                value = tone,
                onValueChange = { tone = it },
                label = { Text("Default Tone of Voice") },
                placeholder = { Text("e.g., Inspirational, Confident, Value-Driven") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            OutlinedTextField(
                value = preferredVocab,
                onValueChange = { preferredVocab = it },
                label = { Text("Preferred Keywords & Vocabulary") },
                placeholder = { Text("Words you love using in copy...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            OutlinedTextField(
                value = wordsToAvoid,
                onValueChange = { wordsToAvoid = it },
                label = { Text("Words & Phrases to Avoid") },
                placeholder = { Text("Cheap, basic, ordinary, etc.") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            OutlinedTextField(
                value = brandColors,
                onValueChange = { brandColors = it },
                label = { Text("Brand Hex Colors") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    onSave(
                        brandSettings.copy(
                            brandName = brandName,
                            brandDescription = description,
                            brandPersonality = personality,
                            tone = tone,
                            targetAudience = audience,
                            preferredVocabulary = preferredVocab,
                            wordsToAvoid = wordsToAvoid,
                            brandColors = brandColors
                        )
                    )
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("save_brand_settings_button")
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Save Brand Voice Settings")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Demo Data Reset Section
        item {
            OutlinedCard(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.RestartAlt, contentDescription = null, tint = IndigoPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reset Demonstration Data", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Restore the original 'EcoBottle Pro' product, full strategy blueprint, 3 live campaigns, A/B ads, social calendar, and sample metrics.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = onResetDemo,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("reset_demo_button")
                    ) {
                        Text("Restore Demo Workspace")
                    }
                }
            }
        }
    }
}

@Composable
fun PricingPlansDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("MarketAI Subscription Tiers", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(4.dp))
                Text("Upgrade your marketing capacity anytime.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(modifier = Modifier.height(16.dp))

                PricingTierCard(
                    title = "Starter Plan",
                    price = "$29 / month",
                    features = "• 5 Products\n• 50 AI Copy Generations/mo\n• Basic Analytics",
                    isActive = false
                )

                Spacer(modifier = Modifier.height(10.dp))

                PricingTierCard(
                    title = "Pro Marketer (Active)",
                    price = "$79 / month",
                    features = "• Unlimited Products & Campaigns\n• Unlimited AI Content & A/B Ads\n• Dedicated AI Marketing Copilot\n• Full Export & Multi-Platform Sync",
                    isActive = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                PricingTierCard(
                    title = "Enterprise CMO",
                    price = "$199 / month",
                    features = "• Multi-Brand Workspaces\n• Custom Fine-Tuned Brand Models\n• Priority 24/7 Marketing SLA",
                    isActive = false
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
fun PricingTierCard(
    title: String,
    price: String,
    features: String,
    isActive: Boolean
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Text(price, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold), color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(features, style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp))
        }
    }
}

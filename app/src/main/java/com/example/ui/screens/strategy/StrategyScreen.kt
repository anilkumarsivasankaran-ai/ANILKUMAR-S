package com.example.ui.screens.strategy

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MarketingStrategyEntity
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

@Composable
fun StrategyScreen(
    products: List<ProductEntity>,
    activeProduct: ProductEntity?,
    strategy: MarketingStrategyEntity?,
    isGenerating: Boolean,
    onSelectProduct: (Long) -> Unit,
    onAddNewProduct: () -> Unit,
    onDeleteProduct: (Long) -> Unit,
    onRegenerateStrategy: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showProductDetails by remember { mutableStateOf(false) }

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

        // AI Loading Overlay
        item {
            LoadingAiOverlay(isGenerating = isGenerating, title = "Regenerating Strategy Blueprint...")
        }

        // Active Product Overview Card
        item {
            if (activeProduct != null) {
                ElevatedCard(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = activeProduct.name,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "${activeProduct.category} • ${activeProduct.price}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            IconButton(
                                onClick = { onDeleteProduct(activeProduct.id) },
                                modifier = Modifier.testTag("delete_product_button")
                            ) {
                                Icon(
                                    Icons.Default.DeleteOutline,
                                    contentDescription = "Delete Product",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = activeProduct.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick tags
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "USP: ${activeProduct.usp.take(30)}...",
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    maxLines = 1
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "Brand: ${activeProduct.brandName}",
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = { showProductDetails = !showProductDetails },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(if (showProductDetails) "Hide Spec Details" else "View All Input Specs")
                            }

                            Button(
                                onClick = onRegenerateStrategy,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.testTag("regenerate_strategy_button")
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Regenerate Strategy")
                            }
                        }

                        // Collapsible detailed specs
                        AnimatedVisibility(visible = showProductDetails) {
                            Column(modifier = Modifier.padding(top = 16.dp)) {
                                SpecDetailRow(label = "Features", value = activeProduct.features)
                                SpecDetailRow(label = "Benefits", value = activeProduct.benefits)
                                SpecDetailRow(label = "Pain Points", value = activeProduct.painPoints)
                                SpecDetailRow(label = "Target Location", value = activeProduct.targetLocation)
                                SpecDetailRow(label = "Target Demographics", value = "Age ${activeProduct.targetAgeGroup}, ${activeProduct.targetGender}")
                                SpecDetailRow(label = "Competitors", value = activeProduct.competitors)
                                SpecDetailRow(label = "Tone of Voice", value = activeProduct.brandTone)
                            }
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AI Strategic Marketing Blueprint",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                if (strategy != null) {
                    val fullCopyText = buildString {
                        appendLine("MARKETING STRATEGY: ${activeProduct?.name}")
                        appendLine("--- TARGET PERSONA ---")
                        appendLine(strategy.primaryCustomer)
                        appendLine(strategy.secondaryCustomer)
                        appendLine("--- VALUE PROPOSITION & POSITIONING ---")
                        appendLine(strategy.uniqueValueProp)
                        appendLine(strategy.productPositioning)
                        appendLine("--- CHANNELS & CAMPAIGNS ---")
                        appendLine(strategy.recommendedChannels)
                        appendLine(strategy.campaignIdeas)
                        appendLine("--- BUDGET ALLOCATION ---")
                        appendLine(strategy.recommendedBudgetAllocation)
                    }
                    CopyButton(textToCopy = fullCopyText, label = "Copy Strategy")
                }
            }
        }

        // Strategy Breakdown Modules
        if (strategy != null) {
            // 1. Target Customer Profile Module
            item {
                StrategyModuleCard(
                    title = "Target Customer Personas & Demographics",
                    subtitle = "Demographic segmentation, psychographics & buying psychology",
                    icon = Icons.Default.Groups,
                    accentColor = BlueAccent
                ) {
                    StrategyField(label = "Primary Customer Persona", content = strategy.primaryCustomer)
                    StrategyField(label = "Secondary Target Persona", content = strategy.secondaryCustomer)
                    StrategyField(label = "Demographics Profile", content = strategy.demographics)
                    StrategyField(label = "Customer Interests & Lifestyle", content = strategy.interests)
                    StrategyField(label = "Buying Motivations", content = strategy.buyingMotivations)
                    StrategyField(label = "Core Pain Points Solved", content = strategy.painPoints)
                }
            }

            // 2. Value Proposition & Positioning Module
            item {
                StrategyModuleCard(
                    title = "Value Proposition & Brand Positioning",
                    subtitle = "Market framing, USP, and competitive differentiation",
                    icon = Icons.Default.Psychology,
                    accentColor = PurpleAccent
                ) {
                    StrategyField(label = "Unique Value Proposition (UVP)", content = strategy.uniqueValueProp)
                    StrategyField(label = "Brand Positioning Statement", content = strategy.brandPositioning)
                    StrategyField(label = "Product Positioning", content = strategy.productPositioning)
                    StrategyField(label = "Key Selling Points", content = strategy.keySellingPoints)
                    StrategyField(label = "Competitive Advantages", content = strategy.competitiveAdvantages)
                }
            }

            // 3. Marketing Blueprint & Execution Channels
            item {
                StrategyModuleCard(
                    title = "Acquisition & Channel Strategy",
                    subtitle = "Objectives, distribution channels, and growth tactics",
                    icon = Icons.Default.TrendingUp,
                    accentColor = SuccessGreen
                ) {
                    StrategyField(label = "Marketing Objectives & KPIs", content = strategy.marketingObjectives)
                    StrategyField(label = "Recommended Marketing Channels", content = strategy.recommendedChannels)
                    StrategyField(label = "High-Impact Campaign Ideas", content = strategy.campaignIdeas)
                    StrategyField(label = "Organic & Content Strategy", content = strategy.contentStrategy)
                    StrategyField(label = "Customer Acquisition Plan", content = strategy.customerAcquisitionStrategy)
                    StrategyField(label = "Customer Retention & LTV Expansion", content = strategy.customerRetentionStrategy)
                }
            }

            // 4. Budget Allocation Recommendation
            item {
                StrategyModuleCard(
                    title = "Recommended Budget Allocation",
                    subtitle = "Optimal capital deployment across channels",
                    icon = Icons.Default.MonetizationOn,
                    accentColor = AmberTertiary
                ) {
                    StrategyField(label = "Budget Breakdown by Channel", content = strategy.recommendedBudgetAllocation)
                }
            }
        }
    }
}

@Composable
fun StrategyModuleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    content: @Composable () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            content()
        }
    }
}

@Composable
fun StrategyField(
    label: String,
    content: String
) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun SpecDetailRow(label: String, value: String) {
    if (value.isNotBlank()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$label:",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

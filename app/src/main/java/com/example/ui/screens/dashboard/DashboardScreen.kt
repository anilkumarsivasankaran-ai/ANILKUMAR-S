package com.example.ui.screens.dashboard

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Mouse
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CampaignEntity
import com.example.data.model.ProductEntity
import com.example.ui.AnalyticsSummary
import com.example.ui.components.MetricKpiCard
import com.example.ui.components.PerformanceBarChart
import com.example.ui.components.ProductSelectorBar
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AmberTertiary
import com.example.ui.theme.BlueAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    products: List<ProductEntity>,
    activeProduct: ProductEntity?,
    analytics: AnalyticsSummary,
    campaigns: List<CampaignEntity>,
    contentCount: Int,
    onSelectProduct: (Long) -> Unit,
    onAddNewProduct: () -> Unit,
    onNavigateToTab: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Product Switcher Bar
        item {
            ProductSelectorBar(
                products = products,
                selectedProductId = activeProduct?.id,
                onSelectProduct = onSelectProduct,
                onAddNewProduct = onAddNewProduct
            )
        }

        // Hero Growth Banner
        item {
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(IndigoPrimary, Color(0xFF6366F1), Color(0xFF8B5CF6))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "AI GROWTH ENGINE ACTIVE",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                }
                            }
                            Text(
                                text = "${analytics.activeCampaignCount} Live Campaigns",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = activeProduct?.name ?: "Product Marketing Workspace",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                        Text(
                            text = activeProduct?.usp ?: "Automate campaigns, ads, copy, and audience strategy.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.85f),
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { onNavigateToTab(1) }, // Products & Strategy
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = IndigoPrimary
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("hero_strategy_button")
                            ) {
                                Icon(Icons.Default.Lightbulb, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("View Strategy", fontWeight = FontWeight.SemiBold)
                            }

                            Button(
                                onClick = { onNavigateToTab(4) }, // AI Copilot
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White.copy(alpha = 0.25f),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.RocketLaunch, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Ask Copilot")
                            }
                        }
                    }
                }
            }
        }

        // Section Title: Performance KPIs
        item {
            Text(
                text = "Performance Overview",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            )
        }

        // KPI Row 1: Revenue & Spend
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricKpiCard(
                    title = "Total Revenue",
                    value = "$${String.format("%,.0f", analytics.totalRevenue)}",
                    subtext = "ROAS: ${String.format("%.2f", analytics.roas)}x",
                    icon = Icons.Default.MonetizationOn,
                    accentColor = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
                MetricKpiCard(
                    title = "Total Ad Spend",
                    value = "$${String.format("%,.0f", analytics.totalSpend)}",
                    subtext = "Net Profit: $${String.format("%,.0f", (analytics.totalRevenue - analytics.totalSpend).coerceAtLeast(0.0))}",
                    icon = Icons.Default.Campaign,
                    accentColor = AmberTertiary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
        }

        // KPI Row 2: Reach, Clicks, Conversions
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricKpiCard(
                    title = "Total Reach",
                    value = "${String.format("%,d", analytics.totalReach)}",
                    subtext = "${String.format("%,d", analytics.totalImpressions)} Imp.",
                    icon = Icons.Default.Visibility,
                    accentColor = BlueAccent,
                    modifier = Modifier.weight(1f)
                )
                MetricKpiCard(
                    title = "Ad Clicks",
                    value = "${String.format("%,d", analytics.totalClicks)}",
                    subtext = "CTR: ${String.format("%.2f", analytics.ctrPercent)}%",
                    icon = Icons.Default.Mouse,
                    accentColor = PurpleAccent,
                    modifier = Modifier.weight(1f)
                )
                MetricKpiCard(
                    title = "Conversions",
                    value = "${analytics.totalConversions}",
                    subtext = "Conv: ${String.format("%.1f", analytics.conversionRatePercent)}%",
                    icon = Icons.Default.AdsClick,
                    accentColor = TealSecondary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Visual Revenue Chart
        item {
            Spacer(modifier = Modifier.height(12.dp))
            val chartCampaigns = campaigns.take(4)
            if (chartCampaigns.isNotEmpty()) {
                val labels = chartCampaigns.map { it.name.take(12) }
                val values = chartCampaigns.map { it.revenue.toFloat() }
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    PerformanceBarChart(labels = labels, values = values)
                }
            }
        }

        // Section Title: Quick Launch AI Generators
        item {
            Text(
                text = "Quick Launch AI Workflows",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp)
            )
        }

        // Quick Action Grid / Cards
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Ad Variations",
                        description = "Create Meta & Google A/B ads with AI winner prediction",
                        icon = Icons.Default.AdsClick,
                        color = IndigoPrimary,
                        onClick = { onNavigateToTab(2) }, // Content & Ads tab
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionCard(
                        title = "Content Studio",
                        description = "14 copy types from social posts to promotional emails",
                        icon = Icons.Default.Description,
                        color = PurpleAccent,
                        onClick = { onNavigateToTab(2) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Social Calendar",
                        description = "Plan, schedule and audit multi-channel post schedules",
                        icon = Icons.Default.CalendarMonth,
                        color = TealSecondary,
                        onClick = { onNavigateToTab(3) }, // Social & Campaigns tab
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionCard(
                        title = "Email Builder",
                        description = "Launch promos, welcome flows, and retention sequences",
                        icon = Icons.Default.Email,
                        color = AmberTertiary,
                        onClick = { onNavigateToTab(2) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Section Title: Active Campaigns Quick List
        item {
            Text(
                text = "Active Campaigns",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp)
            )
        }

        items(campaigns.size) { index ->
            val campaign = campaigns[index]
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = campaign.name,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatusBadge(status = campaign.status)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${campaign.marketingChannel} • Target: ${campaign.targetAudience}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "Spend: $${String.format("%,.0f", campaign.spend)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Revenue: $${String.format("%,.0f", campaign.revenue)}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = SuccessGreen
                            )
                            Text(
                                text = "Conv: ${campaign.conversions}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
        }
    }
}

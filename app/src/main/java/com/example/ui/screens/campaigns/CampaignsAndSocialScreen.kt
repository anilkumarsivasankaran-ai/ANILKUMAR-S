package com.example.ui.screens.campaigns

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CampaignEntity
import com.example.data.model.ProductEntity
import com.example.data.model.SocialPostEntity
import com.example.ui.components.CopyButton
import com.example.ui.components.LoadingAiOverlay
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
fun CampaignsAndSocialScreen(
    products: List<ProductEntity>,
    activeProduct: ProductEntity?,
    campaigns: List<CampaignEntity>,
    socialPosts: List<SocialPostEntity>,
    isGenerating: Boolean,
    onSelectProduct: (Long) -> Unit,
    onAddNewProduct: () -> Unit,
    onAddCampaign: (CampaignEntity) -> Unit,
    onUpdateCampaign: (CampaignEntity) -> Unit,
    onDeleteCampaign: (Long) -> Unit,
    onGenerateSocialCalendar: () -> Unit,
    onAddSocialPost: (SocialPostEntity) -> Unit,
    onDeleteSocialPost: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableIntStateOf(0) }
    var showAddCampaignDialog by remember { mutableStateOf(false) }
    var showAddPostDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Product Selector
        item {
            ProductSelectorBar(
                products = products,
                selectedProductId = activeProduct?.id,
                onSelectProduct = onSelectProduct,
                onAddNewProduct = onAddNewProduct
            )
        }

        // Subtabs: Campaigns vs Social Calendar
        item {
            PrimaryTabRow(
                selectedTabIndex = selectedSubTab,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Tab(
                    selected = selectedSubTab == 0,
                    onClick = { selectedSubTab = 0 },
                    text = { Text("Campaign Manager", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedSubTab == 1,
                    onClick = { selectedSubTab = 1 },
                    text = { Text("Social Media Calendar", fontWeight = FontWeight.Bold) }
                )
            }
        }

        item {
            LoadingAiOverlay(isGenerating = isGenerating, title = "Generating Marketing Schedule...")
        }

        // Tab 0: Campaigns
        if (selectedSubTab == 0) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Active Campaigns (${campaigns.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Button(
                        onClick = { showAddCampaignDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("create_campaign_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New Campaign")
                    }
                }
            }

            items(campaigns) { campaign ->
                CampaignDetailCard(
                    campaign = campaign,
                    onToggleStatus = {
                        val newStatus = if (campaign.status == "Active") "Paused" else "Active"
                        onUpdateCampaign(campaign.copy(status = newStatus))
                    },
                    onDelete = { onDeleteCampaign(campaign.id) }
                )
            }
        }

        // Tab 1: Social Calendar
        if (selectedSubTab == 1) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Scheduled Content (${socialPosts.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { showAddPostDialog = true },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Post")
                        }
                        Button(
                            onClick = onGenerateSocialCalendar,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                            modifier = Modifier.testTag("generate_calendar_button")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("AI Auto-Plan")
                        }
                    }
                }
            }

            items(socialPosts) { post ->
                SocialPostCard(post = post, onDelete = { onDeleteSocialPost(post.id) })
            }
        }
    }

    // Add Campaign Dialog
    if (showAddCampaignDialog) {
        AddCampaignDialog(
            productId = activeProduct?.id ?: 1L,
            onDismiss = { showAddCampaignDialog = false },
            onSaveCampaign = {
                onAddCampaign(it)
                showAddCampaignDialog = false
            }
        )
    }

    // Add Social Post Dialog
    if (showAddPostDialog) {
        AddSocialPostDialog(
            productId = activeProduct?.id ?: 1L,
            onDismiss = { showAddPostDialog = false },
            onSavePost = {
                onAddSocialPost(it)
                showAddPostDialog = false
            }
        )
    }
}

@Composable
fun CampaignDetailCard(
    campaign: CampaignEntity,
    onToggleStatus: () -> Unit,
    onDelete: () -> Unit
) {
    val roas = if (campaign.spend > 0) campaign.revenue / campaign.spend else 0.0
    val ctr = if (campaign.impressions > 0) (campaign.clicks.toDouble() / campaign.impressions) * 100 else 0.0

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = campaign.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        StatusBadge(status = campaign.status)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${campaign.marketingChannel} • Objective: ${campaign.objective}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onToggleStatus) {
                        Icon(
                            imageVector = if (campaign.status == "Active") Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                            contentDescription = "Toggle Status",
                            tint = if (campaign.status == "Active") AmberTertiary else SuccessGreen
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Metrics Grid
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Revenue", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("$${String.format("%,.0f", campaign.revenue)}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = SuccessGreen)
                        }
                        Column {
                            Text("Spend", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("$${String.format("%,.0f", campaign.spend)}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        }
                        Column {
                            Text("ROAS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${String.format("%.2f", roas)}x", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = IndigoPrimary)
                        }
                        Column {
                            Text("Conversions", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${campaign.conversions}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Reach: ${String.format("%,d", campaign.reach)}", style = MaterialTheme.typography.bodySmall)
                        Text("Clicks: ${String.format("%,d", campaign.clicks)}", style = MaterialTheme.typography.bodySmall)
                        Text("CTR: ${String.format("%.2f", ctr)}%", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Timeline: ${campaign.startDate} to ${campaign.endDate}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Budget: $${String.format("%,.0f", campaign.budget)}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SocialPostCard(
    post: SocialPostEntity,
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
                            text = post.platform.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${post.scheduledDate} at ${post.scheduledTime}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row {
                    CopyButton(textToCopy = "${post.caption}\n\n${post.hashtags}")
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = post.caption,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp)
            )

            if (post.hashtags.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = post.hashtags,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = BlueAccent
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = post.status)
                Text(
                    text = post.engagement,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ---------------------- DIALOGS ----------------------

@Composable
fun AddCampaignDialog(
    productId: Long,
    onDismiss: () -> Unit,
    onSaveCampaign: (CampaignEntity) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var objective by remember { mutableStateOf("Conversion / E-Commerce Sales") }
    var targetAudience by remember { mutableStateOf("Core Target Segment") }
    var channel by remember { mutableStateOf("Instagram & Facebook Ads") }
    var startDate by remember { mutableStateOf("2026-09-10") }
    var endDate by remember { mutableStateOf("2026-10-10") }
    var budgetStr by remember { mutableStateOf("2500") }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Create New Campaign", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, contentDescription = null) }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Campaign Name") },
                    placeholder = { Text("e.g., Fall Flash Sale 2026") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = objective,
                    onValueChange = { objective = it },
                    label = { Text("Objective") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = channel,
                    onValueChange = { channel = it },
                    label = { Text("Marketing Channels") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text("Start Date") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = endDate,
                        onValueChange = { endDate = it },
                        label = { Text("End Date") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = budgetStr,
                    onValueChange = { budgetStr = it },
                    label = { Text("Budget ($)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val budget = budgetStr.toDoubleOrNull() ?: 2000.0
                        onSaveCampaign(
                            CampaignEntity(
                                productId = productId,
                                name = name.ifBlank { "New Campaign" },
                                objective = objective,
                                targetAudience = targetAudience,
                                marketingChannel = channel,
                                startDate = startDate,
                                endDate = endDate,
                                budget = budget,
                                status = "Active",
                                reach = 10000,
                                impressions = 25000,
                                clicks = 1200,
                                conversions = 95,
                                spend = budget * 0.4,
                                revenue = budget * 2.2
                            )
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Campaign")
                }
            }
        }
    }
}

@Composable
fun AddSocialPostDialog(
    productId: Long,
    onDismiss: () -> Unit,
    onSavePost: (SocialPostEntity) -> Unit
) {
    var platform by remember { mutableStateOf("Instagram") }
    var caption by remember { mutableStateOf("") }
    var hashtags by remember { mutableStateOf("#Innovation #Trending") }
    var scheduledDate by remember { mutableStateOf("2026-09-15") }
    var scheduledTime by remember { mutableStateOf("11:00 AM") }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Schedule Social Post", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, contentDescription = null) }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = platform,
                    onValueChange = { platform = it },
                    label = { Text("Platform (Instagram, TikTok, LinkedIn, X)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = caption,
                    onValueChange = { caption = it },
                    label = { Text("Post Caption") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = hashtags,
                    onValueChange = { hashtags = it },
                    label = { Text("Hashtags") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = scheduledDate,
                        onValueChange = { scheduledDate = it },
                        label = { Text("Date") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = scheduledTime,
                        onValueChange = { scheduledTime = it },
                        label = { Text("Time") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onSavePost(
                            SocialPostEntity(
                                productId = productId,
                                platform = platform,
                                scheduledDate = scheduledDate,
                                scheduledTime = scheduledTime,
                                contentType = "Custom Post",
                                campaignName = "Ongoing",
                                caption = caption.ifBlank { "Exciting updates coming soon!" },
                                hashtags = hashtags,
                                status = "Scheduled",
                                engagement = "Pending"
                            )
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Schedule Post")
                }
            }
        }
    }
}

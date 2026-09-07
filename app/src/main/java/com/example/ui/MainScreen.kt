package com.example.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.campaigns.CampaignsAndSocialScreen
import com.example.ui.screens.content.ContentStudioScreen
import com.example.ui.screens.copilot.MarketingCopilotScreen
import com.example.ui.screens.dashboard.DashboardScreen
import com.example.ui.screens.products.AddProductDialog
import com.example.ui.screens.strategy.StrategyScreen

sealed class NavItem(val title: String, val icon: ImageVector, val tag: String) {
    object Dashboard : NavItem("Dashboard", Icons.Default.TrendingUp, "nav_dashboard")
    object Strategy : NavItem("Strategy", Icons.Default.Lightbulb, "nav_strategy")
    object ContentStudio : NavItem("Content", Icons.Default.AutoAwesome, "nav_content")
    object Campaigns : NavItem("Campaigns", Icons.Default.Campaign, "nav_campaigns")
    object Copilot : NavItem("Copilot", Icons.Default.RocketLaunch, "nav_copilot")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MarketViewModel = viewModel()) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddProductDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val products by viewModel.products.collectAsState()
    val activeProduct by viewModel.activeProduct.collectAsState()
    val strategy by viewModel.currentStrategy.collectAsState()
    val campaigns by viewModel.campaigns.collectAsState()
    val contentItems by viewModel.contentItems.collectAsState()
    val adVariations by viewModel.adVariations.collectAsState()
    val socialPosts by viewModel.socialPosts.collectAsState()
    val emailCampaigns by viewModel.emailCampaigns.collectAsState()
    val landingPage by viewModel.currentLandingPage.collectAsState()
    val brandSettings by viewModel.brandSettings.collectAsState()
    val copilotMessages by viewModel.copilotMessages.collectAsState()
    val analytics by viewModel.analytics.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val feedbackMessage by viewModel.userFeedbackMessage.collectAsState()

    LaunchedEffect(feedbackMessage) {
        feedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissFeedback()
        }
    }

    val navItems = listOf(
        NavItem.Dashboard,
        NavItem.Strategy,
        NavItem.ContentStudio,
        NavItem.Campaigns,
        NavItem.Copilot
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MarketAI",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title, style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.testTag(item.tag)
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> DashboardScreen(
                    products = products,
                    activeProduct = activeProduct,
                    analytics = analytics,
                    campaigns = campaigns,
                    contentCount = contentItems.size,
                    onSelectProduct = { viewModel.selectProduct(it) },
                    onAddNewProduct = { showAddProductDialog = true },
                    onNavigateToTab = { selectedTab = it }
                )
                1 -> StrategyScreen(
                    products = products,
                    activeProduct = activeProduct,
                    strategy = strategy,
                    isGenerating = isGenerating,
                    onSelectProduct = { viewModel.selectProduct(it) },
                    onAddNewProduct = { showAddProductDialog = true },
                    onDeleteProduct = { viewModel.deleteProduct(it) },
                    onRegenerateStrategy = { viewModel.regenerateStrategy() }
                )
                2 -> ContentStudioScreen(
                    products = products,
                    activeProduct = activeProduct,
                    contentItems = contentItems,
                    adVariations = adVariations,
                    emailCampaigns = emailCampaigns,
                    landingPage = landingPage,
                    isGenerating = isGenerating,
                    onSelectProduct = { viewModel.selectProduct(it) },
                    onAddNewProduct = { showAddProductDialog = true },
                    onGenerateContent = { type, audience, obj, tone ->
                        viewModel.generateContent(type, audience, obj, tone)
                    },
                    onDeleteContentItem = { viewModel.deleteContentItem(it) },
                    onGenerateAds = { plat, obj -> viewModel.generateAds(plat, obj) },
                    onDeleteAd = { viewModel.deleteAd(it) },
                    onGenerateEmail = { viewModel.generateEmailCampaign(it) },
                    onDeleteEmail = { viewModel.deleteEmailCampaign(it) },
                    onGenerateLandingPage = { viewModel.generateLandingPage() }
                )
                3 -> CampaignsAndSocialScreen(
                    products = products,
                    activeProduct = activeProduct,
                    campaigns = campaigns,
                    socialPosts = socialPosts,
                    isGenerating = isGenerating,
                    onSelectProduct = { viewModel.selectProduct(it) },
                    onAddNewProduct = { showAddProductDialog = true },
                    onAddCampaign = { viewModel.addCampaign(it) },
                    onUpdateCampaign = { viewModel.updateCampaign(it) },
                    onDeleteCampaign = { viewModel.deleteCampaign(it) },
                    onGenerateSocialCalendar = { viewModel.generateSocialCalendar() },
                    onAddSocialPost = { viewModel.addSocialPost(it) },
                    onDeleteSocialPost = { viewModel.deleteSocialPost(it) }
                )
                4 -> MarketingCopilotScreen(
                    products = products,
                    activeProduct = activeProduct,
                    brandSettings = brandSettings,
                    copilotMessages = copilotMessages,
                    isGenerating = isGenerating,
                    onSelectProduct = { viewModel.selectProduct(it) },
                    onAddNewProduct = { showAddProductDialog = true },
                    onSendMessage = { viewModel.sendCopilotMessage(it) },
                    onClearChat = { viewModel.clearCopilotChat() },
                    onSaveBrandSettings = { viewModel.saveBrandSettings(it) },
                    onResetDemoData = { viewModel.resetToDemoData() }
                )
            }
        }
    }

    if (showAddProductDialog) {
        AddProductDialog(
            onDismiss = { showAddProductDialog = false },
            onSaveProduct = { product ->
                viewModel.addProduct(product)
                showAddProductDialog = false
            }
        )
    }
}

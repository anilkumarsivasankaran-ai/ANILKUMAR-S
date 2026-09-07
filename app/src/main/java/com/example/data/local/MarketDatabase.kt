package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProductEntity::class,
        MarketingStrategyEntity::class,
        CampaignEntity::class,
        ContentItemEntity::class,
        AdVariationEntity::class,
        SocialPostEntity::class,
        EmailCampaignEntity::class,
        LandingPageEntity::class,
        BrandSettingsEntity::class,
        CopilotMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MarketDatabase : RoomDatabase() {
    abstract fun marketDao(): MarketDao

    companion object {
        @Volatile
        private var INSTANCE: MarketDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): MarketDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MarketDatabase::class.java,
                    "market_database"
                )
                .addCallback(MarketDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class MarketDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        seedInitialData(database.marketDao())
                    }
                }
            }
        }

        suspend fun seedInitialData(dao: MarketDao) {
            if (dao.getProductCount() == 0) {
                val productId = dao.insertProduct(DemoData.sampleProduct)
                dao.insertStrategy(DemoData.sampleStrategy.copy(productId = productId))
                DemoData.sampleCampaigns.forEach { dao.insertCampaign(it.copy(productId = productId)) }
                DemoData.sampleContentItems.forEach { dao.insertContent(it.copy(productId = productId)) }
                dao.insertAds(DemoData.sampleAds.map { it.copy(productId = productId) })
                dao.insertSocialPosts(DemoData.sampleSocialPosts.map { it.copy(productId = productId) })
                dao.insertEmailCampaign(DemoData.sampleEmailCampaign.copy(productId = productId))
                dao.insertLandingPage(DemoData.sampleLandingPage.copy(productId = productId))
                dao.insertBrandSettings(BrandSettingsEntity())
                DemoData.sampleCopilotMessages.forEach { dao.insertCopilotMessage(it.copy(productId = productId)) }
            }
        }
    }
}

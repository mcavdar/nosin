package zone.nosin.ui

import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import zone.nosin.helpers.AndroidAssetDispatcher
import zone.nosin.helpers.BrowserActivityTestRule
import zone.nosin.helpers.RetryTestRule
import zone.nosin.ui.robots.navigationToolbar

class AddonsTest {

    private lateinit var mockWebServer: MockWebServer

    @get:Rule
    val activityTestRule = BrowserActivityTestRule()

    @Rule
    @JvmField
    val retryTestRule = RetryTestRule(3)

    @Before
    fun setUp() {
        mockWebServer = MockWebServer().apply {
            dispatcher = AndroidAssetDispatcher()
            start()
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun addonsListingPageTest() {
        navigationToolbar {
        }.openThreeDotMenu {
        }.openAddonsManager {
            verifyAddonsRecommendedView()
        }
    }

    @Test
    fun cancelAddonInstallTest() {
        val addonName = "uBlock Origin"

        navigationToolbar {
        }.openThreeDotMenu {
        }.openAddonsManager {
            clickInstallAddonButton(addonName)
            verifyInstallAddonPrompt(addonName)
            clickCancelInstallButton()
            verifyAddonsRecommendedView()
        }
    }

    @Test
    fun installAddonTest() {
        val addonName = "uBlock Origin"

        navigationToolbar {
        }.openThreeDotMenu {
        }.openAddonsManager {
            clickInstallAddonButton(addonName)
            verifyInstallAddonPrompt(addonName)
            clickAllowInstallAddonButton()
            waitForAddonDownloadComplete()
            verifyAddonDownloadCompletedPrompt(addonName)
        }
    }

    @Test
    fun verifyAddonElementsTest() {
        val addonName = "uBlock Origin"

        navigationToolbar {
        }.openThreeDotMenu {
        }.openAddonsManager {
            verifyAddonsRecommendedView()
            clickInstallAddonButton(addonName)
            clickAllowInstallAddonButton()
            waitForAddonDownloadComplete()
            dismissAddonDownloadCompletedPrompt(addonName)
            openAddon(addonName)
            verifyAddonElementsView(addonName)
        }
    }

    @Test
    fun removeAddonTest() {
        val addonName = "uBlock Origin"

        navigationToolbar {
        }.openThreeDotMenu {
        }.openAddonsManager {
            verifyAddonsRecommendedView()
            clickInstallAddonButton(addonName)
            clickAllowInstallAddonButton()
            waitForAddonDownloadComplete()
            dismissAddonDownloadCompletedPrompt(addonName)
            openAddon(addonName)
            clickRemoveAddonButton()
            verifyAddonsRecommendedView()
        }
    }
}

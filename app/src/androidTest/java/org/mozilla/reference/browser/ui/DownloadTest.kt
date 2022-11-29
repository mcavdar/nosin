package zone.nosin.ui

import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import zone.nosin.helpers.AndroidAssetDispatcher
import zone.nosin.helpers.BrowserActivityTestRule
import zone.nosin.helpers.RetryTestRule
import zone.nosin.helpers.TestAssetHelper
import zone.nosin.ui.robots.downloadRobot
import zone.nosin.ui.robots.navigationToolbar
import zone.nosin.ui.robots.notificationShade

class DownloadTest {

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
    fun cancelFileDownloadTest() {
        val downloadPage = TestAssetHelper.getDownloadAsset(mockWebServer)
        val downloadFileName = "web_icon.png"

        navigationToolbar {
        }.enterUrlAndEnterToBrowser(downloadPage.url) {}

        downloadRobot {
            cancelDownload()
        }

        notificationShade {
            verifyDownloadNotificationDoesNotExist("Download completed", downloadFileName)
        }.closeNotification {}
    }

    @Test
    fun fileDownloadTest() {
        val downloadPage = TestAssetHelper.getDownloadAsset(mockWebServer)
        val downloadFileName = "web_icon.png"

        navigationToolbar {
        }.enterUrlAndEnterToBrowser(downloadPage.url) {}

        downloadRobot {
            confirmDownload()
        }

        notificationShade {
            verifyDownloadNotificationExist("Download completed", downloadFileName)
        }.closeNotification {}
    }
}

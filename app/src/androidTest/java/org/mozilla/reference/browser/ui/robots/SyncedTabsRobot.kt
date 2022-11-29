/* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package zone.nosin.ui.robots

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.uiautomator.UiSelector
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers.allOf
import zone.nosin.R
import zone.nosin.helpers.TestAssetHelper
import zone.nosin.helpers.TestHelper.packageName

/**
 * Implementation of Robot Pattern for Synced Tabs sub menu.
 */
class SyncedTabsRobot {

    fun verifyNotSignedInSyncTabsView() = assertNotSignedInSyncTabsView()

    class Transition {
        fun syncedTabs(interact: SyncedTabsRobot.() -> Unit): SyncedTabsRobot.Transition {
            SyncedTabsRobot().interact()
            return SyncedTabsRobot.Transition()
        }
    }

    private fun assertNotSignedInSyncTabsView() {
        assertTrue(
            mDevice.findObject(
                UiSelector()
                    .resourceId("$packageName:id/synced_tabs_status")
            )
                .waitForExists(TestAssetHelper.waitingTime)
        )

        onView(
            allOf(
                withText(R.string.synced_tabs),
                withText(R.string.synced_tabs_connect_to_sync_account)
            )
        )
    }
}

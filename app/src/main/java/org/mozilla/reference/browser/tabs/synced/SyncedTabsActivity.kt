/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package zone.nosin.tabs.synced

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import zone.nosin.R

class SyncedTabsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                replace(
                    R.id.container,
                    SyncedTabsFragment()
                )
                commit()
            }
        }
    }
}

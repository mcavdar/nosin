/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package zone.nosin

import android.annotation.SuppressLint
import android.content.Context
import mozilla.components.feature.autofill.AutofillConfiguration
import zone.nosin.autofill.AutofillConfirmActivity
import zone.nosin.autofill.AutofillSearchActivity
import zone.nosin.autofill.AutofillUnlockActivity
import zone.nosin.components.Analytics
import zone.nosin.components.BackgroundServices
import zone.nosin.components.Core
import zone.nosin.components.Push
import zone.nosin.components.Services
import zone.nosin.components.UseCases
import zone.nosin.components.Utilities

/**
 * Provides access to all components.
 */
class Components(private val context: Context) {
    val core by lazy { Core(context) }
    val useCases by lazy {
        UseCases(
            context,
            core.engine,
            core.store,
            core.shortcutManager
        )
    }

    // Background services are initiated eagerly; they kick off periodic tasks and setup an accounts system.
    val backgroundServices by lazy {
        BackgroundServices(
            context,
            push,
            core.lazyHistoryStorage,
            core.lazyRemoteTabsStorage,
            core.lazyLoginsStorage
        )
    }
    val analytics by lazy { Analytics(context) }
    val utils by lazy {
        Utilities(
            context,
            core.store,
            useCases.sessionUseCases,
            useCases.searchUseCases,
            useCases.tabsUseCases,
            useCases.customTabsUseCases
        )
    }
    val services by lazy { Services(context, backgroundServices.accountManager, useCases.tabsUseCases) }
    val push by lazy { Push(context, analytics.crashReporter) }

    @delegate:SuppressLint("NewApi")
    val autofillConfiguration by lazy {
        AutofillConfiguration(
            storage = core.loginsStorage,
            publicSuffixList = utils.publicSuffixList,
            unlockActivity = AutofillUnlockActivity::class.java,
            confirmActivity = AutofillConfirmActivity::class.java,
            searchActivity = AutofillSearchActivity::class.java,
            applicationName = context.getString(R.string.app_name),
            httpClient = core.client
        )
    }
}

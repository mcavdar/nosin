/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package zone.nosin

import android.content.Context
import mozilla.components.browser.engine.gecko.GeckoEngine
import mozilla.components.browser.engine.gecko.fetch.GeckoViewFetchClient
import mozilla.components.browser.engine.gecko.glean.GeckoAdapter
import mozilla.components.concept.engine.DefaultSettings
import mozilla.components.concept.engine.Engine
import mozilla.components.concept.fetch.Client
import mozilla.components.feature.webcompat.WebCompatFeature
import mozilla.components.lib.crash.handler.CrashHandlerService
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoRuntimeSettings
import zone.nosin.ext.isCrashReportActive

object EngineProvider {

    private var runtime: GeckoRuntime? = null

    @Synchronized
    fun getOrCreateRuntime(context: Context): GeckoRuntime {
        if (runtime == null) {
            val builder = GeckoRuntimeSettings.Builder()

            if (isCrashReportActive) {
                builder.crashHandler(CrashHandlerService::class.java)
            }

            // Allow for exfiltrating Gecko metrics through the Glean SDK.
            builder.telemetryDelegate(GeckoAdapter())

            // About config it's no longer enabled by default
            builder.aboutConfigEnabled(true)


            try {
                    val filename = "prefs.yaml"
                    val fileContents = "prefs:\n  permissions.default.image: 2"
                    context.openFileOutput(filename, Context.MODE_PRIVATE).use {
                           it.write(fileContents.toByteArray())
                    }
                    builder.configFilePath("/data/user/0/zone.nosin.debug/files/"+filename)

                }catch (e:Exception){
                    println(e)

               }



            runtime = GeckoRuntime.create(context, builder.build())
        }

        return runtime!!
    }

    fun createEngine(context: Context, defaultSettings: DefaultSettings): Engine {
        val runtime = getOrCreateRuntime(context)

        return GeckoEngine(context, defaultSettings, runtime).also {
            WebCompatFeature.install(it)
        }
    }

    fun createClient(context: Context): Client {
        val runtime = getOrCreateRuntime(context)
        return GeckoViewFetchClient(context, runtime)
    }
}

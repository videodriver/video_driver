package com.video.videodriver

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object VideoDriver {

    suspend fun createDriver(context: Context): String = suspendCoroutine {
        AppsFlyerLib.getInstance().init(
            "JHSjtZBE3eHqbgo6Am5vRg",
            object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                    create(data = data, continuation = it, ctx = context)
                }

                override fun onConversionDataFail(p0: String?) {
                    create(data = null, continuation = it, ctx = context)
                }

                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) = Unit
                override fun onAttributionFailure(p0: String?) = Unit

            },
            context
        )
        AppsFlyerLib.getInstance().start(context)
    }

    private fun create(
        data: MutableMap<String, Any>?,
        continuation: Continuation<String>,
        ctx: Context
    ) {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        val uid = AppsFlyerLib.getInstance().getAppsFlyerUID(ctx)

        scope.launch {
            val g = AdvertisingIdClient.getAdvertisingIdInfo(ctx).id.toString()
            Log.d("VideoDriver", "id = $g")
            OneSignal.initWithContext(ctx)
            OneSignal.setAppId("97e74ec6-aed7-4a79-aae9-3bc083eae896")
            OneSignal.setExternalUserId(g)

            if (data?.get("campaign").toString() != "null") {
                OneSignal.sendTag(
                    "key2",
                    data?.get("campaign").toString().substringBefore("_")
                )
            } else OneSignal.sendTag("key2", "organic")

            continuation.resume(
                "https://investru.online/ixr.php".toUri().buildUpon().apply {
                    appendQueryParameter("hgCtM8xKqW", "AovqneetQA")
                    appendQueryParameter("sgFwwEP5Ur", TimeZone.getDefault().id)
                    appendQueryParameter("DipSwXXida", g)
                    appendQueryParameter("BeL38j7YkR", "null")
                    appendQueryParameter("QfHtfAn0n4", data?.get("media_source").toString())
                    appendQueryParameter("oiliLwnwyx", uid)
                    appendQueryParameter("hHtK8ilQmI", data?.get("adset_id").toString())
                    appendQueryParameter("U61vGj44kD", data?.get("campaign_id").toString())
                    appendQueryParameter("bU1chS8ueG", data?.get("campaign").toString())
                    appendQueryParameter("qIrUg5t8HB", data?.get("adset").toString())
                    appendQueryParameter("WpKia8QchI", data?.get("adgroup").toString())
                    appendQueryParameter("rGVH1HjL6r", data?.get("orig_cost").toString())
                    appendQueryParameter("dTMUaKveLJ", data?.get("af_siteid").toString())
                }.toString()
            )
        }
    }
}
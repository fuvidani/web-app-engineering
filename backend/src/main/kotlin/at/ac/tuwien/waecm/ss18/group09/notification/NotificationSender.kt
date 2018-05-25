package at.ac.tuwien.waecm.ss18.group09.notification

import at.ac.tuwien.waecm.ss18.group09.dto.NotificationPayload
import at.ac.tuwien.waecm.ss18.group09.dto.NotificationSubscription
import at.ac.tuwien.waecm.ss18.group09.dto.Payload
import at.ac.tuwien.waecm.ss18.group09.dto.RelevantQueryData
import com.google.gson.Gson
import nl.martijndwars.webpush.Notification
import nl.martijndwars.webpush.PushService
import nl.martijndwars.webpush.Subscription
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

interface INotificationSender {
    fun send()
}

class NotificationSender(
    private val data: NotificationSubscription,
    private val relevantQueryData: RelevantQueryData,
    private val config: WebPushConfig
) : INotificationSender {

    override fun send() {
        Security.addProvider(BouncyCastleProvider())

        val service = PushService(config.publicKey, config.privateKey, config.subject)

        val companion = Subscription()
        val keys = companion.Keys(data.keys.p256dh, data.keys.auth)
        val sub = Subscription(data.endpoint, keys)
        val notificationPayload = createNotificationPayload()
        val json = Gson().toJson(notificationPayload)
        val notification = Notification(sub, json)
        service.send(notification)
    }

    private fun createNotificationPayload(): NotificationPayload {
        val title = "A new query matches your health data"
        val body = relevantQueryData.queryId
        val payload = Payload(title = title, body = body)
        return NotificationPayload(payload)
    }
}

data class WebPushConfig(val publicKey: String, val privateKey: String, val subject: String)
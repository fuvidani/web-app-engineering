package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.dto.NotificationSubscription
import com.google.gson.Gson
import nl.martijndwars.webpush.Notification
import nl.martijndwars.webpush.PushService
import nl.martijndwars.webpush.Subscription
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

@CrossOrigin
@RestController
class NotificationController {

    @PostMapping("/notifications", consumes = ["application/json"])
    fun subscribeUserNotification(@RequestBody request: NotificationSubscription): ResponseEntity<HttpStatus> {
        Thread(Sender(request)).start()
        return ResponseEntity(HttpStatus.OK)
    }
}

class Sender(private val data: NotificationSubscription) : Runnable {
    override fun run() {
        send()
    }

    private fun send() {
        Security.addProvider(BouncyCastleProvider())

        val service = PushService(
                "these-are-not-the-keys-you-are-looking-for",
                "these-are-not-the-keys-you-are-looking-for", "neither-is-this-subject")

        val companion = Subscription()
        val keys = companion.Keys(data.keys.p256dh, data.keys.auth)
        val sub = Subscription(data.endpoint, keys)

        val json = Gson().toJson(NotificationPayload(Payload()))

        val notification = Notification(sub, json)

        service.send(notification)
    }
}

class NotificationPayload(
    var notification: Payload
)

class Payload(
    var title: String = "title",
    var body: String = "body",
    var icon: String = "",
    var vibrate: Array<Int> = emptyArray()
)
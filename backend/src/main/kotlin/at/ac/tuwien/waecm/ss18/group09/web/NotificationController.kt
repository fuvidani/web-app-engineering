package at.ac.tuwien.waecm.ss18.group09.web

/* ktlint-disable no-wildcard-imports */
import at.ac.tuwien.waecm.ss18.group09.dto.NotificationSubscriptionRequest
import at.ac.tuwien.waecm.ss18.group09.service.INotificationService
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
class NotificationController(private val notificationService: INotificationService) {

    @PostMapping("/notifications", consumes = ["application/json"])
    fun subscribeUserNotification(@RequestBody request: NotificationSubscriptionRequest) {
        notificationService.saveNotificationSubscription(request)
    }
}

package at.ac.tuwien.waecm.ss18.group09.service

/* ktlint-disable no-wildcard-imports */
import at.ac.tuwien.waecm.ss18.group09.dto.*
import at.ac.tuwien.waecm.ss18.group09.notification.NotificationSender
import at.ac.tuwien.waecm.ss18.group09.notification.WebPushConfig
import at.ac.tuwien.waecm.ss18.group09.repository.NotificationSubscriptionRepository
import org.bouncycastle.util.encoders.Base64
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

interface INotificationService {

    fun saveNotificationSubscription(notificationSubscriptionRequest: NotificationSubscriptionRequest)

    fun findNotificationSubscriptionByEmail(email: String): Mono<NotificationSubscriptionRequest>

    fun informPossibleUsers(medicalQuery: MedicalQuery, medicalQueryService: IMedicalQueryService): MedicalQuery
}

@Service
class NotificationService(
    private val repository: NotificationSubscriptionRepository,
    private val securityService: ISecurityService,
    @Value("\${webPush.publicVAPID}") private val publicKey: String,
    @Value("\${webPush.privateVAPID}") private val privateKey: String,
    @Value("\${webPush.subject}") private val subject: String
) : INotificationService {

    override fun informPossibleUsers(medicalQuery: MedicalQuery, medicalQueryService: IMedicalQueryService): MedicalQuery {
        securityService.getAllLoggedInUsers().forEach { user ->
            if (user is User) {
                val info = medicalQueryService
                        .findMatchingQueries(user.id)
                        .filter {
                            it.queryId == medicalQuery.id
                        }
                        .map {
                            informUser(user, it)
                        }
                info.subscribe()
            }
        }
        return medicalQuery
    }

    private fun informUser(user: AbstractUser, message: RelevantQueryData): Mono<AbstractUser> {
        val x = findNotificationSubscriptionByEmail(user.email).map {
            NotificationSender(it.subscription, message, WebPushConfig(publicKey, privateKey, subject)).send()
        }
        x.subscribe()
        return Mono.just(user)
    }

    override fun saveNotificationSubscription(notificationSubscriptionRequest: NotificationSubscriptionRequest) {
        repository.save(encodeNotificationRequest(notificationSubscriptionRequest)).block()
    }

    override fun findNotificationSubscriptionByEmail(email: String): Mono<NotificationSubscriptionRequest> {
        return repository.findById(email).map { decodeNotificationRequest(it) }
    }

    private fun encodeNotificationRequest(notificationSubscriptionRequest: NotificationSubscriptionRequest): NotificationSubscriptionRequest {
        val subscription = notificationSubscriptionRequest.subscription
        val encodedEndpoint = encodePlainString(subscription.endpoint)
        val encodedAuth = encodePlainString(subscription.keys.auth)
        val encodedKey = encodePlainString(subscription.keys.p256dh)
        val encodedSubscription = NotificationSubscription(encodedEndpoint, subscription.expirationTime, Keys(encodedKey, encodedAuth))
        return NotificationSubscriptionRequest(email = notificationSubscriptionRequest.email, subscription = encodedSubscription)
    }

    private fun decodeNotificationRequest(notificationSubscriptionRequest: NotificationSubscriptionRequest): NotificationSubscriptionRequest {
        val subscription = notificationSubscriptionRequest.subscription
        val decodedEndpoint = decodeBase64String(subscription.endpoint)
        val decodedAuth = decodeBase64String(subscription.keys.auth)
        val decodedKey = decodeBase64String(subscription.keys.p256dh)
        val decodedSubscription = NotificationSubscription(decodedEndpoint, subscription.expirationTime, Keys(decodedKey, decodedAuth))
        return NotificationSubscriptionRequest(email = notificationSubscriptionRequest.email, subscription = decodedSubscription)
    }

    private fun decodeBase64String(input: String): String {
        return String(Base64.decode(input))
    }

    private fun encodePlainString(input: String): String {
        return Base64.toBase64String(input.toByteArray())
    }
}
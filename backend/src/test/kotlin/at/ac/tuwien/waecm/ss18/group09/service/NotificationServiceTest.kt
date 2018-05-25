package at.ac.tuwien.waecm.ss18.group09.service

import at.ac.tuwien.waecm.ss18.group09.BackendTestApplication
import at.ac.tuwien.waecm.ss18.group09.dto.Keys
import at.ac.tuwien.waecm.ss18.group09.dto.NotificationSubscription
import at.ac.tuwien.waecm.ss18.group09.dto.NotificationSubscriptionRequest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(value = ["application.yml"], classes = [BackendTestApplication::class])
class NotificationServiceTest {

    @Autowired
    private lateinit var notificationService: INotificationService

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @Before
    fun setUp() {
        mongoTemplate.dropCollection(NotificationSubscriptionRequest::class.java)
    }

    @Test
    fun save_savingValidRequestObject_shouldPersist() {
        val request = getNotificationSubscriptionRequest()
        notificationService.saveNotificationSubscription(request)
        assertNotNull("the created object must not be null", request)
    }

    @Test
    fun findByEmail_creatingAndSearchingVorRequestObject_shouldStoreAndReturn() {
        val request = getNotificationSubscriptionRequest()
        notificationService.saveNotificationSubscription(request)
        val found = notificationService.findNotificationSubscriptionByEmail(request.email).block()
        assertNotNull("the found object must not be null", found)
        assertEquals("the created and searched object must be equal", request, found)
    }

    fun getNotificationSubscriptionRequest(): NotificationSubscriptionRequest {
        val email = "k@k"
        return NotificationSubscriptionRequest(email = email, subscription = getNotificationSubscription())
    }

    fun getNotificationSubscription(): NotificationSubscription {
        return NotificationSubscription("endpoint", null, Keys("p-a-d", "au-th-string"))
    }
}
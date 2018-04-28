package at.ac.tuwien.waecm.ss18.group09.util

import at.ac.tuwien.waecm.ss18.group09.dto.AnonymizedUserInformation
import at.ac.tuwien.waecm.ss18.group09.dto.MedicalInformation
import at.ac.tuwien.waecm.ss18.group09.dto.User
import at.ac.tuwien.waecm.ss18.group09.service.UserService
import java.util.*

class PseduoAnonymizer(private val userService: UserService) {
    private val userIdMap = mutableMapOf<String, String>()
    private val userMap = mutableMapOf<String, User>()

    fun anonymize(info: MedicalInformation): AnonymizedUserInformation {

        val user = userMap.getOrElse(
            info.userId,
            {
                val u = userService.findById(info.userId).block() as User
                userMap[info.userId] = u
                u
            }
        )

        info.userId = userIdMap.getOrElse(info.userId,
            {
                val uid = UUID.randomUUID().toString()
                userIdMap[info.userId] = uid
                uid
            }
        )

        return AnonymizedUserInformation(
            UUID.randomUUID().toString(),
            info,
            info.userId,
            user.birthday,
            user.gender
        )
    }
}
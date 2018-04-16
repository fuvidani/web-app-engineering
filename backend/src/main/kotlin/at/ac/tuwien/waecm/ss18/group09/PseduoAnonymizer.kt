package at.ac.tuwien.waecm.ss18.group09

import at.ac.tuwien.waecm.ss18.group09.dto.AnonymizedUserInformation
import at.ac.tuwien.waecm.ss18.group09.dto.MedicalInformation
import at.ac.tuwien.waecm.ss18.group09.dto.User
import at.ac.tuwien.waecm.ss18.group09.service.UserService
import java.util.*

class PseduoAnonymizer(private val userService: UserService) {
    private val userMap = mutableMapOf<String, String>()

    fun anonymize(info: MedicalInformation): AnonymizedUserInformation {

        val user = userService.findById(info.user).block() as User
        info.user = userMap.getOrElse(info.user,
                {
                    val uid = UUID.randomUUID().toString()
                    userMap[info.user] = uid
                    uid
                }
        )

        return AnonymizedUserInformation(
                UUID.randomUUID().toString(),
                info,
                info.user,
                user.birthday,
                user.gender
        )
    }
}
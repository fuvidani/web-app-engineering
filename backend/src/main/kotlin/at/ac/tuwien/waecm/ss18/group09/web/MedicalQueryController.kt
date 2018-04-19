package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.dto.AnonymizedUserInformation
import at.ac.tuwien.waecm.ss18.group09.dto.MedicalQuery
import at.ac.tuwien.waecm.ss18.group09.dto.SharingPermission
import at.ac.tuwien.waecm.ss18.group09.service.MedicalQueryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

//TODO Authorize
@CrossOrigin
@RestController
@RequestMapping("user/{id}/medicalQuery")
class MedicalQueryController(private val medicalQueryService: MedicalQueryService) {

    @PostMapping
    fun createMedicalQuery(@RequestBody medicalQuery: MedicalQuery): Mono<ResponseEntity<MedicalQuery>> {
        return medicalQueryService.create(medicalQuery)
                .map { query -> ResponseEntity<MedicalQuery>(query, HttpStatus.OK) }
                .defaultIfEmpty(ResponseEntity.badRequest().build())
    }

    @GetMapping(produces = ["text/event-stream"])
    fun getAllMedicalInformationForUser(@PathVariable("id") id: String): Flux<MedicalQuery> {
        return medicalQueryService.findByResearchFacility(id)
    }


    @GetMapping(path = ["/matching"], produces = ["text/event-stream"])
    fun getAllMatchingQueries(@PathVariable("id") id: String): Flux<MedicalQuery> {
        return medicalQueryService.findMatchingQueries(id)
    }

    @PostMapping(path = ["/permissions"])
    fun createSharingPermission(@RequestBody permissions: List<SharingPermission>): Mono<ResponseEntity<List<SharingPermission>>> {
        return medicalQueryService.createSharingPermission(permissions)
                .map { list -> ResponseEntity<List<SharingPermission>>(list, HttpStatus.OK) }
                .defaultIfEmpty(ResponseEntity.badRequest().build())
    }

    @PostMapping(path = ["/permission"])
    fun createSharingPermission(@RequestBody permission: SharingPermission): Mono<ResponseEntity<SharingPermission>> {
        return medicalQueryService.createSharingPermission(permission)
                .map { list -> ResponseEntity<SharingPermission>(list, HttpStatus.OK) }
                .defaultIfEmpty(ResponseEntity.badRequest().build())
    }

    @PostMapping(path = ["{qid}/shared"])
    fun getSharedInformationForQuery(@PathVariable("qid") qid: String): Flux<AnonymizedUserInformation> {
        return medicalQueryService.findSharedInformationForQuery(qid)
    }
}
package at.ac.tuwien.waecm.ss18.group09.web

/* ktlint-disable no-wildcard-imports */
import at.ac.tuwien.waecm.ss18.group09.dto.AnonymizedUserInformation
import at.ac.tuwien.waecm.ss18.group09.dto.MedicalQuery
import at.ac.tuwien.waecm.ss18.group09.dto.RelevantQueryData
import at.ac.tuwien.waecm.ss18.group09.dto.SharingPermission
import at.ac.tuwien.waecm.ss18.group09.service.MedicalQueryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@CrossOrigin
@RestController
@RequestMapping("user/{id}/medicalQuery")
class MedicalQueryController(private val medicalQueryService: MedicalQueryService) {

    @PreAuthorize("hasRole('ROLE_RESEARCH')")
    @PostMapping
    fun createMedicalQuery(@RequestBody medicalQuery: MedicalQuery): Mono<ResponseEntity<MedicalQuery>> {
        return medicalQueryService.create(medicalQuery)
            .map { query -> ResponseEntity<MedicalQuery>(query, HttpStatus.OK) }
            .defaultIfEmpty(ResponseEntity.badRequest().build())
    }

    @PreAuthorize("hasRole('ROLE_RESEARCH')")
    @GetMapping(produces = ["text/event-stream"])
    fun getAllMedicalInformationForUser(@PathVariable("id") id: String): Flux<MedicalQuery> {
        return medicalQueryService.findByResearchFacilityId(id)
    }

    @PreAuthorize("hasRole('ROLE_END_USER')")
    @GetMapping(path = ["/matching"], produces = ["text/event-stream"])
    fun getAllMatchingQueries(@PathVariable("id") id: String): Flux<RelevantQueryData> {
        return medicalQueryService.findMatchingQueries(id)
    }

    @PreAuthorize("hasRole('ROLE_END_USER')")
    @PostMapping(path = ["/permissions"])
    fun createSharingPermission(@RequestBody permissions: List<SharingPermission>): Flux<SharingPermission> {
        return medicalQueryService.createSharingPermission(permissions)
    }

    @PreAuthorize("hasRole('ROLE_END_USER')")
    @PostMapping(path = ["/permission"])
    fun createSharingPermission(@RequestBody permission: SharingPermission): Mono<ResponseEntity<SharingPermission>> {
        return medicalQueryService.createSharingPermission(permission)
            .map { list -> ResponseEntity<SharingPermission>(list, HttpStatus.OK) }
            .defaultIfEmpty(ResponseEntity.badRequest().build())
    }

    @PreAuthorize("hasRole('ROLE_RESEARCH')")
    @GetMapping(path = ["{queryid}/shared"], produces = ["text/event-stream"])
    fun getSharedInformationForQuery(@PathVariable("queryid") qid: String): Flux<AnonymizedUserInformation> {
        return medicalQueryService.findSharedInformationForQuery(qid)
    }
}
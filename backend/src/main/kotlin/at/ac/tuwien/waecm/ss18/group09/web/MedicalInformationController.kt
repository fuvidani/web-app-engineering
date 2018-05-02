package at.ac.tuwien.waecm.ss18.group09.web

/* ktlint-disable no-wildcard-imports */
import at.ac.tuwien.waecm.ss18.group09.dto.AnonymizedUserInformation
import at.ac.tuwien.waecm.ss18.group09.dto.MedicalInformation
import at.ac.tuwien.waecm.ss18.group09.service.IMedicalInformationService
import at.ac.tuwien.waecm.ss18.group09.service.MedicalQueryService
import at.ac.tuwien.waecm.ss18.group09.service.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@CrossOrigin
@RestController
@RequestMapping("/user/{id}/medicalInformation")
class MedicalInformationController(
    private val medicalInformationService: IMedicalInformationService,
    private val medicalQueryService: MedicalQueryService
) {

    @PreAuthorize("hasRole('ROLE_END_USER')")
    @PostMapping
    fun createMedicalInformation(@PathVariable("id") id: String, @RequestBody medicalInformation: MedicalInformation): Mono<ResponseEntity<MedicalInformation>> {

        if (medicalInformation.userId != id) return Mono.just(ResponseEntity(HttpStatus.FORBIDDEN))

        return try {
            medicalInformationService.create(medicalInformation)
                .map { i -> ResponseEntity<MedicalInformation>(i, HttpStatus.OK) }
                .defaultIfEmpty(ResponseEntity(HttpStatus.BAD_REQUEST))
        } catch (e: ValidationException) {
            Mono.just(ResponseEntity(HttpStatus.BAD_REQUEST))
        }
    }

    @PreAuthorize("hasRole('ROLE_END_USER')")
    @GetMapping
    fun getAllMedicalInformationForUser(@PathVariable("id") id: String): Flux<MedicalInformation> {
        return medicalInformationService.findByUserId(id)
    }

    @PreAuthorize("hasRole('ROLE_RESEARCH')")
    @GetMapping(path = ["/shared"])
    fun getAllSharedMedicalInformation(@PathVariable("id") id: String): Flux<AnonymizedUserInformation> {
        return medicalQueryService.findAllSharedInformationOfResearchFacility(id)
    }
}
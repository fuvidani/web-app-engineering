package at.ac.tuwien.waecm.ss18.group09.web

/* ktlint-disable no-wildcard-imports */
import at.ac.tuwien.waecm.ss18.group09.dto.AnonymizedUserInformation
import at.ac.tuwien.waecm.ss18.group09.dto.MedicalInformation
import at.ac.tuwien.waecm.ss18.group09.service.IMedicalInformationService
import at.ac.tuwien.waecm.ss18.group09.service.MedicalQueryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

//TODO Authorize
@CrossOrigin
@RestController
@RequestMapping("/user/{id}/medicalInformation")
class MedicalInformationController(private val medicalInformationService: IMedicalInformationService,
                                   private val medicalQueryService: MedicalQueryService) {

    @PostMapping
    fun createMedicalInformation(@RequestBody medicalInformation: MedicalInformation): Mono<ResponseEntity<MedicalInformation>> {
        return medicalInformationService.create(medicalInformation)
                .map { i -> ResponseEntity<MedicalInformation>(i, HttpStatus.OK) }
                .defaultIfEmpty(ResponseEntity(HttpStatus.BAD_REQUEST))
    }

    @GetMapping(produces = ["text/event-stream"])
    fun getAllMedicalInformationForUser(@PathVariable("id") id: String): Flux<MedicalInformation> {
        return medicalInformationService.findByUserId(id)
    }

    @GetMapping(path = ["/shared"], produces = ["text/event-stream"])
    fun getAllSharedMedicalInformation(@PathVariable("id") id: String): Flux<AnonymizedUserInformation> {
        return medicalQueryService.findAllSharedInformationOfResearchFacility(id)
    }

}
package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.dto.MedicalQuery
import at.ac.tuwien.waecm.ss18.group09.service.MedicalQueryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

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
}
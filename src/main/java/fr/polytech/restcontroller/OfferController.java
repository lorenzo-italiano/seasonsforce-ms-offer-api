package fr.polytech.restcontroller;

import fr.polytech.annotation.*;
import fr.polytech.model.OfferDetailDTO;
import fr.polytech.service.OfferService;
import fr.polytech.model.Offer;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/offer")
public class OfferController {

    private final Logger logger = LoggerFactory.getLogger(OfferController.class);

    @Autowired
    private OfferService offerService;

    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @IsAdminOrCandidate
    @GetMapping("/")
    public ResponseEntity<List<Offer>> getAllOffers() {
        return ResponseEntity.ok(offerService.getAllOffers());
    }

    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @IsAdminOrCandidate
    @GetMapping("/detailed")
    public ResponseEntity<List<OfferDetailDTO>> getAllDetailedOffers(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(offerService.getAllOffersDetailed(token));
    }

    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/{id}")
    public ResponseEntity<OfferDetailDTO> getOfferById(@RequestHeader("Authorization") String token, @PathVariable UUID id) {
        try {
            return ResponseEntity.ok(offerService.getDetailedOfferById(id, token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @IsCandidateOrIsAdminOrIsRecruiterAndInOfferCompany
    @GetMapping("/company/{id}")
    public ResponseEntity<List<OfferDetailDTO>> getOfferListByCompanyId(@RequestHeader("Authorization") String token, @PathVariable UUID id) {
        try {
            return ResponseEntity.ok(offerService.getDetailedOfferListByCompanyId(id, token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @IsRecruiter
    @PostMapping("/")
    public ResponseEntity<Offer> createOffer(@RequestBody Offer offer) {
        try{
            return ResponseEntity.ok(offerService.saveOffer(offer));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @IsAdminOrRecruiterAndOwnerOfRessource
    @PutMapping("/")
    public ResponseEntity<Offer> updateOffer(@RequestHeader("Authorization") String token, @RequestBody Offer offer) {
        Offer existingOffer = offerService.getOfferById(offer.getId());
        if (existingOffer == null) {
            return ResponseEntity.notFound().build();
        }

        Offer newOffer = Offer.updateOfferValues(existingOffer, offer);

        return ResponseEntity.ok(offerService.saveOffer(newOffer));
    }

    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @IsRecruiterAndOwnerOfRessourceById
    @PatchMapping("/{id}/recruited/add/{recruitedId}")
    public ResponseEntity<Offer> addRecruitedCandidate(@RequestHeader("Authorization") String token, @PathVariable("id") UUID id, @PathVariable("recruitedId") UUID recruitedId) {
        Offer existingOffer = offerService.getOfferById(id);

        if (existingOffer == null) {
            return ResponseEntity.notFound().build();
        }

        existingOffer.setRecruitedId(recruitedId);
        existingOffer.setOffer_status("RECRUITED");

        return ResponseEntity.ok(offerService.saveOffer(existingOffer));
    }

    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @IsRecruiterAndOwnerOfRessourceById
    @PatchMapping("/{id}/recruited/remove/")
    public ResponseEntity<Offer> removeRecruitedCandidate(@RequestHeader("Authorization") String token, @PathVariable("id") UUID id) {
        Offer existingOffer = offerService.getOfferById(id);

        if (existingOffer == null) {
            return ResponseEntity.notFound().build();
        }

        existingOffer.setRecruitedId(null);
        existingOffer.setOffer_status("IN_PROGRESS");

        return ResponseEntity.ok(offerService.saveOffer(existingOffer));
    }

    @IsAdminOrRecruiterAndOwnerOfRessourceById
    @DeleteMapping("/{id}")
    public void deleteUser(@RequestHeader("Authorization") String token, @PathVariable UUID id) {
        offerService.deleteOffer(id);
    }
}


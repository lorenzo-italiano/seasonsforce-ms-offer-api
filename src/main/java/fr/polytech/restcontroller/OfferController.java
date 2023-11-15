package fr.polytech.restcontroller;

import fr.polytech.annotation.IsAdminOrCandidate;
import fr.polytech.annotation.IsAdminOrRecruiterAndOwnerOfRessource;
import fr.polytech.annotation.IsCandidateOrIsAdminOrIsRecruiterAndInOfferCompany;
import fr.polytech.annotation.IsRecruiter;
import fr.polytech.model.OfferDetailDTO;
import fr.polytech.service.OfferService;
import fr.polytech.model.Offer;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
//@CrossOrigin(origins = "http://localhost:19006", allowedHeaders = "*", allowCredentials = "true", exposedHeaders = {"Access-Control-Allow-Origin","Access-Control-Allow-Credentials"})
@RequestMapping("/api/v1/offer")
public class OfferController {

    private final Logger logger = LoggerFactory.getLogger(OfferController.class);

    @Autowired
    private OfferService offerService;

    @Produces("application/json")
    @IsAdminOrCandidate
    @GetMapping("/")
    public List<Offer> getAllOffers() {
        return offerService.getAllOffers();
    }

    @Produces("application/json")
    @IsAdminOrCandidate
    @GetMapping("/detailed")
    public List<OfferDetailDTO> getAllDetailedOffers(@RequestHeader("Authorization") String token) {
        return offerService.getAllOffersDetailed(token);
    }

    @Produces("application/json")
    @GetMapping("/{id}")
    public ResponseEntity<OfferDetailDTO> getOfferById(@RequestHeader("Authorization") String token, @PathVariable UUID id) {
        try {
            return ResponseEntity.ok(offerService.getDetailedOfferById(id, token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Produces("application/json")
    @IsCandidateOrIsAdminOrIsRecruiterAndInOfferCompany
    @GetMapping("/company/{id}")
    public List<Offer> getOfferListByCompanyId(@RequestHeader("Authorization") String token, @PathVariable UUID id) {
        return offerService.getOfferListByCompanyId(id);
    }

    @Produces("application/json")
    @Consumes("application/json")
    @IsRecruiter
    @PostMapping("/")
    public Offer createOffer(@RequestHeader("Authorization") String token, @RequestBody Offer offer) {
        return offerService.createOffer(offer, token);
    }

    @Consumes("application/json")
    @Produces("application/json")
    @IsAdminOrRecruiterAndOwnerOfRessource
    @PutMapping("/")
    public Offer updateOffer(@RequestHeader("Authorization") String token, @RequestBody Offer offer) {
        // Vérifiez si l'utilisateur avec l'ID spécifié existe
        Offer existingOffer = offerService.getOfferById(offer.getId());
        if (existingOffer == null) {
            return null; // Vous pouvez gérer cela de manière appropriée, par exemple, en renvoyant une erreur 404
        }

        // Mettez à jour les propriétés de l'utilisateur existant avec les données du nouveau utilisateur
        Offer newOffer = Offer.updateOfferValues(existingOffer, offer);

        // Enregistrez les modifications dans la base de données
        return offerService.saveOffer(newOffer);
    }

    @IsAdminOrRecruiterAndOwnerOfRessource
    @DeleteMapping("/{id}")
    public void deleteUser(@RequestHeader("Authorization") String token, @PathVariable UUID id) {
        offerService.deleteOffer(id);
    }
}


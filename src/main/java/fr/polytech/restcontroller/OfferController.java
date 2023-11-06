package fr.polytech.restcontroller;

import fr.polytech.model.OfferDetailDTO;
import fr.polytech.service.OfferService;
import fr.polytech.model.Offer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/")
    public List<Offer> getAllOffers() {
        return offerService.getAllOffers();
    }

    @GetMapping("/detailed")
    public List<OfferDetailDTO> getAllDetailedOffers(@RequestHeader("Authorization") String token) {
        return offerService.getAllOffersDetailed(token);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDetailDTO> getOfferById(@RequestHeader("Authorization") String token, @PathVariable UUID id) {
        try {
            return ResponseEntity.ok(offerService.getDetailedOfferById(id, token));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/company/{id}")
    public List<Offer> getOfferListByCompanyId(@PathVariable UUID id) {
        return offerService.getOfferListByCompanyId(id);
    }

    @PostMapping("/")
    public Offer createOffer(@RequestBody Offer offer) {
        return offerService.saveOffer(offer);
    }

    @PutMapping("/{id}")
    public Offer updateOffer(@PathVariable UUID id, @RequestBody Offer offer) {
        // Vérifiez si l'utilisateur avec l'ID spécifié existe
        Offer existingOffer = offerService.getOfferById(id);
        if (existingOffer == null) {
            return null; // Vous pouvez gérer cela de manière appropriée, par exemple, en renvoyant une erreur 404
        }

        // Mettez à jour les propriétés de l'utilisateur existant avec les données du nouveau utilisateur
        Offer.updateOfferValues(existingOffer, offer);

        // Enregistrez les modifications dans la base de données
        return offerService.saveOffer(existingOffer);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        offerService.deleteOffer(id);
    }
}


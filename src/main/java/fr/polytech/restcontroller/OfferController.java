package fr.polytech.restcontroller;

import fr.polytech.service.OfferService;
import fr.polytech.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/offer")
public class OfferController {
    @Autowired
    private OfferService offerService;

    @GetMapping("/")
    public List<Offer> getAllOffers() {
        return offerService.getAllOffers();
    }

    @GetMapping("/{id}")
    public Offer getOfferById(@PathVariable UUID id) {
        return offerService.getOfferById(id);
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
        existingOffer.setOffer_language(offer.getOffer_language());
        existingOffer.setOffer_status(offer.getOffer_status());
        existingOffer.setBenefits(offer.getBenefits());
        existingOffer.setCompanyId(offer.getCompanyId());
        existingOffer.setContract_type(offer.getContract_type());
        existingOffer.setHours_per_week(offer.getHours_per_week());
        existingOffer.setLocation(offer.getLocation());
        existingOffer.setModality(offer.getModality());
        existingOffer.setSalary(offer.getSalary());
        existingOffer.setContact_information(offer.getContact_information());
        existingOffer.setJob_description(offer.getJob_description());
        existingOffer.setJob_title(offer.getJob_title());
        existingOffer.setPublication_date(offer.getPublication_date());
        existingOffer.setRequired_skills(offer.getRequired_skills());
        existingOffer.setRequired_experience(offer.getRequired_experience());
        existingOffer.setRequired_degree(offer.getRequired_degree());

        // Enregistrez les modifications dans la base de données
        return offerService.saveOffer(existingOffer);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        offerService.deleteOffer(id);
    }
}


package fr.polytech.service;

import fr.polytech.model.Offer;
import fr.polytech.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public Offer getOfferById(UUID id) {
        return offerRepository.findById(id).orElse(null);
    }

    public List<Offer> getOfferListByCompanyId(UUID id) {
        return offerRepository.findByCompanyId(id);
    }

    public Offer saveOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    public void deleteOffer(UUID id) {
        offerRepository.deleteById(id);
    }
}


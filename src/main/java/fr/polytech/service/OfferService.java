package fr.polytech.service;

import fr.polytech.model.*;
import fr.polytech.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private JobCategoryService jobCategoryService;

    @Autowired
    private RestTemplate restTemplate;

    /*---------------------------------------------------------------*/
    /*------------------------ CRUD METHODS -------------------------*/
    /*---------------------------------------------------------------*/

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public Offer getOfferById(UUID id) {
        return offerRepository.findById(id).orElse(null);
    }

    public OfferDetailDTO getDetailedOfferById(UUID id) throws Exception {

        Offer offer = offerRepository.findById(id).orElse(null);

//        String bearerToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        System.out.println(bearerToken);

        if (offer != null) {
            // Fetching company infos from address microservice
            CompanyDTO companyDTO = fetchCompanyById(offer.getCompanyId());

            AddressDTO addressDTO = fetchAddressById(companyDTO.getAddressId());

            JobCategory jobCategory = jobCategoryService.getJobCategoryById(offer.getJobCategoryId());

            OfferDetailDTO offerDetailDTO = OfferDetailDTO.getOfferDetailDTO(offer, companyDTO, addressDTO, jobCategory);

            return offerDetailDTO;
        }
        else {
            throw new Exception("Offer not found");
        }
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

    /*---------------------------------------------------------------*/
    /*------------------------ OTHER METHODS ------------------------*/
    /*---------------------------------------------------------------*/

    private CompanyDTO fetchCompanyById(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UUID> requestEntity = new HttpEntity<>(null, headers);

        // Sending the request to address microservice
        ResponseEntity<CompanyDTO> responseEntity = restTemplate.exchange(
                "lb://company-api/api/v1/company/" + id,
                HttpMethod.GET,
                requestEntity,
                CompanyDTO.class
        );

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            // If the status code is not 200, then throw the exception to the client
            throw new HttpClientErrorException(responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }

    private AddressDTO fetchAddressById(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UUID> requestEntity = new HttpEntity<>(null, headers);

        // Sending the request to address microservice
        ResponseEntity<AddressDTO> responseEntity = restTemplate.exchange(
                "lb://address-api/api/v1/address/" + id,
                HttpMethod.GET,
                requestEntity,
                AddressDTO.class
        );

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            // If the status code is not 200, then throw the exception to the client
            throw new HttpClientErrorException(responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }

    // TODO findSimilarOffer ?
}


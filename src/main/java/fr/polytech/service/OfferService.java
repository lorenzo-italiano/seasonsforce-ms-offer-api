package fr.polytech.service;

import com.auth0.jwt.JWT;
import fr.polytech.model.*;
import fr.polytech.repository.OfferRepository;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OfferService {

    private Logger logger = LoggerFactory.getLogger(OfferService.class);

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

    public List<OfferDetailDTO> getAllOffersDetailed(String token) throws NotFoundException {
        List<Offer> offers = offerRepository.findAll();
        List<OfferDetailDTO> offerDetailDTOList = new ArrayList<>();

        if (offers != null) {
            token = token.substring(7);
            // Fetching company infos from address microservice
            for (Offer offer : offers) {
                logger.info("before fetching company");
                CompanyDTO companyDTO = fetchCompanyById(offer.getCompanyId(), token);

                logger.info("before fetching address");
                AddressDTO addressDTO = fetchAddressById(offer.getAddressId(), token);

                logger.info("before fetching job category");
                JobCategory jobCategory = jobCategoryService.getJobCategoryById(offer.getJobCategoryId());

                logger.info("before creating offer detail dto");
                OfferDetailDTO offerDetailDTO = OfferDetailDTO.getOfferDetailDTO(offer, companyDTO, addressDTO, jobCategory);

                logger.info("before adding offer detail dto to list");
                offerDetailDTOList.add(offerDetailDTO);
            }

            return offerDetailDTOList;
        }
        else {
            throw new NotFoundException("Offer list not found");
        }
    }

    public Offer getOfferById(UUID id) {
        return offerRepository.findById(id).orElse(null);
    }

    public OfferDetailDTO getDetailedOfferById(UUID id, String token) throws Exception {

        Offer offer = offerRepository.findById(id).orElse(null);

        if (offer != null) {
            token = token.substring(7);

            // Fetching company infos from address microservice
            CompanyDTO companyDTO = fetchCompanyById(offer.getCompanyId(), token);

            AddressDTO addressDTO = fetchAddressById(offer.getAddressId(), token);

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

    public Offer createOffer(Offer offer, String token){
        String tokenToSend = token.split(" ")[1];

        String userId = getUserIdFromToken(tokenToSend);

        offer.setCreatorId(UUID.fromString(userId));

        return offerRepository.save(offer);
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

    private CompanyDTO fetchCompanyById(UUID id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
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

    private AddressDTO fetchAddressById(UUID id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
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

    private RecruiterDTO fetchRecruiter(UUID recruiterId, HttpHeaders headers) {
        logger.info("Fetching recruiter");
        String url = "lb://user-api/api/v1/user/" + recruiterId.toString();
        logger.info(recruiterId.toString());
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<RecruiterDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, RecruiterDTO.class);
        RecruiterDTO recruiter = response.getBody();
        if (recruiter == null || !recruiter.getRole().equals("recruiter")) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "User is not a recruiter");
        }
        return recruiter;
    }

    private String getUserIdFromToken(String token) {
        return JWT.decode(token).getClaim("sub").asString();
    }

    public boolean isRecruiterInCompany(UUID companyId, String token) {
        String tokenToSend = token.split(" ")[1];

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenToSend);

        String userId = getUserIdFromToken(tokenToSend);

        RecruiterDTO recruiterDTO = fetchRecruiter(UUID.fromString(userId), headers);

        if (recruiterDTO.getCompanyId() == null) {
            return false;
        }
        else {
            return recruiterDTO.getCompanyId().equals(companyId);
        }
    }

    public boolean isRecruiterOwnerOfOffer(Offer offer, String token) {
        String tokenToSend = token.split(" ")[1];

        String userId = getUserIdFromToken(tokenToSend);

        if(userId == null || userId.isEmpty()) {
            return false;
        }

        return offer.getCreatorId().equals(UUID.fromString(userId));
    }

    // TODO findSimilarOffer ?
}


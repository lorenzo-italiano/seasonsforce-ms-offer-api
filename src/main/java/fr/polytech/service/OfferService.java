package fr.polytech.service;

import com.auth0.jwt.JWT;
import fr.polytech.model.*;
import fr.polytech.repository.OfferRepository;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OfferService {

    private final Logger logger = LoggerFactory.getLogger(OfferService.class);

    private final OfferRepository offerRepository;

    private final JobCategoryService jobCategoryService;

    private final RestTemplate restTemplate;

    private final KafkaService kafkaService;

    private final String COMPANY_API_URI = "COMPANY_API_URI";
    private final String ADDRESS_API_URI = "ADDRESS_API_URI";
    private final String USER_API_URI = "USER_API_URI";

    @Autowired
    public OfferService(OfferRepository offerRepository, JobCategoryService jobCategoryService, RestTemplate restTemplate, KafkaService kafkaService) {
        this.offerRepository = offerRepository;
        this.jobCategoryService = jobCategoryService;
        this.restTemplate = restTemplate;
        this.kafkaService = kafkaService;
    }

    /*---------------------------------------------------------------*/
    /*------------------------ CRUD METHODS -------------------------*/
    /*---------------------------------------------------------------*/

    /**
     * Create an offer.
     *
     * @return The created offer.
     */
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    /**
     * Get all offers with details.
     *
     * @param token Token
     * @return A list of offers.
     * @throws NotFoundException If the offer list is not found.
     */
    public List<OfferDetailDTO> getAllOffersDetailed(String token) throws NotFoundException {
        List<Offer> offers = offerRepository.findAll();

        if (offers != null) {
            token = token.substring(7);

            return getOfferListDetails(offers, token);
        } else {
            throw new NotFoundException("Offer list not found");
        }
    }

    /**
     * Get all offers with details.
     *
     * @param offerList Offer list
     * @param token     Token
     * @return A list of offers.
     */
    public List<OfferDetailDTO> getOfferListDetails(List<Offer> offerList, String token) {
        List<OfferDetailDTO> offerDetailDTOList = new ArrayList<>();

        for (Offer offer : offerList) {
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

    /**
     * Get an offer by its id.
     *
     * @param id The id of the offer.
     * @return The offer.
     */
    public Offer getOfferById(UUID id) {
        return offerRepository.findById(id).orElse(null);
    }

    /**
     * Get an offer by its id with details.
     *
     * @param id    The id of the offer.
     * @param token Token of the user.
     * @return The offer.
     * @throws Exception If the offer is not found.
     */
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
        } else {
            throw new Exception("Offer not found");
        }
    }

    /**
     * Get all offers by company id.
     *
     * @param id    The id of the company.
     * @param token Token of the user.
     * @return A list of offers.
     */
    public List<OfferDetailDTO> getDetailedOfferListByCompanyId(UUID id, String token) {
        List<Offer> offerList = offerRepository.findByCompanyId(id);
        token = token.split(" ")[1];
        return getOfferListDetails(offerList, token);
    }

    /**
     * Create an offer.
     *
     * @param offer Offer
     * @return The created offer.
     */
    public Offer saveOffer(Offer offer) {
        logger.info("Saving offer");
        Offer createdOffer = offerRepository.save(offer);
        NotificationDTO notificationDTO = createNotification(createdOffer, "Offer created");
        kafkaService.sendMessage(notificationDTO);
        return createdOffer;
    }

    public void deleteOffer(UUID id) {
        offerRepository.deleteById(id);
    }

    /*---------------------------------------------------------------*/
    /*------------------------ OTHER METHODS ------------------------*/
    /*---------------------------------------------------------------*/

    /**
     * Fetch company by id.
     *
     * @param id    Company id
     * @param token Token of the user
     * @return Company
     */
    private CompanyDTO fetchCompanyById(UUID id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<UUID> requestEntity = new HttpEntity<>(null, headers);

        // Sending the request to address microservice
        ResponseEntity<CompanyDTO> responseEntity = restTemplate.exchange(
                System.getenv(COMPANY_API_URI) + "/" + id,
                HttpMethod.GET,
                requestEntity,
                CompanyDTO.class
        );

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            // If the status code is not 200, then throw the exception to the client
            throw new HttpClientErrorException(responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }

    /**
     * Fetch address by id.
     *
     * @param id    Address id
     * @param token Token of the user
     * @return Address
     */
    private AddressDTO fetchAddressById(UUID id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<UUID> requestEntity = new HttpEntity<>(null, headers);

        // Sending the request to address microservice
        ResponseEntity<AddressDTO> responseEntity = restTemplate.exchange(
                System.getenv(ADDRESS_API_URI) + "/" + id,
                HttpMethod.GET,
                requestEntity,
                AddressDTO.class
        );

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            // If the status code is not 200, then throw the exception to the client
            throw new HttpClientErrorException(responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }

    /**
     * Fetch recruiter by id.
     *
     * @param recruiterId Recruiter id
     * @param headers     Headers
     * @return Recruiter
     */
    private RecruiterDTO fetchRecruiter(UUID recruiterId, HttpHeaders headers) {
        logger.info("Fetching recruiter");
        String url = System.getenv(USER_API_URI) + "/" + recruiterId.toString();
        logger.info(recruiterId.toString());
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<RecruiterDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, RecruiterDTO.class);
        RecruiterDTO recruiter = response.getBody();
        if (recruiter == null || !recruiter.getRole().equals("recruiter")) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "User is not a recruiter");
        }
        return recruiter;
    }

    /**
     * Get user id from token.
     *
     * @param token Token of the user
     * @return User id
     */
    private String getUserIdFromToken(String token) {
        return JWT.decode(token).getClaim("sub").asString();
    }

    /**
     * Check if the recruiter is in the company.
     *
     * @param companyId Company id
     * @param token     Token of the user
     * @return True if the recruiter is in the company, false otherwise
     */
    public boolean isRecruiterInCompany(UUID companyId, String token) {
        String tokenToSend = token.split(" ")[1];

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenToSend);

        String userId = getUserIdFromToken(tokenToSend);

        RecruiterDTO recruiterDTO = fetchRecruiter(UUID.fromString(userId), headers);

        if (recruiterDTO.getCompanyId() == null) {
            return false;
        } else {
            return recruiterDTO.getCompanyId().equals(companyId);
        }
    }

    /**
     * Check if the recruiter is the owner of the offer.
     *
     * @param offer Offer
     * @param token Token of the user
     * @return True if the recruiter is the owner of the offer, false otherwise
     */
    public boolean isRecruiterOwnerOfOffer(Offer offer, String token) {
        String tokenToSend = token.split(" ")[1];

        String userId = getUserIdFromToken(tokenToSend);

        if (userId == null || userId.isEmpty()) {
            return false;
        }

        return offer.getCreatorId().equals(UUID.fromString(userId));
    }

    /**
     * Check if the recruiter is the owner of the offer.
     *
     * @param id    Offer id
     * @param token Token of the user
     * @return True if the recruiter is the owner of the offer, false otherwise
     */
    public boolean isRecruiterOwnerOfOfferById(UUID id, String token) {
        String tokenToSend = token.split(" ")[1];

        Offer offer = offerRepository.findById(id).orElse(null);

        String userId = getUserIdFromToken(tokenToSend);

        if (userId == null || userId.isEmpty()) {
            return false;
        }

        if (offer == null) {
            return false;
        }

        return offer.getCreatorId().equals(UUID.fromString(userId));
    }

    /**
     * Create a notification for the offer creator.
     *
     * @param offer   Offer
     * @param message Message
     * @return Notification
     */
    private NotificationDTO createNotification(Offer offer, String message) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setCategory(NotificationCategory.OFFER);
        notificationDTO.setObjectId(offer.getId());
        notificationDTO.setMessage(message);
        notificationDTO.setDate(offer.getPublication_date());
        notificationDTO.setReceiverId(offer.getCreatorId());
        return notificationDTO;
    }

    /**
     * Cron job that checks for completed offers.
     * It sends an Experience to Experience microservice, send a notification to kafka and change the status of the offer to 'completed'.
     */
    // For dev
    @Scheduled(cron = "*/5 * * * * *") // Every 5 seconds
    // TODO change to the cron that is commented under
    // @Scheduled(cron = "0 0 2 * * *") // Every day at 2am
    public void settingCompletedOffers() {
        logger.info("Checking for completed offers");
        List<Offer> offerList = offerRepository.findAll();

        for (Offer offer : offerList) {
            if (offer.getEndDate().toInstant().isBefore(java.time.Instant.now()) && offer.getOffer_status().equals(OfferStatus.RECRUITED.toString())) {
                logger.info("Found completed offer");

                offer.setOffer_status(OfferStatus.COMPLETED.toString());

                ExperienceDTOWithUserId experienceDTOWithUserId = new ExperienceDTOWithUserId();
                experienceDTOWithUserId.setUserId(offer.getRecruitedId());
                experienceDTOWithUserId.setCompanyId(offer.getCompanyId());
                experienceDTOWithUserId.setStartDate(offer.getStartDate());
                experienceDTOWithUserId.setEndDate(offer.getEndDate());
                experienceDTOWithUserId.setJobTitle(offer.getJob_title());
                experienceDTOWithUserId.setJobCategoryId(offer.getJobCategoryId());

                logger.info("Sending experience to kafka");
                kafkaService.sendExperience(experienceDTOWithUserId);

                offerRepository.save(offer);
            }
        }
    }

    /**
     * Get all offers by creator id.
     *
     * @param id    Creator id
     * @param token Token of the user
     * @return List of offers
     */
    public List<OfferDetailDTO> getOfferByCreatorId(UUID id, String token) {
        List<Offer> offerList = offerRepository.findByCreatorId(id);

        logger.info("Offer list size: " + offerList.toString());

        List<OfferDetailDTO> offerDetailDTOList = new ArrayList<>();

        for (Offer offer : offerList) {
            OfferDetailDTO offerDetailDTO;
            try {
                offerDetailDTO = getDetailedOfferById(offer.getId(), token);
                offerDetailDTOList.add(offerDetailDTO);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return offerDetailDTOList;
    }
}


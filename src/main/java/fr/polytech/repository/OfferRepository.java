package fr.polytech.repository;

import fr.polytech.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {
    @Query(value = "SELECT o FROM Offer o WHERE o.companyId = :companyId")
    List<Offer> findByCompanyId(@Param("companyId") UUID companyId);
}

package fr.polytech;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Ajoutez des méthodes personnalisées si nécessaire
}

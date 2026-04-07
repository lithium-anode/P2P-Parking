package parking.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parking.project.model.SpaceOwner;
import java.util.Optional;

@Repository
public interface SpaceOwnerRepository extends JpaRepository<SpaceOwner, Long> {
    // Allows looking up an owner by their unique username for login/profile management
    Optional<SpaceOwner> findByUsername(String username);
}
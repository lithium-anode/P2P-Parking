package parking.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parking.project.model.SpaceOwner;
import java.util.List;

/**
 * [GRASP: Information Expert]
 * Expert for Space Owner data and their associated parking listings.
 */
@Repository
public interface SpaceOwnerRepository extends JpaRepository<SpaceOwner, Long> {
    /**
     * Finds owners who have accumulated significant earnings, 
     * useful for administrative oversight.
     */
    List<SpaceOwner> findByTotalEarningsGreaterThanEqual(double threshold);
}
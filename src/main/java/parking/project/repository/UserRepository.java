package parking.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parking.project.model.User;
import java.util.Optional;

/**
 * [GRASP: Information Expert]
 * This interface is the information expert for accessing the 'users' table. 
 * It provides the necessary data access for authentication and role-based 
 * verification as required by the 'VerifyProxy'. 
 */
/*
    [GRASP: Information Expert]
    This interface is the information expert for accessing the 'users' table. 
    It provides the necessary data access for authentication and role-based 
    verification as required by the 'VerifyProxy'.
*/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Used by the UserService during the login process to find a user
    // by their unique username.
    Optional<User> findByUsername(String username);
}
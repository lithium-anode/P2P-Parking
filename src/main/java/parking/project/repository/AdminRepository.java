package parking.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parking.project.model.Admin;

/*
    [GRASP: Information Expert]
    Expert for Administrator accounts and system-wide management data.
*/
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> { }
package net.proselyte.jwtappdemo.repository;

import net.proselyte.jwtappdemo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}

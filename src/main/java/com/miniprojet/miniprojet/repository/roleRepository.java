package com.miniprojet.miniprojet.repository;

import com.miniprojet.miniprojet.entity.ERole;
import com.miniprojet.miniprojet.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface roleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(ERole role);
}

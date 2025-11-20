package com.miniprojet.miniprojet.repository;

import com.miniprojet.miniprojet.entity.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface eventRepository extends JpaRepository<Evenement,Integer> {
}

package com.miniprojet.miniprojet.repository;

import com.miniprojet.miniprojet.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur,Integer> {
    Utilisateur findByEmail(String email);//il retourne un user c'est comme select from where email =email
    boolean existsByIdAndRoles_Id(Integer userId, Integer roleId);

}

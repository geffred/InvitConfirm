package com.invitation.confirmation.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.invitation.confirmation.Entity.Invite;

import java.util.List;
import java.util.Optional;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Long> {

    /**
     * Recherche un invité par nom et prénom (insensible à la casse avec trim)
     */
    @Query("SELECT i FROM Invite i WHERE LOWER(TRIM(i.nom)) = LOWER(TRIM(:nom)) AND LOWER(TRIM(i.prenom)) = LOWER(TRIM(:prenom))")
    Optional<Invite> findByNomAndPrenom(@Param("nom") String nom, @Param("prenom") String prenom);

    /**
     * Trouve tous les invités confirmés
     */
    List<Invite> findByConfirmeTrue();

    /**
     * Trouve tous les invités non confirmés
     */
    List<Invite> findByConfirmeFalse();

    /**
     * Compte le nombre d'invités confirmés
     */
    long countByConfirmeTrue();

    /**
     * Recherche par nom ou prénom (pour la fonctionnalité de recherche admin)
     */
    @Query("SELECT i FROM Invite i WHERE LOWER(TRIM(i.nom)) LIKE LOWER(CONCAT('%', TRIM(:search), '%')) OR LOWER(TRIM(i.prenom)) LIKE LOWER(CONCAT('%', TRIM(:search), '%'))")
    List<Invite> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(@Param("search") String search);
}
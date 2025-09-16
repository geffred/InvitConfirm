package com.invitation.confirmation.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invites")
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false)
    private boolean confirme = false;

    @Column
    private LocalDateTime dateConfirmation;

    // Constructeurs
    public Invite() {
    }

    public Invite(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.confirme = false;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public boolean isConfirme() {
        return confirme;
    }

    public void setConfirme(boolean confirme) {
        this.confirme = confirme;
    }

    public LocalDateTime getDateConfirmation() {
        return dateConfirmation;
    }

    public void setDateConfirmation(LocalDateTime dateConfirmation) {
        this.dateConfirmation = dateConfirmation;
    }

    // Méthodes utilitaires
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public String getStatutConfirmation() {
        return confirme ? "Confirmé ✅" : "Non confirmé ❌";
    }

    @Override
    public String toString() {
        return "Invite{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", confirme=" + confirme +
                ", dateConfirmation=" + dateConfirmation +
                '}';
    }
}
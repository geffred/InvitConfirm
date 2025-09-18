package com.invitation.confirmation.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.invitation.confirmation.Entity.Invite;
import com.invitation.confirmation.Repository.InviteRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InviteService {

    @Autowired
    private InviteRepository inviteRepository;

    /**
     * Recherche un invité par nom et prénom (insensible à la casse)
     */
    public Optional<Invite> findByNomAndPrenom(String nom, String prenom) {
        if (nom == null || prenom == null || nom.trim().isEmpty() || prenom.trim().isEmpty()) {
            return Optional.empty();
        }

        // Normalisation des données d'entrée
        String nomNormalise = nom.trim();
        String prenomNormalise = prenom.trim();

        return inviteRepository.findByNomAndPrenom(nomNormalise, prenomNormalise);
    }

    /**
     * Confirme la présence d'un invité avec gestion d'erreurs améliorée
     */
    public ConfirmationResult confirmerPresence(String nom, String prenom) {
        // Validation des paramètres d'entrée
        if (nom == null || prenom == null || nom.trim().isEmpty() || prenom.trim().isEmpty()) {
            return new ConfirmationResult(false, "Veuillez remplir tous les champs obligatoires", null);
        }

        try {
            Optional<Invite> inviteOpt = findByNomAndPrenom(nom, prenom);

            if (inviteOpt.isEmpty()) {
                return new ConfirmationResult(false,
                        "Aucun invité trouvé avec le nom '" + nom.trim() + " " + prenom.trim() + "'. " +
                                "Veuillez vérifier l'orthographe exacte de vos nom et prénom.",
                        null);
            }

            Invite invite = inviteOpt.get();

            if (invite.isConfirme()) {
                String dateConfirmation = invite.getDateConfirmation() != null
                        ? invite.getDateConfirmation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm"))
                        : "date inconnue";

                return new ConfirmationResult(false,
                        "Votre présence a déjà été confirmée le " + dateConfirmation + ". " +
                                "Vous n'avez pas besoin de confirmer à nouveau.",
                        invite);
            }

            // Confirmer l'invité
            invite.setConfirme(true);
            invite.setDateConfirmation(LocalDateTime.now());
            inviteRepository.save(invite);

            return new ConfirmationResult(true,
                    "Confirmation réussie ! Merci " + invite.getNomComplet() + ", votre présence est enregistrée.",
                    invite);

        } catch (Exception e) {
            // Log de l'erreur pour le debug
            System.err.println("Erreur lors de la confirmation pour " + nom + " " + prenom + ": " + e.getMessage());
            e.printStackTrace();

            return new ConfirmationResult(false,
                    "Une erreur technique s'est produite lors de l'enregistrement. Veuillez réessayer dans quelques instants.",
                    null);
        }
    }

    /**
     * Récupère tous les invités
     */
    public List<Invite> getAllInvites() {
        return inviteRepository.findAll();
    }

    /**
     * Récupère les invités confirmés
     */
    public List<Invite> getInvitesConfirmes() {
        return inviteRepository.findByConfirmeTrue();
    }

    /**
     * Récupère les invités non confirmés
     */
    public List<Invite> getInvitesNonConfirmes() {
        return inviteRepository.findByConfirmeFalse();
    }

    /**
     * Statistiques
     */
    public InviteStats getStats() {
        long total = inviteRepository.count();
        long confirmes = inviteRepository.countByConfirmeTrue();
        return new InviteStats(total, confirmes, total - confirmes);
    }

    /**
     * Ajouter un nouvel invité
     */
    public Invite ajouterInvite(String nom, String prenom) {
        if (nom == null || prenom == null || nom.trim().isEmpty() || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom et le prénom sont obligatoires");
        }

        Invite invite = new Invite(nom.trim(), prenom.trim());
        return inviteRepository.save(invite);
    }

    /**
     * Récupère un invité par son ID
     */
    public Optional<Invite> findById(Long id) {
        return inviteRepository.findById(id);
    }

    /**
     * Crée un nouvel invité
     */
    public Invite createInvite(Invite invite) {
        if (invite.getNom() != null)
            invite.setNom(invite.getNom().trim());
        if (invite.getPrenom() != null)
            invite.setPrenom(invite.getPrenom().trim());
        return inviteRepository.save(invite);
    }

    /**
     * Met à jour un invité existant avec gestion correcte des statuts
     */
    public Invite updateInvite(Long id, Invite inviteDetails) {
        Invite invite = inviteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invité non trouvé avec l'ID: " + id));

        // Nettoyage des données
        invite.setNom(inviteDetails.getNom().trim());
        invite.setPrenom(inviteDetails.getPrenom().trim());

        // Gestion du statut de confirmation
        boolean ancienStatut = invite.isConfirme();
        boolean nouveauStatut = inviteDetails.isConfirme();

        invite.setConfirme(nouveauStatut);

        if (!ancienStatut && nouveauStatut) {
            // Passer de non confirmé à confirmé
            invite.setDateConfirmation(LocalDateTime.now());
        } else if (ancienStatut && !nouveauStatut) {
            // Passer de confirmé à non confirmé
            invite.setDateConfirmation(null);
        }
        // Si on reste dans le même état, on garde la date existante

        return inviteRepository.save(invite);
    }

    /**
     * Supprime un invité
     */
    public void deleteInvite(Long id) {
        Invite invite = inviteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invité non trouvé avec l'ID: " + id));
        inviteRepository.delete(invite);
    }

    /**
     * Recherche des invités (pour l'admin)
     */
    public List<Invite> searchInvites(String searchQuery) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            return getAllInvites();
        }
        return inviteRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(searchQuery.trim());
    }

    /**
     * Classe pour le résultat de confirmation
     */
    public static class ConfirmationResult {
        private final boolean success;
        private final String message;
        private final Invite invite;

        public ConfirmationResult(boolean success, String message, Invite invite) {
            this.success = success;
            this.message = message;
            this.invite = invite;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Invite getInvite() {
            return invite;
        }
    }

    /**
     * Classe pour les statistiques
     */
    public static class InviteStats {
        private final long total;
        private final long confirmes;
        private final long nonConfirmes;

        public InviteStats(long total, long confirmes, long nonConfirmes) {
            this.total = total;
            this.confirmes = confirmes;
            this.nonConfirmes = nonConfirmes;
        }

        public long getTotal() {
            return total;
        }

        public long getConfirmes() {
            return confirmes;
        }

        public long getNonConfirmes() {
            return nonConfirmes;
        }

        public double getPourcentageConfirmes() {
            return total > 0 ? (double) confirmes / total * 100 : 0;
        }
    }
}
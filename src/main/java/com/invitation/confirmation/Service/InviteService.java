package com.invitation.confirmation.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.invitation.confirmation.Entity.Invite;
import com.invitation.confirmation.Repository.InviteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InviteService {

    @Autowired
    private InviteRepository inviteRepository;

    /**
     * Recherche un invité par nom et prénom
     */
    public Optional<Invite> findByNomAndPrenom(String nom, String prenom) {
        if (nom == null || prenom == null || nom.trim().isEmpty() || prenom.trim().isEmpty()) {
            return Optional.empty();
        }
        return inviteRepository.findByNomAndPrenom(nom.trim(), prenom.trim());
    }

    /**
     * Confirme la présence d'un invité
     */
    public ConfirmationResult confirmerPresence(String nom, String prenom) {
        Optional<Invite> inviteOpt = findByNomAndPrenom(nom, prenom);

        if (inviteOpt.isEmpty()) {
            return new ConfirmationResult(false, "Nom/prénom introuvable ❌", null);
        }

        Invite invite = inviteOpt.get();

        if (invite.isConfirme()) {
            return new ConfirmationResult(false,
                    "Déjà confirmé ✅ le " + invite.getDateConfirmation().format(
                            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")),
                    invite);
        }

        // Confirmer l'invité
        invite.setConfirme(true);
        invite.setDateConfirmation(LocalDateTime.now());
        inviteRepository.save(invite);

        return new ConfirmationResult(true,
                "Confirmation réussie ! ✅ Merci " + invite.getNomComplet(),
                invite);
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
     * Ajouter un nouvel invité (pour les tests)
     */
    public Invite ajouterInvite(String nom, String prenom) {
        Invite invite = new Invite(nom, prenom);
        return inviteRepository.save(invite);
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
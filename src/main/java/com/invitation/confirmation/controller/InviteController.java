package com.invitation.confirmation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.invitation.confirmation.Service.InviteService;

@Controller
@RequestMapping("/invitation")
public class InviteController {

    @Autowired
    private InviteService inviteService;

    /**
     * Page d'accueil des invitations (accessible via QR Code)
     */
    @GetMapping("/valider")
    public String showConfirmationForm(Model model) {
        return "confirmationForm";
    }

    /**
     * Traitement du formulaire de confirmation avec validation robuste
     */
    @PostMapping("/confirmer")
    public String confirmerPresence(
            @RequestParam(value = "nom", required = false) String nom,
            @RequestParam(value = "prenom", required = false) String prenom,
            Model model) {

        try {
            // Validation côté serveur (sécurité)
            if (nom == null || prenom == null ||
                    nom.trim().isEmpty() || prenom.trim().isEmpty()) {

                model.addAttribute("success", false);
                model.addAttribute("message", "Veuillez remplir tous les champs obligatoires (nom et prénom)");
                model.addAttribute("invite", null);
                return "confirmationResult";
            }

            // Validation de la longueur
            if (nom.trim().length() > 100 || prenom.trim().length() > 100) {
                model.addAttribute("success", false);
                model.addAttribute("message", "Le nom et le prénom ne peuvent pas dépasser 100 caractères");
                model.addAttribute("invite", null);
                return "confirmationResult";
            }

            // Traitement de la confirmation
            InviteService.ConfirmationResult result = inviteService.confirmerPresence(nom, prenom);

            model.addAttribute("success", result.isSuccess());
            model.addAttribute("message", result.getMessage());
            model.addAttribute("invite", result.getInvite());

            // Log pour le debugging (optionnel en développement)
            if (result.isSuccess()) {
                System.out.println("✓ Confirmation réussie pour: " + nom.trim() + " " + prenom.trim());
            } else {
                System.out.println("✗ Échec de confirmation pour: " + nom.trim() + " " + prenom.trim() +
                        " - Raison: " + result.getMessage());
            }

        } catch (Exception e) {
            // Gestion d'erreur globale pour éviter les crashes
            System.err.println("Erreur inattendue dans confirmerPresence: " + e.getMessage());
            e.printStackTrace();

            model.addAttribute("success", false);
            model.addAttribute("message", "Une erreur technique inattendue s'est produite. " +
                    "Veuillez réessayer dans quelques instants ou contacter l'organisateur.");
            model.addAttribute("invite", null);
        }

        return "confirmationResult";
    }
}
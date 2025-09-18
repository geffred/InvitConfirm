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
     * Traitement du formulaire de confirmation
     */
    @PostMapping("/confirmer")
    public String confirmerPresence(
            @RequestParam("nom") String nom,
            @RequestParam("prenom") String prenom,
            Model model) {

        InviteService.ConfirmationResult result = inviteService.confirmerPresence(nom, prenom);

        model.addAttribute("success", result.isSuccess());
        model.addAttribute("message", result.getMessage());
        model.addAttribute("invite", result.getInvite());

        return "confirmationResult";
    }
}
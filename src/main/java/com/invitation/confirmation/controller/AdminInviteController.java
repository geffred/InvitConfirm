package com.invitation.confirmation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.invitation.confirmation.Entity.Invite;
import com.invitation.confirmation.Service.InviteService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/invites")
public class AdminInviteController {

    @Autowired
    private InviteService inviteService;

    /**
     * Page d'administration des invités
     */
    @GetMapping
    public String adminInvites(Model model,
            @RequestParam(value = "search", required = false) String searchQuery) {
        List<Invite> invites;

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            // Filtrer les invités en fonction de la recherche
            invites = inviteService.getAllInvites().stream()
                    .filter(invite -> invite.getNom().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            invite.getPrenom().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList());
        } else {
            invites = inviteService.getAllInvites();
        }

        InviteService.InviteStats stats = inviteService.getStats();

        model.addAttribute("invites", invites);
        model.addAttribute("stats", stats);
        model.addAttribute("invite", new Invite());
        model.addAttribute("searchQuery", searchQuery);

        return "admin/invites";
    }

    /**
     * Ajouter un nouvel invité OU modifier un invité existant
     * Cette méthode gère les deux cas selon la présence d'un ID
     */
    @PostMapping
    public String addInvite(@ModelAttribute Invite invite, RedirectAttributes redirectAttributes) {
        try {
            inviteService.createInvite(invite);
            redirectAttributes.addFlashAttribute("successMessage", "Invité ajouté avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'ajout: " + e.getMessage());
        }
        return "redirect:/admin/invites";
    }

    /**
     * NOUVELLE ROUTE : Modifier un invité existant - correspond au formulaire HTML
     */
    @PostMapping("/{id}")
    public String updateInvite(@PathVariable Long id,
            @RequestParam("nom") String nom,
            @RequestParam("prenom") String prenom,
            @RequestParam("confirme") boolean confirme,
            RedirectAttributes redirectAttributes) {
        try {
            // Validation des données
            if (nom == null || prenom == null || nom.trim().isEmpty() || prenom.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Le nom et le prénom sont obligatoires");
                return "redirect:/admin/invites";
            }

            // Créer un objet Invite avec les nouvelles données
            Invite inviteDetails = new Invite(nom.trim(), prenom.trim());
            inviteDetails.setConfirme(confirme);

            // Mettre à jour l'invité
            inviteService.updateInvite(id, inviteDetails);
            redirectAttributes.addFlashAttribute("successMessage", "Invité modifié avec succès!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la modification: " + e.getMessage());
        }
        return "redirect:/admin/invites";
    }

    /**
     * Afficher le formulaire d'édition
     */
    @GetMapping("/edit/{id}")
    public String editInviteForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<Invite> inviteOpt = inviteService.findById(id);
            if (inviteOpt.isPresent()) {
                Invite invite = inviteOpt.get();
                List<Invite> invites = inviteService.getAllInvites();
                InviteService.InviteStats stats = inviteService.getStats();

                model.addAttribute("invites", invites);
                model.addAttribute("stats", stats);
                model.addAttribute("editingInvite", invite);
                model.addAttribute("invite", new Invite());

                return "admin/invites";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Invité non trouvé");
                return "redirect:/admin/invites";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur: " + e.getMessage());
            return "redirect:/admin/invites";
        }
    }

    /**
     * ANCIENNE ROUTE : Garder pour compatibilité si utilisée ailleurs
     */
    @PostMapping("/update/{id}")
    public String updateInviteOld(@PathVariable Long id, @ModelAttribute Invite inviteDetails,
            RedirectAttributes redirectAttributes) {
        try {
            inviteService.updateInvite(id, inviteDetails);
            redirectAttributes.addFlashAttribute("successMessage", "Invité modifié avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la modification: " + e.getMessage());
        }
        return "redirect:/admin/invites";
    }

    /**
     * Supprimer un invité
     */
    @PostMapping("/delete/{id}")
    public String deleteInvite(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            inviteService.deleteInvite(id);
            redirectAttributes.addFlashAttribute("successMessage", "Invité supprimé avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/admin/invites";
    }

    /**
     * Récupérer les détails d'un invité en JSON
     */
    @GetMapping("/details/{id}")
    @ResponseBody
    public Invite getInviteDetails(@PathVariable Long id) {
        return inviteService.findById(id)
                .orElseThrow(() -> new RuntimeException("Invité non trouvé avec l'ID: " + id));
    }
}
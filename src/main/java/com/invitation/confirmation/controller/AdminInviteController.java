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

        return "admin/invites"; // Assurez-vous que ce template existe
    }

    /**
     * Ajouter un nouvel invité
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
     * Afficher le formulaire d'édition
     */
    @GetMapping("/edit/{id}")
    public String editInviteForm(@PathVariable Long id, Model model) {
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
                return "redirect:/admin/invites?error=Invité non trouvé";
            }
        } catch (Exception e) {
            return "redirect:/admin/invites?error=" + e.getMessage();
        }
    }

    /**
     * Modifier un invité existant
     */
    @PostMapping("/update/{id}")
    public String updateInvite(@PathVariable Long id, @ModelAttribute Invite inviteDetails,
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
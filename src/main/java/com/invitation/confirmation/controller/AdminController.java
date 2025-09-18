package com.invitation.confirmation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.invitation.confirmation.Entity.Invite;
import com.invitation.confirmation.Service.InviteService;
import com.invitation.confirmation.Service.PdfService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private InviteService inviteService;

    @Autowired
    private PdfService pdfService;

    /**
     * Page principale d'administration - Liste des invités
     * 
     * @GetMapping("/invites")
     * public String listInvites(Model model) {
     * List<Invite> invites = inviteService.getAllInvites();
     * InviteService.InviteStats stats = inviteService.getStats();
     * 
     * model.addAttribute("invites", invites);
     * model.addAttribute("stats", stats);
     * 
     * return "admin/invites";
     * }
     */

    /**
     * Export PDF de la liste des invités
     */
    @GetMapping("/invites/export-pdf")
    public ResponseEntity<byte[]> exportPdf() {
        try {
            byte[] pdfBytes = pdfService.exportInvitesPdf();

            String filename = "invites-" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmm")) +
                    ".pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            // En cas d'erreur, retourner une réponse d'erreur
            return ResponseEntity.internalServerError().build();
        }
    }
}
package com.invitation.confirmation.Service;

import com.invitation.confirmation.Entity.Invite;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfService {

    @Autowired
    private InviteService inviteService;

    public byte[] exportInvitesPdf() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 54, 36); // marges
        PdfWriter.getInstance(document, baos);

        document.open();

        // Fontes
        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);

        // Titre
        Paragraph title = new Paragraph("Liste des Invités", fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Date d'export
        Paragraph exportDate = new Paragraph(
                "Exporté le : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")),
                FontFactory.getFont(FontFactory.HELVETICA, 10));
        exportDate.setAlignment(Element.ALIGN_RIGHT);
        exportDate.setSpacingAfter(20);
        document.add(exportDate);

        // Statistiques
        InviteService.InviteStats stats = inviteService.getStats();
        Paragraph statsP = new Paragraph(
                "Total des invités : " + stats.getTotal() +
                        "\nConfirmés : " + stats.getConfirmes() +
                        "\nNon confirmés : " + stats.getNonConfirmes() +
                        "\nTaux de confirmation : " + String.format("%.1f%%", stats.getPourcentageConfirmes()),
                fontNormal);
        statsP.setSpacingAfter(20);
        document.add(statsP);

        // Table des invités
        List<Invite> invites = inviteService.getAllInvites();

        if (!invites.isEmpty()) {
            Paragraph tableTitle = new Paragraph("Détail des invités", fontBold);
            tableTitle.setSpacingAfter(10);
            document.add(tableTitle);

            // Table avec 4 colonnes
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);

            // En-têtes
            table.addCell(new Phrase("Nom", fontBold));
            table.addCell(new Phrase("Prénom", fontBold));
            table.addCell(new Phrase("Statut", fontBold));
            table.addCell(new Phrase("Date confirmation", fontBold));

            // Format date
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Lignes
            for (Invite invite : invites) {
                table.addCell(new Phrase(invite.getNom(), fontNormal));
                table.addCell(new Phrase(invite.getPrenom(), fontNormal));
                table.addCell(new Phrase(invite.getStatutConfirmation(), fontNormal));

                String dateConfirmation = invite.getDateConfirmation() != null
                        ? invite.getDateConfirmation().format(dateFormatter)
                        : "-";
                table.addCell(new Phrase(dateConfirmation, fontNormal));
            }

            document.add(table);
        } else {
            Paragraph noData = new Paragraph("Aucun invité trouvé.", fontNormal);
            noData.setAlignment(Element.ALIGN_CENTER);
            noData.setSpacingBefore(20);
            document.add(noData);
        }

        // Pied de page
        Paragraph footer = new Paragraph("Généré par le Système de Gestion des Invitations",
                FontFactory.getFont(FontFactory.HELVETICA, 8));
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(30);
        document.add(footer);

        document.close();
        return baos.toByteArray();
    }
}

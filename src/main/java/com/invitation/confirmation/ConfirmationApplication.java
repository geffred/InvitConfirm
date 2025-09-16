package com.invitation.confirmation;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.invitation.confirmation.Service.InviteService;

@SpringBootApplication
public class ConfirmationApplication implements CommandLineRunner {

	private final InviteService inviteService;

	public ConfirmationApplication(InviteService inviteService) {
		this.inviteService = inviteService;
	}

	public static void main(String[] args) {
		SpringApplication.run(ConfirmationApplication.class, args);
	}

	/**
	 * Initialisation de données de test
	 */
	@Override
	public void run(String... args) throws Exception {
		// Ajouter quelques invités de test si la base est vide
		if (inviteService.getAllInvites().isEmpty()) {
			inviteService.ajouterInvite("Dupont", "Jean");
			inviteService.ajouterInvite("Martin", "Marie");
			inviteService.ajouterInvite("Durand", "Pierre");
			inviteService.ajouterInvite("Bernard", "Sophie");
			inviteService.ajouterInvite("Moreau", "Lucas");

			System.out.println("✅ Données de test créées :");
			System.out.println("- Jean Dupont");
			System.out.println("- Marie Martin");
			System.out.println("- Pierre Durand");
			System.out.println("- Sophie Bernard");
			System.out.println("- Lucas Moreau");
		}
	}
}

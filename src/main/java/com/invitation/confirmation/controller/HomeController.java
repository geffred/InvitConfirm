package com.invitation.confirmation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    /**
     * Page d'accueil
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }

    /**
     * Page de connexion personnalisée
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect");
        }
        return "login";
    }
}
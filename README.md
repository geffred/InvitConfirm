# 🎯 Système de Gestion des Invitations

Une application web moderne développée en **Spring Boot 3.x** avec **Thymeleaf** pour gérer les invitations et confirmations de présence via QR Code unique.

## 🚀 Fonctionnalités

### ✅ Gestion des invités
- Entité `Invite` avec nom, prénom, statut de confirmation et date
- Repository JPA avec recherche par nom/prénom
- Données de test automatiquement créées au démarrage

### 📱 Confirmation via QR Code
- **Un seul QR Code** pour tous les invités pointant vers `/invitation/valider`
- Formulaire Thymeleaf responsive pour saisir nom/prénom
- Validation automatique et mise à jour du statut en base
- Messages de confirmation avec animations

### 🔒 Interface d'administration sécurisée
- Authentification requise pour accéder à `/admin/**`
- Dashboard avec statistiques en temps réel
- Liste complète des invités avec filtrage
- Export PDF de la liste des invités

### 📊 Export PDF
- Génération automatique avec **iText 7**
- Rapport complet avec statistiques
- Téléchargement direct depuis l'interface admin

## 🛠️ Stack technique

- **Backend**: Spring Boot 3.2, Spring MVC, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, Bootstrap 5.3, Font Awesome 6.4
- **Base de données**: MySQL 8.0
- **ORM**: Hibernate
- **PDF**: openpdf
- **Build**: Maven

## 📋 Prérequis

- Java 17+
- MySQL 8.0+
- Maven 3.6+

## 🚀 Installation et démarrage

### 1. Préparer la base de données

```sql
CREATE DATABASE invitations_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Configurer l'application

Modifier `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/invitations_db
spring.datasource.username=root
spring.datasource.password=votre_mot_de_passe
```

### 3. Compiler et démarrer

```bash
mvn clean install
mvn spring-boot:run
```

L'application sera accessible sur http://localhost:8080

## 🔐 Accès

### Interface publique
- **Page d'accueil**: http://localhost:8080/
- **Confirmation d'invitation**: http://localhost:8080/invitation/valider

### Interface d'administration
- **URL**: http://localhost:8080/admin/invites
- **Identifiants par défaut**:
  - Login: `admin`
  - Mot de passe: `admin123`

## 📱 Utilisation

### Pour les invités

1. **Scanner le QR Code** généré (pointe vers `/invitation/valider`)
2. **Saisir nom et prénom** exactement comme sur l'invitation
3. **Confirmer la présence** - le statut est mis à jour instantanément

### Pour les administrateurs

1. **Se connecter** avec les identifiants admin
2. **Consulter le dashboard** avec statistiques en temps réel
3. **Filtrer et rechercher** dans la liste des invités
4. **Exporter en PDF** la liste complète

## 🎨 Fonctionnalités de l'interface

### Design moderne
- Gradient backgrounds avec effet glassmorphisme
- Animations CSS fluides
- Interface responsive (mobile-friendly)
- Thème sombre/coloré professionnel

### Expérience utilisateur
- Animations de confirmation avec confettis
- Messages d'erreur/succès contextuels
- Recherche en temps réel
- Actualisation automatique des données

### Sécurité
- Authentification Spring Security
- Validation côté serveur
- Protection CSRF (configurable)
- Sessions sécurisées

## 📊 Structure du projet

```
src/main/java/com/example/invitations/
├── entity/
│   └── Invite.java                 # Entité JPA
├── repository/
│   └── InviteRepository.java       # Repository avec méthodes custom
├── service/
│   ├── InviteService.java          # Logique métier
│   └── PdfService.java             # Génération PDF
├── controller/
│   ├── InviteController.java       # Contrôleur invitations
│   ├── AdminController.java        # Contrôleur admin
│   └── HomeController.java         # Pages générales
├── config/
│   └── SecurityConfig.java         # Configuration sécurité
└── InvitationManagerApplication.java

src/static/css/
├── admin.css
├── confirmation.css
├── confirmationResult.css
├── login.css
├── style.css

src/main/resources/templates/
├── index.html                      # Page d'accueil
├── login.html                      # Page de connexion
├── confirmationForm.html           # Formulaire confirmation
├── confirmationResult.html         # Résultat confirmation
└── admin/
├    └── invites.html               # Interface administration
└── fragments/
    └── header.html 
    └── footer.html                 # header && footer
```

## 🔧 Configuration personnalisée

### Modifier les identifiants admin

Dans `SecurityConfig.java` :
```java
UserDetails admin = User.builder()
    .username("votre_login")
    .password(passwordEncoder().encode("votre_password"))
    .roles("ADMIN")
    .build();
```

### Ajouter des invités par défaut

Dans `InvitationManagerApplication.java`, méthode `run()` :
```java
inviteService.ajouterInvite("Nom", "Prénom");
```

## 📈 Fonctionnalités avancées

### Statistiques en temps réel
- Nombre total d'invités
- Confirmations en temps réel
- Taux de confirmation automatique
- Barre de progression visuelle

### Export PDF personnalisable
- En-tête avec date d'export
- Statistiques complètes
- Table détaillée avec tous les invités
- Format professionnel prêt à imprimer

### Recherche et filtrage
- Recherche instantanée dans la liste
- Filtrage par statut
- Tri par colonnes
- Interface responsive

## 🚀 Améliorations possibles

- [ ] **QR Code Generator**: Génération automatique du QR Code
- [ ] **Notifications email**: Confirmation automatique par email
- [ ] **Import CSV**: Import en masse des invités
- [ ] **API REST**: Endpoints pour applications mobiles
- [ ] **Multi-événements**: Gestion de plusieurs événements
- [ ] **Dashboard analytics**: Graphiques de suivi avancés
- [ ] **Thèmes personnalisables**: Interface admin customizable
- [ ] **Backup automatique**: Sauvegarde des données

## 🐛 Résolution de problèmes

### Erreur de connexion MySQL
- Vérifier que MySQL est démarré
- Contrôler les identifiants dans `application.properties`
- S'assurer que la base `invitations_db` existe

### Problème d'authentification
- Les identifiants par défaut sont `admin` / `admin123`
- Vérifier la configuration dans `SecurityConfig.java`

### Erreur de génération PDF
- Vérifier que la dépendance iText est bien incluse
- Contrôler les permissions d'écriture

## 📝 Licence

Ce projet est sous licence MIT - voir le fichier LICENSE pour plus de détails.

## 👥 Contribution

Les contributions sont les bienvenues ! N'hésitez pas à ouvrir une issue ou proposer une pull request.

---

**Développé avec ❤️ en Spring Boot** 🍃
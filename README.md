
# Projet Android + PHP : Gestion des étudiants

Ce document décrit l’architecture du projet, son installation et son utilisation.  
Il résume exactement ce que tu as mis en place : une application Android qui communique avec un serveur PHP (WAMP) pour gérer une liste d’étudiants (ajout, affichage, modification, suppression).

---

## 1. Objectif du projet

- Créer une application Android permettant :
  - d’ajouter un étudiant (nom, prénom, ville, sexe),
  - d’afficher la liste des étudiants enregistrés,
  - de modifier les informations d’un étudiant,
  - de supprimer un étudiant après confirmation.
- Les données sont stockées dans une base MySQL, via un serveur PHP (WAMP/XAMPP).
- La communication se fait en HTTP entre le téléphone Android et le PC (même réseau Wi‑Fi).

---

## 2. Architecture générale

### 2.1. Côté serveur (PHP / WAMP)

- Serveur : WAMP ou XAMPP installé sur le PC.
- Base de données MySQL contenant une table `etudiant` avec les colonnes :
  - identifiant (clé primaire, auto‑incrément),
  - nom,
  - prénom,
  - ville,
  - sexe.
- Dossier de projet PHP dans le répertoire `www` ou `www/html` de WAMP, par exemple :


  `C:/wamp64/www/projet/`

Organisation recommandée des sous‑dossiers :

- `config/` : connexion à la base de données.
- `classes/` : définition de la classe métier « Étudiant ».
- `dao/` : accès aux données (CRUD) sur la table `etudiant`.
- `service/` : logique métier (appel des méthodes DAO).
- `ws/` : « web services » exposés à l’application Android (scripts PHP appelés par HTTP).

Scripts principaux côté `ws` :

- Script d’ajout d’un étudiant.
- Script de récupération de la liste des étudiants (retourne du JSON).
- Script de suppression d’un étudiant (à partir de son identifiant).
- Script de mise à jour d’un étudiant (nom, prénom, ville, sexe).

Tous ces scripts renvoient des réponses au format JSON pour être facilement interprétés par l’application Android.




**Affichage Postman**



<img width="1447" height="941" alt="Capture d&#39;écran 2025-11-24 112341" src="https://github.com/user-attachments/assets/e7075ac5-75a3-4792-a9c0-a39f12b70724" />





### 2.2. Côté application Android

Principaux éléments :

- Une activité d’ajout d’étudiant (écran avec formulaire) :
  - champs de saisie pour nom et prénom,
  - liste déroulante pour la ville,
  - boutons radio pour le sexe,
  - bouton pour valider l’ajout,
  - bouton pour ouvrir la liste des étudiants.

- Une activité de liste des étudiants :
  - utilisation d’un `RecyclerView` pour afficher les étudiants,
  - un adaptateur personnalisé pour relier les objets « Étudiant » à l’affichage,
  - clic sur un élément pour afficher un popup avec les options :
    - modifier l’étudiant,
    - supprimer l’étudiant.

- Une classe « Étudiant » (côté Android) correspondant exactement aux champs de la table MySQL.

- Un client HTTP basé sur la bibliothèque Volley pour envoyer les requêtes au serveur (POST/GET) et recevoir les réponses JSON.

---

## 3. Configuration réseau

Pour que le téléphone Android accède au serveur PHP sur le PC :

1. Le PC et le téléphone doivent être **sur le même réseau Wi‑Fi**.
2. Récupérer l’adresse IP locale du PC (par exemple `192.168.0.172`).
3. Utiliser cette adresse IP dans l’URL des web services, par exemple :  
   `http://192.168.0.115/projet/ws/...`
4. Vérifier que le pare‑feu Windows autorise le serveur Apache en entrée sur les réseaux privés et publics.
5. Tester les URL des scripts PHP directement dans un navigateur du PC puis du téléphone pour vérifier l’accessibilité.

---

## 4. Configuration Android

### 4.1. Permissions et sécurité réseau

Dans la configuration de l’application Android :

- Déclarer l’autorisation d’accès à Internet.
- Autoriser le trafic HTTP « en clair » (non chiffré) pour l’adresse locale du serveur (utile en développement).
- Ajouter, si nécessaire, une configuration de sécurité réseau pour accepter les requêtes vers l’adresse IP locale.

### 4.2. Constante d’URL de base

Dans les activités qui communiquent avec le serveur, définir une constante pour l’URL de base correspondant à l’IP du PC, afin de pouvoir la modifier facilement si l’adresse change.

Exemple d’organisation logique (sans code) :

- Une constante représentant l’adresse de base (IP du PC + chemin du projet).
- Des URL construites à partir de cette base pour chaque opération : ajout, liste, suppression, mise à jour.

---




![WhatsApp Image 2025-11-24 at 23 46 56](https://github.com/user-attachments/assets/34a90bda-1727-4431-908f-27a503e88593)


<img width="1000" height="235" alt="image" src="https://github.com/user-attachments/assets/17baa5b2-f528-46d6-b281-3f8dd536bab9" />




## 5. Fonctionnement des principales fonctionnalités

### 5.1. Ajout d’un étudiant

1. L’utilisateur remplit le formulaire (nom, prénom, ville, sexe).
2. Lorsqu’il clique sur le bouton « Ajouter » :
   - l’application envoie une requête HTTP vers le script PHP d’ajout,
   - les données sont transmises en paramètres,
   - le script PHP insère la ligne dans la base de données,
   - le script renvoie une réponse (par exemple la liste mise à jour ou un message de succès).
3. L’application affiche un message de confirmation et peut, si besoin, rafraîchir la liste.

### 5.2. Affichage de la liste des étudiants

1. L’activité « liste » envoie une requête HTTP (type GET) vers le script PHP de récupération.
2. Le script PHP renvoie toutes les lignes de la table `etudiant` au format JSON.
3. L’application convertit ce JSON en objets « Étudiant ».
4. L’adaptateur du `RecyclerView` affiche chaque étudiant dans un item de la liste.

### 5.3. Modification d’un étudiant

1. L’utilisateur appuie sur un étudiant dans la liste.
2. Une boîte de dialogue propose « Modifier » ou « Supprimer ».
3. Si l’utilisateur choisit « Modifier » :
   - une autre boîte de dialogue s’affiche avec les champs pré‑remplis (nom, prénom, ville, sexe),
   - l’utilisateur peut corriger les informations,
   - en validant, l’application envoie une requête HTTP vers le script PHP de mise à jour, avec l’identifiant de l’étudiant et les nouvelles valeurs,
   - le serveur met à jour la ligne correspondante dans la base de données,
   - l’application met à jour l’élément dans la liste sans recharger toute l’activité.

### 5.4. Suppression d’un étudiant

1. L’utilisateur appuie sur un étudiant et choisit l’option « Supprimer ».
2. Une boîte de dialogue de confirmation s’affiche.
3. En cas de confirmation :
   - une requête HTTP est envoyée au script PHP de suppression avec l’identifiant de l’étudiant,
   - le serveur supprime la ligne dans la table,
   - l’application retire l’élément de la liste (`RecyclerView`) immédiatement pour un affichage dynamique.

---

## 6. Lancement du projet

1. Démarrer WAMP/XAMPP et s’assurer que :
   - Apache est en cours d’exécution,
   - MySQL est en cours d’exécution,
   - la base de données et la table `etudiant` sont créées,
   - les scripts PHP sont accessibles via le navigateur avec l’IP locale.

2. Ouvrir le projet Android dans Android Studio :
   - vérifier que l’URL de base des web services pointe vers la bonne adresse IP,
   - connecter un appareil réel Android sur le même Wi‑Fi, ou utiliser un émulateur correctement configuré.

3. Lancer l’application :
   - tester l’ajout d’un étudiant,
   - tester l’affichage de la liste,
   - tester la modification et la suppression,
   - vérifier dans phpMyAdmin que les changements apparaissent bien dans la table.

---

## 7. Démonstration Vidéo :





https://github.com/user-attachments/assets/05ae9db1-81fd-426f-8a88-f142b5d4c63e








# Citronix

## Description
Citronix est une application de gestion développée pour répondre aux besoins des fermiers de citrons. Elle offre des fonctionnalités permettant de gérer facilement les fermes, champs, arbres, récoltes et ventes. Cette solution vise à optimiser la productivité et à simplifier la gestion quotidienne grâce à une interface intuitive.

---

## Fonctionnalités Principales

### Gestion des Fermes
- Création, modification et consultation des informations d'une ferme (nom, localisation, superficie, etc.).
- Recherche multicritère pour trouver rapidement les fermes.

### Gestion des Champs
- Association de champs à une ferme avec contrôle des superficies.
- Vérification automatique : la somme des superficies des champs doit être inférieure à celle de la ferme.

### Gestion des Arbres
- Suivi des arbres : date de plantation, âge, et champ d'appartenance.
- Calcul automatique de l'âge et de la productivité :
    - Jeunes arbres (< 3 ans) : 2,5 kg/saison.
    - Arbres matures (3-10 ans) : 12 kg/saison.
    - Arbres vieux (> 10 ans) : 20 kg/saison.

### Gestion des Récoltes
- Suivi des récoltes saisonnières avec date et quantité totale.
- Détails des récoltes : enregistrement des quantités par arbre.

### Gestion des Ventes
- Enregistrement des ventes avec client, prix unitaire, et quantité.
- Calcul automatique du revenu généré.

---

## Contraintes Techniques et Métier
- **Superficie minimale des champs :** 0,1 hectare (1 000 m²).
- **Superficie maximale des champs :** 50% de la superficie totale de la ferme.
- **Durée de vie des arbres :** un arbre n'est productif que jusqu'à 20 ans.
- **Récolte saisonnière unique :** une récolte par champ et par saison.

---

## Architecture Technique
- **Backend :** Spring Boot (API REST).
- **Architecture en couches :** Controller, Service, Repository, Entity.
- **Validation :** Annotations Spring pour la validation des données.
- **Transformation des données :** Utilisation de MapStruct pour les conversions entre entités, DTOs, et View Models.
- **Tests :** JUnit et Mockito pour les tests unitaires.

---

## Instructions pour le Déploiement
1. **Configuration de l'environnement :**
    - Java 17 ou version ultérieure.
    - Maven pour la gestion des dépendances.
    - Une base de données relationnelle (MySQL, PostgreSQL, etc.).

2. **Installation :**
    - Clonez le dépôt :
      ```bash
      git clone <URL_DU_DEPOT>
      ```
    - Naviguez dans le dossier :
      ```bash
      cd citronix
      ```
    - Compilez le projet :
      ```bash
      mvn clean install
      ```

3. **Exécution :**
    - Lancer l'application :
      ```bash
      mvn spring-boot:run
      ```

4. **Accès à l'API :**
    - Documentation API : Swagger disponible à `http://localhost:8080/swagger-ui.html`.

---


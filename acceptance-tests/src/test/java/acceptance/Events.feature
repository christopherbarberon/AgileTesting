#noinspection NonAsciiCharacters
Feature: Page événements
  
  Scenario: Access depuis la page d'accueil
    Given je suis sur la homepage
    When je clique sur le menu burger
    And je clique sur "Événements"
    Then l'url de la page est "https://www.tesla.com/fr_FR/events"

  Scenario: Vérification du nombre d'événements affichés
    Given je suis sur la page "events"
    # je clique sur voir tous les événements sinon il n'y en a pas assez
    And je clique sur "Voir tous les événements"
    Then il y a 15 événements affichés

  Scenario: Vérification des liens
    Given je suis sur la page "events"
    Then il existe un lien "Voir tous les événements"
    Then il existe un lien "Afficher plus"

  Scenario: Vérification du formulaire
    Given je suis sur la page "events"
    Then il existe les champs suivants
      | Prénom | Nom | E-mail | Téléphone | Region | Code postal | Recevoir les News Tesla |
    And il existe un bouton "Suivant"

  Scenario: Vérification de remplissage du formulaire
    Given je suis sur la page "events"
    When je remplis le formulaire avec les valeurs suivantes
      | Jhon       | Prénom      |
      | Jhon       | Nom         |
      | 0612345678 | Téléphone   |
      | 95000      | Code postal |
    And je valide le formulaire
    Then un message "obligatoire" apparait pour le champ "E-mail"

  Scenario: Vérification de la recherche
    Given je suis sur la page "events"
    When je remplis le formulaire avec les valeurs suivantes
      | Londres | à proximité de |
    And j'attend que ".pac-container" soit visible
    And je valide le formulaire
    Then le premier événement est au "Royaume-Uni"


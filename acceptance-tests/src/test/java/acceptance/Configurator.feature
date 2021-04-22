Feature: Configuration model S

  Scenario: Vérification bouton commander
    Given je suis sur la page "models"
    When je clique sur "Commander"
    Then l'url de la page est "https://www.tesla.com/fr_fr/models/design#overview"

  Scenario: Verification prix par défaut
    Given je suis sur la page "models/design"
    Then le prix affiché est un prix "LOA" de "847"€ par mois
    
  Scenario: Vérification prix qui change
    Given je suis sur la page "models/design"
    When je clique sur "Plaid+"
    Then le prix affiché est un prix "LOA" de "1 559"€ par mois
    And l'économie de carburant est de "108"€ par mois
    And le prix total est de "149 990"€

    When je clique sur "Plaid"
    Then le prix affiché est un prix "LOA" de "1 203"€ par mois
    And l'économie de carburant est de "108"€ par mois
    And le prix total est de "119 990"€

  Scenario: Ajout d'option
    Given je suis sur la page "models/design"
    When j'ajoute l'option de conduite totalement automatique
    Then le prix total est de "97 490"€

  Scenario: findus
    Given je suis sur la page "models/design"
    When je clique sur le logo tesla
    And je clique sur "France"
    And je scroll jusqu'en bas de la page
    And je clique sur "Localisations"
    Then je suis sur la page "findus/list"

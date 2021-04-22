#noinspection NonAsciiCharacters
Feature: Configuration model 3

  Scenario Outline: Différentes valeurs
    Given je suis sur la page "model3"
    When je clique sur l'onglet "Caractéristiques"
    And je clique sur le lien "<gamme>"
    Then le poids est de "<poids>" kg
    And l'accélération est de "<accélération>"s
    And l'autonomie est de "<autonomie>" km
    Examples:
      | gamme                | poids | accélération | autonomie |
      | Performance          | 1 844 | 3.3          | 567       |
      | Grande Autonomie AWD | 1 844 | 4,4          | 580       |
      | Standard Plus        | 1 745 | 5,6          | 448       |
    # les virgules sont différents sur la page entre performance et les autres
    # j'ai pour ça que j'ai mis un . pour performance et des , pour les autres ici
#noinspection NonAsciiCharacters
Feature: Fonctionnalités de ma page d'accueil

	Scenario: Vérification du titre et de la description
		Given je suis sur la homepage
		Then le titre doit être "Voitures électriques, énergie solaire et propre | Tesla France"
		And la description doit être "Tesla accélère la transition mondiale vers une énergie durable en proposant des véhicules électriques, des panneaux solaires et des solutions intégrées d'énergie renouvelable pour les particuliers et les entreprises."

	Scenario: Vérification des punchlines
		Given je suis sur la homepage
		Then il existe les punchlines suivantes
			| Model 3 | Model S | Model X | Model Y | Systèmes d'énergie solaire et Powerwalls |

	Scenario: Vérifications des liens du haut
		Given je suis sur la homepage
		Then le menu du haut contient les liens suivants
			| Model S   | https://www.tesla.com/fr_fr/models    |
			| Model 3   | https://www.tesla.com/fr_fr/model3    |
			| Model X   | https://www.tesla.com/fr_fr/modelx    |
			| Model Y   | https://www.tesla.com/fr_fr/modely    |
			| Powerwall | https://www.tesla.com/fr_fr/powerwall |
			| Recharge  | https://www.tesla.com/fr_fr/charging  |

	Scenario: Verification du menu burger
		Given je suis sur la homepage
		When je clique sur le menu burger
		Then le burger menu contient les liens suivants
			| Véhicules Disponibles  |
			| Véhicules D'occasion   |
			| Reprise                |
			| Cybertruck             |
			| Roadster               |
			| Énergie                |
			| Essais                 |
			| Flottes et entreprises |
			| Nous trouver           |
			| Événements             |
			| Assistance             |

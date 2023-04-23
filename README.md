DELTARM - Application de controle du bras robotique

Ce projet est une application qui permet de contrôler un bras robotique Delta via Bluetooth. Il permet de piloter les mouvements du bras via une interface graphique.

Pour commencer

Cette application Android permet de contrôler le robot Delta par Bluetooth. Elle permet d'envoyer des commandes pour faire bouger le robot dans différentes directions, ainsi que pour contrôler l'activation et la désactivation de l'electroaimant afin d'attraper des objets.

L'interface utilisateur est simple et intuitive. Elle est composée de boutons pour les différentes directions de mouvement et d'un bouton pour contrôler l'electroaimant. Les boutons de direction permettent de contrôler le mouvement dans la direction correspondante : vers le haut, vers le bas, vers la droite ou vers la gauche et monter / descendre


Pré-requis :

	Android Studio
	Java 18
	SDK 19 (Android 4.4 Kitkat Minimum)
	cable de liaison entre appareil android et ordinateur

Installation de l'application via apk :
	 
	Ouvrir depuis votre appareil android le fichier app-release.apk dasn le dossier : 	AndroidApp\app\release\app-release.apk
	Installer le fichier .apk
    Avant d’ouvrir l’application DELTARM activer le bluetooth de l’appareil

	
Installation de l'application via android studio :

	Clonez le dépôt git sur votre ordinateur : git clone https://github.com/AdeshCannou/BrasRobotAPP.git
	Ouvrez Android Studio et importez le projet en sélectionnant le dossier cloné.
	Attendez que les dépendances soient téléchargées.
	Connectez votre téléphone Android à votre ordinateur à l'aide d'un câble USB.
	Activez le mode développeur et l'option de débogage USB sur votre téléphone.
	Cliquez sur le bouton "Run" dans Android Studio pour compiler et exécuter l'application sur votre téléphone.
	
Comment l'utiliser :

1. Selectionner dasn la liste le module bluetooth du bras robotique

L'application se connecte au robot Delta par Bluetooth. Elle utilise la librairie Android Bluetooth pour établir la connexion. Elle recherche les appareils Bluetooth disponibles et affiche la liste des appareils appairés dans un menu déroulant.

2. Controlez le bras :)

Une fois connecté, l'application envoie des commandes de mouvement au robot Delta en fonction des boutons cliqués. Les commandes sont envoyées sous forme de chaînes de caractères via Bluetooth. Les commandes sont :

"F" pour avancer
"B" pour reculer
"L" pour déplacer à gauche
"R" pour déplacer à droite

"U" pour monter
"D" pour descendre

"M" pour activer l'electroaiment
"W" pour désactiver l'electroaiment

Le bouton pour contrôler l'electroaimant du robot est un bouton à bascule. Lorsque le bouton est activé, l'electroaiment s'active. Lorsqu'il est désactivé, il se désactive.

Les boutons de mouvement sont sensibles au toucher. Si l'utilisateur appuie et maintient enfoncé un bouton, le robot continuera de se déplacer dans cette direction jusqu'à ce que l'utilisateur relâche le bouton.


Ce projet a été développé par :

Adesh CANNOU
Mamady BARADJI

License :

Ce projet est sous licence MIT.

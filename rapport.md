# Rapport de bonus

## Téléchargement automatique des fichier `.osm` et `.hgt`

### Amélioration du point de vue de l'utilisateur
L'utilisateur n'a plus besoin de télécharger et sélectionner lui-même les données dont le programme a besoin pour générer la carte. Il lui suffit désormais de sélectionner les coordonnées du cadre de la carte. Ce qui augmente la flexibilité de la génération de carte. L'utilisateur gagne ainsi beaucoup de temps.

Cependant, du à la disponibilité limitée des fichiers HGT, cette fonctionnalité est limitée à la Suisse.

### Mise en oeuvre Java
Le téléchargement des données OSM, ce fait par une requête en utilisant l'Overpass API. Elle est générée dans la classe `QueryGenerator` du paquetage `ch.epfl.imhof.utility` à partir d'une expression régulière qui récupère les coordonées des boundaries. Une requête pour l'Overpass API ressemble à http://overpass-api.de/api/interpreter?data=(node(minLat, minLon, maxLat, maxLon);<;>;);out; Cette requête est utilisée comme flux entrant dans `OSMMapReader` dont un constructeur particulier a du être généré pour l'occasion.


## Ajout des noms de villes, parcs, étendues d'eau et forêts

### Amélioration du point de vue de l'utilisateur

### Mise en oeuvre Java



## Ajout de textures à la carte

### Amélioration du point de vue de l'utilisateur

### Mise en oeuvre Java



## Ajout d'une interface graphique

### Amélioration du point de vue de l'utilisateur
L'utilisateur peut dorénavant utiliser une interface graphique pour l'entrée de données. Le fait d'avoir une telle interface facilite grandement la tâche pour l'utilisateur: premièrement, il n'a plus besoin de connaître l'ordre exact des arguments, puisque le formulaire possède des étiquettes et des descriptions (*labels* et *tooltips*) qui le guident dans la démarche. En d'autres termes, l'utilisateur n'a plus besoin de lire de la documentation ou de connaître le projet pour s'en servir. De plus, un utilisateur qui ne serait pas habitué à la ligne de commande ne sera pas dépaysé de son environnement habituel.

L'interface graphique fournit aussi un moyen plus simple de sélectionner les fichiers, surtout si l'utilisateur veut sélectionner des fichiers en dehors du dossier où est placé le projet (en ligne de commande, il aurait fallu rentrer le chemin absolu, ce qui est fastidieux).

Finalement, cette interface produit aussi une sortie verbeuse qui permet à l'utilisateur de suivre le progrès de l'exécution du programme.

### Mise en oeuvre Java
L'interface graphique est décrite dans la classe `GraphicalUserInterface` du paquetage `ch.epfl.imhof.gui`. Cette classe utilise Swing pour créer une fenêtre contenant de nombreux composants, dont des `JLabel`, `JTextField`, `JTextArea`, `JButton`...

Les boutons qui permettent de sélectionner et sauvegarder un fichier sont définis par la classe abstraite `FileButton`. Un `FileButton` est intimement lié à un `JTextArea` (qui doit être donné dans le constructeur), et le fait de sélectionner un fichier dans le `JFileChooser` créé en cliquant sur le bouton remplit automatiquement le `JTextArea` lié au bouton. Les classes héritant de `FileButton` (à savoir `OpenFileButton` et `SaveFileButton`) doivent redéfinir la méthode `showDialog()` pour montrer une fenêtre d'ouverture ou de sauvegarde de fichier. Ils peuvent éventuellement aussi redéfinir une extension par défaut qui sera rajoutée aux noms des fichiers si besoin en est.

Le code pour créer une carte (défini dans `MapMaker`) tourne dans un fil d'exéction séparé afin de ne pas bloquer l'interface pendant la création de carte.

L'ajout de texte à la console de l'interface se fait par la méthode `System.out.println`. Pour cela, un nouvel `OutputStream` est défini par la classe `GUIConsoleOutputStream` qui ajoute le texte directement au `JTextArea` donnée en argument à son constructeur. La classe `GraphicalUserInterface` utilise cette classe pour rediriger l'output de `System.out`.


## Détection et lecture de fichiers `.osm` et `.osm.gz`

### Amélioration du point de vue de l'utilisateur
Cet ajout permet de lire des fichiers `.osm` non gzippés. La classe `MapMaker` peut détecter si un fichier est au format `.gz` ou non, et le lire dans les deux cas, alors qu'elle était uniquement capable de lire les `.osm.gz` avant.

### Mise en oeuvre en Java
La détection du format du fichier se fait à l'aide d'une expression régulière (*regex*) qui retourne `true` si le nom du ficher se termine en `.gz`, et false autrement. 

Cette valeur est ensuite donnée comme second argument à `OSMMapReader.readOSMFile()`, et le fichier ne sera donc dézippé que s'il est nécessaire de le faire.


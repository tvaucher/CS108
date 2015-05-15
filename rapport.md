# Rapport de bonus

## Détection d'erreur dans le nombre d'arguments

### Amélioration du point de vue de l'utilisateur
Cet ajout permet à l'utilisateur de voir si le nombre d'arguments donnés est erroné; la classe `Main` s'attend à recevoir exactement 8 arguments, et donnera un message d'erreur s'il en est autrement.

### Mise en oeuvre Java
La détection du nombre d'arguments se fait à l'aide de de `args.length`. Si cette valeur n'est pas égale à 8, alors on lance une `IllegalArgumentException`.

## Détection et lecture de fichiers `.osm` et `.osm.gz`

### Amélioration du point de vue de l'utilisateur
Cet ajout permet de lire des fichiers `.osm` non gzippés. La classe `Main` peut détecter si un fichier est au format `.gz` ou non, et le lire dans les deux cas, alors qu'elle était uniquement capable de lire les `.osm.gz` avant.

### Mise en oeuvre en Java
La détection du format du fichier se fait à l'aide d'une expression régulière (*regex*) qui retourne `true` si le nom du ficher se termine en `.gz`, et false autrement. 

Cette valeur est ensuite donnée comme second argument à `OSMMapReader.readOSMFile()`, et le fichier ne sera donc dézippé que s'il est nécessaire de le faire.


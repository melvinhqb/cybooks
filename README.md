
# Projet CY Books

CY Books est une application graphique destinée aux bibliothécaires pour gérer une bibliothèque, ses usagers et les emprunts. Elle utilise le framework JavaFX pour l'interface utilisateur et une base de données MySQL pour la gestion des données.

L'application est utilisable au clavier et à la souris et offre les fonctionnalités suivantes :

- Gestion des utilisateurs
- Gestion des livres
- Gestion des emprunts

## Prérequis

Avant de commencer, assurez-vous d'avoir les éléments suivants installés sur votre machine :

- [Java Development Kit (JDK) 8 ou supérieur](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [MySQL](https://dev.mysql.com/downloads/installer/)
- [Maven](https://maven.apache.org/install.html)
## Dépendances

Ce projet utilise les dépendances suivantes :

- [JavaFX](https://openjfx.io/openjfx-docs/)
- [MySQL Connector/J](https://mvnrepository.com/artifact/mysql/mysql-connector-java/8.0.27)
## Installation et Configuration

### Cloner le projet

```bash
git clone https://github.com/melvinhqb/cybooks.git
```

### Modifier les variables d'environnement
Assurez-vous que votre configuration de connexion correspond aux identifiants de connexion MySQL.
Par défaut, le code se connecte en utilisant `root` sans mot de passe, mais vous pouvez modifier cela dans le fichier `DatabaseConnection.java` :

```java
private final static String URL = "jdbc:mysql://localhost:3306/";
private final static String DB_NAME = "cybooks";
private final static String USERNAME = "root";
private final static String PASSWORD = "";
```

### Configuration
Edit Configuration:
    (S'il n'y a rien, cliquez sur + en haut à gauche puis sur Application)
    Main Class: fr.cyu.cybooks.view.CyBooksApplication
    Cliquer sur Modifier les options -> ajouter VM options -> --module-path "path\to\your\javafx\sdk\lib" --add-modules javafx.controls,javafx.fxml
    Cliquer sur Apply
    Cliquer sur Ok
Fichier -> Structure du projet
        -> Librairies
                     ->Cliquez sur le + (Java)
                     -> Ajoutez la lib de votre javafx SDK (doit obligatoirement avoir javafx.controls et javafx.fxml)
        ->Modules
                    ->Dependances
                    ->Vérifiez que vous avez mysql connector (lien plus haut dans ### Dépendances)

Fichier -> Paramètres
        ->Languages et Frameworks (cliquez sur JavaFx)
        ->Mettez le path to SceneBuilder.exe

pom.xml -> Vérifiez que toutes les dépendances sont bien configurées avec la bonne version (>8)

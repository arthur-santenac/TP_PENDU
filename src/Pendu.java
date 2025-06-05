import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import java.util.List;

import java.util.Arrays;
import java.io.File;
import java.util.ArrayList;


/**
 * Vue du jeu du pendu
 */
public class Pendu extends Application {
    /**
     * modèle du jeu
     **/
    private MotMystere modelePendu;
    /**
     * Liste qui contient les images du jeu
     */
    private ArrayList<Image> lesImages;
    /**
     * Liste qui contient les noms des niveaux
     */    
    public List<String> niveaux;

    // les différents contrôles qui seront mis à jour ou consultés pour l'affichage
    /**
     * le dessin du pendu
     */
    private ImageView dessin;
    /**
     * le mot à trouver avec les lettres déjà trouvé
     */
    private Label motCrypte;
    /**
     * la barre de progression qui indique le nombre de tentatives
     */
    private ProgressBar pg;
    /**
     * le clavier qui sera géré par une classe à implémenter
     */
    private Clavier clavier;
    /**
     * le text qui indique le niveau de difficulté
     */
    private Label leNiveau;
    /**
     * le chronomètre qui sera géré par une clasee à implémenter
     */
    private Chronometre chrono;
    /**
     * le panel Central qui pourra être modifié selon le mode (accueil ou jeu)
     */
    private BorderPane panelCentral;
    /**
     * le bouton Paramètre / Engrenage
     */
    private ToggleButton boutonParametres;
    /**
     * le bouton Accueil / Maison
     */    
    private ToggleButton boutonMaison;
    /**
     * le bouton qui permet de (lancer ou relancer une partie
     */ 
    private Button bJouer;

    private Stage stage;

    private ToggleGroup groupeNiveaux;
    /**
     * initialise les attributs (créer le modèle, charge les images, crée le chrono ...)
     */
    @Override
    public void init() {
        this.modelePendu = new MotMystere("/usr/share/dict/french", 3, 10, MotMystere.FACILE, 10);
        this.lesImages = new ArrayList<Image>();
        this.chargerImages("./img");
        
    }

    /**
     * @return  le graphe de scène de la vue à partir de methodes précédantes
     */
    private Scene laScene(){
        BorderPane fenetre = new BorderPane();
        fenetre.setTop(this.titre());
        fenetre.setCenter(this.fenetreAccueil());
        return new Scene(fenetre, 800, 1000);
    }

    /**
     * @return le panel contenant le titre du jeu
     */
    private Pane titre(){

        
        Label titre = new Label("Jeu du Pendu");
        titre.setPadding(new Insets(15));
        titre.setFont(Font.font("Arial", FontWeight.NORMAL, 32));


        ToggleGroup toggleGroup = new ToggleGroup();
        this.boutonMaison = new ToggleButton("");
        this.boutonParametres = new ToggleButton("");
        ToggleButton button = new ToggleButton("");
        this.boutonMaison.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/home.png"), 35, 35, false, false)));
        this.boutonParametres.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/parametres.png"), 35, 35, false, false)));
        button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/info.png"), 35, 35, false, false)));
        this.boutonMaison.setToggleGroup(toggleGroup);
        this.boutonParametres.setToggleGroup(toggleGroup);
        button.setToggleGroup(toggleGroup);

        button.setOnAction(new ControleurInfos(this));

        BorderPane banniere = new BorderPane();
        banniere.setStyle("-fx-background-color:rgb(226, 229, 250);");
        banniere.setPrefSize(200,80);
        banniere.setLeft(titre);
        HBox inside = new HBox();
        banniere.setRight(inside);
        inside.getChildren().addAll(this.boutonMaison, this.boutonParametres, button);
        inside.setPadding(new Insets(15));
        return banniere;
    }

    /**
     * @return le panel du chronomètre
     */
    private TitledPane leChrono(){
        TitledPane res = new TitledPane("Chronomètre", this.chrono);
        res.setCollapsible(false);
        return res;
    }

    /**
     * @return la fenêtre de jeu avec le mot crypté, l'image, la barre
     *         de progression et le clavier
     */
    private Pane fenetreJeu(){

        this.dessin = new ImageView(this.lesImages.get(0));
        this.dessin.setFitWidth(450);
        this.dessin.setPreserveRatio(true);

        BorderPane imageBox = new BorderPane(this.dessin);
        imageBox.setPadding(new Insets(10));


        Button nouveauMotBtn = new Button("Nouveau mot");
        nouveauMotBtn.setOnAction(new ControleurLancerPartie(modelePendu, this, this.stage));

        VBox panneauDroite = new VBox(20, leNiveau, leChrono(), nouveauMotBtn);
        panneauDroite.setAlignment(Pos.TOP_CENTER);
        panneauDroite.setPadding(new Insets(20));

        this.pg = new ProgressBar(0);

        this.clavier = new Clavier("ABCDEFGHIJKLMNOPQRSTUVWXYZ-", new ControleurLettres(modelePendu, this));

        VBox clavierBox = new VBox(10, this.pg, this.clavier);
        clavierBox.setAlignment(Pos.CENTER);

        VBox centre = new VBox(15);
        centre.getChildren().addAll(this.motCrypte, imageBox, clavierBox);
        centre.setAlignment(Pos.TOP_CENTER);
        centre.setPadding(new Insets(20));

        BorderPane res = new BorderPane();
        res.setCenter(centre);
        res.setRight(panneauDroite);

        return res;
    
        
    }

    /**
     * @return la fenêtre d'accueil sur laquelle on peut choisir les paramètres de jeu
     */
    private Pane fenetreAccueil() {
    this.bJouer = new Button("Lancer une partie");
    this.bJouer.setOnAction(e -> {
        if (this.groupeNiveaux.getSelectedToggle() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Il faut choisir un niveau de difficulté");
            alert.showAndWait();
        } else {
            this.lancePartie(this.stage);
        }
    });

    VBox vboxCenter = new VBox(5);

    this.groupeNiveaux = new ToggleGroup();


    RadioButton radioB1 = new RadioButton("facile");
    RadioButton radioB2 = new RadioButton("moyen");
    RadioButton radioB3 = new RadioButton("difficile");
    RadioButton radioB4 = new RadioButton("expert");

    radioB1.setToggleGroup(this.groupeNiveaux);
    radioB2.setToggleGroup(this.groupeNiveaux);
    radioB3.setToggleGroup(this.groupeNiveaux);
    radioB4.setToggleGroup(this.groupeNiveaux);

    // Connecte chaque RadioButton au contrôleur
    radioB1.setOnAction(new ControleurNiveau(modelePendu, this));
    radioB2.setOnAction(new ControleurNiveau(modelePendu, this));
    radioB3.setOnAction(new ControleurNiveau(modelePendu, this));
    radioB4.setOnAction(new ControleurNiveau(modelePendu, this));

    vboxCenter.getChildren().addAll(radioB1, radioB2, radioB3, radioB4);
    vboxCenter.setPadding(new Insets(10));

    TitledPane titrePanneau = new TitledPane("Niveau", vboxCenter);
    titrePanneau.setCollapsible(false);

    VBox res = new VBox(20, this.bJouer, titrePanneau);
    res.setPadding(new Insets(20));

    return res;
}

    /**
     * charge les images à afficher en fonction des erreurs
     * @param repertoire répertoire où se trouvent les images
     */
    private void chargerImages(String repertoire){
        for (int i=0; i<this.modelePendu.getNbErreursMax()+1; i++){
            File file = new File(repertoire+"/pendu"+i+".png");
            System.out.println(file.toURI().toString());
            this.lesImages.add(new Image(file.toURI().toString()));
        }
    }

    public void modeAccueil(){

    }
    
    public Scene modeJeu(){
        BorderPane fenetre = new BorderPane();
        fenetre.setTop(this.titre());
        fenetre.setCenter(this.fenetreJeu());
        return new Scene(fenetre, 800, 1000);
    }
    
    public void modeParametres(){
        // A implémenter
    }

    /** lance une partie */
    public void lancePartie(Stage stage){

        this.modelePendu.setMotATrouver();

        this.motCrypte = new Label(modelePendu.getMotCrypte());
        this.motCrypte.setStyle("-fx-font-size: 24px; -fx-font-weight: normal;");

        this.chrono = new Chronometre();
        this.chrono.start();

        this.dessin = new ImageView(this.lesImages.get(0));
        this.dessin.setFitWidth(450);
        this.dessin.setPreserveRatio(true);

        this.pg = new ProgressBar(0);

        this.clavier = new Clavier("ABCDEFGHIJKLMNOPQRSTUVWXYZ-", new ControleurLettres(modelePendu, this));


        stage.setScene(this.modeJeu());
        stage.show();
    }

    public void setLeNiveau(String niveau) {
        this.leNiveau = new Label(niveau);
    }

    /**
     * raffraichit l'affichage selon les données du modèle
     */
    public void majAffichage(){
        // A implementer
    }

    /**
     * accesseur du chronomètre (pour les controleur du jeu)
     * @return le chronomètre du jeu
     */
    public Chronometre getChrono(){
        return this.chrono;
    }

    public Alert popUpPartieEnCours(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"La partie est en cours!\n Etes-vous sûr de l'interrompre ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Attention");
        return alert;
    }
        
    public Alert popUpReglesDuJeu(){
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("règles du jeu");
        alert.setContentText("Cliquer sur les lettres pour voir si elle sont \n dans le mot a trouver");
        return alert;
    }
    
    public Alert popUpMessageGagne(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("gagné");
        alert.setContentText("Bravo c'est super");
        return alert;
    }
    
    public Alert popUpMessagePerdu(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("PERDU !!!!!!!!");
        alert.setContentText("Dommage, le bon mot était : " + modelePendu.getMotATrouve());
        return alert;
    }

    public MotMystere getModele() {
    return this.modelePendu;
    }

    public Label getMotCrypte() {
        return this.motCrypte;
    }

    public ImageView getDessin() {
        return this.dessin;
    }

    public ProgressBar getProgressBar() {
        return this.pg;
    }

    public Clavier getClavier() {
        return this.clavier;
    }

    public Image getImage(int i) {
        return this.lesImages.get(i);
    }

    /**
     * créer le graphe de scène et lance le jeu
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        stage.setScene(this.laScene());
        this.modeAccueil();
        stage.show();
    }

    /**
     * Programme principal
     * @param args inutilisé
     */
    public static void main(String[] args) {
        launch(args);
    }


}

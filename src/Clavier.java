import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Circle ;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.plaf.synth.Region;

/**
 * Génère la vue d'un clavier et associe le contrôleur aux touches
 * le choix ici est d'un faire un héritié d'un TilePane
 */
public class Clavier extends TilePane{
    /**
     * il est conseillé de stocker les touches dans un ArrayList
     */
    private List<Button> clavier;

    /**
     * constructeur du clavier
     * @param touches une chaine de caractères qui contient les lettres à mettre sur les touches
     * @param actionTouches le contrôleur des touches
     * @param tailleLigne nombre de touches par ligne
     */
    public Clavier(String touches, EventHandler<ActionEvent> actionTouches) {
        this.clavier = new ArrayList<>();

        this.setHgap(8);
        this.setVgap(8);
        this.setPadding(new Insets(10));
  
        this.setPrefColumns(8);

        this.setAlignment(Pos.CENTER);

        for (int i=0;i<touches.length();i++) {
            Button button = new Button(String.valueOf(touches.charAt(i)));
            button.setMinWidth(40);
            button.setMinHeight(40);
            button.setStyle("-fx-background-radius: 20px; -fx-font-size: 14px;");

            button.setOnAction(actionTouches);
            this.clavier.add(button);
            this.getChildren().add(button);
        }
    }

    /**
     * permet de désactiver certaines touches du clavier (et active les autres)
     * @param touchesDesactivees une chaine de caractères contenant la liste des touches désactivées
     */
    public void desactiveTouches(){
        for (Button b : this.clavier) {
            b.setDisable(true);
        }
    }
}

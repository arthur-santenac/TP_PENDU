import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


/**
 * Permet de gérer un Text associé à une Timeline pour afficher un temps écoulé
 */
public class Chronometre extends Text{
    /**
     * timeline qui va gérer le temps
     */
    private Timeline timeline;
    /**
     * la fenêtre de temps
     */
    private KeyFrame keyFrame;
    /**
     * le contrôleur associé au chronomètre
     */
    private ControleurChronometre actionTemps;
    
    /**
     * temps écoulé en millisecondes
     */
    private long tempsEcoule = 0;

    /**
     * Constructeur permettant de créer le chronomètre
     * avec un label initialisé à "0:0:0"
     * Ce constructeur créer la Timeline, la KeyFrame et le contrôleur
     */
    public Chronometre(){
        this.setText("0:00");
        this.setFont(new Font("Arial", 16));
        this.setTextAlignment(TextAlignment.CENTER);

        // Créer la Timeline pour mettre à jour toutes les secondes
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            tempsEcoule += 1000;
            setTime(tempsEcoule);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * Permet au controleur de mettre à jour le text
     * la durée est affichée sous la forme m:s
     * @param tempsMillisec la durée depuis à afficher
     */
    public void setTime(long tempsMillisec){
        long totalSeconds = tempsMillisec / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        String temps = String.format("%d:%02d", minutes, seconds);
        this.setText(temps);
    }

    /**
     * Permet de démarrer le chronomètre
     */
    public void start(){
        timeline.play();
    }

    /**
     * Permet d'arrêter le chronomètre
     */
    public void stop(){
        timeline.stop();
    }

    /**
     * Permet de remettre le chronomètre à 0
     */
    public void resetTime(){
        timeline.stop();
        tempsEcoule = 0;
        this.setText("0:00");
    }
}

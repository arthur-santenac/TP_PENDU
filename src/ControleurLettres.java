import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

/**
 * Controleur du clavier
 */
public class ControleurLettres implements EventHandler<ActionEvent> {

    /**
     * modèle du jeu
     */
    private MotMystere modelePendu;
    /**
     * vue du jeu
     */
    private Pendu vuePendu;

    /**
     * @param modelePendu modèle du jeu
     * @param vuePendu vue du jeu
     */
    ControleurLettres(MotMystere modelePendu, Pendu vuePendu){
        this.modelePendu = modelePendu;
        this.vuePendu = vuePendu;
    }

    /**
     * Actions à effectuer lors du clic sur une touche du clavier
     * Il faut donc: Essayer la lettre, mettre à jour l'affichage et vérifier si la partie est finie
     * @param actionEvent l'événement
     */
    @Override
    public void handle(ActionEvent actionEvent) {

        Button bouton = (Button) actionEvent.getSource();
        String lettre = bouton.getText();
        bouton.setDisable(true);

        int bonneLettre = vuePendu.getModele().essaiLettre(lettre.charAt(0));

        vuePendu.getMotCrypte().setText(vuePendu.getModele().getMotCrypte());

        if (bonneLettre == 0) {

            if (vuePendu.getModele().getNbEssais() >= vuePendu.getModele().getNbErreursMax()) {
                vuePendu.getDessin().setImage(vuePendu.getImage(vuePendu.getModele().getNbErreursMax()));
                vuePendu.getProgressBar().setProgress(1.0);
                vuePendu.getChrono().stop();
                vuePendu.getClavier().desactiveTouches();
                vuePendu.popUpMessagePerdu().showAndWait();
                return;
            }

            int nbErreurs = vuePendu.getModele().getNbEssais();
            ImageView pendu = vuePendu.getDessin();
            pendu.setImage(vuePendu.getImage(nbErreurs));

            double progression = (double) nbErreurs / vuePendu.getModele().getNbErreursMax();
            vuePendu.getProgressBar().setProgress(progression);
        }

        if (vuePendu.getModele().gagne()) {
            vuePendu.getChrono().stop();
            vuePendu.popUpMessageGagne().showAndWait();
        }
    }
}

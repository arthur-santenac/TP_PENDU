import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;

/**
 * Contrôleur des radio boutons gérant le niveau
 */
public class ControleurNiveau implements EventHandler<ActionEvent> {

    private MotMystere modelePendu;
    private Pendu vue;

    public ControleurNiveau(MotMystere modelePendu, Pendu vue) {
        this.modelePendu = modelePendu;
        this.vue = vue;
    }

    @Override
    public void handle(ActionEvent event) {
        RadioButton bouton = (RadioButton) event.getSource();
        String niveauChoisi = bouton.getText();

        switch (niveauChoisi) {
            case "Facile":
                modelePendu.setNiveau(MotMystere.FACILE);
                break;
            case "Médium":
                modelePendu.setNiveau(MotMystere.MOYEN);
                break;
            case "Difficile":
                modelePendu.setNiveau(MotMystere.DIFFICILE);
                break;
            case "Expert":
                modelePendu.setNiveau(MotMystere.EXPERT);
                break;
        }

        vue.setLeNiveau("niveau " + niveauChoisi);

    }
}
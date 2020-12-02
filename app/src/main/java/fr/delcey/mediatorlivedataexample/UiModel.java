package fr.delcey.mediatorlivedataexample;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * Le UiModel sert à représenter l'état de la vue.<br/>
 *
 * Il contient toutes les données "dynamiques" de la vue ( = les données qui peuvent changer pendant l'utilisation de l'app)
 */
public class UiModel {

    private final String numberToDisplay;
    private final String sentence;

    public UiModel(String numberToDisplay, String sentence) {
        this.numberToDisplay = numberToDisplay;
        this.sentence = sentence;
    }

    public String getNumberToDisplay() {
        return numberToDisplay;
    }

    public String getSentence() {
        return sentence;
    }

    @NonNull
    @Override
    public String toString() {
        return "UiModel{" +
            "numberToDisplay='" + numberToDisplay + '\'' +
            ", sentence='" + sentence + '\'' +
            '}';
    }

    // Les fonctions equals(), hashcode() et sont utiles pour les tests unitaires (dans les assertions)
    // et peuvent être autogénérées avec Alt + Inser
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UiModel uiModel = (UiModel) o;
        return Objects.equals(numberToDisplay, uiModel.numberToDisplay) &&
            Objects.equals(sentence, uiModel.sentence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberToDisplay, sentence);
    }
}

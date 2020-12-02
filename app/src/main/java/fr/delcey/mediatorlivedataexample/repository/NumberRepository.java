package fr.delcey.mediatorlivedataexample.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Les repository servent à "stocker" ou "accéder" à la donnée. Ca peut être une donnée sur internet, en local,
 * en base de donnée, liée au téléphone (GPS ou Bluetooth par exemple), etc.
 *
 * Ici on gère juste une donnée "volatile" non persistée (au redémarrage de l'app les données seront perdues).
 */
public class NumberRepository {

    private final MutableLiveData<Integer> numberLiveData = new MutableLiveData<>();

    // On récupère la LiveData qui représente un nombre. Ce nombre n'est pas encore présent  dans la LiveData tant que l'utilisateur n'a pas
    // cliqué sur le bouton "Random". Il va donc falloir "réagir" dans le ViewModel à l'émission de cette nouvelle valeur (via un "addSource).
    public LiveData<Integer> getNumberLiveData() {
        return numberLiveData;
    }

    // On peut avoir plusieurs méthodes publiques dans un Repository qui permettent de modifier la donnée ou d'accéder au(x) LiveData(s)
    public void addToNumber(int toAdd) {
        Integer previousValue = numberLiveData.getValue();

        if (previousValue == null) {
            previousValue = 0;
        }

        Integer newValue = previousValue + toAdd;

        // On change la valeur de la LiveData, cela va activer l'Observer qui regarde cette LiveData (comme dans le MainViewModel) et
        // provoquer l'appel de la méthode "combine" du MainViewModel avec les nouvelles valeurs
        numberLiveData.setValue(newValue);
    }

    public void multiplyNumber(int factor) {
        Integer previousValue = numberLiveData.getValue();

        if (previousValue == null) {
            previousValue = 0;
        }

        Integer newValue = previousValue * factor;

        // On change la valeur de la LiveData, cela va activer l'Observer qui regarde cette LiveData (comme dans le MainViewModel) et
        // provoquer l'appel de la méthode "combine" du MainViewModel avec les nouvelles valeurs
        numberLiveData.setValue(newValue);
    }
}

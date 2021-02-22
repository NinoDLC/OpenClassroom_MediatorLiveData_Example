package fr.delcey.mediatorlivedataexample.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Random;

/**
 * Les repository servent à "stocker" ou "accéder" à la donnée. Ca peut être une donnée sur internet, en local,
 * en base de donnée, liée au téléphone (GPS ou Bluetooth par exemple), etc.
 *
 * Ici on gère juste une donnée "volatile" non persistée (au redémarrage de l'app les données seront perdues).
 */
public class RandomRepository {

    private final MutableLiveData<Integer> randomNumberLiveData = new MutableLiveData<>();

    public RandomRepository() {
        // On peut donner une valeur initiale à la LiveData si besoin (uncomment)
        // randomNumberLiveData.setValue(1);
    }

    // On récupère la LiveData qui représente un nombre. Contrairement à l'autre repository, cette LiveData va avoir une valeur initiale
    // puisqu'elle est définie dans le constructeur avec "randomNumberLiveData.setValue(1)". Cela va tout de même provoquer la réaction de
    // l'Observer (dans le MainViewModel) dès qu'il va "commencer à la regarder" (lors du "addSource()")
    public LiveData<Integer> getRandomNumberLiveData() {
        return randomNumberLiveData;
    }

    // On donne une nouvelle valeur à la LiveData. Cela va activer l'Observer qui regarde cette LiveData (comme dans le MainViewModel) et
    // provoquer l'appel de la méthode "combine" du MainViewModel avec les nouvelles valeurs
    public void rollNewRandom() {
        // 1 - 10 (inclus)
        randomNumberLiveData.setValue(new Random().nextInt(10)+ 1);
    }
}

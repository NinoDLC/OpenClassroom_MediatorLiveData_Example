package fr.delcey.mediatorlivedataexample;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import fr.delcey.mediatorlivedataexample.repository.NumberRepository;
import fr.delcey.mediatorlivedataexample.repository.RandomRepository;

/**
 * Cette classe est le "coeur" de notre application, elle est l'intermédiaire entre la View (MainActivity)
 * et le stockage des données (NumberRepository et RandomRepository).
 *
 * C'est elle qui va avoir toute "l'intelligence" de notre application. Par "intelligence", on entend "fonctionnalités". Pour cette
 * application, ça concerne donc le fait de construire une phrase complexe suivant 2 nombres (un qui s'incrémente 1 à 1 ou se double, un
 * autre qui est aléatoire).
 *
 * Cette classe va être testée unitairement puisque c'est elle qui va contenir toutes nos règles de gestion.
 */
public class MainViewModel extends ViewModel {

    private final NumberRepository numberRepository;
    private final RandomRepository randomRepository;

    private final MediatorLiveData<UiModel> uiModelMediatorLiveData = new MediatorLiveData<>();

    // Injection de dépendance depuis la Factory
    public MainViewModel(
        NumberRepository numberRepository,
        RandomRepository randomRepository
    ) {
        this.numberRepository = numberRepository;
        this.randomRepository = randomRepository;

        // Attention au bug !! Quand on utilise un mediatorLiveData, lorsqu'on fait les "addSource", il faut bien utiliser la même variable
        // dans le "addSource" et dans le onChanged des autres sources (quand on fait "numberLiveData.getValue()" par exemple)
        LiveData<Integer> numberLiveData = numberRepository.getNumberLiveData();
        LiveData<Integer> randomNumberLiveData = randomRepository.getRandomNumberLiveData();

        uiModelMediatorLiveData.addSource(numberLiveData, new Observer<Integer>() {
            @Override
            public void onChanged(Integer number) {
                combine(number, randomNumberLiveData.getValue());
            }
        });

        uiModelMediatorLiveData.addSource(randomNumberLiveData, new Observer<Integer>() {
            @Override
            public void onChanged(Integer randomNumber) {
                combine(numberLiveData.getValue(), randomNumber);
            }
        });
    }

    // Attention ici, les paramètres de la méthode "combine" (ce n'est pas un mot clef mais on l'utilise beaucoup avec le MediatorLiveData)
    // doivent toujours être considérés comme "nullables". En effet, "randomNumberLiveData.getValue()" (ou "numberLiveData.getValue()") peut
    // renvoyer une valeur null si jamais on n'a pas encore exposé de donnée dans cette LiveData.
    private void combine(@Nullable Integer number, @Nullable Integer randomNumber) {
        // On ne peut pas calculer le produit de ces 2 nombres si l'un d'eux est null.
        if (number == null || randomNumber == null) {
            return;
        }

        int result = number * randomNumber;
        String isEvenOrOdd;

        if (result % 2 == 0) {
            isEvenOrOdd = "pair";
        } else {
            isEvenOrOdd = "impair";
        }

        String sentence = "Le nombre " + result + " est " + isEvenOrOdd + ", le nombre aléatoire est " + randomNumber;

        uiModelMediatorLiveData.setValue(
            new UiModel(
                // On affiche toujours des Strings, pas des int.
                // Le UiModel ne doit donc avoir que des Strings (sauf pour des id de base de donnée par exemple, vu qu'ils ne seront pas affichés)
                String.valueOf(result),
                sentence
            )
        );
    }

    // Getter typé en LiveData (et pas MediatorLiveData pour éviter la modification de la valeur de la LiveData dans la View)
    public LiveData<UiModel> getUiModelLiveData() {
        return uiModelMediatorLiveData;
    }

    // Les méthodes publiques ici représentent les différentes actions que l'utilisateur peut faire sur l'interface, le ViewModel se charge
    // de modifier les données comme nécessaire.
    public void onAddButtonClicked() {
        numberRepository.addToNumber(1);
    }

    public void onMultiplyButtonClicked() {
        numberRepository.multiplyNumber(2);
    }

    public void onRandomButtonClicked() {
        randomRepository.rollNewRandom();
    }
}

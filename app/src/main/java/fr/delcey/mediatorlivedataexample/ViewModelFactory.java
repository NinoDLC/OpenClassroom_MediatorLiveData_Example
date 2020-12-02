package fr.delcey.mediatorlivedataexample;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import fr.delcey.mediatorlivedataexample.repository.NumberRepository;
import fr.delcey.mediatorlivedataexample.repository.RandomRepository;

/**
 * L'Injection de dépendance se fait dans la ViewModelFactory. C'est elle qui se charge de créer tous les ViewModels, c'est donc le point
 * d'entrée de tous les ViewModels
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory sFactory;

    // Pattern singleton : pas seule la classe elle-même peut s'instancier
    private ViewModelFactory() {

    }

    // Pattern singleton : récupération de l'Instance unique disponible partout dans l'app
    public static ViewModelFactory getInstance() {
        if (sFactory == null) {
            synchronized (ViewModelFactory.class) {
                if (sFactory == null) {
                    sFactory = new ViewModelFactory();
                }
            }
        }

        return sFactory;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(
                new NumberRepository(),
                new RandomRepository());
        }
        // C'est ici qu'on va créer tous les différents VM : on utilise une seule ViewModelFactory pour toute l'application
        // Exemple pour un deuxième ViewModel :
        // else if (modelClass.isAssignableFrom(AnotherViewModel.class)) {
        //     return (T) new AnotherViewModel(
        //         new NumberRepository(),
        //         new RandomRepository());
        // }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
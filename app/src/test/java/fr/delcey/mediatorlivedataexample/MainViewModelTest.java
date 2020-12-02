package fr.delcey.mediatorlivedataexample;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import fr.delcey.mediatorlivedataexample.repository.NumberRepository;
import fr.delcey.mediatorlivedataexample.repository.RandomRepository;

import static fr.delcey.mediatorlivedataexample.UnitTestUtils.getOrAwaitValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class MainViewModelTest {

    @Rule
    public final InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    // Un simple test unitaire sans injection de mock. On utilise juste les classes "tel quelles".
    // On simule l'appui sur le bouton en appelant la méthode "onAddButtonClicked()"
    @Test
    public void when_addButtonClicked_should_display1() throws InterruptedException {
        // Given
        // On crée le MainViewModel avec les dépendances "classiques"
        MainViewModel mainViewModel = new MainViewModel(new NumberRepository(), new RandomRepository());

        // When
        // On "utilise" le ViewModel et on récupère le UiModel produit suite à cette "utilisation"
        mainViewModel.onAddButtonClicked();
        UiModel result = getOrAwaitValue(mainViewModel.getUiModelLiveData());

        // Then
        // On regarde que le UiModel correspond bien à ce qu'on s'attend à avoir
        assertEquals(
            new UiModel(
                "1",
                "Le nombre 1 est impair, le nombre aléatoire est 1"
            ),
            result
        );
    }

    // Un autre simple test unitaire sans injection de mock. On est obligé d'appeler 2 fois la méthode
    @Test
    public void when_addButtonClickedTwice_should_display2() throws InterruptedException {
        // Given
        MainViewModel mainViewModel = new MainViewModel(new NumberRepository(), new RandomRepository());

        // When
        mainViewModel.onAddButtonClicked();
        mainViewModel.onAddButtonClicked();
        UiModel result = getOrAwaitValue(mainViewModel.getUiModelLiveData());

        // Then
        assertEquals(
            new UiModel(
                "2",
                "Le nombre 2 est pair, le nombre aléatoire est 1"
            ),
            result
        );
    }

    // Un dernier simple test unitaire sans injection de mock. Je pense que tu as compris le fonctionnement...
    @Test
    public void when_addButtonClickedTwiceAndThenMultiply_should_display4() throws InterruptedException {
        // Given
        MainViewModel mainViewModel = new MainViewModel(new NumberRepository(), new RandomRepository());

        // When
        mainViewModel.onAddButtonClicked();
        mainViewModel.onAddButtonClicked();
        mainViewModel.onMultiplyButtonClicked();
        UiModel result = getOrAwaitValue(mainViewModel.getUiModelLiveData());

        // Then
        assertEquals(
            new UiModel(
                "4",
                "Le nombre 4 est pair, le nombre aléatoire est 1"
            ),
            result
        );
    }

    // On va aller un peu plus loin dans les Tests Unitaires avec Mockito parce qu'il a tout de même un soucis,
    // impossible de savoir quel nombre aléatoire va être tiré par le "RandomRepository"... comment tester le comportement ?
    @Test
    public void when_randomNumberIs5_should_display5() throws InterruptedException {
        // Given
        // On crée notre propre MutableLiveData pendant le test pour la manipuler comme on souhaite
        MutableLiveData<Integer> randomNumberMutableLiveData = new MutableLiveData<>();

        // On veut que la LiveData renvoie 5 comme "nombre aléatoire"
        randomNumberMutableLiveData.setValue(5);

        // On construit le mock du Repository... Un Mock c'est une "coquille vide". Il fait semblant d'être un RandomRepository mais il ne
        // se passe rien dans ses méthodes et il renverra "null" s'il doit renvoyer une valeur. On peut lui dire quelle variable renvoyer
        // si quelqu'un utilise une de ses fonctions (par exemple le MainViewModel)
        RandomRepository randomRepository = Mockito.mock(RandomRepository.class);

        // Pour pouvoir modifier l'objet retourné par la méthode getRandomNumberLiveData(). Maintenant, c'est notre MutableLiveData qui est
        // est donnée au MainViewModel (elle a comme valeur préprogrammée 5), pas la "vraie" MutableLiveData du RandomRepository dont on
        // ne peux pas connaitre la valeur à l'avance.
        Mockito.doReturn(randomNumberMutableLiveData).when(randomRepository).getRandomNumberLiveData();

        // On injecte notre mock dans notre MainViewModel pour "intervenir" lorsque
        // le MainViewModel va demander au RandomRepository la LiveData.
        // A noter que le NumberRepository n'est pas mocké, le comportement du NumberRepository nous convient !
        MainViewModel mainViewModel = new MainViewModel(new NumberRepository(), randomRepository);

        // When
        mainViewModel.onAddButtonClicked();
        mainViewModel.onRandomButtonClicked();
        UiModel result = getOrAwaitValue(mainViewModel.getUiModelLiveData());

        // Then
        assertEquals(
            new UiModel(
                "5",
                "Le nombre 5 est impair, le nombre aléatoire est 5" // Grâce au Mock on a pu contrôler le nombre aléatoire !
            ),
            result
        );
        // Autre chose à ne pas oublier avec Mockito : le verify !
        // On peut vérifier que les méthodes du repository ont bien été appelées (ou pas !). C'est une sécurité supplémentaire
        // On vérifie que le MainViewModel lui a bien demandé sa LiveData
        Mockito.verify(randomRepository, Mockito.times(1)).getRandomNumberLiveData();
        // On vérifie que le MainViewModel lui a bien demandé de générer un nouveau nombre (suite à l'appel de la méthode "onRandomButtonClicked()")
        Mockito.verify(randomRepository, Mockito.times(1)).rollNewRandom();
        // On vérifie qu'il n'y a pas eu d'autre interaction
        Mockito.verifyNoMoreInteractions(randomRepository);
    }

    // Allons encore plus loin avec 2 mocks (pour éviter d'appeler 42 fois la méthode "onAddButtonClicked")
    @Test
    public void when_onAddButtonClicked42TimesAndRandomNumberIs7_should_display294() throws InterruptedException {
        // Given
        // On mock le NumberRepository aussi cette fois
        MutableLiveData<Integer> numberMutableLiveData = new MutableLiveData<>();
        numberMutableLiveData.setValue(42); // C'est comme si on avait cliqué 42 fois sur le bouton
        NumberRepository numberRepository = Mockito.mock(NumberRepository.class);
        Mockito.doReturn(numberMutableLiveData).when(numberRepository).getNumberLiveData();

        // On mock comme plus haut le nombre random, cette fois il renvoit 7 par exemple
        MutableLiveData<Integer> randomNumberMutableLiveData = new MutableLiveData<>();
        randomNumberMutableLiveData.setValue(7);
        RandomRepository randomRepository = Mockito.mock(RandomRepository.class);
        Mockito.doReturn(randomNumberMutableLiveData).when(randomRepository).getRandomNumberLiveData();

        // On injecte nos 2 mocks dans le MainViewModel cette fois, on veut modifier les comportements des 2 repository
        MainViewModel mainViewModel = new MainViewModel(numberRepository, randomRepository);

        // When
        UiModel result = getOrAwaitValue(mainViewModel.getUiModelLiveData());

        // Then
        assertEquals(
            new UiModel(
                "294",
                "Le nombre 294 est pair, le nombre aléatoire est 7"
            ),
            result
        );
    }

    // Lorsqu'une fonction en appelle juste une autre (comme dans MainViewModel.onAddButtonClicked()), on peut faire un petit test unitaire
    // pour vérifier ce comportement avec 'spy' plutôt que 'mock' : ça permet de "contrôler" le comportement d'un vrai objet
    @Test
    public void when_onAddButtonClicked_should_call_numberRepository() {
        // Given
        // On spy le NumberRepository
        NumberRepository numberRepository = Mockito.spy(new NumberRepository());

        // On injecte notre "espion" dans le MainViewModel
        MainViewModel mainViewModel = new MainViewModel(numberRepository, new RandomRepository());

        // When
        mainViewModel.onAddButtonClicked();

        // Then
        // On vérifie que le MainViewModel lui a bien demandé sa LiveData
        Mockito.verify(numberRepository, Mockito.times(1)).getNumberLiveData();
        // On vérifie que le MainViewModel lui a bien demandé d'ajouter "1" au nombre (suite à l'appel de la méthode "onAddButtonClicked()")
        Mockito.verify(numberRepository, Mockito.times(1)).addToNumber(eq(1));
        // On vérifie qu'il n'y a pas eu d'autre interaction
        Mockito.verifyNoMoreInteractions(numberRepository);
    }
}
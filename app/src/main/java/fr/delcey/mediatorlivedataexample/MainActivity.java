package fr.delcey.mediatorlivedataexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

/**
 * Le rôle de la partie "View" (ici, l'Activity ou le Fragment) de l'architecture MVVM est d'afficher les informations du UiModel à
 * l'utilisateur, et de faire transiter les actions utilisateur au ViewModel, rien de plus.<br/>
 *
 * Tout formattage de donnée (concaténation de String, manipulation de chiffre, association de données, etc...) doit être fait dans le
 * ViemModel, pas dans la View.
 */
public class MainActivity extends AppCompatActivity {

    // Pas de variable de class (property) nécessaire ici. Moins il y en a, mieux c'est. Il est parfois possible de ne jamais en utiliser.

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button buttonAdd = findViewById(R.id.button_add);
        Button buttonMultiply = findViewById(R.id.button_multiply);
        Button buttonRandom = findViewById(R.id.button_random);

        TextView textViewNumber = findViewById(R.id.number_textview);
        TextView textViewSentence = findViewById(R.id.sentence_textview);

        final MainViewModel mainViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MainViewModel.class);
        mainViewModel.getUiModelLiveData().observe(this, new Observer<UiModel>() {
            @Override
            public void onChanged(UiModel uiModel) {
                textViewNumber.setText(uiModel.getNumberToDisplay());
                textViewSentence.setText(uiModel.getSentence());
            }
        });

        // Pas de code / intelligence ici ! On "rapporte" juste au ViewModel ce qui se passe sur la View, rien d'autre !
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.onAddButtonClicked();
            }
        });

        buttonMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.onMultiplyButtonClicked();
            }
        });

        buttonRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.onRandomButtonClicked();
            }
        });
    }
}

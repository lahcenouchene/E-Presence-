package com.example.e_presence;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_login_acitvity);

        usernameField = findViewById(R.id.editTextTextUsername);
        passwordField = findViewById(R.id.editTextTextPassword);
        Button showLangue = findViewById(R.id.showLangueButton);
        showLangue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLangueDialog();
            }
        });

        setToolbar();
    }

    @SuppressLint("SetTextI18n")
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitel_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);
        title.setText("page de Connexion");
        subtitle.setVisibility(View.INVISIBLE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }




    public void loginUser(View view) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        // Obtenez une instance de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);

        // Récupérez la liste d'utilisateurs
        Set<String> userSet = sharedPreferences.getStringSet("users", new HashSet<>());

        // Convertissez la chaîne JSON en objet User
        Gson gson = new Gson();
        for (String json : userSet) {
            User user = gson.fromJson(json, User.class);

            // Vérifiez si l'utilisateur existe dans la liste
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                // Utilisateur authentifié, redirigez vers le menu principal ou toute autre activité souhaitée
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish(); // Pour éviter de revenir à l'écran de connexion avec le bouton de retour
                return; // Sortez de la boucle dès que l'utilisateur est trouvé
            }
        }

        // Échec de l'authentification
        Toast.makeText(this, "Login failed. Check your credentials.", Toast.LENGTH_SHORT).show();
    }


    public void goToRegister(View view) {
        // Redirigez vers l'activité d'enregistrement
        Intent intent = new Intent(this, MainActivity1.class);
        startActivity(intent);
    }

    private void showChangeLangueDialog() {
        final String[] listItems = {"Francais", "Anglais"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
        mBuilder.setTitle("choisie une langue ...");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    setLocale("fr");
                    recreate();
                }

                else  if (i == 1){
                    setLocale("en");
                    recreate();
                }

                dialogInterface.dismiss();

            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang ) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("parameters", MODE_PRIVATE).edit();
        editor.putString("Ma lang", lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("parameters", Activity.MODE_PRIVATE);
        String langue =prefs.getString("Ma lang", "");
        setLocale(langue);

    }
}

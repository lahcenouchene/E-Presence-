package com.example.e_presence;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class MainActivity1 extends AppCompatActivity {
    private EditText usernameField, emailField, phoneField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        usernameField = findViewById(R.id.editTextTextusername);
        emailField = findViewById(R.id.editTextTextEmailAddress);
        phoneField = findViewById(R.id.editTextPhone);
        passwordField = findViewById(R.id.editTextTextPassword);
        setToolbar();
    }

    @SuppressLint("SetTextI18n")
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitel_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);
        title.setText("Create account");
        subtitle.setVisibility(View.INVISIBLE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }

    public void registerUser(View view) {
        // Obtenez les informations saisies
        String username = usernameField.getText().toString();
        String email = emailField.getText().toString();
        String phone = phoneField.getText().toString();
        String password = passwordField.getText().toString();

        // Obtenez une instance de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Obtenez la liste actuelle d'utilisateurs
        Set<String> userSet = sharedPreferences.getStringSet("users", new HashSet<>());

        // Convertissez l'objet User en chaîne JSON
        User newUser = new User(username, email, phone, password);
        Gson gson = new Gson();
        String json = gson.toJson(newUser);

        // Ajoutez la nouvelle chaîne JSON à la liste
        userSet.add(json);
        Log.d("DEBUG", "User added: " + json);
        Log.d("DEBUG", "Current user set: " + userSet.toString());

        // Enregistrez la nouvelle liste d'utilisateurs
        editor.putStringSet("users", userSet);

        // Appliquez les changements
        editor.apply();

        Toast.makeText(this, "User information saved", Toast.LENGTH_SHORT).show();
    }


    public void showAccountList(View view) {
        // Obtenez une instance de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);

        // Récupérez la liste d'utilisateurs
        Set<String> userSet = sharedPreferences.getStringSet("users", new HashSet<>());
        Log.d("DEBUG", "Number of users: " + userSet.size());

        // Créez une boîte de dialogue pour afficher le contenu
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Account list");

        if (userSet.isEmpty()) {
            builder.setMessage("No users found");
        } else {
            // Affichez tous les utilisateurs
            StringBuilder userList = new StringBuilder();
            Gson gson = new Gson();
            for (String json : userSet) {
                // Convertissez la chaîne JSON en objet User
                User user = gson.fromJson(json, User.class);
                Log.d("lahcen", "User: " + user.toString());
                userList.append(user.toString()).append("\n\n");
            }
            builder.setMessage(userList.toString());
        }

        builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());

        // Afficher la boîte de dialogue
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void goToLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }





}

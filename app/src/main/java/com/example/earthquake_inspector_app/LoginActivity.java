package com.example.earthquake_inspector_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.sql.SQLOutput;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    FirebaseFirestore db;
    CollectionReference citiesRef;
    TextInputEditText login_inputed_username, login_inputed_password, login_inputed_PM_id;
    Button final_login, back_to_welcome_screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Cast Views
        login_inputed_username = findViewById(R.id.login_user_email);
        login_inputed_password = findViewById(R.id.login_user_password);
        final_login = findViewById(R.id.final_login_button);
        back_to_welcome_screen = findViewById(R.id.from_login_back_to_welcome_screen);

        db = FirebaseFirestore.getInstance();
        citiesRef = db.collection("users");

        //Login btn Listener
        final_login.setOnClickListener(v -> {

            //Format final values
            String username = Objects.requireNonNull(login_inputed_username.getText()).toString().trim();
            String password = Objects.requireNonNull(login_inputed_password.getText()).toString().trim();

            System.out.println(username);
            System.out.println(password);

            //Login fields Requirements
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(LoginActivity.this, "Παρακαλώ πληκτρολογήστε ένα έγκυρο όνομα χρήστη !", Toast.LENGTH_SHORT).show();
            } else if (username.length() <= 4) {
                Toast.makeText(LoginActivity.this, "Παρακαλώ πληκτρολογήστε ένα όνομα χρήστη με τουλάχιστον 5 χαρακτήρες!", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Παρακαλώ πληκτρολογήστε ένα έγκυρο κωδικό πρόσβασης χρήστη !", Toast.LENGTH_SHORT).show();
            } else if (password.length() <= 10) {
                Toast.makeText(LoginActivity.this, "Παρακαλώ πληκτρολογήστε ένα κωδικό πρόσβασης χρήστη με τουλάχιστον 10 χαρακτήρες !", Toast.LENGTH_SHORT).show();
            }

            //Query
            citiesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                    for (QueryDocumentSnapshot ds : documentSnapshots) {

                        System.out.println("wtf");
                        System.out.println("get username"+ds.get("username"));
                        System.out.println("get pass"+ ds.get("password"));
                        System.out.println( (ds.get("username").equals(username)) && ((ds.get("password").equals(password))) );

                        if ( (ds.get("username").equals(username)) &&  (ds.get("password").equals(password)) && (ds.get("pm_id").equals(""))) {
                            Toast.makeText(LoginActivity.this, "Welcome Civil Engineer", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, base_activity.class));
                        }
                        else if( (ds.get("username").equals(username)) &&  (ds.get("password").equals(password)) && !(ds.get("pm_id").equals("")) ) {
                            Toast.makeText(LoginActivity.this, "Welcome Guest User", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, base_activity.class));
                        }
                    }
                }
            });
        });
    }
}

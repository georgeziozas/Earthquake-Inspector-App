package com.example.earthquake_inspector_app;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    //Component Values
    TextInputEditText signup_inputed_username, signup_inputed_password, signup_inputed_PM_id;
    Button final_register, back_to_welcome_screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Cast Views
        signup_inputed_username = findViewById(R.id.signup_user_email);
        signup_inputed_password = findViewById(R.id.signup_user_password);
        signup_inputed_PM_id = findViewById(R.id.signup_user_pm);
        final_register = findViewById(R.id.final_register_button);
        back_to_welcome_screen = findViewById(R.id.from_signup_back_to_welcome_screen);

        //Firebase db instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Register btn Listener
        final_register.setOnClickListener(v -> {

            //Format final values
            String username = Objects.requireNonNull(signup_inputed_username.getText()).toString().trim();
            String password = Objects.requireNonNull(signup_inputed_password.getText()).toString().trim();
            String polmix_id = Objects.requireNonNull(signup_inputed_PM_id.getText()).toString().trim();

            //User object insantiate
            User user = new User(username, password, polmix_id);

            //Signup fields Requirements
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(SignupActivity.this, "Παρακαλώ πληκτρολογήστε ένα έγκυρο όνομα χρήστη !", Toast.LENGTH_SHORT).show();
            } else if (username.length() <= 4) {
                Toast.makeText(SignupActivity.this, "Παρακαλώ πληκτρολογήστε ένα όνομα χρήστη με τουλάχιστον 5 χαρακτήρες!", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(SignupActivity.this, "Παρακαλώ πληκτρολογήστε ένα έγκυρο κωδικό πρόσβασης χρήστη !", Toast.LENGTH_SHORT).show();
            } else if (password.length() <= 10) {
                Toast.makeText(SignupActivity.this, "Παρακαλώ πληκτρολογήστε ένα κωδικό πρόσβασης χρήστη με τουλάχιστον 10 χαρακτήρες !", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(polmix_id)) {
                Toast.makeText(SignupActivity.this, "Παρακαλώ πληκτρολογήστε ένα έγκυρο αναγνωριστικό Πολιτικού Μηχανικού χρήστη !", Toast.LENGTH_SHORT).show();
            } else if (polmix_id.length() <= 10) {
                Toast.makeText(SignupActivity.this, "Παρακαλώ πληκτρολογήστε ένα έγκυρο αναγνωριστικό Πολιτικού Μηχανικού χρήστη !", Toast.LENGTH_SHORT).show();
            }

            //TODO CRYPTOGRAPHIC SOLUTION FOR DB PASSWORDS AND USERNAMES

            //Push data to the server
            db.collection("users")
                    .add(user)
                    .addOnSuccessListener(documentReference -> {
                        String TAG = "";
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(SignupActivity.this, "Η εγγραφή σας ολοκληρώθηκε επιτυχώς! \n Προσπαθήστε να συνδεθείτε μέσω της καρτέλας Log In!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    })
                    .addOnFailureListener(e -> {
                        String TAG = "";
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(SignupActivity.this, "Η εγγραφή σας απέτυχε! Προσπαθήστε ξανά!", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}



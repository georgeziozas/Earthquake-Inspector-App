package com.example.earthquake_inspector_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FirstOrderCheck extends AppCompatActivity {
    RadioButton apofasi_katalilotitas_radiobtn;
    String sintetagmenes_ktiriou_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_order_check);

        Intent intent = getIntent();
        final String[] tipos_ktiriou_spinner_text = new String[1];
        //cast views
        TextView sintetagmenes_textview = findViewById(R.id.sintetagmenes_ktiriou);
        EditText dimos_editext = findViewById(R.id.dimos);
        EditText odos_editext = findViewById(R.id.odos);
        EditText arithmos_editext = findViewById(R.id.arithmos);
        EditText perioxi_editext = findViewById(R.id.perioxi);
        EditText onoma_editext = findViewById(R.id.onoma);
        EditText eponimo_editext = findViewById(R.id.eponimo);
        EditText tilefono_editext = findViewById(R.id.tilefono);
        EditText arithmos_orofon_editext = findViewById(R.id.arithmos_orofon);
        EditText arithmos_diamerismaton_editext = findViewById(R.id.arithmos_diamerismaton);
        RadioGroup ektimisi_katalilotitas_editext = findViewById(R.id.radioGroup);
        Button first_order_final_btn = findViewById(R.id.first_order_final_btn);
        // Get selected radio button from radioGroup

        double apo_base_sintetagmenes_lat = intent.getDoubleExtra("marker_sintetagmenes_lat", 0);
        double apo_base_sintetagmenes_long = intent.getDoubleExtra("marker_sintetagmenes_long", 0);
        sintetagmenes_ktiriou_text = String.valueOf(apo_base_sintetagmenes_lat) + "\n" + String.valueOf(apo_base_sintetagmenes_long);
        sintetagmenes_textview.setText(sintetagmenes_ktiriou_text);


        //dynamic spinner
        Spinner dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);
        dynamicSpinner.setTooltipText("ΕΠΕΛΕΞΕ ΤΥΠΟ ΚΤΙΡΙΟΥ");
        String[] items = new String[]{"Κατοικία σε χρήση", "Κατοικία εγκαταλελειμένη", "Σταύλος/Αποθήκη", "Επαγγελματικός χώρος", "Σχολείο", "Ξενοδοχείο"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        dynamicSpinner.setAdapter(adapter);
        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipos_ktiriou_spinner_text[0] = parent.getItemAtPosition(position).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(FirstOrderCheck.this, "Είναι Υποχρεωτικό να επιλέξεις έναν απο τους διαθέσιμους τύπους κτηρίου", Toast.LENGTH_SHORT).show();
            }
        });


/*
        //Form fields Requirements
        if (TextUtils.isEmpty(dimos_text)) {
            Toast.makeText(FirstOrderCheck.this, "Παρακαλώ πληκτρολογήστε μια έγκυρη τιμή διεύθυνσης !", Toast.LENGTH_SHORT).show();
        } else if (dimos_text.length() <= 3) {
            Toast.makeText(FirstOrderCheck.this, "Παρακαλώ πληκτρολογήστε μια έγκυρη τιμή διεύθυνσης με τουλάχιστον 3 χαρακτήρες!", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(odos_text)) {
            Toast.makeText(FirstOrderCheck.this, "Παρακαλώ πληκτρολογήστε μια έγκυρη τιμή οδού !", Toast.LENGTH_SHORT).show();
        } else if (odos_text.length() <= 3) {
            Toast.makeText(FirstOrderCheck.this, "Παρακαλώ πληκτρολογήστε μια έγκυρη τιμή οδού με τουλάχιστον 3 χαρακτήρες !", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(arithmos_text)) {
            Toast.makeText(FirstOrderCheck.this, "Παρακαλώ πληκτρολογήστε ένα έγκυρο αριθμό οδού !", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isDigitsOnly(arithmos_text)) {
            Toast.makeText(FirstOrderCheck.this, "Παρακαλώ πληκτρολογήστε μόνο αριθμούς στο παρών πεδίο !", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(perioxi_text)) {
            Toast.makeText(FirstOrderCheck.this, "Παρακαλώ πληκτρολογήστε μια έγκυρη τιμή περιοχής!", Toast.LENGTH_SHORT).show();
        } else if (perioxi_text.length() <= 3) {
            Toast.makeText(FirstOrderCheck.this, "Παρακαλώ πληκτρολογήστε μια έγκυρη τιμή περιοχής με τουλάχιστον 4 χαρακτήρες!", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(onoma_text)) {
            Toast.makeText(FirstOrderCheck.this, "Παρακαλώ πληκτρολογήστε μια έγκυρη τιμή ονόματος!", Toast.LENGTH_SHORT).show();
        } else if (onoma_text.length() <= 2) {
            Toast.makeText(FirstOrderCheck.this, "Παρακαλώ πληκτρολογήστε μια έγκυρη τιμή ονόματος με τουλάχιστον 3 χαρακτήρες !", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(eponimo_text)) {
            Toast.makeText(FirstOrderCheck.this, "Παρακαλώ πληκτρολογήστε μια έγκυρη τιμή επωνύμου !", Toast.LENGTH_SHORT).show();
        } else if (eponimo_text.length() <= 2) {
            Toast.makeText(FirstOrderCheck.this, "Παρακαλώ πληκτρολογήστε μια έγκυρη τιμή επωνύμου με τουλάχιστον 3 χαρακτήρες!", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(tilefono_text)) {
            Toast.makeText(FirstOrderCheck.this, "Παρακαλώ πληκτρολογήστε μια έγκυρη τιμή τηλεφώνου μόνο με αριθμούς!", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isDigitsOnly(tilefono_text)) {
            Toast.makeText(FirstOrderCheck.this, "Παρακαλώ πληκτρολογήστε μια έγκυρη τιμή τηλεφώνου  !", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(arithmos_diamerismaton_text)) {
            Toast.makeText(FirstOrderCheck.this, "Παρακαλώ πληκτρολογήστε μια έγκυρη τιμή αριθμού διαμερισμάτων!", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isDigitsOnly(arithmos_diamerismaton_text)) {
            Toast.makeText(FirstOrderCheck.this, "Παρακαλώ πληκτρολογήστε μια έγκυρη τιμή αριθμού διαμερισμάτων μόνο με αριθμούς!", Toast.LENGTH_SHORT).show();
        }
*/
        first_order_final_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = ektimisi_katalilotitas_editext.getCheckedRadioButtonId();
                String dimos_text = Objects.requireNonNull(dimos_editext.getText()).toString().trim();
                String odos_text = Objects.requireNonNull(odos_editext.getText()).toString().trim();
                String arithmos_text = Objects.requireNonNull(arithmos_editext.getText()).toString().trim();
                String perioxi_text = Objects.requireNonNull(perioxi_editext.getText()).toString().trim();
                String onoma_text = Objects.requireNonNull(onoma_editext.getText()).toString().trim();
                String eponimo_text = Objects.requireNonNull(eponimo_editext.getText()).toString().trim();
                String tilefono_text = Objects.requireNonNull(tilefono_editext.getText()).toString().trim();
                String arithmos_orofon_text = Objects.requireNonNull(arithmos_orofon_editext.getText()).toString().trim();
                String arithmos_diamerismaton_text = Objects.requireNonNull(arithmos_diamerismaton_editext.getText()).toString().trim();
                Button apofasi_katalilotitas_radiobtn = findViewById(selectedId);
                String apofasi_katalilotitas_text = Objects.requireNonNull(apofasi_katalilotitas_radiobtn.getText()).toString().trim();



                FormaProtovathmias form = new FormaProtovathmias(
                        sintetagmenes_ktiriou_text, dimos_text,
                        odos_text, arithmos_text, perioxi_text,
                        onoma_text, eponimo_text, tilefono_text,
                        arithmos_orofon_text, arithmos_diamerismaton_text,
                        apofasi_katalilotitas_text, tipos_ktiriou_spinner_text[0]
                );

                //Push form to database
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                //Push data to the server
                db.collection("FirstOrderForms")
                        .add(form)
                        .addOnSuccessListener(documentReference -> {
                            String TAG = "";
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        })
                        .addOnFailureListener(e -> {
                            String TAG = "";
                            Log.w(TAG, "Error adding document", e);
                        });


                int apo_protovathmia_katastasi = intent.getIntExtra("katastasi_protovathmias",100);

                if (apofasi_katalilotitas_text.equals("ΚΑΤΟΙΚΙΣΙΜΟ")) {
                    apo_protovathmia_katastasi = 0;
                    Intent intent = new Intent();
                    intent.putExtra("message_return_sintetagmenes_lat", apo_base_sintetagmenes_lat);
                    intent.putExtra("message_return_sintetagmenes_long", apo_base_sintetagmenes_long);
                    intent.putExtra("teliki_apantisi_protovathmias", apo_protovathmia_katastasi);
                    setResult(1, intent);
                    finish();
                    //startActivity(new Intent(FirstOrderCheck.this,base_activity.class));


                } else if (apofasi_katalilotitas_text.equals("ΜΗ ΚΑΤΟΙΚΙΣΙΜΟ")) {
                    apo_protovathmia_katastasi = 1;
                    Intent intent = new Intent();
                    intent.putExtra("message_return_sintetagmenes_lat", apo_base_sintetagmenes_lat);
                    intent.putExtra("message_return_sintetagmenes_long", apo_base_sintetagmenes_long);
                    intent.putExtra("teliki_apantisi_protovathmias", apo_protovathmia_katastasi);
                    setResult(1, intent);
                    finish();
                    startActivity(new Intent(FirstOrderCheck.this,base_activity.class));

                }


            }
        });


    }
}
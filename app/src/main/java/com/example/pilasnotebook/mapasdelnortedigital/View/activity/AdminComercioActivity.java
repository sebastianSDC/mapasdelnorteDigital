package com.example.pilasnotebook.mapasdelnortedigital.view.activity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pilasnotebook.mapasdelnortedigital.R;
import com.example.pilasnotebook.mapasdelnortedigital.model.POJO.Cliente;
import com.example.pilasnotebook.mapasdelnortedigital.model.POJO.Zona;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AdminComercioActivity extends AppCompatActivity {


    private static final String NOMBRE = "nombre";
    private static final String DIRECCION = "direccion";
    private static final String TELEFONO = "telefono";
    private static final String CATEGORIA = "categoria";
    private static final String TAG = "tag";
    private String categoriaTxt;

    private Zona zona;
    private Cliente cliente;
    private TextView datosTraidos;
    private EditText nombreEd, direccionEd, telefonoEd;
    private Button btncargar, btntraer;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected Spinner categorias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_comercio);

        nombreEd = (EditText) findViewById(R.id.inNombre_Comercio);
        direccionEd = (EditText) findViewById(R.id.inDireccion_Comercio);
        telefonoEd = (EditText) findViewById(R.id.inTelefono_Comercio);
        categorias = (Spinner) findViewById(R.id.spinn_categoria_Comercio);
        datosTraidos = (TextView) findViewById(R.id.datos_Cliente);
        btncargar = (Button) findViewById(R.id.btn_cargar);
        btntraer = (Button) findViewById(R.id.btn_traer_datos);


        ArrayAdapter<CharSequence> adapterSpinCAtegorias = ArrayAdapter.createFromResource(this,
                R.array.combo_categorias, android.R.layout.simple_spinner_item);

        categorias.setAdapter(adapterSpinCAtegorias);

        categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoriaTxt = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btncargar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                String nombreTxt = nombreEd.getText().toString().trim();
                String direccionTxt = direccionEd.getText().toString().trim();
                String telefonoTxt = telefonoEd.getText().toString().trim();


                cliente = new Cliente();


                if (nombreTxt.isEmpty() || direccionTxt.isEmpty() || telefonoTxt.isEmpty() || categoriaTxt.isEmpty()) {
                    return;
                }

                Map<String, Object> datosACargar = new HashMap<String, Object>();
                datosACargar.put(NOMBRE, nombreTxt);
                datosACargar.put(DIRECCION, direccionTxt);
                datosACargar.put(TELEFONO, telefonoTxt);
                datosACargar.put(CATEGORIA, categoriaTxt);
                db.collection("clientes").add(datosACargar).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

            }
        });

        btntraer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                db.collection("clientes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String nombreTxt = document.getString(NOMBRE);
                                String direccionTxt = document.getString(DIRECCION);
                                String telefonoTxt = document.getString(TELEFONO);
                                String categoriaTxt = document.getString(CATEGORIA);
                                datosTraidos.setText("BIENVENIDO : " + nombreTxt +
                                        "\n" + direccionTxt +
                                        "   \n     " + telefonoTxt +
                                        "   \n   " + categoriaTxt);

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

            }


        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

}


package com.example.pilasnotebook.mapasdelnortedigital.View.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pilasnotebook.mapasdelnortedigital.Model.POJO.Cliente;
import com.example.pilasnotebook.mapasdelnortedigital.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminComercioActivity extends AppCompatActivity {


    private static final String NOMBRE = "nombre";
    private static final String DIRECCION = "direccion";
    private static final String TELEFONO = "telefono";
    private static final String MAIL = "mail";
    private static final String CLIENTE = "cliente";

    private Cliente datosCliente;

    private TextView datosTraidos;
    private EditText nombreEd, direccionEd, telefonoEd, mailEd;
    private Button btncargar, btntraer;
    private FirebaseFirestore db;
    private DocumentReference dbref = db.getInstance().document("clientes/datos");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_comercio);

        nombreEd = (EditText) findViewById(R.id.inNombre_Comercio);
        direccionEd = (EditText) findViewById(R.id.inDireccion_Comercio);
        telefonoEd = (EditText) findViewById(R.id.inTelefono_Comercio);
        mailEd = (EditText) findViewById(R.id.inMail_Comercio);
        datosTraidos = (TextView) findViewById(R.id.datos_Cliente);
        btncargar = (Button) findViewById(R.id.btn_cargar);
        btntraer = (Button) findViewById(R.id.btn_traer_datos);


        btncargar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String nombreTxt = nombreEd.getText().toString();
                String direccionTxt = direccionEd.getText().toString();
                String telefonoTxt = telefonoEd.getText().toString();
                String mailTxt = mailEd.getText().toString();

                datosCliente = new Cliente(nombreTxt, direccionTxt, telefonoTxt, mailTxt);


                if (nombreTxt.isEmpty() || direccionTxt.isEmpty() || telefonoTxt.isEmpty() || mailTxt.isEmpty()) {
                    return;
                }

                Map<String, Object> datosACargar = new HashMap<String, Object>();
                datosACargar.put(NOMBRE, nombreTxt);
                datosACargar.put(DIRECCION, direccionTxt);
                datosACargar.put(TELEFONO, telefonoTxt);
                datosACargar.put(MAIL, mailTxt);
                dbref.set(datosACargar);
            }
        });

        btntraer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dbref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {


                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String nombreTxt = documentSnapshot.getString(NOMBRE);
                            String direccionTxt = documentSnapshot.getString(DIRECCION);
                            String telefonoTxt = documentSnapshot.getString(TELEFONO);
                            String mailTxt = documentSnapshot.getString(MAIL);
                            datosTraidos.setText("BIENVENIDO     " + "   nombre:   " + nombreTxt +
                                    "   direccion:    " + direccionTxt +
                                    "   telefono:      " + telefonoTxt +
                                    "   mail:   " + mailTxt);
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


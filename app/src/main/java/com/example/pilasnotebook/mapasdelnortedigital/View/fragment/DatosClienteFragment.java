package com.example.pilasnotebook.mapasdelnortedigital.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pilasnotebook.mapasdelnortedigital.R;
import com.example.pilasnotebook.mapasdelnortedigital.controller.ClienteController;
import com.example.pilasnotebook.mapasdelnortedigital.model.POJO.Cliente;
import com.example.pilasnotebook.mapasdelnortedigital.model.POJO.Zona;
import com.example.pilasnotebook.mapasdelnortedigital.utils.ResultListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class DatosClienteFragment extends Fragment  {

    private static final String NOMBRE = "nombre";
    private static final String DIRECCION = "direccion";
    private static final String TELEFONO = "telefono";
    private static final String CATEGORIA = "categoria";
    private Integer categoriaTxt;

    private Zona zona;
    private Cliente cliente;
    private TextView datosTraidos;
    private EditText nombreEd, direccionEd, telefonoEd;
    private Button btncargar, btntraer;
    private FirebaseFirestore db;
    private DocumentReference dbref = db.getInstance().document("clientes/datos");
    protected Spinner categorias;


    public DatosClienteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_datos, container, false);

        nombreEd = view.findViewById(R.id.inNombre_Comercio);
        direccionEd = (EditText) view.findViewById(R.id.inDireccion_Comercio);
        telefonoEd = (EditText) view.findViewById(R.id.inTelefono_Comercio);
        categorias = (Spinner) view.findViewById(R.id.spinn_categoria_Comercio);
        datosTraidos = (TextView) view.findViewById(R.id.datos_Cliente);
        btncargar = (Button) view.findViewById(R.id.btn_cargar);
        btntraer = (Button) view.findViewById(R.id.btn_traer_datos);

        ArrayAdapter<CharSequence> adapterSpinCAtegorias = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()),
                R.array.combo_categorias, android.R.layout.simple_spinner_item);

        categorias.setAdapter(adapterSpinCAtegorias);

        categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoriaTxt = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btncargar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                cargarDatosCliente();

            }
        });




        btntraer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                traerDatosClientes();
            }
        });


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void cargarDatosCliente( ) {
        final String nombreTxt = nombreEd.getText().toString().trim();
        final String direccionTxt = direccionEd.getText().toString().trim();
        final String telefonoTxt = telefonoEd.getText().toString().trim();

        cliente = new Cliente(nombreTxt, categoriaTxt, zona.getDireccion());

        if (nombreTxt.isEmpty() || direccionTxt.isEmpty() || telefonoTxt.isEmpty() || categoriaTxt == null) {
            return;
        }

        Map<String, Object> datosACargar = new HashMap<String, Object>();
        datosACargar.put(NOMBRE, nombreTxt);
        datosACargar.put(DIRECCION, direccionTxt);
        datosACargar.put(TELEFONO, telefonoTxt);
        datosACargar.put(CATEGORIA, categoriaTxt);
        dbref.set(datosACargar);

    }

    public void traerDatosClientes(){

        dbref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {


            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String nombreTxt = documentSnapshot.getString(NOMBRE);
                    String direccionTxt = documentSnapshot.getString(DIRECCION);
                    String telefonoTxt = documentSnapshot.getString(TELEFONO);
                    String categoriaTxt = documentSnapshot.getString(CATEGORIA);
                    datosTraidos.setText("BIENVENIDO :    " + nombreTxt +
                            "   //    " + direccionTxt +
                            "   //      " + telefonoTxt +
                            "   categoria:   " + categoriaTxt);
                }
            }


        });
    }

}

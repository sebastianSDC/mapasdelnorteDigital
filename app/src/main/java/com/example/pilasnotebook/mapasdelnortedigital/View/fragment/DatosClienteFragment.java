package com.example.pilasnotebook.mapasdelnortedigital.view.fragment;


/**
 * Creada por Sebastián Suárez Da Costa y Pablo Herrera.
 */


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pilasnotebook.mapasdelnortedigital.R;
import com.example.pilasnotebook.mapasdelnortedigital.model.POJO.Cliente;
import com.example.pilasnotebook.mapasdelnortedigital.model.POJO.Zona;
import com.example.pilasnotebook.mapasdelnortedigital.utils.Constantes;
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


public class DatosClienteFragment extends Fragment {

    private OnFragmentInteractionListener mListener; // interface

    String TAG = Constantes.TAG;

    private String categoriaTxt;

    private Zona zona;
    private Cliente cliente;
    private TextView datosTraidos;
    private EditText nombreEd, descripcionEd, direccionEd, localidadED, codigoPostalEd, provinciaEd, paisEd, telefonoEd, mailEd;
    private ImageView fotodeContacto;
    private Button btnCargarFoto, btnCargar, btnTraer;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //String documentId= db.collection("clientes").document().getId();
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

        nombreEd = (EditText) view.findViewById(R.id.ed_nombre_comercio);
        descripcionEd = (EditText) view.findViewById(R.id.ed_descripcion_comercio);
        categorias = (Spinner) view.findViewById(R.id.spinn_categorias_comercio);
        btnCargarFoto = (Button)view.findViewById(R.id.btn_cargarFoto_comercio);
        fotodeContacto = (ImageView) view.findViewById(R.id.imagen_contacto_comercio);
        direccionEd = (EditText) view.findViewById(R.id.ed_direccion_comercio);
        localidadED = (EditText) view.findViewById(R.id.ed_localidad_comercio);
        codigoPostalEd = (EditText) view.findViewById(R.id.ed_codigoPostal_comercio);
        provinciaEd = (EditText) view.findViewById(R.id.ed_provincia_comercio);
        paisEd = (EditText) view.findViewById(R.id.ed_pais_comercio);
        telefonoEd = (EditText) view.findViewById(R.id.ed_telefono_comercio);
        mailEd = (EditText) view.findViewById(R.id.ed_mail_comercio);

        datosTraidos = (TextView) view.findViewById(R.id.datos_comercio);
        btnCargar = (Button) view.findViewById(R.id.btn_cargar_datos_comercio);
        btnTraer = (Button) view.findViewById(R.id.btn_traer_datos_comercio);

        //logica del spinner
        ArrayAdapter<CharSequence> adapterSpinCAtegorias = ArrayAdapter.createFromResource(getActivity(),
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

        btnCargar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                String nombreTxt = nombreEd.getText().toString().trim();
                String direccionTxt = direccionEd.getText().toString().trim();
                String telefonoTxt = telefonoEd.getText().toString().trim();


                cliente = new Cliente();

                if (nombreTxt.isEmpty() || direccionTxt.isEmpty() || telefonoTxt.isEmpty() || categoriaTxt.isEmpty()) {

                    Toast.makeText(getActivity(), "debe completar los campos obligatorios", Toast.LENGTH_LONG).show();

                    return ;
                }

                Map<String, Object> datosACargar = new HashMap<String, Object>();
                datosACargar.put(Constantes.NOMBRE, nombreTxt);
                datosACargar.put(Constantes.DIRECCION, direccionTxt);
                datosACargar.put(Constantes.TELEFONO, telefonoTxt);
                datosACargar.put(Constantes.CATEGORIA, categoriaTxt);


                //esta instruccion es para poner un cliente con sub-coleccion cuponera...
                // db.collection("clientes").document("OPpLzJFx6CD8VilCvy4t").collection("cuponera").add(datosACargar)

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

        btnTraer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                db.collection("clientes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String nombreTxt = document.getString(Constantes.NOMBRE);
                                String direccionTxt = document.getString(Constantes.DIRECCION);
                                String telefonoTxt = document.getString(Constantes.TELEFONO);
                                String categoriaTxt = document.getString(Constantes.CATEGORIA);
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
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DatosClienteFragment.OnFragmentInteractionListener) {
            mListener = (DatosClienteFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
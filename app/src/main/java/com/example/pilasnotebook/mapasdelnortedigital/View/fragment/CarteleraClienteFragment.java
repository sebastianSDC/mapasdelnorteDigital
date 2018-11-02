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
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pilasnotebook.mapasdelnortedigital.R;
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


public class CarteleraClienteFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private static final String TIPO = "tipo";
    private static final String FECHA = "fecha";
    private static final String TITULO = "titulo";
    private static final String DESCRIPCION = "descripcion";
    private static final String DIRECCION = "direccion";
    private static final String TELEFONO = "telefono";
    private static final String MAIL = "mail";
    private static final String TAG = "tag";

    private EditText tituloEd, descripcionEd, direccionEd, telefonoEd, mailEd;
    private Spinner tipos;

    private Button btnCargar, btnTraer;
    private TextView datosCuponera;
    private String tipoCuponera;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String documentId= db.collection("clientes").document().getId();


    public CarteleraClienteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cuponera, container, false);

        tituloEd = view.findViewById(R.id.ed_titulo_cuponera);
        descripcionEd = view.findViewById(R.id.ed_descripcion_cuponera);
        direccionEd = view.findViewById(R.id.inDireccion_Comercio);
        telefonoEd = view.findViewById(R.id.inTelefono_Comercio);
        mailEd = view.findViewById(R.id.inMail_Comercio);
        tipos = view.findViewById(R.id.spinn_tipo_cuponera);

        datosCuponera = view.findViewById(R.id.datos_cuponera);
        btnCargar = view.findViewById(R.id.btn_cargar_cuponera);
        btnTraer = view.findViewById(R.id.btn_traer_datos_cuponera);

        ArrayAdapter<CharSequence> adapterSpinCAtegorias = ArrayAdapter.createFromResource(getActivity(),
                R.array.combo_tipos_cuponera, android.R.layout.simple_spinner_item);

        tipos.setAdapter(adapterSpinCAtegorias);

        tipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoCuponera = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCargar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String tituloTxt = tituloEd.getText().toString().trim();
                String descripcionTxt = descripcionEd.getText().toString().trim();
                String mailTxt = mailEd.getText().toString().trim();
                String direccionTxt = direccionEd.getText().toString().trim();
                String telefonoTxt = telefonoEd.getText().toString().trim();

                if (tituloTxt.isEmpty() || direccionTxt.isEmpty() || telefonoTxt.isEmpty() || tipoCuponera.isEmpty()) {
                    return;
                }

                Map<String, Object> datosACargar = new HashMap<String, Object>();
                datosACargar.put(TIPO, tipoCuponera);
                datosACargar.put(TITULO, tituloTxt);
                datosACargar.put(DESCRIPCION, descripcionTxt);
                datosACargar.put(DIRECCION, direccionTxt);
                datosACargar.put(TELEFONO, telefonoTxt);
                datosACargar.put(MAIL, mailTxt);


                //esta instruccion es para poner un cliente con sub-coleccion cuponera...
                // db.collection("clientes").document("OPpLzJFx6CD8VilCvy4t").collection("cuponera").add(datosACargar)

                db.collection("clientes").document(documentId).collection("cuponera").add(datosACargar).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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

                db.collection("clientes").document(documentId).collection("cuponera").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String tipoCuponera = document.getString(TIPO);
                                String tituloTxt = document.getString(TITULO);
                                String direccionTxt = document.getString(DIRECCION);
                                String mailTxt = document.getString(MAIL);
                                String telefonoTxt = document.getString(TELEFONO);
                                String descripcionTxt = document.getString(DESCRIPCION);
                                datosCuponera.setText("UD. acaba de Publicar : " + tipoCuponera +"\n"+tituloTxt+"\n"+ descripcionTxt+
                                        "\n" + direccionTxt +
                                        "   \n     " + telefonoTxt +
                                        "   \n   " + mailTxt);

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

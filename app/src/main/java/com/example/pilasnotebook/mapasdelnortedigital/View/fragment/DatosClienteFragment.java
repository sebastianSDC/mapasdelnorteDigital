package com.example.pilasnotebook.mapasdelnortedigital.view.fragment;


/**
 * Creada por Sebastián Suárez Da Costa y Pablo Herrera.
 */


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.ContextCompat.checkSelfPermission;


public class DatosClienteFragment extends Fragment {


    private OnFragmentInteractionListener mListener; // interface
    String TAG = Constantes.TAG;

    private final String CARPETA_RAIZ = "MAPASDELNORTE/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "fotos";
    private String path;
    private File foto;
    private Uri filePath, uriFoto;


    private String categoriaTxt;

    private Zona zona;
    private Cliente cliente;
    private TextView datosTraidos;
    private EditText nombreEd, descripcionEd, direccionEd, localidadED,
            codigoPostalEd, provinciaEd, paisEd, telefonoEd, mailEd;
    protected ImageView fotodeContacto;
    private Button btnCargarFoto, btnCargar, btnTraer;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storage = FirebaseStorage.getInstance().getReference();

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
        btnCargarFoto = (Button) view.findViewById(R.id.btn_cargarFoto_comercio);
        if (validaPermisos()) {

            btnCargarFoto.setEnabled(true);
        } else {
            btnCargarFoto.setEnabled(false);

        }


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
                String descripcionTxt = descripcionEd.getText().toString().trim();
                String direccionTxt = direccionEd.getText().toString().trim();
                String localidadTxt = localidadED.getText().toString().trim();
                String codPostTxt = codigoPostalEd.getText().toString().trim();
                String provinciaTxt = provinciaEd.getText().toString().trim();
                String paisTxt = paisEd.getText().toString().trim();
                String telefonoTxt = telefonoEd.getText().toString().trim();
                String mailTxt = mailEd.getText().toString().trim();
                Integer foto = fotodeContacto.getId();
                cliente = new Cliente(nombreTxt, categoriaTxt, direccionTxt);
                //String fotoTxt = fotodeContacto.get;


                if (nombreTxt.isEmpty() || direccionTxt.isEmpty() || telefonoTxt.isEmpty() || categoriaTxt.isEmpty()) {

                    Toast.makeText(getActivity(), "debe completar los campos obligatorios", Toast.LENGTH_LONG).show();
                }

                Map<String, Object> datosACargar = new HashMap<String, Object>();
                datosACargar.put(Constantes.NOMBRE, nombreTxt);
                datosACargar.put(Constantes.DESCRIPCION, descripcionTxt);
                datosACargar.put(Constantes.CATEGORIA, categoriaTxt);
                datosACargar.put(Constantes.DIRECCION, direccionTxt);
                datosACargar.put(Constantes.LOCALIDAD, localidadTxt);
                datosACargar.put(Constantes.CODIGO_POSTAL, codPostTxt);
                datosACargar.put(Constantes.PROVINCIA, provinciaTxt);
                datosACargar.put(Constantes.PAIS, paisTxt);
                datosACargar.put(Constantes.TELEFONO, telefonoTxt);
                datosACargar.put(Constantes.MAIL, mailTxt);

                subirAStorageUri(filePath);


                db.collection("clientes").add(datosACargar).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                }).

                        addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        });

        btnTraer.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View v) {

                db.collection("clientes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String nombreTxt = document.getString(Constantes.NOMBRE);
                                String descripcionTxt = document.getString(Constantes.DESCRIPCION);
                                String categoriaTxt = document.getString(Constantes.CATEGORIA);
                                String direccionTxt = document.getString(Constantes.DIRECCION);
                                String localidadTxt = document.getString(Constantes.LOCALIDAD);
                                String codPostTxt = document.getString(Constantes.CODIGO_POSTAL);
                                String provinciaTxt = document.getString(Constantes.PROVINCIA);
                                String paisTxt = document.getString(Constantes.PAIS);
                                String telefonoTxt = document.getString(Constantes.TELEFONO);
                                String mailTxt = document.getString(Constantes.MAIL);
                                String fotoTxt = document.getString(Constantes.FOTO);

                                datosTraidos.setText("DATOS CARGADOS : " + "\n" +
                                        "Foto:  " + fotoTxt + "\n"
                                        + nombreTxt + "\n"
                                        + descripcionTxt + "\n"
                                        + categoriaTxt + "   \n     "
                                        + direccionTxt + "   \n     "
                                        + localidadTxt + "   \n   "
                                        + codPostTxt + "   \n   "
                                        + provinciaTxt + "   \n   "
                                        + paisTxt + "   \n   "
                                        + telefonoTxt + "   \n   "
                                        + mailTxt);

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

            }


        });

        btnCargarFoto.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View v) {
                cargarFoto();
            }
        });

        return view;
    }

    private boolean validaPermisos() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if ((checkSelfPermission(getActivity(), CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }
        if ((shouldShowRequestPermissionRationale(CAMERA)) || (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))) {
            cargarDialogoRecomendacion();
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                btnCargarFoto.setEnabled(true);
            } else {
                solicitarPermisosManual();
            }
        }


    }

    private void solicitarPermisosManual() {
        final CharSequence[] OPCIONES_CARGAR_FOTO = {"Si", "No"};
        final AlertDialog.Builder alertOPCIONES = new AlertDialog.Builder(getActivity());
        alertOPCIONES.setTitle("Desea configurar los permisos de forma manual?");
        alertOPCIONES.setItems(OPCIONES_CARGAR_FOTO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (OPCIONES_CARGAR_FOTO[i].equals("Si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), "DatosClienteFragment");
                    intent.setData(uri);
                    startActivity(intent);
                } else {

                    Toast.makeText(getActivity(), "Los Permisos no fueron Aceptados", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        alertOPCIONES.show();
    }

    private void cargarDialogoRecomendacion() {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(getActivity());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
            }
        });
        dialogo.show();
    }

    public void cargarFoto() {

        final CharSequence[] OPCIONES_CARGAR_FOTO = {"Tomar Foto", "Abrir Galeria", "Cancelar"};
        final AlertDialog.Builder alertOPCIONES = new AlertDialog.Builder(getActivity());
        alertOPCIONES.setTitle("Seleccione una Opcion");
        alertOPCIONES.setItems(OPCIONES_CARGAR_FOTO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (OPCIONES_CARGAR_FOTO[i].equals("Tomar Foto")) {
                    abrirCamara();
                } else {
                    if (OPCIONES_CARGAR_FOTO[i].equals("Abrir Galeria")) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                .setType("image/");
                        startActivityForResult(Intent.createChooser(intent, "Seleccione la aplicación"), Constantes.COD_GALERIA);
                    } else {
                        dialog.dismiss();
                    }
                }
            }
        });
        alertOPCIONES.show();
    }

    private void abrirCamara() {
        File fileFoto = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        boolean isCreada = fileFoto.exists();
        String nombreFoto = "";

        if (isCreada == false) {
            isCreada = fileFoto.mkdirs();

        }
        if (isCreada == true) {
            nombreFoto = (System.currentTimeMillis() / 1000) + ".jpg";
        }

        path = Environment.getExternalStorageDirectory() + File.separator + RUTA_IMAGEN + File.separator + nombreFoto;

        File foto = new File(path);
        uriFoto = Uri.fromFile(foto);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
        startActivityForResult(intent, Constantes.COD_CAMARA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case Constantes.COD_GALERIA:
                    filePath = data.getData();
                    fotodeContacto.setImageURI(filePath);
                    subirAStorageUri(filePath);
                    break;

                case Constantes.COD_CAMARA:
                    MediaScannerConnection.scanFile(getActivity(), new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    uriFoto = data.getData();
                                    fotodeContacto.setImageURI(uriFoto);
                                    subirAStorageUri(uriFoto);

                                }
                            });break;


            }
        }
    }


    public void subirAStorageUri(Uri uriFoto) {

        if (filePath != null && filePath != uriFoto) {

            final StorageReference storageRef = storage.child("fotos/" + filePath.getLastPathSegment());
            storageRef.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw new Exception();
                    }

                    return storageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                Map<String, Object> datosACargar = new HashMap<String, Object>();


                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        Uri downloadLink = task.getResult();

                        datosACargar.put(Constantes.FOTO, downloadLink);

                    }

                }
            });
        } else {
            final StorageReference storageRef = storage.child("fotos/" + uriFoto.getLastPathSegment());
            storageRef.putFile(uriFoto).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw new Exception();
                    }

                    return storageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                Map<String, Object> datosACargar = new HashMap<String, Object>();


                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        Uri downloadLink = task.getResult();

                        datosACargar.put(Constantes.FOTO, downloadLink);
                    }
                }
            });
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}


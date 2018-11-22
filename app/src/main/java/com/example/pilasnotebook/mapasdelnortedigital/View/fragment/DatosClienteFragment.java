package com.example.pilasnotebook.mapasdelnortedigital.view.fragment;


/**
 * Creada por Sebastián Suárez Da Costa y Pablo Herrera.
 */


import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.ContextCompat.checkSelfPermission;


public class DatosClienteFragment extends Fragment {


    private OnFragmentInteractionListener mListener; // INTERFACE
    String TAG = Constantes.TAG;

    private final String CARPETA_RAIZ = "MAPASDELNORTE/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "fotos";
    private String path;
    private Uri fotoPerfilUri;

    private final Map<String, Object> datosACargar = new HashMap<String, Object>();
    private String categoriaTxt;

    //private Zona zona;
    private Cliente cliente;
    private TextView datosTraidos;

    private EditText nombreEd, descripcionEd, direccionEd, localidadED,
            codigoPostalEd, provinciaEd, paisEd, telefonoEd, mailEd;

    protected ImageView fotodeContacto;
    private Button btnCargarFoto, btnCargar, btnTraer;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storage = FirebaseStorage.getInstance().getReference();

    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private String pickerPath;
    //String documentId= db.collection("clientes").document().getId();
    protected Spinner categorias;

    private ProgressDialog dialog;

    //private Geocoder geocoder;


    public DatosClienteFragment() {
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

        // CAMPOS DEL FORMULARIO
        nombreEd = (EditText) view.findViewById(R.id.ed_nombre_comercio);
        descripcionEd = (EditText) view.findViewById(R.id.ed_descripcion_comercio);
        direccionEd = (EditText) view.findViewById(R.id.ed_direccion_comercio);
        localidadED = (EditText) view.findViewById(R.id.ed_localidad_comercio);
        codigoPostalEd = (EditText) view.findViewById(R.id.ed_codigoPostal_comercio);
        provinciaEd = (EditText) view.findViewById(R.id.ed_provincia_comercio);
        paisEd = (EditText) view.findViewById(R.id.ed_pais_comercio);
        telefonoEd = (EditText) view.findViewById(R.id.ed_telefono_comercio);
        mailEd = (EditText) view.findViewById(R.id.ed_mail_comercio);

        categorias = (Spinner) view.findViewById(R.id.spinn_categorias_comercio);
        fotodeContacto = (ImageView) view.findViewById(R.id.imagen_contacto_comercio);
        datosTraidos = (TextView) view.findViewById(R.id.datos_comercio);

        // BOTONES DE FORMULARIO
        btnCargar = (Button) view.findViewById(R.id.btn_cargar_datos_comercio);
        btnTraer = (Button) view.findViewById(R.id.btn_traer_datos_comercio);
        btnCargarFoto = (Button) view.findViewById(R.id.btn_cargarFoto_comercio);
        if (validaPermisos()) {
            btnCargarFoto.setEnabled(true);
        } else {
            btnCargarFoto.setEnabled(false);
        }

        dialog = new ProgressDialog(getActivity());

       /* // TOMAR FOTO DE GALERIA
        imagePicker = new ImagePicker(DatosClienteFragment.this);
        //imagePicker.setQuality(100);
        //imagePicker.ensureMaxSize(400, 400);
        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                //if (!list.isEmpty()) {
                path = list.get(0).getOriginalPath();
                fotoPerfilUri = Uri.parse(path);
                fotodeContacto.setImageURI(fotoPerfilUri);
                // }
            }

            @Override
            public void onError(String s) {
                Toast.makeText(getContext(), "Error: " + s, Toast.LENGTH_SHORT).show();
            }
        });

        // TOMAR FOTO DE CAMARA
        cameraPicker = new CameraImagePicker(DatosClienteFragment.this);
        cameraPicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_PUBLIC_DIR);
        //cameraPicker.setQuality(100);
        //cameraPicker.ensureMaxSize(400, 400);
        cameraPicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                //if (!list.isEmpty()) {
                path = list.get(0).getOriginalPath();
                Uri uri = Uri.fromFile(new File(path));
                fotodeContacto.setImageURI(uri);
                //}
            }

            @Override
            public void onError(String s) {
                Toast.makeText(getContext(), "Error: " + s, Toast.LENGTH_SHORT).show();
            }
        });
*/

        // LOGICA DEL SPINNER
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

// LOGICA DEL BOTON CARGAR DATOS
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

                cliente = new Cliente(nombreTxt, direccionTxt, telefonoTxt);

                if (nombreTxt.isEmpty() || direccionTxt.isEmpty() || telefonoTxt.isEmpty()) {

                    Toast.makeText(getActivity(), "debe completar los campos obligatorios", Toast.LENGTH_LONG).show();
                }

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
                // datosACargar.put(Constantes.FOTO, fotoPerfilUri.getLastPathSegment());


                db.collection("clientes").add(datosACargar).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
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

        btnCargarFoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                seleccionarFoto();
            }
        });

        return view;
    }

    private void seleccionarFoto() {
        final CharSequence[] option = {"Tomar foto", "Abrir Galería", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Eleige una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    case 0://"Tomar foto"
                        openCamera();
                        //pickerPath = cameraPicker.pickImage();

                        break;
                    case 1: //"Abrir Galeriía"
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent, "Abrir con..."), Constantes.COD_GALERIA);
                        //imagePicker.pickImage();

                }
            }
        });

        builder.show();
    }

    private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            path = Environment.getExternalStorageDirectory() + File.separator + RUTA_IMAGEN
                    + File.separator + imageName;

            File newFile = new File(path);
            fotoPerfilUri = Uri.fromFile(newFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoPerfilUri);
            startActivityForResult(intent, Constantes.COD_CAMARA);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path"/*"pickerPath"*/, path);
    }

   /* @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        path = savedInstanceState.getString("file_path");
    }
*/

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("file_path")) {
                path = savedInstanceState.getString("file_path");
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constantes.COD_CAMARA:
                    MediaScannerConnection.scanFile(getActivity(),
                            new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);

                                    //Bitmap bitmap = BitmapFactory.decodeFile(path);
                                    //fotodeContacto.setImageBitmap(bitmap);
                                    //redimensionarImagen(bitmap, 400, 400);
                                    //convertirDeBitmapUri(getActivity(),bitmap);
                                    fotoPerfilUri = Uri.parse(path);
                                    fotodeContacto.setImageURI(fotoPerfilUri);
                                }
                            });
                    //subirAStorageUri(fotoPerfilUri);
                    break;

                case Constantes.COD_GALERIA:
                    fotoPerfilUri = data.getData();
                    fotodeContacto.setImageURI(fotoPerfilUri);
                    //subirAStorageUri(fotoPerfilUri);
                    break;

            }subirAStorageUri(fotoPerfilUri);

       /*if(resultCode==RESULT_OK){
           switch (requestCode){
               case Picker.PICK_IMAGE_CAMERA:
                   cameraPicker.reinitialize(pickerPath);
                   cameraPicker.submit(data);
                   break;
               case Picker.PICK_IMAGE_DEVICE:
                   imagePicker.submit(data);
                   fotoPerfilUri = data.getData();
           }
            subirAStorageUri(fotoPerfilUri);*/
        }
    }



    public void subirAStorageUri(Uri uri) {

        if (uri == null) {
            Toast.makeText(getContext(), "No es posible cargar esta imagen", Toast.LENGTH_SHORT).show();


        } else if (uri != null) {

            String nombreFoto = "";
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss", Locale.getDefault());
            nombreFoto = simpleDateFormat.format(date);
            final StorageReference storageRef = storage.child("fotos/").child("datos" + nombreFoto);
            storageRef.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {

                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Toast.makeText(getContext(), "uri no es nulo" + task, Toast.LENGTH_SHORT).show();
                    if (task.isSuccessful()) {

                        Uri urifoto = task.getResult();


                        datosACargar.put(Constantes.FOTO, urifoto.toString());
                        Toast.makeText(getContext(), "sube al storage y al firestore exitosamente" + task, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    // LOGICA DE CONVERTIR DIRECCION A LATLNG
   /* public void getLocationFromAddress(String strAddress) {

        List<Address> address;

        try {
            address = geocoder.getFromLocationName(strAddress, 1);
            Address dirInicial = address.get(0);
            double latitud = dirInicial.getLatitude();
            double longitud = dirInicial.getLongitude();
            datosACargar.put("coordenadas", latitud + longitud);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //TODO: ver si Latlng es una clase de java o no.*/

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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

     /*public Uri convertirDeBitmapUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();

        if (ancho > anchoNuevo || alto > altoNuevo) {
            float escalaAncho = anchoNuevo / ancho;
            float escalaAlto = altoNuevo / alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho, escalaAlto);

            return Bitmap.createBitmap(bitmap, 0, 0, ancho, alto, matrix, false);

        } else {
            return bitmap;
        }
    }
*/
}
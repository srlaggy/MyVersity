package com.example.myversity;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myversity.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class ConfiguracionFragment extends Fragment{

    public ConfiguracionFragment() {
    }

    public static ConfiguracionFragment newInstance() {
        ConfiguracionFragment fragment = new ConfiguracionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuracion, container, false);

        ActivityResultLauncher<Intent> sActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri fileExportUri = data.getData();
                        String ruta = Utils.buscarRuta(fileExportUri, getActivity());

                        if(Utils.nombreValido(ruta)){
                            Boolean estado = ImportarAsignaturaFragment.importarAsignaturas(ruta, getActivity());
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Archivo con formato incorrecto", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Problema al importar asignatura", Toast.LENGTH_LONG).show();
                    }
                }
            }
        );

        List<String> opcionesConfiguracion = Arrays.asList(getString(R.string.opcion_1_config), getString(R.string.opcion_2_config), getString(R.string.opcion_3_config));
        ArrayAdapter adapterConfiguracion = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.list_configuracion, opcionesConfiguracion);
        ListView lvConfiguracion = (ListView) view.findViewById(R.id.lista_configuraciones);
        lvConfiguracion.setAdapter(adapterConfiguracion);
        lvConfiguracion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    // CASO CONFIGURACION INICIAL
                    Activity activity = getActivity();
                    if (activity instanceof MainActivity){
                        ((MainActivity) activity).replaceFragment(new ConfiguracionInicialFragment(), ((MainActivity) activity).getSupportFragmentManager(), R.id.framecentral);
                        activity.setTitle(getString(R.string.title_opcion_1_config));
                        ((MainActivity) activity).setFragmentActual(getString(R.string.title_opcion_1_config));
                        ((MainActivity) activity).setActionBarActivityArrow(true);
                    }
                } else if(i == 1){
                    // CASO EXPORTAR ASIGNATURA
                    Activity activity = getActivity();
                    if (activity instanceof MainActivity){
                        ((MainActivity) activity).replaceFragment(new ExportarAsignaturaFragment(), ((MainActivity) activity).getSupportFragmentManager(), R.id.framecentral);
                        activity.setTitle(getString(R.string.title_opcion_2_config));
                        ((MainActivity) activity).setFragmentActual(getString(R.string.title_opcion_2_config));
                        ((MainActivity) activity).setActionBarActivityArrow(true);
                    }
                } else {
                    // CASO IMPORTAR ASIGNATURA
                    Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    data.setType("*/*");
                    data = Intent.createChooser(data, "Elige un archivo de MyVersity para importar");
                    sActivityResultLauncher.launch(data);
                }
            }
        });

        return view;
    }

}

// Toast.makeText(getActivity().getApplicationContext(), "La configuración de notas fue actualizada", Toast.LENGTH_LONG).show();

// FALTA
// -> AL BORRAR NOTA, SE DEBIESE DISMINUIR LA CANTIDAD EN LA EVALUACION
// EXTRA
// -> LAS NOTAS AL COMIENZO SERAN 0? (DUDA)

// PENDIENTES (NO DEPENDE DE MI)
// -> CUANDO SE LLAME A LA CONFIG INICIAL DESDE ASIGNATURAS, SE LE DEBE QUITAR LA FLECHA HACIA ATRAS (<-)
//     -> ADEMAS SE LE DEBE ENTREGAR EN EL CONSTRUCTOR DEL FRAME EL ESTADO 1 (EL COMPORTAMIENTO YA ESTA MANEJADO)
//     -> HAY QUE VER COMO MANEJAR LA ASIGNATURA Y SU INSERCION -> SE GUARDARA EL NOMBRE Y LOS DATOS EN UNA VARIABLE GLOBAL DE ACTIVITY? SE INSERTARA DESDE LA VISTA DE CONFIGURACION INICIAL?

// ASIGNATURAS
// -> ARREGLAR BUG DE QUE PRESIONO FUERA DEL DIALOG Y AL PRESIONAR EN EL FAB, SE CIERRA LA APP
// -> AGREGAR VALIDADOR EN EL NOMBRE DE LA ASIGNATURA
// -> VALIDADOR PARA QUE NO SE REPITAN ASIGNATURAS, COMPARANDO VARIACIONES EN MAYUSCULAS Y MINUSCULAS
// -> AGREGAR METODO QUE DEJE LA PRIMERA LETRA EN MAYUSCULA Y EL RESTO EN MINUSCULA (PARA LISTA DE ASIGNATURAS)
// -> AGREGAR METODO QUE DEJE TODA LA PALABRA EN MAYUSCULA (PARA TITULO EN LA BARRA)
// -> AGREGAR DIALOG QUE PREGUNTE TIPO DE PROMEDIO Y LUEGO PREGUNTE CONFIG INICIAL
// -> CREAR TODA LA LOGICA DE LA VISTA DE LAS ASIGNATURAS

// FILES DE IMPORT EN MI EMULADOR
// -> 0.96kb 7 19pm -> SOLO BDA
// -> 1.60kb 6 20pm -> AMBAS
// -> 25b 3 53pm    -> TEXTO DE PRUEBA -> ERROR

// CODIGO
//DbConfigInicial dbConfigInicial = new DbConfigInicial(getActivity().getApplicationContext());
//    ArrayList<ConfiguracionInicial> listaConfigs = dbConfigInicial.buscarConfiguraciones();
//                    if(!listaConfigs.isEmpty()){
//                            for(ConfiguracionInicial x : listaConfigs){
//                            System.out.println(x);
//                            }
//                            } else {
//                            System.out.println("No hay valores en la lista");
//                            }
//                            Toast.makeText(getActivity().getApplicationContext(), "Coming soon...", Toast.LENGTH_LONG).show();
//                            dbConfigInicial.close();

// DbConfigInicial dbConfigInicial = new DbConfigInicial(getActivity().getApplicationContext());
//                    ConfiguracionInicial configUltima = dbConfigInicial.buscarPrimeraConfiguracion();
//                    if(configUltima != null){
//                        System.out.println(configUltima);
//                    } else {
//                        System.out.println("No existen registros");
//                    }
//                    Toast.makeText(getActivity().getApplicationContext(), "Coming soon...", Toast.LENGTH_LONG).show();
//                    dbConfigInicial.close();
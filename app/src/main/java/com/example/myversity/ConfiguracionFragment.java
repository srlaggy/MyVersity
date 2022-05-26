package com.example.myversity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myversity.db.DbConfigInicial;
import com.example.myversity.entidades.ConfiguracionInicial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfiguracionFragment extends Fragment{
    public ConfiguracionFragment() {
        // Required empty public constructor
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
                    DbConfigInicial dbConfigInicial = new DbConfigInicial(getActivity().getApplicationContext());
                    ArrayList<ConfiguracionInicial> listaConfigs = dbConfigInicial.buscarConfiguraciones();
                    if(!listaConfigs.isEmpty()){
                        for(ConfiguracionInicial x : listaConfigs){
                            System.out.println(x);
                        }
                    } else {
                        System.out.println("No hay valores en la lista");
                    }
                    Toast.makeText(getActivity().getApplicationContext(), "Coming soon...", Toast.LENGTH_LONG).show();
                    dbConfigInicial.close();
                } else {
                    // CASO IMPORTAR ASIGNATURA
                    DbConfigInicial dbConfigInicial = new DbConfigInicial(getActivity().getApplicationContext());
                    ConfiguracionInicial configUltima = dbConfigInicial.buscarPrimeraConfiguracion();
                    if(configUltima != null){
                        System.out.println(configUltima);
                    } else {
                        System.out.println("No existen registros");
                    }
                    Toast.makeText(getActivity().getApplicationContext(), "Coming soon...", Toast.LENGTH_LONG).show();
                    dbConfigInicial.close();
                }
            }
        });

        return view;
    }
}

// Toast.makeText(getActivity().getApplicationContext(), "La configuración de notas fue actualizada", Toast.LENGTH_LONG).show();

// FALTA
// -> AL EXPORTAR HAY QUE VER QUE EXPORTAR Y COMO GUARDARLO
// -> AL IMPORTAR HAY QUE VER COMO SOBREESCRIBIR LOS IDS DE LAS RELACIONES Y LOS FLUJOS
//      -> SOBREESCRIBIR LAS NOTAS AL MINIMO
// EXTRA
// -> LAS NOTAS AL COMIENZO SERAN 0? (DUDA)
// -> SE AGREGARAN METODOS CRUD PARA ACTUALIZAR EL ID? (PARA CASO IMPORT)

// PENDIENTES (NO DEPENDE DE MI)
// -> CUANDO SE LLAME A LA CONFIG INICIAL DESDE ASIGNATURAS, SE LE DEBE QUITAR LA FLECHA HACIA ATRAS (<-)
//     -> ADEMAS SE LE DEBE ENTREGAR EN EL CONSTRUCTOR DEL FRAME EL ESTADO 1 (EL COMPORTAMIENTO YA ESTA MANEJADO)
//     -> HAY QUE VER COMO MANEJAR LA ASIGNATURA Y SU INSERCION -> SE GUARDARA EL NOMBRE Y LOS DATOS EN UNA VARIABLE GLOBAL DE ACTIVITY? SE INSERTARA DESDE LA VISTA DE CONFIGURACION INICIAL?
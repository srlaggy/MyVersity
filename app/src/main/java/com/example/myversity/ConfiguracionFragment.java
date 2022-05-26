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

import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.db.DbConfigInicial;
import com.example.myversity.entidades.Asignaturas;
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

                    /* DbAsignaturas dbAsignaturas = new DbAsignaturas(getActivity().getApplicationContext());
                    ArrayList<Asignaturas> listaAsign = dbAsignaturas.buscarAsignaturas();
                    if(!listaAsign.isEmpty()){
                        for(Asignaturas a : listaAsign){
                            System.out.println(a);
                        }
                    } else {
                        System.out.println("No hay valores en la lista de asignaturas");
                    }
                    dbAsignaturas.close(); */

                } else {
                    // CASO IMPORTAR ASIGNATURA
                    DbConfigInicial dbConfigInicial = new DbConfigInicial(getActivity().getApplicationContext());
                    ConfiguracionInicial configUltima = dbConfigInicial.buscarUltimaConfiguracion();
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

// FALTA:
// -> FUNCIONALIDAD DE IMPORTAR Y EXPORTAR ASIGNATURA

// Toast.makeText(getActivity().getApplicationContext(), "La configuración de notas fue actualizada - " + id.toString(), Toast.LENGTH_LONG).show();

//    DbConfigInicial dbConfigInicial = new DbConfigInicial(getActivity().getApplicationContext());
//    ArrayList<ConfiguracionInicial> listaConfigs = dbConfigInicial.buscarConfiguraciones();
//    for(ConfiguracionInicial x : listaConfigs){
//        System.out.println(x);
//    }

//    DbConfigInicial dbConfigInicial = new DbConfigInicial(getActivity().getApplicationContext());
//    ConfiguracionInicial configUltima = dbConfigInicial.buscarUltimaConfiguracion();
//    System.out.println(configUltima);

// NUEVA REUNION
// -> HACER QUE CONFIG INICIAL SOBREESCRIBA SOBRE EL ID 1 (1 -> CONFIG POR DEFECTO | OTROS SON IMPORTS Y ESO)
// -> CREAR FLAG QUE PERMITA SABER SI SE INGRESA A LA CONFIG DESDE CONFIGURACIONES O DESDE POPUP DE CREACION DE ASIGNATURAS
//      -> PARA CUANDO SEA TRUE, NO DEBE SALIR LA FLECHA (<-) Y DEBE APARECER UN BOTON CANCELAR QUE LLEVE A LA SECCION DE ASIGNATURAS (AL GUARDAR SE DEBE GUARDAR EN BD (GUARDAR NOMBRE DE ASIGNATURA EN VARIABLE GLOBAL DE ACTIVITY) Y REDIRIGIR A LA SECCION DE ASIGNATURAS)
// -> AL EXPORTAR HAY QUE VER QUE EXPORTAR Y COMO GUARDARLO
// -> AL IMPORTAR HAY QUE VER COMO SOBREESCRIBIR LOS IDS DE LAS RELACIONES Y LOS FLUJOS
//      -> SOBREESCRIBIR LAS NOTAS AL MINIMO
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
                    Toast.makeText(getActivity().getApplicationContext(), "Coming soon...", Toast.LENGTH_LONG).show();
                } else {
                    // CASO IMPORTAR ASIGNATURA
                    Toast.makeText(getActivity().getApplicationContext(), "Coming soon...", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}

// FALTA:
// -> QUE SE GUARDEN LOS VALORES DESDE LA BASE DE DATOS Y SE MUESTREN EN PANTALLA SI ES QUE EXISTEN
// -> CREAR BASE DE DATOS O ARCHIVO QUE ALMACENE LA CONFIGURACION INICIAL

// -> FUNCIONALIDAD DE IMPORTAR Y EXPORTAR ASIGNATURA
// -> AL PRESIONAR FLECHA HACIA ATRAS, SE DEBIESE VOLVER A UNA VISTA VACIA SIN VALORES? O SE DEBEN GUARDAR LOS VALORES
//    ANTES PUESTOS?
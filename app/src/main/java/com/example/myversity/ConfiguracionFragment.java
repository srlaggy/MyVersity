package com.example.myversity;

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

        List<String> opcionesConfiguracion = Arrays.asList("Configuración inicial", "Exportar Asignatura", "Importar Asignatura");
        ArrayAdapter adapterConfiguracion = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.list_configuracion, opcionesConfiguracion);
        ListView lvConfiguracion = (ListView) view.findViewById(R.id.lista_configuraciones);
        lvConfiguracion.setAdapter(adapterConfiguracion);
        lvConfiguracion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    // CASO CONFIGURACION INICIAL
                    Fragment fragment = new ConfiguracionInicialFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.framecentral, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
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
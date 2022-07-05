package com.example.myversity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.myversity.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class EditarAsignaturaFragment extends Fragment{

    public EditarAsignaturaFragment() {
    }

    public static EditarAsignaturaFragment newInstance() {
        EditarAsignaturaFragment fragment = new EditarAsignaturaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuracion, container, false);

        List<String> opcionesConfiguracion = Arrays.asList("Nombre", "Tipo de promedio", getString(R.string.opcion_3_config));
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
                }
            }
        });

        return view;
    }

}
package com.example.myversity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.db.DbConfigInicial;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

public class AsignaturasFragment extends Fragment {
    EditText input;
    FloatingActionButton fab;

    // ----------------------------------------------------------------//
    public AsignaturasFragment() {
        // Required empty public constructor
    }

    public static AsignaturasFragment newInstance() {
        AsignaturasFragment fragment = new AsignaturasFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asignaturas, container, false);
        View viewPopup = getLayoutInflater().inflate(R.layout.fragment_asignaturas_popup, null);

        input = (EditText) viewPopup.findViewById(R.id.nombre_asignatura_popup);

        // ---- LISTA ASIGNATURAS ---- //
        List<String> opcionesConfiguracion = Arrays.asList(getString(R.string.asign1), getString(R.string.asign2), getString(R.string.asign3));
        ArrayAdapter adapterConfiguracion = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.list_actividades, opcionesConfiguracion);
        ListView lvConfiguracion = (ListView) view.findViewById(R.id.lista_asignaturas);
        lvConfiguracion.setAdapter(adapterConfiguracion);


        // ---- BOTÓN Y POPUP ----- //
        fab = (FloatingActionButton) view.findViewById(R.id.botonAgregarAsignatura);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                builder.setView(viewPopup);

                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();

                        // almacenar esto luego en la bd del usuario
                        DbAsignaturas dbAsignaturas = new DbAsignaturas(getActivity().getApplicationContext());
                        Long idAux = dbAsignaturas.crearAsignatura(1, 1, m_Text);
                        dbAsignaturas.close();
                        if(idAux != 0){
                            Toast.makeText(getActivity().getApplicationContext(), "Asignatura creada!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Error al crear asignatura", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        return view;
    }
}
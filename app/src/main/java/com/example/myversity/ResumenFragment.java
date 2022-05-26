package com.example.myversity;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.db.DbConfigInicial;
import com.example.myversity.db.DbHelper;
import com.example.myversity.entidades.ConfiguracionInicial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResumenFragment extends Fragment {

    public ResumenFragment() {
        // Required empty public constructor
    }

    public static ResumenFragment newInstance() {
        ResumenFragment fragment = new ResumenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resumen, container, false);

        ListView listaResumenes = (ListView) view.findViewById(R.id.lista_resumenes);

        DbAsignaturas dbAsignaturas = new DbAsignaturas(this.getContext());

        listaResumenes.setAdapter(new ResumenAdaptador(this.getContext(),dbAsignaturas.buscarAsignaturas()));

        return view;
    }
}
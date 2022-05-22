package com.example.myversity;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResumenFragment extends Fragment {

    String[][] dato = {
            {"A","50"},
            {"B","70"}
    };

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

        listaResumenes.setAdapter(new ResumenAdaptador(this.getContext(),dato));

        return view;
    }
}
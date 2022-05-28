package com.example.myversity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ImportarAsignaturaFragment extends Fragment {
    public ImportarAsignaturaFragment() {
    }

    public static ImportarAsignaturaFragment newInstance() {
        ImportarAsignaturaFragment fragment = new ImportarAsignaturaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_importar_asignatura, container, false);
        return view;
    }
}
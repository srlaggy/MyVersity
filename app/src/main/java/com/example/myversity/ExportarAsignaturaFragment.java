package com.example.myversity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExportarAsignaturaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExportarAsignaturaFragment extends Fragment {
    public ExportarAsignaturaFragment() {
    }

    public static ExportarAsignaturaFragment newInstance(String param1, String param2) {
        ExportarAsignaturaFragment fragment = new ExportarAsignaturaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exportar_asignatura, container, false);
        return view;
    }
}
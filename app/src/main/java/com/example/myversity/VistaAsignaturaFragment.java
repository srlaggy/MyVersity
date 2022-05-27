package com.example.myversity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VistaAsignaturaFragment extends Fragment {

    public VistaAsignaturaFragment() {
    }
    // TODO: Rename and change types and number of parameters
    public static VistaAsignaturaFragment newInstance() {
        VistaAsignaturaFragment fragment = new VistaAsignaturaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_asignatura, container, false);

        Integer id_asign = AsignaturasFragment.getId_Asignatura();
        String name_asign = AsignaturasFragment.getName_Asignatura();
        System.out.println("NOMBRE ASIGNATURA ->");
        System.out.println(name_asign);

        TextView nameView = view.findViewById(R.id.id_nombre_asignatura);
        nameView.setText(name_asign);

        return view;
    }
}
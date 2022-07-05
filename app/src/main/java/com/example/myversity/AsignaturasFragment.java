package com.example.myversity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.entidades.Asignaturas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AsignaturasFragment extends Fragment {
    EditText input;
    FloatingActionButton fab;
    static Integer id_Asignatura, id_config_Asignatura;
    static String name_Asignatura;
    static Asignaturas asignatura_seleccionada;
    private FloatingActionButton btnFragment;
    public static String nombre_Asignatura_ingresada;
    public static String tipoPromedio_ingresada;

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

        // ---- LISTA ASIGNATURAS ---- //
        DbAsignaturas dbAsignaturas = new DbAsignaturas(getActivity().getApplicationContext());
        List<Asignaturas> listaAsignaturas = dbAsignaturas.buscarAsignaturas();
        dbAsignaturas.close();

        // SE VERIFICA SI HAY DATOS O NO
        List<String> nombreAsignaturas = new ArrayList<>();
        if( listaAsignaturas.size() == 0){
            System.out.println("NO HAY ASIGNATURAS");
        }else{
            TextView view_null = (TextView) view.findViewById(R.id.view_null_asign);
            view_null.setText("");
            for (Asignaturas a : listaAsignaturas){
                    nombreAsignaturas.add(a.getNombre());
            }
            System.out.println(listaAsignaturas);
            System.out.println("LISTA NOMBREASIGNATURAS:");
            System.out.println(nombreAsignaturas);

            ArrayAdapter adapterAsignaturas = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.list_actividades, nombreAsignaturas);
            ListView lvAsignaturas = (ListView) view.findViewById(R.id.lista_asignaturas);
            lvAsignaturas.setAdapter(adapterAsignaturas);

            // Se transforma la ListView en una Lista clickeable
            lvAsignaturas.setLongClickable(true);
            lvAsignaturas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    name_Asignatura = listaAsignaturas.get(position).getNombre();
                    id_Asignatura = listaAsignaturas.get(position).getId();
                    asignatura_seleccionada = listaAsignaturas.get(position);

                    Activity activity = getActivity();
                    if (activity instanceof MainActivity){
                        ((MainActivity) activity).replaceFragment(new VistaAsignaturaFragment(), ((MainActivity) activity).getSupportFragmentManager(), R.id.framecentral);
                        activity.setTitle(name_Asignatura);
                        ((MainActivity) activity).setFragmentActual(name_Asignatura);
                        ((MainActivity) activity).setActionBarActivityArrow(true);
                    }
                }
            });

            lvAsignaturas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    asignatura_seleccionada = listaAsignaturas.get(position);
                    name_Asignatura = listaAsignaturas.get(position).getNombre();
                    id_Asignatura = listaAsignaturas.get(position).getId();
                    id_config_Asignatura = listaAsignaturas.get(position).getId_configInicial();

                    DialogFragmentOpciones dialogFragmentOpciones = new DialogFragmentOpciones();

                    Bundle args = new Bundle();
                    args.putString("name", name_Asignatura);
                    args.putInt("id", id_Asignatura);
                    args.putInt("idConfig", id_config_Asignatura);
                    dialogFragmentOpciones.setArguments(args);

                    dialogFragmentOpciones.show(getActivity().getSupportFragmentManager(), "My  Fragment Options");

                    return true;
                }
            });
        }

        // ---- BOTÓN DIALOG FRAGMENT ---- //
        btnFragment = (FloatingActionButton) view.findViewById(R.id.botonAgregarAsignatura);
        btnFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ---- AVANZAR AL DIALOGO 1 DE 3 ---- //
                DialogFragment dialogFragment1 = new DialogFragment();
                dialogFragment1.show(getActivity().getSupportFragmentManager(), "My  Fragment");
            }
        });

        return view;
    }

    public static Integer getId_Asignatura() {
        return id_Asignatura;
    }

    public static String getName_Asignatura() {
        return name_Asignatura;
    }

    // Nombre asignatura ingresada por el botón de agregar
    public static void setNombre_Asignatura_ingresada(String n){nombre_Asignatura_ingresada = n;}
    public static String getNombre_Asignatura_ingresada(){return nombre_Asignatura_ingresada;}

    // obtener y setear el tipo de promedio
    public static String getTipoPromedio_ingresada(){return tipoPromedio_ingresada;}
    public static void setTipoPromedio_ingresada(String t){tipoPromedio_ingresada = t;}

    // obtener y setear una asignatura
    public static Asignaturas getAsignatura_seleccionada() {
        return asignatura_seleccionada;
    }
    public static void setAsignatura_seleccionada(Asignaturas a) {
        asignatura_seleccionada = a;
    }
}
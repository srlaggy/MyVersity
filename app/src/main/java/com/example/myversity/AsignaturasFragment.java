package com.example.myversity;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.entidades.Asignaturas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AsignaturasFragment extends Fragment {
    EditText input;
    FloatingActionButton fab;
    static Integer id_Asignatura;
    static String name_Asignatura;
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
            TextView view_null = (TextView) view.findViewById(R.id.view_null_asign);
        }else{
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
            lvAsignaturas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    name_Asignatura = listaAsignaturas.get(position).getNombre();
                    id_Asignatura = listaAsignaturas.get(position).getId();

                    Activity activity = getActivity();
                    if (activity instanceof MainActivity){
                        ((MainActivity) activity).replaceFragment(new VistaAsignaturaFragment(), ((MainActivity) activity).getSupportFragmentManager(), R.id.framecentral);
                        activity.setTitle(name_Asignatura);
                        ((MainActivity) activity).setFragmentActual(name_Asignatura);
                        ((MainActivity) activity).setActionBarActivityArrow(true);
                    }
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

        // ---- BOTÓN Y POPUP ----- //
        /*
        View viewPopup = getLayoutInflater().inflate(R.layout.fragment_asignaturas_popup, null);
        input = (EditText) viewPopup.findViewById(R.id.nombre_asignatura_popup);
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
                        // APPS -> solo suma
                        // Long idAux = dbAsignaturas.crearAsignatura(1, 5, m_Text);
                        // BDA -> ponderada
                        // Long idAux = dbAsignaturas.crearAsignatura(1, 2, m_Text);
                        dbAsignaturas.close();
                        if(idAux != 0){
                            Toast.makeText(getActivity().getApplicationContext(), "Asignatura creada!", Toast.LENGTH_LONG).show();
                            // REFRESH DEL FRAMELAYOUT PARA OBTENER LA LISTA DE ASIGNATURA ACTUALIZADA
                            Activity activity = getActivity();
                            if (activity instanceof MainActivity){
                                ((MainActivity) activity).replaceFragment(new AsignaturasFragment(), ((MainActivity) activity).getSupportFragmentManager(), R.id.framecentral);
                            }
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
        */

        return view;
    }

    public static Integer getId_Asignatura() {
        return id_Asignatura;
    }

    public static String getName_Asignatura() {
        return name_Asignatura;
    }

    public static void setNombre_Asignatura_ingresada(String n){nombre_Asignatura_ingresada = n;}
    public static String getNombre_Asignatura_ingresada(){return nombre_Asignatura_ingresada;}

    public static void setTipoPromedio_ingresada(String t){tipoPromedio_ingresada = t;}
    public static String getTipoPromedio_ingresada(){return tipoPromedio_ingresada;}
}
package com.example.myversity.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myversity.AsignaturasFragment;
import com.example.myversity.MainActivity;
import com.example.myversity.R;
import com.example.myversity.VistaAsignaturaFragment;
import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.db.DbCondAsignatura;
import com.example.myversity.entidades.CondAsignatura;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

public class CondAdapter extends RecyclerView.Adapter<CondAdapter.CondViewHolder> {
    private List<CondAsignatura> condItemList;
    Context context, ctx;
    View rootView;
    TextView notaFinal;

    public CondAdapter(Context ct, List<CondAsignatura> condItemList){
        this.context = ct;
        this.condItemList = condItemList;
    }

    @NonNull
    @Override
    public CondAdapter.CondViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_condicion, parent,false);

        ctx = parent.getContext();
        rootView = ((Activity) ctx).getWindow().getDecorView().findViewById(android.R.id.content);
        notaFinal = rootView.findViewById(R.id.asignatura_promedio);
        return new CondViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CondAdapter.CondViewHolder holder, int position) {
        CondAsignatura condAsignatura = condItemList.get(position);
        holder.titleCond.setText(condAsignatura.getCondicion());
        holder.condSwitch.setChecked(condAsignatura.getChequeado());

        holder.condSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DbCondAsignatura dbCondAsignatura = new DbCondAsignatura(context.getApplicationContext());
                //actualizar estado de condición
                condAsignatura.setChequeado(b);
                //guardar estado en db
                Long state = dbCondAsignatura.actualizarChequeado(condAsignatura.getId(), condAsignatura.getChequeado());

                // ---- actualizar color nota final ----- //
                Integer idAsig = AsignaturasFragment.getAsignatura_seleccionada().getId();
                Boolean cuandoPenaliza = condAsignatura.getTp().getCuando_penaliza();
                Integer tipoCond = condAsignatura.getId_tiposPenalizacion();
                Float nota_final_actual;
                if (AsignaturasFragment.getAsignatura_seleccionada().getNota_final() == null){
                    Integer i = 0;
                    nota_final_actual = i.floatValue() ;
                }
                else{
                    nota_final_actual =  Float.parseFloat(AsignaturasFragment.getAsignatura_seleccionada().getNota_final());
                }

                // checked == tp.getCuando_penaliza:
                //     caso asistencia: poner en rojo la nota final
                //     caso descuento porcentaje: descontar porcentaje a nota final y poner en rojo si esta menor a la aprobacion
                //     caso descuento nota: lo mismo pero con el puntaje menos
                //     caso adicion nota: lo mismo pero agregando puntaje y revisando si se pone en rojo

                // ---- 1er caso: ---- //
                if(condAsignatura.getChequeado() == cuandoPenaliza){
                    // 1: Reprobación                           NO REQUIERE_VALOR
                    // 2: Descuento porcentaje nota final       SI REQUIERE_VALOR
                    // 3: Descuento puntaje nota final          SI REQUIERE_VALOR
                    // 4: Adición puntaje nota final            SI REQUIERE_VALOR
                    switch (tipoCond){
                        case 1:
                            // condición en 0 -> reprueba
                            notaFinal.setBackground(ctx.getDrawable(R.drawable.roundstyle_error_final));
                            break;
                        case 2:
                            // condición en 0 -> se descuenta porcentaje en la nota final
                            Integer valorCond_2 = Integer.parseInt(condAsignatura.getValor());
                            Float nota_final_actualiza_c2 = nota_final_actual*(1- valorCond_2/100);
                            // ---- actualizamos la nota final en bd ---- //
                            DbAsignaturas dbAsignaturas_c2 = new DbAsignaturas(context.getApplicationContext());
                            Long idAux_c2 = dbAsignaturas_c2.actualizarNotaAsignatura(idAsig, nota_final_actualiza_c2.toString());
                            dbAsignaturas_c2.close();
                            break;
                        case 3:
                            // condición en 0 -> se descuenta puntos en la nota final
                            Integer valorCond_3 = Integer.parseInt(condAsignatura.getValor());
                            Float nota_final_actualiza_c3 = nota_final_actual - valorCond_3;
                            // ---- actualizamos la nota final en bd ---- //
                            DbAsignaturas dbAsignaturas_c3 = new DbAsignaturas(context.getApplicationContext());
                            Long idAux_c3 = dbAsignaturas_c3.actualizarNotaAsignatura(idAsig, nota_final_actualiza_c3.toString());
                            dbAsignaturas_c3.close();
                            break;
                        case 4:
                            // condición en 1 -> se aumenta puntos en la nota final
                            Integer valorCond_4 = Integer.parseInt(condAsignatura.getValor());
                            Float nota_final_actualiza_c4 = nota_final_actual + valorCond_4;
                            // ---- actualizamos la nota final en bd ---- //
                            DbAsignaturas dbAsignaturas_c4 = new DbAsignaturas(context.getApplicationContext());
                            Long idAux_c4 = dbAsignaturas_c4.actualizarNotaAsignatura(idAsig, nota_final_actualiza_c4.toString());
                            dbAsignaturas_c4.close();
                            break;
                    }
                } // ---- 2do caso: ---- //
                else{
                    switch (tipoCond){
                        case 1:
                            // condición en 1 -> reprueba
                            notaFinal.setBackground(ctx.getDrawable(R.drawable.roundstyle_nota_final));
                            break;
                        case 2:
                            // condición en 1 -> no se descuenta porcentaje en la nota final
                            Integer valorCond_2 = Integer.parseInt(condAsignatura.getValor());
                            Float nota_final_actualiza_c2 = nota_final_actual*(100/(100-valorCond_2)); // nota*(100/(100-descuento))
                            // ---- actualizamos la nota final en bd ---- //
                            DbAsignaturas dbAsignaturas_c2 = new DbAsignaturas(context.getApplicationContext());
                            Long idAux_c2 = dbAsignaturas_c2.actualizarNotaAsignatura(idAsig, nota_final_actualiza_c2.toString());
                            dbAsignaturas_c2.close();
                            break;
                        case 3:
                            // condición en 1 -> no se descuenta puntos en la nota final
                            Integer valorCond_3 = Integer.parseInt(condAsignatura.getValor());
                            Float nota_final_actualiza_c3 = nota_final_actual + valorCond_3;
                            // ---- actualizamos la nota final en bd ---- //
                            DbAsignaturas dbAsignaturas_c3 = new DbAsignaturas(context.getApplicationContext());
                            Long idAux_c3 = dbAsignaturas_c3.actualizarNotaAsignatura(idAsig, nota_final_actualiza_c3.toString());
                            dbAsignaturas_c3.close();
                            break;
                        case 4:
                            // condición en 0 -> no se aumenta puntos en la nota final
                            Integer valorCond_4 = Integer.parseInt(condAsignatura.getValor());
                            Float nota_final_actualiza_c4 = nota_final_actual - valorCond_4;
                            // ---- actualizamos la nota final en bd ---- //
                            DbAsignaturas dbAsignaturas_c4 = new DbAsignaturas(context.getApplicationContext());
                            Long idAux_c4 = dbAsignaturas_c4.actualizarNotaAsignatura(idAsig, nota_final_actualiza_c4.toString());
                            dbAsignaturas_c4.close();
                            break;
                    }
                }

                if(!condAsignatura.getChequeado()){
                    notaFinal.setBackground(ctx.getDrawable(R.drawable.roundstyle_error_final));
                }else{
                    notaFinal.setBackground(ctx.getDrawable(R.drawable.roundstyle_nota_final));
                }

                System.out.println("Successfully updated condición: " + state);
                System.out.println("ESTADO CONDICIÒN: "+ condAsignatura.getChequeado());

                dbCondAsignatura.close();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(condItemList != null){
            return condItemList.size();
        }else{
            return 0;
        }
    }

    public class CondViewHolder extends RecyclerView.ViewHolder{
        private TextView titleCond;
        private SwitchMaterial condSwitch;

        public CondViewHolder(@NonNull View itemView) {
            super(itemView);
            titleCond = itemView.findViewById(R.id.title_condición);
            condSwitch = itemView.findViewById(R.id.cond_switch);
        }
    }
}

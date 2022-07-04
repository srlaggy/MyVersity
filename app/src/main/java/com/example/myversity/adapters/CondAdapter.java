package com.example.myversity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myversity.R;
import com.example.myversity.db.DbCondAsignatura;
import com.example.myversity.entidades.CondAsignatura;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

public class CondAdapter extends RecyclerView.Adapter<CondAdapter.CondViewHolder> {
    private List<CondAsignatura> condItemList;
    Context context;
    public Boolean isChecked;

    public CondAdapter(Context ct, List<CondAsignatura> condItemList){
        this.context = ct;
        this.condItemList = condItemList;
    }

    @NonNull
    @Override
    public CondAdapter.CondViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_condicion, parent,false);
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

package com.example.myversity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myversity.R;
import com.example.myversity.entidades.Evaluaciones;

import java.util.ArrayList;
import java.util.List;

public class RvEvalAdapter extends RecyclerView.Adapter<RvEvalAdapter.EvalViewHolder> {

    private List<Evaluaciones> evalItemList;
    //List<String> nombreEvaluaciones = new ArrayList<>();
    //List<Integer> cantidadEvaluaciones = new ArrayList<>();
    //List<Integer> idEvaluaciones = new ArrayList<>();
    Context context;
    //Integer curr_Id;

    public RvEvalAdapter(Context ct, List<Evaluaciones> evalItemList /*, List<String> nombreEval, List<Integer> cantidadEval, List<Integer> idEval*/){
        this.context = ct;
        this.evalItemList = evalItemList;
        //this.nombreEvaluaciones = nombreEval;
        //this.cantidadEvaluaciones = cantidadEval;
        //this.idEvaluaciones = idEval;
    }

    @NonNull
    @Override
    public EvalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evaluacion, parent,false);
        return new EvalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EvalViewHolder holder, int position) {
        Evaluaciones evaluacion = evalItemList.get(position);
        holder.titleEval.setText(evaluacion.getTipo());

        holder.notaRecyclerView.setHasFixedSize(true);
        holder.notaRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        GridNotasAdapter gridNotasAdapter = new GridNotasAdapter(holder.notaRecyclerView.getContext(), evaluacion.getNotas());
        holder.notaRecyclerView.setAdapter(gridNotasAdapter);
        gridNotasAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        if(evalItemList != null){
            return evalItemList.size();
        }else{
            return 0;
        }
    }

    public class EvalViewHolder extends RecyclerView.ViewHolder{
        private TextView titleEval;
        private RecyclerView notaRecyclerView;

        //EditText nota_1, nota_2, nota_3;

        public EvalViewHolder(@NonNull View itemView) {
            super(itemView);
            titleEval = (TextView) itemView.findViewById(R.id.title_evaluacion);
            notaRecyclerView = (RecyclerView) itemView.findViewById(R.id.grid_notas);

            //nota_1 = itemView.findViewById(R.id.editText_nota_dummy1);
            //nota_2 = itemView.findViewById(R.id.editText_nota_dummy2);
            //nota_3 = itemView.findViewById(R.id.editText_nota_dummy3);
        }
    }
}

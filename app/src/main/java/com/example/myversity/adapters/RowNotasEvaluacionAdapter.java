package com.example.myversity.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myversity.R;

import java.util.ArrayList;
import java.util.List;

public class RowNotasEvaluacionAdapter extends ArrayAdapter<RowNotasEvaluacion> {
    private Context mContext;
    private List<RowNotasEvaluacion> rows = new ArrayList<>();
    private boolean isOnValueChanged = false;

    public RowNotasEvaluacionAdapter(@NonNull Context context, List<RowNotasEvaluacion> objects) {
        super(context, 0, objects);
        mContext = context;
        rows = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        Integer pos = position;
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_ponderaciones_notas_evaluacion,parent,false);
        }

        RowNotasEvaluacion currentRow = rows.get(position);

        TextView text = (TextView) listItem.findViewById(R.id.texto_ponderacion_item);
        text.setText(currentRow.getTitulo());

        EditText input = (EditText) listItem.findViewById(R.id.input_ponderacion_item);
        input.setText(currentRow.getValor());

        input.addTextChangedListener(new TextWatcher() {
                 @Override
                 public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                 }

                 @Override
                 public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                     isOnValueChanged = true;
                     Log.d("ejemplo_ponderacion", "CAMBIO EN INPUT " + position);
                 }

                 @Override
                 public void afterTextChanged(Editable editable) {
                     if (isOnValueChanged) {
                         isOnValueChanged = false;

                         Log.d("ejemplo_ponderacion", "Hubo cambio en input " + position + " | CAMBIO: '" + editable.toString() + "'");
                         rows.get(position).setValor(editable.toString());
                         rows.get(position).getNota().setPeso(editable.toString());
                     }
                 }
             }
        );

        return listItem;
    }
}

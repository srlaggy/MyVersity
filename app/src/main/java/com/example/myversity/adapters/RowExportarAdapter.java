package com.example.myversity.adapters;

import android.content.Context;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myversity.R;

import java.util.ArrayList;
import java.util.List;

public class RowExportarAdapter extends ArrayAdapter<RowExportar> {
    private Context mContext;
    private List<RowExportar> rows = new ArrayList<>();

    public RowExportarAdapter(@NonNull Context context, List<RowExportar> objects) {
        super(context, 0, objects);
        mContext = context;
        rows = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_exportar,parent,false);
        }

        RowExportar currentRow = rows.get(position);

        TextView text = (TextView) listItem.findViewById(R.id.texto_exportar_item);
        text.setText(currentRow.getTitulo());

        CheckBox check = (CheckBox) listItem.findViewById(R.id.checkbox_exportar);
        check.setChecked(currentRow.isChecked());

        check.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentRow.setChecked(!currentRow.isChecked());
                check.setChecked(currentRow.isChecked());
            }
        });

        return listItem;
    }
}

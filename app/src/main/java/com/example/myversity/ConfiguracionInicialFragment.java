package com.example.myversity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

public class ConfiguracionInicialFragment extends Fragment {
    public ConfiguracionInicialFragment() {
        // Required empty public constructor
    }

    public static ConfiguracionInicialFragment newInstance(String param1, String param2) {
        ConfiguracionInicialFragment fragment = new ConfiguracionInicialFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_configuracion_inicial, container, false);
    }
}
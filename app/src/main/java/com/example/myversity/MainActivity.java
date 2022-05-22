package com.example.myversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.myversity.databinding.ActivityMainBinding;
import com.example.myversity.db.DbHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    // DEBE IRSE ACTUALIZANDO EN CADA CAMBIO DE FRAGMENT
    private String fragmentActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // CREAMOS BD
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        setActionBarActivityArrow(false);
        setTitle(getString(R.string.asignatura_title_topbar));
        replaceFragment(new AsignaturasFragment(), getSupportFragmentManager(), R.id.framecentral);
        setFragmentActual(getString(R.string.asignatura_title_topbar));

        binding.bottombar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.btn_nav_asignatura:
                    setTitle(getString(R.string.asignatura_title_topbar));
                    replaceFragment(new AsignaturasFragment(), getSupportFragmentManager(), R.id.framecentral);
                    setFragmentActual(getString(R.string.asignatura_title_topbar));
                    setActionBarActivityArrow(false);
                    break;
                case R.id.btn_nav_resumen:
                    setTitle(getString(R.string.resumen_title_topbar));
                    replaceFragment(new ResumenFragment(), getSupportFragmentManager(), R.id.framecentral);
                    setFragmentActual(getString(R.string.resumen_title_topbar));
                    setActionBarActivityArrow(false);
                    break;
                case R.id.btn_nav_configuracion:
                    setTitle(getString(R.string.configuracion_title_topbar));
                    replaceFragment(new ConfiguracionFragment(), getSupportFragmentManager(), R.id.framecentral);
                    setFragmentActual(getString(R.string.configuracion_title_topbar));
                    setActionBarActivityArrow(false);
                    break;
            }
            return true;
        });
    }

    // METODO PARA AHCER EL CAMBIO DE FRAGMENTS
    // UTIL EN ACTIVIDADES Y FRAGMENTOS
    public void replaceFragment(Fragment fragment, FragmentManager fragmentManager, int idFrameReplace) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(idFrameReplace, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // OVERRIDE A METODO QUE SE DISPARA AL PRESIONAR EN CUALQUIER LADO DE LA PANTALLA
    // UTIL PARA QUITAR FOCUS A INPUT TEXTS Y OCULTAR EL TECLADO
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof TextInputEditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public ActionBar getActionBarActivity() {
        return getSupportActionBar();
    }

    // METODO PARA ACTIVAR O DESACTIVAR LA FLECHA DE LA BARRA DE TAREAS (<-)
    // SE UTILIZA EN CADA CAMBIO DE FRAGMENTOS O ACTIVIDADES, DEPENDIENDO SI ES UN PADRE O NO
    public void setActionBarActivityArrow(boolean value) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(value);
        }
    }

    // OVERRIDE DE METODO QUE SE ACTIVA AL PRESIONAR LA FLECHA DE LA BARRA DE TAREAS (<-)
    // PERMITE DEFINIR LAS RUTAS AL IR PRESIONANDO LA FLECHA HACIA ATRAS
    // DEBE CALZAR CON LAS VISTAS QUE TIENEN FLECHA ACTIVADA
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // CONFIGURACION DE NOTAS -> CONFIGURACION
                if (Objects.equals(fragmentActual, getString(R.string.title_opcion_1_config))) {
                    setTitle(getString(R.string.configuracion_title_topbar));
                    replaceFragment(new ConfiguracionFragment(), getSupportFragmentManager(), R.id.framecentral);
                    setFragmentActual(getString(R.string.configuracion_title_topbar));
                    setActionBarActivityArrow(false);
                    return true;
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public String getFragmentActual() {
        return fragmentActual;
    }

    public void setFragmentActual(String fragmentActual) {
        this.fragmentActual = fragmentActual;
    }
}
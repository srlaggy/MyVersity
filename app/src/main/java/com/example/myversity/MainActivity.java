package com.example.myversity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.myversity.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(getString(R.string.asignatura_title_topbar));
        replaceFragment(new AsignaturasFragment());

        binding.bottombar.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.btn_nav_asignatura:
                    setTitle(getString(R.string.asignatura_title_topbar));
                    replaceFragment(new AsignaturasFragment());
                    break;
                case R.id.btn_nav_resumen:
                    setTitle(getString(R.string.resumen_title_topbar));
                    replaceFragment(new ResumenFragment());
                    break;
                case R.id.btn_nav_configuracion:
                    setTitle(getString(R.string.configuracion_title_topbar));
                    replaceFragment(new ConfiguracionFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framecentral, fragment);
        fragmentTransaction.commit();
    }
}
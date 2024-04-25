package com.oguz.e_commerce.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.oguz.e_commerce.R;
import com.oguz.e_commerce.databinding.ActivityMainBinding;
import com.oguz.e_commerce.model.ProductModel;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setupNavigationController();
        //menu();
        auth = FirebaseAuth.getInstance();



    }

    private void setupNavigationController() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if (navHostFragment != null){
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
        }

    }

    public void showMenu(){
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
    }
    public void hideMenu(){
        binding.bottomNavigationView.setVisibility(View.GONE);
    }

    public void showLoading(){
        binding.pbLoadhg.setVisibility(View.VISIBLE);
    }

    public void hideLoading(){
        binding.pbLoadhg.setVisibility(View.GONE);
    }




}
package com.oguz.e_commerce.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.oguz.e_commerce.R;
import com.oguz.e_commerce.databinding.FragmentGirisEkraniBinding;
import com.oguz.e_commerce.view.MainActivity;


public class GirisEkraniFragment extends Fragment {

    private FragmentGirisEkraniBinding binding;

    private FirebaseAuth auth;

    public GirisEkraniFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentGirisEkraniBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupMenuVisibility();
        setupClickEvents();
    }

    private void setupClickEvents() {
        binding.kayitol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayitol();
            }
        });
        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(v);
            }
        });

    }

    private void setupMenuVisibility() {
        ((MainActivity) requireActivity()).hideMenu();

    }

    public void kayitol() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_girisEkraniFragment_to_signupFragment);
    }

    public void signIn(View view) {
        ((MainActivity) requireActivity()).showLoading();
        String email = binding.mail.getText().toString();
        String password = binding.sifre.getText().toString();

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                ((MainActivity) requireActivity()).hideLoading();
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_girisEkraniFragment_to_homeFragment);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }


}
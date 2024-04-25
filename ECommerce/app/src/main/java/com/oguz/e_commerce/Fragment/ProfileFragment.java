
package com.oguz.e_commerce.Fragment;

import static com.oguz.e_commerce.utils.Constants.UID;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.oguz.e_commerce.R;
import com.oguz.e_commerce.databinding.FragmentProfileBinding;
import com.oguz.e_commerce.utils.Constants;
import com.oguz.e_commerce.utils.SharedPreferencesHelper;
import com.oguz.e_commerce.view.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.Map;
import java.util.UUID;


public class ProfileFragment extends Fragment {

    private FirebaseFirestore db;

    private FragmentProfileBinding binding;

    private FirebaseAuth auth;

    private SharedPreferencesHelper sharedPreferencesHelper;

    private AlertDialog.Builder alertDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(requireContext());
        db = FirebaseFirestore.getInstance();
        alertDialog = new AlertDialog.Builder(requireContext());

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

        getData();

        setupClickEvents();

    }

    private void setupClickEvents() {
        binding.exit.setOnClickListener(v -> {
            showAlertDialog();
        });
    }

    private void showAlertDialog(){
        alertDialog.setTitle("Exit");
        alertDialog.setMessage("Are you sure to exit application?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exit();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }


    public void getData(){

        //String uid = SharedPreferencesHelper.getInstance(requireContext()).getString(UID,"");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (!uid.isEmpty()) {
            DocumentReference documentRef = db.collection("Information").document(uid);

            documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {


                        String userEmail = documentSnapshot.getString("user-email");
                        String name = documentSnapshot.getString("name");
                        String downloadURL = documentSnapshot.getString("downloadUrl");
                        Log.d("PROFILE", "User email: " + userEmail);
                        Log.d("PROFILE", "name: " + name);
                        Log.d("PROFILE", "URL: " + downloadURL);
                        binding.email.setText(userEmail);
                        binding.surname.setText(name);
                        Glide.with(requireContext()).load(downloadURL).into(binding.imageView2);

                    } else {
                        Log.d("PROFILE", "No such document");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("PROFILE", "Error getting document", e);
                }
            });
        } else {
            Log.d("PROFILE", "UID is empty");
        }
    }


    public void exit() {
        auth.signOut();
        SharedPreferencesHelper.getInstance(requireContext()).clear();
        startActivity(new Intent(requireActivity(),MainActivity.class));
        requireActivity().finish();
    }


}

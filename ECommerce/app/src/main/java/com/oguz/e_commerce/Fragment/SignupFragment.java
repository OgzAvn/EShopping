
package com.oguz.e_commerce.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.oguz.e_commerce.R;
import com.oguz.e_commerce.databinding.FragmentSignupBinding;
import com.oguz.e_commerce.utils.SharedPreferencesHelper;

import java.util.HashMap;
import java.util.UUID;


public class SignupFragment extends Fragment {

    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private FragmentSignupBinding binding;
    FirebaseUser firebaseUser;
    ActivityResultLauncher<Intent> resultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    Uri imageData;
    Bitmap selectedImage;

    private SharedPreferencesHelper sharedPreferencesHelper;



    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentSignupBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = firebaseStorage.getReference();
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(requireContext());
        registerLauncher();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupClickEvents();
    }

    public void setupClickEvents(){
        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(v);
            }
        });

        binding.selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedimage(v);
            }
        });

        binding.girisyap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girisyap(v);
            }
        });
    }

    public void selectedimage(View view){


        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_MEDIA_IMAGES)){
                Snackbar.make(view,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                    public void onClick(View v) {
                        //request
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                    }
                }).show();
            }
            else{
                //request
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        }else{
            //Go to gallery
            Intent intentToGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            resultLauncher.launch(intentToGallery);
        }

    }




    public void registerLauncher(){

        resultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent intentFromResult = result.getData(); //Burada veriyi aldık
                        if (intentFromResult != null){
                            imageData = intentFromResult.getData(); //burada bize bir URI döndürüyor verinin nerede kayıtlı olduğunu tam adresini

                            try {
                                if (Build.VERSION.SDK_INT>=28){
                                    ImageDecoder.Source source = ImageDecoder.createSource(requireActivity().getContentResolver(),imageData);
                                    selectedImage = ImageDecoder.decodeBitmap(source);
                                    binding.selectImage.setImageBitmap(selectedImage);
                                }else{
                                    selectedImage = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(),imageData);
                                    binding.selectImage.setImageBitmap(selectedImage);
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                });

        permissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), o -> {
                    if (o){
                        //Permission granted
                        Intent intentToGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivity(intentToGallery);
                    }else {
                        //Permission denied
                        Toast.makeText(requireContext(),"Permission needed!",Toast.LENGTH_LONG).show();
                    }
                });

    }
    public void signup(View view){

        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();

        if (email.equals("") || password.equals("") || imageData == null){
            Toast.makeText(requireContext(),"Enter email, password and image",Toast.LENGTH_LONG).show();
        }else{

            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
                if(imageData != null){
                    //Universal unique id resim değişince önceki resmi silmeyecek
                    UUID uuid = UUID.randomUUID();
                    String imageName = "images/" +uuid+".jpg"; // Resmi böyle ekle

                    storageReference.child(imageName).putFile(imageData)
                            .addOnSuccessListener(taskSnapshot -> {
                                //Download URL kayıtlı olduğu yeri alabilmemiz için yeni bir referans a ihtiyacımız var
                                // bunu da kaydettikten sonra yapmamız gerekiyor yukarıdaki referans ile yapamayız

                                StorageReference newReference = firebaseStorage.getReference(imageName);
                                newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String downloadUrl = uri.toString(); //Resmi aldık

                                        String name = binding.adSoyad.getText().toString(); //Ad soyad aldık

                                        //Birde kullanıcının kendisini alalım
                                        FirebaseUser user = auth.getCurrentUser();
                                        assert user != null;
                                        String email = user.getEmail();

                                        HashMap<String, Object> informationData = new HashMap<String, Object>();
                                        informationData.put("user-email",email);
                                        informationData.put("downloadUrl",downloadUrl);
                                        informationData.put("name",name);

                                        firebaseFirestore.collection("Information").document(auth.getCurrentUser().getUid()).set(informationData).addOnSuccessListener(documentReference -> {
                                            //SharedPreferencesHelper.getInstance(requireContext()).saveString(UID,auth.getCurrentUser().getUid());
                                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_signupFragment_to_homeFragment);
                                        }).addOnFailureListener(e -> Toast.makeText(requireContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show());
                                    }
                                });

                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(requireContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                            });
                }else {

                }

            }).addOnFailureListener(e -> {
                Toast.makeText(requireContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            });
        }

    }

    public void girisyap(View view){

        Navigation.findNavController(view).popBackStack();

    }


}

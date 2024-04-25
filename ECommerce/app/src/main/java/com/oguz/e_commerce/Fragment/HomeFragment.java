package com.oguz.e_commerce.Fragment;

import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oguz.e_commerce.R;
import com.oguz.e_commerce.Roomdb.ProductDao;
import com.oguz.e_commerce.Roomdb.ProductDatabase;
import com.oguz.e_commerce.adapter.RecyclerViewAdapter;
import com.oguz.e_commerce.databinding.FragmentHomeBinding;

import com.oguz.e_commerce.intefaces.AddCartListener;
import com.oguz.e_commerce.model.ProductModel;

import com.oguz.e_commerce.serivce.ProductAPI;
import com.oguz.e_commerce.view.MainActivity;

import java.util.ArrayList;
import java.util.List;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ArrayList<ProductModel> productModelArrayList;
    private String BASE_URL = "https://fakestoreapi.com/";
    Retrofit retrofit;

    CompositeDisposable compositeDisposable;

    private RecyclerViewAdapter adapter;

    ProductDatabase db;
    ProductDao productDao;




    public HomeFragment() {
        // Required empty public constructor


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentHomeBinding.inflate(getLayoutInflater());


        db = Room.databaseBuilder(requireContext(),ProductDatabase.class,"Products").build();
        productDao = db.productDao();

        //Retrofit & JSON

        Gson gson = new GsonBuilder().setLenient().create(); //JSON ı oluşturduk
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        getDatafromAPI();
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
        ((MainActivity) requireActivity()).showMenu();

    }

    private void getDatafromAPI(){

        final ProductAPI productAPI = retrofit.create(ProductAPI.class);

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(productAPI.getData()
                .subscribeOn(Schedulers.io())//Hangi thread de gözlemleme işlemi yani kayıt olma işleminin yapılacağı
                .observeOn(AndroidSchedulers.mainThread())//alınan verileri main thread de göstereceğiz
                .subscribe(this::handleresponse));//sonucta cıkan şeyi nerede ele alacağız

    }

    private void handleresponse(List<ProductModel> productModelList){

        productModelArrayList = new ArrayList<>(productModelList);
        productModelArrayList.addAll(productModelList);

        //RecyclerView
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(binding.recyclerView.getContext(),2); //Furkana sor
        binding.recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(productModelArrayList, new AddCartListener() {
            @Override
            public void onAddCartListener(ProductModel model) {
                //TODO: Room'a kaydedilecek veriler
                // Image,title,price

                ProductModel productModel = new ProductModel(model.title,model.price, model.image,model.rate);
                productDao.insert(productModel);

                setupAddCart(productModel);


                Log.e("PRODUCTID",String.valueOf(model.id));
            }


        });
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupAddCart(ProductModel productModel) {
        compositeDisposable.add(productDao.insert(productModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleAddCart));
    }

    private void handleAddCart() {
        Toast.makeText(requireContext(),"Ürün Sepete eklendi",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
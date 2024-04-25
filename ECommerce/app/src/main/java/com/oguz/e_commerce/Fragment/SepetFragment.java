package com.oguz.e_commerce.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;


import com.oguz.e_commerce.Roomdb.ProductDao;
import com.oguz.e_commerce.Roomdb.ProductDatabase;
import com.oguz.e_commerce.adapter.ProductAdapter;

import com.oguz.e_commerce.databinding.FragmentSepetBinding;
import com.oguz.e_commerce.intefaces.DeleteCartListener;
import com.oguz.e_commerce.model.ProductModel;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class SepetFragment extends Fragment {

    private FragmentSepetBinding binding;

    ProductDatabase db;
    ProductDao productDao;

    private ProductAdapter productAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();




    public SepetFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentSepetBinding.inflate(getLayoutInflater());


        db = Room.databaseBuilder(requireContext(),ProductDatabase.class,"Products").build();
        productDao = db.productDao();


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        compositeDisposable.add(productDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleGetCart));

    }


    private void handleGetCart(List<ProductModel> productModelList) {
        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        productAdapter = new ProductAdapter((ArrayList<ProductModel>) productModelList, new DeleteCartListener() {
            @Override
            public void deleteCartListener(ProductModel Model) {
                //TODO: Sepetteki ürünleri silme işlemi yapılacak.
                compositeDisposable.add(productDao.delete(Model)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleDelete));
            }

            private void handleDelete() {

                Toast.makeText(requireContext(),"Ürün sepetinizden silindi",Toast.LENGTH_LONG).show();
            }
        });
        binding.recyclerView2.setAdapter(productAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
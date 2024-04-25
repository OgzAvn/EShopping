package com.oguz.e_commerce.serivce;

import com.oguz.e_commerce.model.ProductModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductAPI {

    //https://fakestoreapi.com/products
    @GET("products")
    Observable<List<ProductModel>> getData();

    //Call<List<ProductModel>> getData();


}


package com.oguz.e_commerce.Roomdb;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RxRoom;

import com.oguz.e_commerce.model.ProductModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;


@Dao
public interface ProductDao {

    @Query("SELECT * FROM ProductModel")
    Observable<List<ProductModel>> getAll();

    @Insert
    public Completable insert(ProductModel productModel);

    @Delete
    public Completable delete(ProductModel productModel);

}

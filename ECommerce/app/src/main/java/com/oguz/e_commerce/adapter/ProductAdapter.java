package com.oguz.e_commerce.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oguz.e_commerce.databinding.ProductLayoutBinding;
import com.oguz.e_commerce.databinding.RowLayoutBinding;
import com.oguz.e_commerce.intefaces.DeleteCartListener;
import com.oguz.e_commerce.model.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    ArrayList<ProductModel> productModelList;
    ProductLayoutBinding rowLayoutBinding;

    private DeleteCartListener deleteCartListener;

    public ProductAdapter(ArrayList<ProductModel> productModelList,DeleteCartListener deleteCartListener){
        this.productModelList = productModelList;
        this.deleteCartListener = deleteCartListener;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        rowLayoutBinding  = ProductLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new ProductHolder(rowLayoutBinding.getRoot());

    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {

        ProductModel productModel = productModelList.get(position);
        holder.bind(productModel);

    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder{

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

        }

        public void bind(ProductModel productModel){

            rowLayoutBinding.sepetprice.setText(String.valueOf(productModel.getPrice()));
            rowLayoutBinding.sepetTitle.setText(productModel.getTitle());
            Glide.with(rowLayoutBinding.getRoot())
                    .load(productModel.getImage())
                    .into(rowLayoutBinding.sepetImage);


            rowLayoutBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteCartListener.deleteCartListener(productModel);
                }
            });
        }
    }
}
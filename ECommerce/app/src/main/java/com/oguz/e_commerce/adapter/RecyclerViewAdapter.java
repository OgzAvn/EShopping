
package com.oguz.e_commerce.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oguz.e_commerce.databinding.RowLayoutBinding;

import com.oguz.e_commerce.intefaces.AddCartListener;
import com.oguz.e_commerce.model.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RowHolder> {

    private final ArrayList<ProductModel> productModelArrayList;

    private AddCartListener listener;
    RowLayoutBinding binding;


    public RecyclerViewAdapter(ArrayList<ProductModel> productModelArrayList, AddCartListener listener){
        this.productModelArrayList = productModelArrayList;
        this.listener = listener;
    }



    @NonNull
    @Override
    public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = RowLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new RowHolder(binding.getRoot());

    }

    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) {

        holder.bind(productModelArrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    public class RowHolder extends RecyclerView.ViewHolder {
        public RowHolder(@NonNull View itemView) {
            super(itemView);

        }
        public void bind(ProductModel productModel){

            Glide.with(itemView.getContext())
                    .load(productModel.getImage()) // Ürünün resim URL'si
                    .into(binding.picture);


            binding.tvProductName.setText(productModel.title);
            binding.price.setText(String.valueOf(productModel.getPrice()));
            binding.rate.setText(String.valueOf(productModel.getRate().getRate()));


            binding.btnAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onAddCartListener(productModel);
                }
            });

        }
    }
}

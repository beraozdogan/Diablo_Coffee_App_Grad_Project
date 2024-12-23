package com.ercin.socialmediaclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ercin.socialmediaclone.databinding.RecyclerRowChildBinding;
import com.ercin.socialmediaclone.model.Product;

import java.util.ArrayList;

public class MenuChildAdapter extends RecyclerView.Adapter<MenuChildAdapter.MenuHolder> {

    private ArrayList<Product> productArrayList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public MenuChildAdapter(ArrayList<Product> productArrayList, Context context) {
        this.productArrayList = productArrayList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClicked(Product product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowChildBinding recyclerRowChildBinding = RecyclerRowChildBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MenuHolder(recyclerRowChildBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, int position) {

        Product product = productArrayList.get(position);


        holder.recyclerRowChildBinding.productNameTxt.setText(productArrayList.get(position).getProductName());
        Glide.with(context)
                .load(productArrayList.get(position).getProductPhotoUrl())
                .into(holder.recyclerRowChildBinding.productImage);


        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClicked(product);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    static class MenuHolder extends RecyclerView.ViewHolder {

        RecyclerRowChildBinding recyclerRowChildBinding;

        public MenuHolder(RecyclerRowChildBinding recyclerRowChildBinding) {
            super(recyclerRowChildBinding.getRoot());
            this.recyclerRowChildBinding = recyclerRowChildBinding;
        }
    }


}

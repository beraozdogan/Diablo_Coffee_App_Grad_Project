package com.ercin.socialmediaclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ercin.socialmediaclone.databinding.RecyclerRowBinding;
import com.ercin.socialmediaclone.databinding.RecyclerRowParentBinding;
import com.ercin.socialmediaclone.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MenuParentAdapter extends RecyclerView.Adapter<MenuParentAdapter.MenuHolder>{

    private ArrayList<Product> productArrayList;
    private List<String> typeList;
    private Context context;
    private Product product;
    private OnProductClickListener onProductClickListener;


    public MenuParentAdapter (ArrayList<Product> productArrayList, List<String> typeList, Context context, OnProductClickListener onProductClickListener){

        this.typeList = typeList;
        this.productArrayList = productArrayList;
        this.context = context;
        this.onProductClickListener = onProductClickListener;

    }


    public interface OnProductClickListener {
        void onProductClicked(Product product);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerRowParentBinding recyclerRowParentBinding = RecyclerRowParentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MenuHolder(recyclerRowParentBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, int position) {
        String type = typeList.get(position);
        holder.recyclerRowParentBinding.turAdiTxt.setText(type);

        ArrayList<Product> filteredProducts = filterProductsByType(type);
        MenuChildAdapter menuChildAdapter = new MenuChildAdapter(filteredProducts, context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.recyclerRowParentBinding.childRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerRowParentBinding.childRecyclerView.setLayoutManager(layoutManager);
        holder.recyclerRowParentBinding.childRecyclerView.setAdapter(menuChildAdapter);


        menuChildAdapter.setOnItemClickListener(new MenuChildAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Product product) {
                onProductClickListener.onProductClicked(product);
            }
        });


    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }


    static class MenuHolder extends RecyclerView.ViewHolder {
        RecyclerRowParentBinding recyclerRowParentBinding;

        public MenuHolder(RecyclerRowParentBinding recyclerRowParentBinding) {
            super(recyclerRowParentBinding.getRoot());
            this.recyclerRowParentBinding = recyclerRowParentBinding;
        }
    }

    private ArrayList<Product> filterProductsByType(String type) {
        ArrayList<Product> filteredList = new ArrayList<>();
        for (Product product : productArrayList) {
            if (product.getProductType().equals(type)) {
                filteredList.add(product);
            }
        }
        return filteredList;
    }

}
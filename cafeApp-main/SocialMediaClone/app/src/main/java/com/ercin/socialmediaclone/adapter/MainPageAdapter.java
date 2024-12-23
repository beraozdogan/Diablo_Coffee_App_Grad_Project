package com.ercin.socialmediaclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ercin.socialmediaclone.databinding.RecyclerRowMainPageBinding;
import com.ercin.socialmediaclone.model.Innovation;

import java.util.ArrayList;

public class MainPageAdapter extends RecyclerView.Adapter<MainPageAdapter.MainPageHolder> {

    private ArrayList<Innovation> innovationArrayList;
    private Context context;

    public MainPageAdapter(ArrayList<Innovation> innovationArrayList, Context context) {
        this.innovationArrayList = innovationArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public MainPageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerRowMainPageBinding binding = RecyclerRowMainPageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MainPageHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MainPageHolder holder, int position) {

        Glide.with(context)
                .load(innovationArrayList.get(position).getInnovationPhotoUrl())
                .into(holder.binding.itemImageView);
        holder.binding.yenilikIsmi.setText(innovationArrayList.get(position).getInnovationName());
        holder.binding.yenilikIcerik.setText(innovationArrayList.get(position).getInnovationContent());

    }

    @Override
    public int getItemCount() {
        return innovationArrayList.size();
    }

    static class MainPageHolder extends RecyclerView.ViewHolder {

        RecyclerRowMainPageBinding binding;

        public MainPageHolder(RecyclerRowMainPageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

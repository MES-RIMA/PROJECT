package com.openclassrooms.realestatemanager.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.databinding.PropertyListItemBinding;
import com.openclassrooms.realestatemanager.models.Property;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class PropertyListAdapter extends RecyclerView.Adapter<PropertyListAdapter.PropertyViewHolder> {
    private List<Property> propertyList;
    private final Consumer<Property> onItemClickListener;

    public PropertyListAdapter(List<Property> propertyList, Consumer<Property> onItemClickListener) {
        this.propertyList = propertyList;
        this.onItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final PropertyListItemBinding itemBinding =
                PropertyListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PropertyViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyListAdapter.PropertyViewHolder holder, int position) {
        final Property property = propertyList.get(position);
        holder.propertyListItemBinding.setProperty(property);
        Glide.with(holder.propertyListItemBinding.getRoot())
                .load(property.getMainPhotoUrl())
                .centerCrop()
                .into(holder.propertyListItemBinding.photo);
        holder
                .propertyListItemBinding
                .getRoot()
                .setOnClickListener(__ -> onItemClickListener.accept(property));
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public void updateList(List<Property> newProperties) {
        propertyList = newProperties;
        notifyDataSetChanged();
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {

        private final PropertyListItemBinding propertyListItemBinding;

        public PropertyViewHolder(@NotNull PropertyListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            propertyListItemBinding = itemBinding;
        }
    }
}

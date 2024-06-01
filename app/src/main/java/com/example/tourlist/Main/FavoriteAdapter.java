package com.example.tourlist.Main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlist.Memory_Activity.MemoryActivity;
import com.example.tourlist.R;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<FavoriteLocation> favoriteLocations;
    private Context context;
    private OnItemLongClickListener onItemLongClickListener;

    public FavoriteAdapter(Context context, List<FavoriteLocation> favoriteLocations) {
        this.context = context;
        this.favoriteLocations = favoriteLocations;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_list_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteLocation location = favoriteLocations.get(position);
        holder.favoriteName.setText(location.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MemoryActivity.class);
            intent.putExtra("location_key", location.getKey());
            intent.putExtra("location_name", location.getName());
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(location);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return favoriteLocations.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        public TextView favoriteName;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            favoriteName = itemView.findViewById(R.id.favorite_name);
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(FavoriteLocation location);
    }
}

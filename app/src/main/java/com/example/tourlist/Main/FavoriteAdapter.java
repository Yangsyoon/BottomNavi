package com.example.tourlist.Main;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlist.Memory_Activity.MemoryActivity;
import com.example.tourlist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<FavoriteLocation> favoriteLocations;
    private Context context;
    private OnItemLongClickListener onItemLongClickListener;


    private DatabaseReference mDatabase;

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

    public void removeItem(int position) {
        if (position < 0 || position >= favoriteLocations.size()) {
            // 인덱스가 유효한 범위를 벗어나는 경우
            Log.e("FavoriteAdapter", "Index out of bounds: " + position);
            return;
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(userId).child("favorites");

            FavoriteLocation location = favoriteLocations.get(position);
            String key = location.getKey();

//            favoriteLocations.remove(position);



            if (key != null) {
                mDatabase.child(key).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (position < favoriteLocations.size()) {
                            favoriteLocations.remove(position);
//                            notifyItemRemoved(position);
//                            notifyItemRangeChanged(position, favoriteLocations.size());
                        }
                    } else {
                        // 오류 처리
                        Log.e("FavoriteAdapter", "Failed to remove item from database");
                    }
                });
            } else {
                if (position < favoriteLocations.size()) {
                    favoriteLocations.remove(position);
//                    notifyItemRemoved(position);
//                    notifyItemRangeChanged(position, favoriteLocations.size());
                }
            }
        }
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

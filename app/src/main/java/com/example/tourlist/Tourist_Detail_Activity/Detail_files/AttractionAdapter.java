package com.example.tourlist.Tourist_Detail_Activity.Detail_files;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlist.R;

import java.util.List;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionViewHolder> {

    private List<TouristAttraction> attractions;

    AttractionAdapter(List<TouristAttraction> attractions) {
        this.attractions = attractions;
    }

    @NonNull
    @Override
    public AttractionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.old_frag3_item_attraction, parent, false);
        return new AttractionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttractionViewHolder holder, int position) {
        TouristAttraction attraction = attractions.get(position);
        holder.bind(attraction);
    }

    @Override
    public int getItemCount() {
        return attractions.size();
    }


}

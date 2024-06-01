package com.example.tourlist.Tourist_Detail_Activity.Detail_files;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlist.R;
import com.example.tourlist.Tourist_Detail_Activity.TouristPlaceDetailActivity;

import java.util.List;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.AttractionViewHolder> {

    private List<TouristAttraction> attractions;

    AttractionAdapter(List<TouristAttraction> attractions) {
        this.attractions = attractions;
    }

    @NonNull
    @Override
    public AttractionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag4_item_attraction, parent, false);
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

    class AttractionViewHolder extends RecyclerView.ViewHolder {

        Button attractionButton;

        AttractionViewHolder(View itemView) {
            super(itemView);
            attractionButton = itemView.findViewById(R.id.attractionButton);
        }

        void bind(final TouristAttraction attraction) {
            attractionButton.setText(attraction.getName());
            attractionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), TouristPlaceDetailActivity.class);
                    intent.putExtra("PLACE_NAME", attraction.getName());
                    intent.putExtra("PLACE_ADDRESS", attraction.getAddress());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}

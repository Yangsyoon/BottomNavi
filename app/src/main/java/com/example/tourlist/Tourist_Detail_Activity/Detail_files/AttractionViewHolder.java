package com.example.tourlist.Tourist_Detail_Activity.Detail_files;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlist.R;
import com.example.tourlist.Tourist_Detail_Activity.TouristPlaceDetailActivity;

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

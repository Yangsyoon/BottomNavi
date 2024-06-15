package com.example.tourlist.A_Place;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tourlist.R;

import java.util.List;

public class Place_Adapter extends RecyclerView.Adapter<Place_Adapter.Place_ViewHolder> {

    public List<Place> places;

    public Place_Adapter(List<Place> places) {
        this.places = places;

    }

    @NonNull
    @Override
    public Place_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        Log.d("P", "onBindViewHolder: viewholder create");

        return new Place_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Place_ViewHolder holder, int position) {
        Place place = places.get(position);

//        Log.d("P", "onBindViewHolder: " + place.getTitle());
        Log.d("P", "onBindViewHolder: ");

        holder.bind(place);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }


    public class Place_ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDescription;
        TextView tvAddress;
        TextView tvdist;
        ImageView imageView;
        CardView placeButton;


        Place_ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvAddress = itemView.findViewById(R.id.tv_address);
            placeButton = itemView.findViewById(R.id.placeButton);
            imageView=itemView.findViewById(R.id.iv_picture);
            tvdist=itemView.findViewById(R.id.tv_dist);
        }

        void bind(final Place place) {
            tvName.setText(place.getTitle());
            tvDescription.setText(place.getOverview());
            tvAddress.setText(place.getAddr1());
//            tvdist.setText(place.getDistance());

            String imageUrl = place.getFirstimage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Uri uri = Uri.parse(imageUrl);
                Glide.with(itemView.getContext()).load(uri).into(imageView);
            } else {
                Log.d("2", "Image URL is null or empty");
            }

            placeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), Place_DetailActivity.class);
                    intent.putExtra("CONTENT_ID", place.getContentid());
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        /*Button placeButton;

        Place_ViewHolder(View itemView) {
            super(itemView);
            placeButton = itemView.findViewById(R.id.placeButton);
        }

        void bind(final Place place) {
            placeButton.setText(place.getTitle());
            Log.d("t", "bind: " + place.getTitle());
            Log.d("t", "bind: ");
            placeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), Place_DetailActivity.class);
                    intent.putExtra("CONTENT_ID", place.getContentid());
                    itemView.getContext().startActivity(intent);
                }
            });
        }*/
    }
}

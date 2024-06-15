package com.example.tourlist.A_Course;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tourlist.R;

import java.util.List;

public class CourseDetail_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlaceViewModel_course viewModel;
    private PlaceAdapter placeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Log.d("1", "3");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        placeAdapter = new PlaceAdapter();
        recyclerView.setAdapter(placeAdapter);

        viewModel = new ViewModelProvider(this).get(PlaceViewModel_course.class);

        String contentId = getIntent().getStringExtra("CONTENT_ID");
        Log.d("1", "1contentId: " + contentId);

        viewModel.loadTouristCoursePlaces(contentId);
        Log.d("1", "2contentId: " + contentId);

        viewModel.getPlaces().observe(this, new Observer<List<TouristCoursePlace>>() {
            @Override
            public void onChanged(List<TouristCoursePlace> places) {
                if (places != null) {
                    placeAdapter.setPlaces(places);
                    Log.d("1", "4contentId: " + contentId);
                }
            }
        });
    }

    public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

        private List<TouristCoursePlace> places;

        public void setPlaces(List<TouristCoursePlace> places) {
            this.places = places;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_place_detail, parent, false);
            return new PlaceViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
            TouristCoursePlace place = places.get(position);
            holder.bind(place);
        }

        @Override
        public int getItemCount() {
            return places == null ? 0 : places.size();
        }

        class PlaceViewHolder extends RecyclerView.ViewHolder {

            private TextView nameTextView;
            private ImageView imageView;
            private TextView addressTextView;
            private TextView overviewTextView;

            public PlaceViewHolder(@NonNull View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.placeNameTextView);
                imageView = itemView.findViewById(R.id.placeImageView);
                addressTextView = itemView.findViewById(R.id.placeAddressTextView);
                overviewTextView = itemView.findViewById(R.id.placeOverviewTextView);
            }

            public void bind(TouristCoursePlace place) {
                nameTextView.setText(place.getSubname());
                addressTextView.setText(place.getAddr1()); // 주소 설정
//                addressTextView.setText("icandoit"); // 주소 설정
                overviewTextView.setText(place.getSubdetailoverview());

                String imageUrl = place.getFirstimage();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Uri uri = Uri.parse(imageUrl);
                    Glide.with(itemView.getContext()).load(uri).into(imageView);
                } else {
                    Log.d("2", "Image URL is null or empty");
                }
            }
        }
    }
}
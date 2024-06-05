package com.example.tourlist.Course;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlist.R;
import com.example.tourlist.Tourist_Detail_Activity.Detail_files.TouristAttraction;
import com.example.tourlist.Tourist_Detail_Activity.Detail_files.TouristPlace;
import com.example.tourlist.Tourist_Detail_Activity.Detail_files.TouristPlaceDataHolder;
import com.example.tourlist.Tourist_Detail_Activity.Detail_files.TouristViewModel;
import com.example.tourlist.Tourist_Detail_Activity.TouristPlaceDetailActivity;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Frag_Course_List extends Fragment {
    private static final String TAG = "Frag4_GoogleMap";



    private String selectedRegion = "";



    private CourseViewModel courseViewModel;
    private CourseAdapter courseAdapter;
    private List<TouristCourse> courses;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_course_list, container, false);


        RecyclerView recyclerView2_course = view.findViewById(R.id.recyclerView2);


        recyclerView2_course.setLayoutManager(new LinearLayoutManager(getContext()));




        // CourseViewModel 설정
        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        courses = new ArrayList<>();

        //course adapter
        courseAdapter = new CourseAdapter(courses);
        recyclerView2_course.setAdapter(courseAdapter);


        courseViewModel.getTouristCourses().observe(getViewLifecycleOwner(), new Observer<List<TouristCourse>>() {
            @Override
            public void onChanged(List<TouristCourse> touristCourses) {
                courses.clear();
                courses.addAll(touristCourses);
                courseAdapter.notifyDataSetChanged();
            }
        });







        return view;
    }


/*
    private class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.AttractionViewHolder> {

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
            holder.bind(attraction);// 장소 리스트(배열의 인덱스해당하는 값.). 0 , 1, 2
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
                        TouristPlace place = new TouristPlace(attraction.getName(), attraction.getlatitude(), attraction.getlongitude(), attraction.getAddress(), attraction.getDescription(), attraction.getPhone());
                        TouristPlaceDataHolder.getInstance().setPlace(place);

                        Toast.makeText(getContext(), "openTour " + place.getPlaceName(), Toast.LENGTH_SHORT).show();

//                        Places.initialize(getContext(), "AIzaSyAlkrLP2vM_bjmH2vFcRjNSQNN4IZkBKD4");
//                        placesClient = Places.createClient(getContext());
//
//                        searchPlaceIdByName(place.getPlaceName(), place);
                    }
                });
            }
        }
    }

    private void searchPlaceIdByName(String placeName, TouristPlace place) {
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(placeName)
                .build();
        Log.d(TAG, "No predictions found for place: " + placeName);

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            if (!response.getAutocompletePredictions().isEmpty()) {
                AutocompletePrediction prediction = response.getAutocompletePredictions().get(0);
                String placeId = prediction.getPlaceId();
                Log.d(TAG, "Found placeId: " + placeId);
                fetchPlaceDetails(placeId, place);
            } else {
                Log.d(TAG, "No predictions found for place: " + placeName);
            }
        }).addOnFailureListener((exception) -> {
            Log.e(TAG, "Error finding place predictions: " + exception.getMessage());
        });
    }

    private void fetchPlaceDetails(String placeId, TouristPlace place) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.PHOTO_METADATAS);

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place fetchedPlace = response.getPlace();
            String name = fetchedPlace.getName();
            Log.d(TAG, "Place name: " + name);

            if (fetchedPlace.getPhotoMetadatas() != null && !fetchedPlace.getPhotoMetadatas().isEmpty()) {
                PhotoMetadata photoMetadata = fetchedPlace.getPhotoMetadatas().get(0);
                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(500)
                        .setMaxHeight(300)
                        .build();

                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                    Bitmap bitmap = fetchPhotoResponse.getBitmap();
                    if (bitmap != null) {
                        place.setPhotoUrl(bitmapToBase64(bitmap));
                        Log.d(TAG, "Photo URL set for place: " + place.getPlaceName());
                    } else {
                        Log.d(TAG, "Bitmap is null");
                    }
                    openTouristPlaceDetailActivity(place);
                }).addOnFailureListener((exception) -> {
                    Log.e(TAG, "Error fetching photo: " + exception.getMessage());
                    openTouristPlaceDetailActivity(place);
                });
            } else {
                Log.d(TAG, "No photo metadata available");
                openTouristPlaceDetailActivity(place);
            }
        }).addOnFailureListener((exception) -> {
            Log.e(TAG, "Place not found: " + exception.getMessage());
        });
    }

    private void openTouristPlaceDetailActivity(TouristPlace place) {
        Toast.makeText(getContext(), "openTour~" + place.getPlaceName(), Toast.LENGTH_SHORT).show();
        TouristPlaceDataHolder.getInstance().setPlace(place);
        Intent intent = new Intent(getActivity(), TouristPlaceDetailActivity.class);
        startActivity(intent);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.d(TAG, "Encoded bitmap to Base64: " + encoded);
        return encoded;
    }*/
}

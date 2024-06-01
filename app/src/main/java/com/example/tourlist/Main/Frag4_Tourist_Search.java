package com.example.tourlist.Main;



import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.tourlist.Tourist_Detail_Activity.TouristPlaceDetailActivity;
import com.example.tourlist.Tourist_Detail_Activity.Detail_files.TouristViewModel;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
//

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;


import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;


public class Frag4_Tourist_Search extends Fragment {
    private static final String TAG = "Frag5_GoogleMap";

    private List<TouristAttraction> attractions;
    private List<TouristAttraction> filteredAttractions;
    private AttractionAdapter adapter;
    private EditText searchEditText;
    private String selectedRegion = "";

    private PlacesClient placesClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag4_tourist, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
        Button seoulButton = view.findViewById(R.id.seoulButton);
        Button daeguButton = view.findViewById(R.id.daeguButton);
        Button busanButton = view.findViewById(R.id.busanButton);
        Button gyeonggiButton = view.findViewById(R.id.gyeonggiButton);
        Button jeonButton = view.findViewById(R.id.jeonButton);
        Button chungButton = view.findViewById(R.id.chungButton);
        Button gyeongButton = view.findViewById(R.id.gyeongButton);
        Button daejeonButton = view.findViewById(R.id.daejeonButton);
        Button gangwonButton = view.findViewById(R.id.gangwonButton);
        Button jejuButton = view.findViewById(R.id.jejuButton);
        Button incheonButton = view.findViewById(R.id.incheonButton);
        Button ulsanButton = view.findViewById(R.id.ulsanButton);
        Button allButton = view.findViewById(R.id.allButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        filteredAttractions = new ArrayList<>();
        adapter = new AttractionAdapter(filteredAttractions);
        recyclerView.setAdapter(adapter);

        TouristViewModel viewModel = new ViewModelProvider(requireActivity()).get(TouristViewModel.class);
        viewModel.getTouristAttractions().observe(getViewLifecycleOwner(), new Observer<List<TouristAttraction>>() {
            @Override
            public void onChanged(List<TouristAttraction> touristAttractions) {
                attractions = touristAttractions;
                filter(searchEditText.getText().toString(), selectedRegion);
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString(), selectedRegion);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        View.OnClickListener regionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRegion = ((Button) v).getText().toString();
                filter(searchEditText.getText().toString(), selectedRegion);
            }
        };

        seoulButton.setOnClickListener(regionClickListener);
        daeguButton.setOnClickListener(regionClickListener);
        busanButton.setOnClickListener(regionClickListener);
        gyeonggiButton.setOnClickListener(regionClickListener);
        jeonButton.setOnClickListener(regionClickListener);
        chungButton.setOnClickListener(regionClickListener);
        gyeongButton.setOnClickListener(regionClickListener);
        daejeonButton.setOnClickListener(regionClickListener);
        gangwonButton.setOnClickListener(regionClickListener);
        jejuButton.setOnClickListener(regionClickListener);
        incheonButton.setOnClickListener(regionClickListener);
        ulsanButton.setOnClickListener(regionClickListener);



        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRegion = "";
                filter(searchEditText.getText().toString(), selectedRegion);
            }
        });

        return view;
    }

    private void filter(String text, String region) {
        if (attractions == null || attractions.isEmpty()) {
            // 데이터가 아직 로드되지 않았습니다.
            Toast.makeText(getContext(), "데이터가 로드되지 않았습니다.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Attractions data is null or empty. Skipping filter.");
            return;
        }
        filteredAttractions.clear();
        for (TouristAttraction attraction : attractions) {
            if ((attraction.getName().contains(text) || attraction.getAddress().contains(text)) &&
                    (region.isEmpty() || attraction.getAddress().contains(region))) {
                filteredAttractions.add(attraction);
            }
        }
        adapter.notifyDataSetChanged();
    }

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
                            TouristPlace place=new TouristPlace(attraction.getName(), attraction.getlatitude(), attraction.getlongitude(),attraction.getAddress(),attraction.getDescription(),attraction.getPhone());
//                            new TouristPlace(placeName[0], latitude[0], longitude[0], address[0], description[0], phone[0])
                            TouristPlaceDataHolder.getInstance().setPlace(place);

                            Toast.makeText(getContext(),"openTour "+place.getPlaceName(),Toast.LENGTH_SHORT).show();


                            Places.initialize(getContext(), "AIzaSyBTmeSMNBR9Fp8IQ3SjG16BU3m-DJdSqPI");
                            placesClient = Places.createClient(getContext());

                            searchPlaceIdByName(place.getPlaceName(), place);



//                            Intent intent = new Intent(itemView.getContext(), TouristPlaceDetailActivity.class);
                            //상세 액티비티에서 인텐트받아서 설정하는게 아니라 place hold.. 로 설정해서 못쓴다.
//                            intent.putExtra("PLACE_NAME", attraction.getName());
//                            intent.putExtra("ADDRESS", attraction.getAddress());
//                            intent.putExtra("DESCRIPTION", attraction.getDescription());
//                            intent.putExtra("PHONE", attraction.getPhone());
//                            intent.putExtra("PHOTO_URL", attraction.getPhotoUrl());
//                            itemView.getContext().startActivity(intent);
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
//                 장소 세부 정보 가져오기
                fetchPlaceDetails(placeId, place);
            } else {
                Log.d(TAG, "No predictions found for place: " + placeName);
            }
        }).addOnFailureListener((exception ) -> {
            Log.e(TAG, "Error finding place predictions: " + exception.getMessage());
        });
    }


    private void fetchPlaceDetails(String placeId, TouristPlace place) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.PHOTO_METADATAS);

        // FetchPlaceRequest 객체 생성
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place fetchedPlace = response.getPlace();

            // 장소 이름 가져오기
            String name = fetchedPlace.getName();
            Log.d(TAG, "Place name: " + name);

            // 장소 사진 가져오기
            if (fetchedPlace.getPhotoMetadatas() != null && !fetchedPlace.getPhotoMetadatas().isEmpty()) {
                // 첫 번째 사진 메타데이터 가져오기
                PhotoMetadata photoMetadata = fetchedPlace.getPhotoMetadatas().get(0);

                // 사진을 가져오기 위한 요청
                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(500) // 사진의 최대 너비
                        .setMaxHeight(300) // 사진의 최대 높이
                        .build();

                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                    Bitmap bitmap = fetchPhotoResponse.getBitmap();
                    if (bitmap != null) {
                        // 사진 URL을 TouristPlace 객체에 설정
                        place.setPhotoUrl(bitmapToBase64(bitmap));
                        Log.d(TAG, "Photo URL set for place: " + place.getPlaceName());
                    } else {
                        Log.d(TAG, "Bitmap is null");
                    }
                    // TouristPlaceDetailActivity로 이동
                    openTouristPlaceDetailActivity(place);
                }).addOnFailureListener((exception) -> {
                    Log.e(TAG, "Error fetching photo: " + exception.getMessage());
                    // 사진이 없을 경우에도 TouristPlaceDetailActivity로 이동
                    openTouristPlaceDetailActivity(place);
                });
            } else {
                Log.d(TAG, "No photo metadata available");
                // 사진이 없을 경우에도 TouristPlaceDetailActivity로 이동
                openTouristPlaceDetailActivity(place);
            }
        }).addOnFailureListener((exception) -> {
            Log.e(TAG, "Place not found: " + exception.getMessage());
        });
    }


    private void openTouristPlaceDetailActivity(TouristPlace place) {
        Toast.makeText(getContext(),"openTour~"+place.getPlaceName(),Toast.LENGTH_SHORT).show();
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
    }

}


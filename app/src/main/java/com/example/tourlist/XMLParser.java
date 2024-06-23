package com.example.tourlist;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlist.A_Course.Course;
import com.example.tourlist.A_Course.CourseAdapter;
import com.example.tourlist.A_Course.CourseViewModel;
import com.example.tourlist.Tourist_Detail_Activity.Detail_files.TouristAttraction;
import com.example.tourlist.Tourist_Detail_Activity.Detail_files.TouristPlace;
import com.example.tourlist.Tourist_Detail_Activity.Detail_files.TouristPlaceDataHolder;
import com.example.tourlist.Tourist_Detail_Activity.Detail_files.TouristViewModel;
import com.example.tourlist.Tourist_Detail_Activity.TouristPlaceDetailActivity;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XMLParser {
    public List<TouristAttraction> parse(InputStream inputStream) {
        List<TouristAttraction> attractions = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            NodeList nodeList = document.getElementsByTagName("record");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String name = element.getElementsByTagName("관광지명").item(0).getTextContent();
                    String address = element.getElementsByTagName("소재지도로명주소").item(0).getTextContent();

                    attractions.add(new TouristAttraction(name, address));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return attractions;
    }

    public static class Old_Frag3_Tourist_Search extends Fragment {
        private static final String TAG = "Frag3_Tourist_Search";

        private List<TouristAttraction> attractions;
        private List<TouristAttraction> filteredAttractions;
        private AttractionAdapter adapter;
        private EditText searchEditText;
        private String selectedRegion = "";

        private PlacesClient placesClient;

        private CourseViewModel courseViewModel;
        private CourseAdapter courseAdapter;
        private List<Course> courses;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.old_frag3_tourist_search, container, false);
        Log.d("m", "onCreateView: 3");
            RecyclerView recyclerView1_placelist = view.findViewById(R.id.recyclerView1);
    //        RecyclerView recyclerView2_course = view.findViewById(R.id.recyclerView2);
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

            recyclerView1_placelist.setLayoutManager(new LinearLayoutManager(getContext()));
    //        recyclerView2_course.setLayoutManager(new LinearLayoutManager(getContext()));

            filteredAttractions = new ArrayList<>();
            adapter = new AttractionAdapter(filteredAttractions);
            recyclerView1_placelist.setAdapter(adapter);

            // Places API 초기화 - 사진
            if (!Places.isInitialized()) {
                // 형준 : AIzaSyAlkrLP2vM_bjmH2vFcRjNSQNN4IZkBKD4
                Places.initialize(requireContext(), "AIzaSyAlkrLP2vM_bjmH2vFcRjNSQNN4IZkBKD4");
            }
            placesClient = Places.createClient(requireContext());

            // 설정된 ViewModel을 가져옴
            TouristViewModel viewModel = new ViewModelProvider(requireActivity()).get(TouristViewModel.class);
            viewModel.getTouristAttractions().observe(getViewLifecycleOwner(), new Observer<List<TouristAttraction>>() {
                @Override
                public void onChanged(List<TouristAttraction> touristAttractions) {
                    attractions = touristAttractions;
                    filter(searchEditText.getText().toString(), selectedRegion);
                }
            });

            // CourseViewModel 설정
    //        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
    //        courses = new ArrayList<>();
    //        courseAdapter = new CourseAdapter(courses);
    //        recyclerView2_course.setAdapter(courseAdapter);

           /* courseViewModel.getTouristCourses().observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
                @Override
                public void onChanged(List<Course> places) {
                    courses.clear();
                    courses.addAll(places);
                    courseAdapter.notifyDataSetChanged();
                }
            });*/

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

            public class AttractionViewHolder extends RecyclerView.ViewHolder {

                CardView attractionButton;
                ImageView ivPicture;
                TextView tvName;
                TextView tvDescription;
                TextView tvAddress;

                AttractionViewHolder(View itemView) {
                    super(itemView);
                    attractionButton = itemView.findViewById(R.id.attractionButton);
                    ivPicture = itemView.findViewById(R.id.iv_picture);
                    tvName = itemView.findViewById(R.id.tv_name);
                    tvDescription = itemView.findViewById(R.id.tv_description);
                    tvAddress = itemView.findViewById(R.id.tv_address);
                }

                void bind(final TouristAttraction attraction) {
                    // TextView 및 ImageView에 데이터를 설정
                    tvName.setText(attraction.getName());
                    tvDescription.setText(attraction.getDescription());
                    tvAddress.setText(attraction.getAddress());

                    attractionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TouristPlace place = new TouristPlace(attraction.getName(), attraction.getlatitude(), attraction.getlongitude(),attraction.getAddress(), attraction.getDescription(), attraction.getPhone());
                            TouristPlaceDataHolder.getInstance().setPlace(place);

                            Toast.makeText(itemView.getContext(), "openTour " + place.getPlaceName(), Toast.LENGTH_SHORT).show();

                            if (placesClient == null) {
                                Log.e(TAG, "PlacesClient가 초기화되지 않았습니다.");
                                return;
                            }

                            searchPlaceIdByName(place.getPlaceName(), place);
                        }
                    });
                }
            }
        }

        private void searchPlaceIdByName(String placeName, TouristPlace place) {
            if (placesClient == null) {
                Log.e(TAG, "PlacesClient가 초기화되지 않았습니다.");
                return;
            }

            FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                    .setQuery(placeName)
                    .build();

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
            List<com.google.android.libraries.places.api.model.Place.Field> placeFields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.PHOTO_METADATAS);

            FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

            placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                com.google.android.libraries.places.api.model.Place fetchedPlace = response.getPlace();
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
        }
    }
}


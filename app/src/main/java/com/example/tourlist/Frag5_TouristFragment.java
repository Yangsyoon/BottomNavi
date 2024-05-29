package com.example.tourlist;

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
import java.util.ArrayList;
import java.util.List;

public class Frag5_TouristFragment extends Fragment {

    private List<TouristAttraction> attractions;
    private List<TouristAttraction> filteredAttractions;
    private AttractionAdapter adapter;
    private EditText searchEditText;
    private String selectedRegion = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tourist, container, false);

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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attraction, parent, false);
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
                            intent.putExtra("ADDRESS", attraction.getAddress());
                            intent.putExtra("DESCRIPTION", attraction.getDescription());
                            intent.putExtra("PHONE", attraction.getPhone());
                            intent.putExtra("PHOTO_URL", attraction.getPhotoUrl());
                            itemView.getContext().startActivity(intent);
                        }
                    });
                }

            }
    }
}

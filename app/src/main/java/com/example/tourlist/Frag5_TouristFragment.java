package com.example.tourlist;

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

        return view;
    }

    private void filter(String text, String region) {
        filteredAttractions.clear();
        for (TouristAttraction attraction : attractions) {
            if ((attraction.getName().contains(text) || attraction.getAddress().contains(text)) &&
                    attraction.getAddress().contains(region)) {
                filteredAttractions.add(attraction);
            }
        }
        adapter.notifyDataSetChanged();
    }
}

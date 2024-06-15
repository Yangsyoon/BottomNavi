package com.example.tourlist.Main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlist.A_Place.Place;
import com.example.tourlist.A_Place.Place_Adapter;
import com.example.tourlist.A_Place.Place_ViewModel;
import com.example.tourlist.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class Slide1_Place_List extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Place_ViewModel placeViewModel;
    private Place_Adapter placeAdapter;
    private List<Place> places;
    private FusedLocationProviderClient fusedLocationClient;
    private String currentLatitude;
    private String currentLongitude;

    public Location currentLocation;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slide1_place_list, container, false);

        RecyclerView recyclerView_place = view.findViewById(R.id.recyclerView_place);
        recyclerView_place.setLayoutManager(new LinearLayoutManager(getContext()));

        placeViewModel = new ViewModelProvider(requireActivity()).get(Place_ViewModel.class);
        places = new ArrayList<>();

        placeAdapter = new Place_Adapter(places);
        Log.d("m", "onBindViewHolder: createview");

        recyclerView_place.setAdapter(placeAdapter);

        //이거 현재 위치로 갈때 필요한거.
//        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());







        placeViewModel.getTouristPlaces().observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> places) {
                Log.d("P", "onBindViewHolder: createview");


                Slide1_Place_List.this.places.clear();
                Slide1_Place_List.this.places.addAll(places);
                placeAdapter.notifyDataSetChanged();
            }
        });

        Button allButton = view.findViewById(R.id.all_Button);
        Button seoulButton = view.findViewById(R.id.seoul_Button);
        Button daeguButton = view.findViewById(R.id.daegu_Button);
        Button busanButton = view.findViewById(R.id.busan_Button);
        Button incheonButton = view.findViewById(R.id.incheon_Button);
        Button ulsanButton = view.findViewById(R.id.ulsan_Button);
        Button daejeonButton = view.findViewById(R.id.daejeon_Button);
        Button gyeonggiButton = view.findViewById(R.id.gyeonggi_Button);
        Button jeonButton = view.findViewById(R.id.jeon_Button);
        Button chungButton = view.findViewById(R.id.chung_Button);
        Button gyeongButton = view.findViewById(R.id.gyeong_Button);
        Button gangwonButton = view.findViewById(R.id.gangwon_Button);
        Button jejuButton = view.findViewById(R.id.jeju_Button);
//        Button gpsButton = view.findViewById(R.id.my_location);




        /*getCurrentLocation();

        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                }
                placeViewModel.filterCoursesByGps(currentLatitude, currentLongitude);

            }
        });*/

        /*allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeViewModel.filterCoursesByAreaCode("");
            }
        });

        seoulButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeViewModel.filterCoursesByAreaCode("1");
            }
        });

        daeguButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeViewModel.filterCoursesByAreaCode("4");
            }
        });

        busanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeViewModel.filterCoursesByAreaCode("6");
            }
        });

        incheonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeViewModel.filterCoursesByAreaCode("2");
            }
        });

        ulsanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeViewModel.filterCoursesByAreaCode("7");
            }
        });

        daejeonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeViewModel.filterCoursesByAreaCode("3");
            }
        });

        gyeonggiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeViewModel.filterCoursesByAreaCode("31");
            }
        });

        jeonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeViewModel.filterCoursesByAreaCode("37");
            }
        });

        chungButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeViewModel.filterCoursesByAreaCode("34");
            }
        });

        gyeongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeViewModel.filterCoursesByAreaCode("35");
            }
        });

        gangwonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeViewModel.filterCoursesByAreaCode("32");
            }
        });

        jejuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeViewModel.filterCoursesByAreaCode("39");
            }
        });*/

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getCurrentLocation();

        Log.d("m","created slide place");
//        Log.d("m", String.valueOf(currentLocation.getLatitude()));
    }

    /*private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location location = task.getResult();
                            currentLatitude = Double.toString(location.getLatitude());
                            currentLongitude = Double.toString(location.getLongitude());
                            currentLocation=location;
                            placeAdapter.setCurrentLocation(currentLocation);
                            // 현재 위치를 사용하여 필요한 작업을 수행
                            // 예: 현재 위치 기반으로 코스를 필터링하거나 다른 작업 수행
//                            Toast.makeText(getContext(), "현재 위치: " + currentLatitude + ", " + currentLongitude, Toast.LENGTH_LONG).show();
                        } else {
//                            Toast.makeText(getContext(), "위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }*/

    //방금
    /*private void getCurrentLocation() {
        try {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLocation = location;
                            currentLatitude = Double.toString(location.getLatitude());
                            currentLongitude = Double.toString(location.getLongitude());
                            placeAdapter.setCurrentLocation(location);

                            Log.d("PP","ok2");
//                            Toast.makeText(getContext(), "Current location acquired", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }*/

    //이거 frag1 NaverMap에서 복북한건데 멤버변수 초기화 안된다.
    public void getCurrentLocation() {
        try {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLatitude = Double.toString(location.getLatitude());
                            currentLongitude = Double.toString(location.getLongitude());
                            currentLocation = location;
                            placeAdapter.setCurrentLocation(location);
                            Log.d("PP","ok2");
                            Toast.makeText(getContext(), "Current location acquired", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(getContext(), "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

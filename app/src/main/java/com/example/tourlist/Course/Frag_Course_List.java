package com.example.tourlist.Course;

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

import com.example.tourlist.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class Frag_Course_List extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private CourseViewModel courseViewModel;
    private CourseAdapter courseAdapter;
    private List<TouristCourse> courses;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String currentLatitude;
    private String currentLongitude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_course_list, container, false);

        RecyclerView recyclerView2_course = view.findViewById(R.id.recyclerView2);
        recyclerView2_course.setLayoutManager(new LinearLayoutManager(getContext()));

        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        courses = new ArrayList<>();

        courseAdapter = new CourseAdapter(courses);
        recyclerView2_course.setAdapter(courseAdapter);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        courseViewModel.getTouristCourses().observe(getViewLifecycleOwner(), new Observer<List<TouristCourse>>() {
            @Override
            public void onChanged(List<TouristCourse> touristCourses) {
                courses.clear();
                courses.addAll(touristCourses);
                courseAdapter.notifyDataSetChanged();
            }
        });

        Button allButton = view.findViewById(R.id.allButton);
        Button seoulButton = view.findViewById(R.id.seoulButton);
        Button daeguButton = view.findViewById(R.id.daeguButton);
        Button busanButton = view.findViewById(R.id.busanButton);
        Button incheonButton = view.findViewById(R.id.incheonButton);
        Button ulsanButton = view.findViewById(R.id.ulsanButton);
        Button daejeonButton = view.findViewById(R.id.daejeonButton);
        Button gyeonggiButton = view.findViewById(R.id.gyeonggiButton);
        Button jeonButton = view.findViewById(R.id.jeonButton);
        Button chungButton = view.findViewById(R.id.chungButton);
        Button gyeongButton = view.findViewById(R.id.gyeongButton);
        Button gangwonButton = view.findViewById(R.id.gangwonButton);
        Button jejuButton = view.findViewById(R.id.jejuButton);
        Button gpsButton = view.findViewById(R.id.mylocation);

        getCurrentLocation();

        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                }
                courseViewModel.filterCoursesByGps(currentLatitude, currentLongitude);

            }
        });

        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseViewModel.filterCoursesByAreaCode("");
            }
        });

        seoulButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseViewModel.filterCoursesByAreaCode("1");
            }
        });

        daeguButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseViewModel.filterCoursesByAreaCode("4");
            }
        });

        busanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseViewModel.filterCoursesByAreaCode("6");
            }
        });

        incheonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseViewModel.filterCoursesByAreaCode("2");
            }
        });

        ulsanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseViewModel.filterCoursesByAreaCode("7");
            }
        });

        daejeonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseViewModel.filterCoursesByAreaCode("3");
            }
        });

        gyeonggiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseViewModel.filterCoursesByAreaCode("31");
            }
        });

        jeonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseViewModel.filterCoursesByAreaCode("37");
            }
        });

        chungButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseViewModel.filterCoursesByAreaCode("34");
            }
        });

        gyeongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseViewModel.filterCoursesByAreaCode("35");
            }
        });

        gangwonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseViewModel.filterCoursesByAreaCode("32");
            }
        });

        jejuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseViewModel.filterCoursesByAreaCode("39");
            }
        });

        return view;
    }

    private void getCurrentLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location location = task.getResult();
                            currentLatitude = Double.toString(location.getLatitude());
                            currentLongitude = Double.toString(location.getLongitude());

                            // 현재 위치를 사용하여 필요한 작업을 수행
                            // 예: 현재 위치 기반으로 코스를 필터링하거나 다른 작업 수행
//                            Toast.makeText(getContext(), "현재 위치: " + currentLatitude + ", " + currentLongitude, Toast.LENGTH_LONG).show();
                        } else {
//                            Toast.makeText(getContext(), "위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

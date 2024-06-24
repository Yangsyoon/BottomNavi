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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlist.A_Course.CourseAdapter;
import com.example.tourlist.A_Course.Course;
import com.example.tourlist.A_Course.CourseViewModel;
import com.example.tourlist.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;

public class Slide1_Course_List extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private CourseViewModel courseViewModel;
    private CourseAdapter courseAdapter;
    private List<Course> courses;
    private FusedLocationProviderClient fusedLocationClient;
    private String currentLatitude;
    private String currentLongitude;
    private Location currentLocation;
    private FusedLocationSource locationSource;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slide1_course_list, container, false);

        //이거 현재 위치로 갈때 필요한거.
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getCurrentLocation();


        Log.d("m","8");

        //코스 목록 띄울 리사이클러뷰
        RecyclerView recyclerView2_course = view.findViewById(R.id.recyclerView2);
        recyclerView2_course.setLayoutManager(new LinearLayoutManager(getContext()));

        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        courses = new ArrayList<>();


        //코스 목록 구성할 adapter
//        courseAdapter = new CourseAdapter(courses);
        courseAdapter = new CourseAdapter(courses, currentLocation);
        recyclerView2_course.setAdapter(courseAdapter);
        if(currentLocation!=null) {
            Log.d("PP", "CourseAdapter: 123" + currentLocation.getLatitude());
        }

        //코스 데이터들을 저장할 코스 뷰 모델
        //코스 초기 전체 코스목록 받아오기 위한 함수.
        //Tourapi의 지역기반 서비스의 코스 xml을 요청한다.
        courseViewModel.getTouristCourses().observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                getCurrentLocation();

                Slide1_Course_List.this.courses.clear();
                Slide1_Course_List.this.courses.addAll(courses);
                courseAdapter.notifyDataSetChanged();
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
        Button gpsButton = view.findViewById(R.id.my_location);


        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                }
                Log.d("p", "3");

                getCurrentLocation();
                Log.d("p", currentLatitude + " " + currentLongitude);

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//이거 현재 위치로 갈때 필요한거.


    }


    /*private void getCurrentLocation(View view) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한 요청
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
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
                            currentLocation = location;
                            Log.d("PP", "현재 위치: " + currentLatitude + ", " + currentLongitude);
                            Log.d("PP", "현재 위치: " + location.getLatitude() + ", " + location.getLongitude());

                            // 현재 위치를 사용하여 필요한 작업을 수행
                            // 예: 현재 위치 기반으로 코스를 필터링하거나 다른 작업 수행
                            courseAdapter = new CourseAdapter(courses, currentLocation);
                            RecyclerView recyclerView2_course = view.findViewById(R.id.recyclerView2);
                            recyclerView2_course.setAdapter(courseAdapter);
                        } else {
                            Log.d("PP", "위치를 가져올 수 없습니다.");
                        }
                    }
                });
    }*/

    private void getCurrentLocation() {
        try {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLocation = location;
                            currentLatitude = Double.toString(location.getLatitude());
                            currentLongitude = Double.toString(location.getLongitude());
                            courseAdapter.setCurrentLocation(location);

                            Log.d("PP","ok");
//                            Toast.makeText(getContext(), "Current location acquired", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getCurrentLocation(view);
//            } else {
//                Toast.makeText(getContext(), "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}

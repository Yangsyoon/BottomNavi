package com.example.tourlist.Main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.net.URLEncoder;
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

    String serviceType = "areaBasedList1";
    String numOfRows = "3";
    String pageNo = "1";
    String mobileOS = "AND";
    String mobileApp = "AppTest";
    String listYN = "Y";
    String arrange = "O";
    String contentTypeId = "12";//
    String areaCode = "";
    String sigunguCode = "";
    String cat1 = "";
    String cat2 = "";
    String cat3 = "";

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        placeViewModel.getTouristPlaces_observe().observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
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
        Button gpsButton = view.findViewById(R.id.my_location2);

        Button placeButton = view.findViewById(R.id.place_Button);
        Button cafeButton = view.findViewById(R.id.cafe_Button);
        Button foodButton = view.findViewById(R.id.food_Button);
        Button areaButton=view.findViewById(R.id.area_Button);

        Button searchButton = view.findViewById(R.id.search_Button);


        Button contenttypeButton = view.findViewById(R.id.contenttype_Button);

        contenttypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentTypeDialog();
            }
        });


        getCurrentLocation();

        {
        areaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAreaDialog();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceKey = "jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D";

                String requestUrl = buildRequestUrl(serviceType, serviceKey, numOfRows, pageNo, mobileOS, mobileApp, listYN, arrange, contentTypeId, areaCode, sigunguCode, cat1, cat2, cat3);

                placeViewModel.filterPlacesByAreaCode(requestUrl);

                cat1="";
                cat2="";
                cat3="";
                areaCode="";

                // 필요한 로직 추가
                // 예: placeViewModel.fetchPlaces(requestUrl);
            }
        });

        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentTypeId = "12";
                // 예를 들어:
                // placeViewModel.filterPlacesByContentTypeId(contentTypeId);
            }
        });

        cafeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentTypeId = "39";
                cat1 = "A05";
                cat2 = "A0502";
                cat3 = "A05020900";
            }
        });

        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFoodTypeDialog();
            }
        });



        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                     placeViewModel.filterPlacesByGps(currentLatitude, currentLongitude, "12");
                }
            }
        });

        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaCode = "";
                // placeViewModel.filterPlacesByAreaCode("");
            }
        });

        seoulButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaCode = "1";
                // placeViewModel.filterPlacesByAreaCode("1");
            }
        });

        daeguButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaCode = "4";
                // placeViewModel.filterPlacesByAreaCode("4");
            }
        });

        busanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaCode = "6";
                // placeViewModel.filterPlacesByAreaCode("6");
            }
        });

        incheonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaCode = "2";
                // placeViewModel.filterPlacesByAreaCode("2");
            }
        });

        ulsanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaCode = "7";
                // placeViewModel.filterPlacesByAreaCode("7");
            }
        });

        daejeonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaCode = "3";
                // placeViewModel.filterPlacesByAreaCode("3");
            }
        });

        gyeonggiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaCode = "31";
                // placeViewModel.filterPlacesByAreaCode("31");
            }
        });

        jeonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaCode = "37";
                // placeViewModel.filterPlacesByAreaCode("37");
            }
        });

        chungButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaCode = "34";
                // placeViewModel.filterPlacesByAreaCode("34");
            }
        });

        gyeongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaCode = "35";
                // placeViewModel.filterPlacesByAreaCode("35");
            }
        });

        gangwonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaCode = "32";
                // placeViewModel.filterPlacesByAreaCode("32");
            }
        });

        jejuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaCode = "39";
                // placeViewModel.filterPlacesByAreaCode("39");
            }
        });

    }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCurrentLocation();
        Log.d("m","created slide place");
    }


    private void showContentTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("관광타입")
                .setItems(new String[]{"관광지", "문화시설", "축제공연행사", "여행코스", "레포츠", "숙박", "쇼핑", "음식점"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 항목 선택 시의 동작을 정의합니다.
                        switch (which) {
                            case 0:
                                // 관광지 선택됨
                                contentTypeId = "12";
                                break;
                            case 1:
                                // 문화시설 선택됨
                                contentTypeId = "14";
                                break;
                            case 2:
                                // 축제공연행사 선택됨
                                contentTypeId = "15";
                                break;
                            case 3:
                                // 여행코스 선택됨
                                contentTypeId = "25";
                                break;
                            case 4:
                                // 레포츠 선택됨
                                contentTypeId = "28";
                                break;
                            case 5:
                                // 숙박 선택됨
                                contentTypeId = "32";
                                break;
                            case 6:
                                // 쇼핑 선택됨
                                contentTypeId = "38";
                                break;
                            case 7:
                                // 음식점 선택됨
                                contentTypeId = "39";
                                break;
                        }
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 취소 버튼 클릭 시 동작
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void showFoodTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("음식점 타입")
                .setItems(new String[]{"한식", "서양식", "일식", "중식", "이색음식점"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 항목 선택 시의 동작을 정의합니다.
                        contentTypeId = "39";
                        cat1 = "A05";
                        cat2 = "A0502";
                        switch (which) {
                            case 0:
                                // 한식 선택됨
                                cat3 = "A05020100";
                                break;
                            case 1:
                                // 서양식 선택됨
                                cat3 = "A05020200";
                                break;
                            case 2:
                                // 일식 선택됨
                                cat3 = "A05020300";
                                break;
                            case 3:
                                // 중식 선택됨
                                cat3 = "A05020400";
                                break;
                            case 4:
                                // 이색음식점 선택됨
                                cat3 = "A05020700";
                                break;
                        }
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 취소 버튼 클릭 시 동작
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void showAreaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("지역 선택")
                .setItems(new String[]{"서울", "인천", "대전", "대구", "광주", "부산", "울산", "세종특별자치시", "경기도", "강원특별자치도"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 항목 선택 시의 동작을 정의합니다.
                        switch (which) {
                            case 0:
                                // 서울 선택됨
                                areaCode = "1";
                                break;
                            case 1:
                                // 인천 선택됨
                                areaCode = "2";
                                break;
                            case 2:
                                // 대전 선택됨
                                areaCode = "3";
                                break;
                            case 3:
                                // 대구 선택됨
                                areaCode = "4";
                                break;
                            case 4:
                                // 광주 선택됨
                                areaCode = "5";
                                break;
                            case 5:
                                // 부산 선택됨
                                areaCode = "6";
                                break;
                            case 6:
                                // 울산 선택됨
                                areaCode = "7";
                                break;
                            case 7:
                                // 세종특별자치시 선택됨
                                areaCode = "8";
                                break;
                            case 8:
                                // 경기도 선택됨
                                areaCode = "31";
                                break;
                            case 9:
                                // 강원특별자치도 선택됨
                                areaCode = "32";
                                break;
                        }
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 취소 버튼 클릭 시 동작
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }



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

    private String buildRequestUrl(String serviceType, String serviceKey, String numOfRows, String pageNo, String mobileOS, String mobileApp, String listYN, String arrange, String contentTypeId, String areaCode, String sigunguCode, String cat1, String cat2, String cat3) {
        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/";
        try {
            StringBuilder urlBuilder = new StringBuilder(requestUrl);
            urlBuilder.append(URLEncoder.encode(serviceType, "UTF-8"));
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode(mobileOS, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode(mobileApp, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("listYN", "UTF-8") + "=" + URLEncoder.encode(listYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("arrange", "UTF-8") + "=" + URLEncoder.encode(arrange, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" + URLEncoder.encode(contentTypeId, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("areaCode", "UTF-8") + "=" + URLEncoder.encode(areaCode, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("sigunguCode", "UTF-8") + "=" + URLEncoder.encode(sigunguCode, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("cat1", "UTF-8") + "=" + URLEncoder.encode(cat1, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("cat2", "UTF-8") + "=" + URLEncoder.encode(cat2, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("cat3", "UTF-8") + "=" + URLEncoder.encode(cat3, "UTF-8"));
            return urlBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

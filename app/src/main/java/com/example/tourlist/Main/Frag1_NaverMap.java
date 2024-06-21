package com.example.tourlist.Main;

import static android.widget.Toast.LENGTH_SHORT;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.TimeoutError;
import com.bumptech.glide.Glide;
import com.example.tourlist.R;
import com.example.tourlist.Tourist_Detail_Activity.Detail_files.TouristPlace;
import com.example.tourlist.Tourist_Detail_Activity.Detail_files.TouristPlaceDataHolder;
import com.example.tourlist.Tourist_Detail_Activity.TouristPlaceDetailActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;


import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;








import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;




import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

public class Frag1_NaverMap extends Fragment implements OnMapReadyCallback, View.OnClickListener{
    private String fragmentTag="NaverMap";

    public void setFragmentTag(String tag) {
        this.fragmentTag = tag;
    }

    public String getFragmentTag() {
        return fragmentTag;
    }


    private View view;

    MapView mapView;
    private NaverMap mMap;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    private boolean isFabOpen = false; // FAB 상태를 나타내는 변수


    private static final String TAG = "Frag3_GoogleMap";


    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
//    private LatLng selectedLocation;



    private FloatingActionButton fab_main, fab_sub1, fab_sub2;
    private Animation fab_open, fab_close;



    /////////////////////////////////////////////////
//    private ArrayList<String> selectedPlaces = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationClient;
    private PlacesClient placesClient;


    /////
    // 두 개의 마커를 저장할 변수
    private Marker startMarker = null;
    private Marker destinationMarker = null;

    private Marker current_beforeMarker;
    private Marker selectedMarker;
    private Marker poiMarker;

    private LatLng destinationLatLng; // 추가
    private LatLng sourceLatLng; // 추가
    private String sourceName; // 추가
    private String destinationName; // 추가

    private float distance;
    ///////////////////////////////////////

//    private Button placeNameButton;
    private Button startButton;
    private Button destinationButton;

    private CardView infoCard;
    private TextView infoTitle;
    private TextView infoDescription;
    private TextView infoAddress;

    private TextView tv_dist;

    private ImageView iv_picture;

    private Location currentLocation;
    private TouristPlace selectedPlace;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag1_navermap,container,false);

        Context mContext = getContext();
        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);

        fab_main = view.findViewById(R.id.fab_main);
        fab_sub1 = view.findViewById(R.id.fab_sub1);
        fab_sub2 = view.findViewById(R.id.fab_sub2);

        fab_main.setOnClickListener(this);
        fab_sub1.setOnClickListener(this);
        fab_sub2.setOnClickListener(this);


        try {
            // Places SDK 초기화 - 사진
            if (getActivity() != null) {
                Context context = getActivity().getApplicationContext();
                //형준 : AIzaSyAlkrLP2vM_bjmH2vFcRjNSQNN4IZkBKD4
                Places.initialize(context, "AIzaSyAlkrLP2vM_bjmH2vFcRjNSQNN4IZkBKD4");
                placesClient = Places.createClient(context);
                Log.d(TAG, "Places SDK 초기화 성공");
            } else {
                Log.e(TAG, "getActivity()가 null입니다.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Places SDK 초기화 실패", e);
        }



        startButton=view.findViewById(R.id.start_button);

        destinationButton=view.findViewById(R.id.destination_button);

        Button favoriteButton = view.findViewById(R.id.btn_fav);

//        placeNameButton = view.findViewById(R.id.place_name_button);




        infoCard = view.findViewById(R.id.info_card);
        infoTitle = view.findViewById(R.id.tv_name);
        infoDescription = view.findViewById(R.id.tv_description);
        infoAddress = view.findViewById(R.id.tv_address);

        iv_picture=view.findViewById(R.id.iv_picture);

        tv_dist=view.findViewById(R.id.tv_dist);

        //지도 출력
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        //현재 위치 설정
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());




        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (selectedMarker == null) {

                    Toast.makeText(getContext(), "출발지 마커선택하세요 ", LENGTH_SHORT).show();
                }
                else{
                    startMarker = selectedMarker;
                    sourceLatLng = startMarker.getPosition();
                    sourceName = startMarker.getCaptionText();
                    Toast.makeText(getContext(), "출발지 마커 선택: " + startMarker.getCaptionText(), LENGTH_SHORT).show();
                }
            }
        });

        destinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedMarker == null) {
                    Toast.makeText(getContext(), "도착지 마커 선택하세요: ", LENGTH_SHORT).show();
                }
                else{
                    destinationMarker = selectedMarker;
                    destinationLatLng = destinationMarker.getPosition();
                    destinationName = destinationMarker.getCaptionText();
                    Toast.makeText(getContext(), "도착지 마커 선택: " + destinationMarker.getCaptionText(), LENGTH_SHORT).show();

                }
            }
        });

        //즐겨찾기 추가 버튼
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = mAuth.getCurrentUser();
                if(user==null){
                    Toast.makeText(getContext(), "즐겨찾기 기능은 로그인이 필요합니다.", Toast.LENGTH_SHORT).show();

                }
                if (selectedMarker != null) {
                    // 데이터베이스에 위도 경도 추가 함수...
                    addFavoriteLocation(selectedMarker.getCaptionText(),selectedMarker.getPosition().latitude,selectedMarker.getPosition().longitude);
                } else {
                    Toast.makeText(getContext(), "먼저 마커를 클릭하여 위치를 선택하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //상세로 버튼
//        placeNameButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selectedPlace != null) {
//
//                    searchPlaceIdByName(selectedPlace.getPlaceName(), selectedPlace);
//                } else {
//                    Toast.makeText(getContext(), "선택된 장소가 없습니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });







        // jhj...

        //
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        //로그인 안했으면 익명 로그인
        /*if (user == null) {

            mAuth.signInAnonymously().addOnCompleteListener(getActivity(), task -> {
                if (task.isSuccessful()) {
                    // 익명 로그인 성공
                    Toast.makeText(getContext(), "게스트 로그인 성공", Toast.LENGTH_SHORT).show();
                } else {
                    // 익명 로그인 실패
                    Toast.makeText(getContext(), "게스트 로그인 실패", Toast.LENGTH_SHORT).show();
                }
            });

        }*/
        //

        //버튼들



        return view;

    }



    @Override
    public void onMapReady(@NonNull NaverMap map){

        mMap = map;

        Log.d(TAG, "GoogleMap is ready");

        //위치 권한 요청 설정
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }


        //지도 시작시, 현재 위치 변수에 저장.
        getCurrentLocation();
        Log.d("m", "cur 1");

        //내 위치 세팅
        map.setLocationSource(locationSource);
        map.setLocationTrackingMode(LocationTrackingMode.Follow);
        Log.d("m", locationSource.toString());

        // 현재 위치로 버튼
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        // POI 설정
        //setupPOI();


        //맵 클릭 시 정보창 숨기기
        mMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
// 이전에 생성된 마커가 있으면 제거
                if (current_beforeMarker != null && selectedMarker!= current_beforeMarker) {
                    current_beforeMarker.setMap(null);
                }
                // 이전에 생성된 POI 마커가 있다면 제거합니다.
                if (poiMarker != null) {
                    poiMarker.setMap(null);
                }
                //선택한 위치에 파란 마커 생성및, 다시 클릭시 '선택된 위치'라고 박스 뜸.

//                currentMarker=new Marker();
//                currentMarker.setPosition(latLng);
//                currentMarker.setCaptionText("선택된 위치");
//                currentMarker.setIconTintColor(0x478EEC);
//                currentMarker.setMap(mMap);
// 마커 클릭이 아닌 지도를 클릭했을 때 버튼 숨기기
//                placeNameButton.setVisibility(View.GONE);
            }

        });


        //길찾기!!!
        // 경로 버튼 클릭 리스너
        Button routeButton = view.findViewById(R.id.view_map_button); // 경로 버튼의 ID를 사용하여 버튼을 찾습니다.
        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startMarker == null) {
//                    toMarker = tour;
                    Toast.makeText(getContext(), "출발지 마커 선택하세요 ", LENGTH_SHORT).show();
                } else if (destinationMarker == null) {
//                    fromMarker = tour;
                    Toast.makeText(getContext(), "도착지 마커 선택하세요", LENGTH_SHORT).show();
                    // 선택된 두 마커의 이름을 가져와서 대중교통 길찾기 URL 생성

                } else {

                    String sourceName = startMarker.getCaptionText();
                    String destinationName = destinationMarker.getCaptionText();
                    LatLng sourceLatLng = startMarker.getPosition();
                    LatLng destinationLatLng = destinationMarker.getPosition();
                    String url = generateTransitRouteURL(sourceLatLng, sourceName, destinationLatLng, destinationName);

                    Toast.makeText(getContext(), "url: "+url, LENGTH_SHORT).show();
                    // 생성된 URL로 네이버 지도 앱을 열기
                    openNaverMapWithTransitRoute(url);


                    destinationMarker =null;
                    startMarker =null;

                    // 이미 두 개의 마커가 선택되어 있음
//                    Toast.makeText(getContext(), "이미 두 개의 마커를 선택하셨습니다.", LENGTH_SHORT).show();
                }
            }
        });

        infoCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                infoCard.setVisibility(View.GONE);


                if (selectedPlace != null) {

                    searchPlaceIdByName(selectedPlace.getPlaceName(), selectedPlace);
                } else {
                    Toast.makeText(getContext(), "선택된 장소가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 공공데이터로부터 관광지 정보 받아오기
        loadTouristPlaces();

    }

    //현재 위치 멤버 변수 currentLocation에 저장
    private void getCurrentLocation() {
        try {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLocation = location;
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


    private String generateTransitRouteURL(LatLng sourceLatLng, String sourceName, LatLng destinationLatLng, String destinationName) {
        String url = "nmap://route/public?";
        try {
            url += "slat=" + sourceLatLng.latitude;
            url += "&slng=" + sourceLatLng.longitude;
            url += "&sname=" + URLEncoder.encode(sourceName, "UTF-8"); // 이름에 공백 등이 있을 수 있으므로 인코딩
            url += "&dlat=" + destinationLatLng.latitude;
            url += "&dlng=" + destinationLatLng.longitude;
            url += "&dname=" + URLEncoder.encode(destinationName, "UTF-8"); // 이름에 공백 등이 있을 수 있으므로 인코딩
            url += "&appname=com.example.tourlist"; // 앱 이름 추가
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    private void openNaverMapWithTransitRoute(String url) {

//        url="nmap://map?&TourList=com.example.tourlist";

//        url = "nmap://actionPath?parameter=value&appname=com.example.tourlist";
//        url = "nmap://route/public?slat=37.4640070&slng=126.9522394&sname=%EC%84%9C%EC%9A%B8%EB%8C%80%ED%95%99%EA%B5%90&dlat=37.5209436&dlng=127.1230074&dname=%EC%98%AC%EB%A6%BC%ED%94%BD%EA%B3%B5%EC%9B%90&appname=com.example.myapp";



        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);


        startActivity(intent);

        /*if (list == null || list.isEmpty()) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("nmap://map?&TourList=com.example.tourlist")));
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nhn.android.nmap")));
        } else {
            startActivity(intent);
        }*/
    }



    private void processXmlResponse(String response) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(response));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && parser.getName().equals("record")) {
                    final String[] placeName = {""};
                    final double[] latitude = {0.0};
                    final double[] longitude = {0.0};
                    final String[] address = {""};
                    final String[] description = {""};
                    final String[] phone = {""};

                    while (!(eventType == XmlPullParser.END_TAG && parser.getName().equals("record"))) {
                        if (eventType == XmlPullParser.START_TAG) {
                            String tagName = parser.getName();
                            switch (tagName) {
                                case "관광지명":
                                    placeName[0] = parser.nextText();
                                    break;
                                case "위도":
                                    latitude[0] = Double.parseDouble(parser.nextText());
                                    break;
                                case "경도":
                                    longitude[0] = Double.parseDouble(parser.nextText());
                                    break;
                                case "소재지도로명주소":
                                    address[0] = parser.nextText();
                                    break;
                                case "관광지소개":
                                    description[0] = parser.nextText();
                                    break;
                                case "관리기관전화번호":
                                    phone[0] = parser.nextText();
                                    break;
                                default:
                                    break;
                            }
                        }
                        eventType = parser.next();
                    }

                    LatLng latLng = new LatLng(latitude[0], longitude[0]);


                    try {


                        //파싱하면서 마커 당 장소를 저장
                        Marker tourMarker = new Marker();

                        tourMarker.setPosition(latLng);
                        tourMarker.setCaptionText(placeName[0]);

                        setupMarkerIcon(tourMarker);

                        tourMarker.setTag(new TouristPlace(placeName[0], latitude[0], longitude[0], address[0], description[0], phone[0])); // Tag에 객체 저장

                        tourMarker.setMap(mMap);

                        tourMarker.setOnClickListener(new Overlay.OnClickListener() {
                            @Override
                            public boolean onClick(@NonNull Overlay overlay) {
                                getCurrentLocation();
                                selectedMarker = tourMarker;

                                    //거리 계산.
                                if (currentLocation != null) {

                                    Location markerLocation = new Location("");
                                    markerLocation.setLatitude(selectedMarker.getPosition().latitude);
                                    markerLocation.setLongitude(selectedMarker.getPosition().longitude);

                                    distance = currentLocation.distanceTo(markerLocation);
                                    float distanceInKm = distance / 1000;
                                } else {
                                }

                                TouristPlace place = (TouristPlace) tourMarker.getTag();
                                if (place != null) {
                                    infoCard.setVisibility(View.VISIBLE);

                                    // tourmarker클릭시, 위 info cardview 바뀜.
                                    // 나중에 여기에 장소 정보를 설정할 수 있습니다
                                    infoTitle.setText(place.getPlaceName());
                                    infoDescription.setText(place.getDescription());
                                    infoAddress.setText(place.getAddress());
                                    loadPlaceImage(place);

                                    if(distance>=1000){

                                        String s= String.format("%.0f", distance/1000);
                                        tv_dist.setText(s+"Km");

                                    }
                                    else{
                                        String s= String.format("%.0f", distance);
                                        tv_dist.setText(s+"m");

                                    }

                                    selectedPlace = place;
//                                    placeNameButton.setText(place.getPlaceName());
//                                    placeNameButton.setVisibility(View.VISIBLE); // 버튼 보이기
                                }
                                else {
                                    selectedPlace = null; // place가 null일 경우 selectedPlace를 null로 설정
//                                    placeNameButton.setVisibility(View.GONE); // 버튼 숨기기

                                }




                                return true; // true로 설정하여 기본 마커 클릭 동작을 유지하지 않음
                            }
                        });
                        mMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                                infoCard.setVisibility(View.GONE);
                            }
                        });

                        Log.d(TAG, "Tourist place marker added for: " + placeName + " at: " + latLng.toString());
                    } catch (Exception e) {
                        Log.e(TAG, "Error adding marker for tourist place: " + placeName, e);
                    }

                    Log.d(TAG, "Tourist place marker added for: " + placeName[0] + " at: " + latLng.toString());
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "Error parsing XML response", e);
            showToast("Error parsing XML response");
        }
    }

    private void loadPlaceImage(TouristPlace place) {
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(place.getPlaceName())
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
            if (!response.getAutocompletePredictions().isEmpty()) {
                AutocompletePrediction prediction = response.getAutocompletePredictions().get(0);
                String placeId = prediction.getPlaceId();
                Log.d(TAG, "Found placeId: " + placeId);
                fetchPlacePhoto_to_cardview(placeId, place);
            } else {
                Log.d(TAG, "No predictions found for place: " + place.getPlaceName());
            }
        }).addOnFailureListener(exception -> {
            Log.e(TAG, "Error finding place predictions: " + exception.getMessage());
        });
    }

    private void fetchPlacePhoto_to_cardview(String placeId, TouristPlace place) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.PHOTO_METADATAS);

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(request).addOnSuccessListener(response -> {
            Place fetchedPlace = response.getPlace();

            if (fetchedPlace.getPhotoMetadatas() != null && !fetchedPlace.getPhotoMetadatas().isEmpty()) {
                PhotoMetadata photoMetadata = fetchedPlace.getPhotoMetadatas().get(0);

                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(500)
                        .setMaxHeight(300)
                        .build();

                placesClient.fetchPhoto(photoRequest).addOnSuccessListener(fetchPhotoResponse -> {
                    Bitmap bitmap = fetchPhotoResponse.getBitmap();
                    if (bitmap != null) {
                        Glide.with(this)
                                .load(bitmap)
                                .into(iv_picture);
                        Log.d(TAG, "Photo loaded into ImageView for place: " + place.getPlaceName());
                    } else {
                        Log.d(TAG, "Bitmap is null");
                    }
                }).addOnFailureListener(exception -> {
                    Log.e(TAG, "Error fetching photo: " + exception.getMessage());
                });
            } else {
                Log.d(TAG, "No photo metadata available");
            }
        }).addOnFailureListener(exception -> {
            Log.e(TAG, "Place not found: " + exception.getMessage());
        });
    }





    private void setupMarkerIcon(Marker marker) {
        //마커 아이콘 설정
        OverlayImage icon = OverlayImage.fromResource(R.drawable.baseline_location_on_24); // 새로운 아이콘 리소스 사용
       marker.setIcon(icon);
       // marker.setWidth와 marker.setHeight는 사용하지 않습니다. 아이콘 자체를 작은 크기로 준비합니다.
       // 주황색으로 설정
    }



    // 이거 권한 요청하는 거라는데 뭐 안 뜨는데??
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    //테스트삼아 함 해봄.
    private void setupPOI() {
        // 예제 POI 추가
//        LatLng poiLocation = new LatLng(37.5665, 126.9780); // 예제 좌표 (서울특별시청)

        // 마커 추가
//        Marker marker = new Marker();
//        marker.setPosition(poiLocation);
//        marker.setMap(mMap);

        // 정보 창 추가
//        InfoWindow infoWindow = new InfoWindow();
//        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getContext()) {
//            @NonNull
//            @Override
//            public CharSequence getText(@NonNull InfoWindow infoWindow) {
//                return "서울특별시청"; // 예제 POI 이름
//            }
//        });
//        infoWindow.open(marker);
    }




    private void loadTouristPlaces() {
        // 공공 데이터 API 엔드포인트
        String url = "https://www.data.go.kr/download/15021141/standard.do";
        String apiKey = "M4q3CWc0OP6VctrSKmKMdcNJAY3CWOj5XmhvM7WF2GkyXgdKb2IpCrGO8LRWl9Wl9986gSB%2Bi6t29viXcyV58g%3D%3D"; // 여기에 공공 데이터에서 발급한 API 키를 입력합니다.
        String requestUrl = url + "?dataType=xml&ServiceKey=" + apiKey + "&pageNo=1&numOfRows=100";

        // API 요청
        StringRequest request = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 응답을 로그로 출력하여 확인
                        Log.d(TAG, "API Response: " + response);

                        // 응답의 시작 부분을 확인하여 XML 형식인지 확인
                        if (response.trim().startsWith("<")) {
                            // XML 응답을 처리하는 메서드 호출
                            processXmlResponse(response);
                            showToast("Tourist places loaded successfully");
                        } else {
                            showToast("Received non-XML response");
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError) {
                            Log.e(TAG, "TimeoutError occurred while fetching tourist places.");
                        } else if (error instanceof NoConnectionError) {
                            Log.e(TAG, "NoConnectionError occurred while fetching tourist places.");
                        } else if (error instanceof ParseError) {
                            Log.e(TAG, "ParseError occurred while fetching tourist places.");
                        } else {
                            Log.e(TAG, "Unknown error occurred: " + error.getMessage());
                        }
                        showToast("Error fetching tourist places");
                    }
                });

        // 요청에 타임아웃 설정 추가
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000, // 10초
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        // 요청을 큐에 추가
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        queue.add(request);
    }



    private void searchPlaceIdByName(String placeName, TouristPlace place) {
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(placeName)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            if (!response.getAutocompletePredictions().isEmpty()) {
                AutocompletePrediction prediction = response.getAutocompletePredictions().get(0);
                String placeId = prediction.getPlaceId();
                Log.d(TAG, "Found placeId: " + placeId);
                // 장소 세부 정보 가져오기
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




    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }


    //파이어베이스에.  위도 경도 문자열 추가.
    private void addFavoriteLocation(String place_name, double latitude, double longitude) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            // 해당 유저 계정에 해당하는 데이터베이스 받아옴.

            String userId = user.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(userId).child("favorites");


            // 받아온 데이터 베이스에서 키 받고,  그 키를 통해  favoriteLocation(위도 경도) 등록.
            String key = mDatabase.push().getKey();
//            FavoriteLocation favoriteLocation = new FavoriteLocation(location.place_name, location.latitude, location.longitude);
            FavoriteLocation favoriteLocation = new FavoriteLocation(place_name,latitude, longitude);
            mDatabase.child(key).setValue(favoriteLocation).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "즐겨찾기 추가 실패", Toast.LENGTH_SHORT).show();
                }
            });

            mDatabase = FirebaseDatabase.getInstance().getReference("course").child(userId).child("favorites");

        }
    }
    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.fab_main:
                toggleFab();
                break;
            case R.id.fab_sub1:
                toggleFab();
                Toast.makeText(this, "Camera Open-!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_sub2:
                toggleFab();
                Toast.makeText(this, "Map Open-!", Toast.LENGTH_SHORT).show();
                break;
        }*/
        int id = v.getId();
        if(id == R.id.fab_main)
        {
            toggleFab();
        }
        else if(id == R.id.fab_sub1)
        {
            toggleFab();
            Toast.makeText(getContext(), "Camera Open-!", Toast.LENGTH_SHORT).show();
        }
        else if(id == R.id.fab_sub2)
        {
            toggleFab();
            Toast.makeText(getContext(), "Map Open-!", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleFab() {
        if (isFabOpen) {
            fab_main.setImageResource(R.drawable.ic_add);
            fab_sub1.startAnimation(fab_close);
            fab_sub2.startAnimation(fab_close);
            fab_sub1.setClickable(false);
            fab_sub2.setClickable(false);
            isFabOpen = false;
        } else {
            fab_main.setImageResource(R.drawable.ic_close);
            fab_sub1.startAnimation(fab_open);
            fab_sub2.startAnimation(fab_open);
            fab_sub1.setClickable(true);
            fab_sub2.setClickable(true);
            isFabOpen = true;
        }
    }
}










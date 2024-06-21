package com.example.tourlist.A_Place;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.tourlist.A_Course.InputStreamRequest;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class TouristPlaceRepository_call_parser {

    private static TouristPlaceRepository_call_parser instance;
    private RequestQueue requestQueue;
    private Place_XmlParser place_xmlParser;

    private TouristPlaceRepository_call_parser(Application application) {
        requestQueue = Volley.newRequestQueue(application);
        place_xmlParser = new Place_XmlParser();
    }

    public static synchronized TouristPlaceRepository_call_parser getInstance(Application application) {
        if (instance == null) {
            instance = new TouristPlaceRepository_call_parser(application);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

//    jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D

    public void loadTouristPlaces_first(final MutableLiveData<List<Place>> touristPlaces) {
        try {
        // 코스 키
        //String requestUrl = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?numOfRows=30&pageNo=1&MobileOS=ETC&MobileApp=AppTest&ServiceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&listYN=Y&arrange=O&contentTypeId=25&areaCode=&sigunguCode=&cat1=&cat2=&cat3=";
//        String requestUrl ="https://apis.data.go.kr/B551011/KorService1/areaBasedList1?numOfRows=12&pageNo=1&MobileOS=ETC&MobileApp=AppTest&ServiceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&listYN=Y&arrange=A&contentTypeId=39&areaCode=4&sigunguCode=5&cat1=A05&cat2=A0502&cat3=A05020100";
        String requestUrl ="https://apis.data.go.kr/B551011/KorService1/";
//         홈페이지에서 받은 키
        String serviceKey = "jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D";


        String serviceType = "areaBasedList1";
        String numOfRows = "3";
        String pageNo = "1";
        String mobileOS = "AND";
        String mobileApp = "AppTest";
        String listYN = "Y";
        String arrange = "O";
        String contentTypeId = "12";//
        String areaCode = "6";
        String sigunguCode = "";
        String cat1 = "";
        String cat2 = "";
        String cat3 = "";


            StringBuilder urlBuilder = new StringBuilder(requestUrl);
            urlBuilder.append(URLEncoder.encode(serviceType, "UTF-8"));
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "="+serviceKey);
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

            requestUrl = urlBuilder.toString();

            Log.d("urlf", requestUrl);


        InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                new Response.Listener<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        List<Place> places = place_xmlParser.parseCommon_return_List_Place(response);
                        touristPlaces.setValue(places);

                        Log.d("urlf", places.get(0).getContenttypeid());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        requestQueue.add(request);

        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            // 예외를 처리할 다른 방법을 추가할 수 있습니다.
        }

//        Log.d("tt", requestUrl);
    }

    public void loadCommonInfo(final String contentid, final String contentTypeId,final MutableLiveData<Place> placeDetail) {
        try {

            String requestUrl = "https://apis.data.go.kr/B551011/KorService1/";
//         홈페이지에서 받은 키
            String serviceKey = "RG%2BJOg2NmquDjVNABdeiaNIlMlem1pPi1a4ZWMVVAFuUWsQNCN29nz45kkKunmmqq0IU0aVlpWWHtUsPM6vtkA%3D%3D";


            String serviceType = "detailCommon1";
            String mobileOS = "ETC";
            String mobileApp = "AppTest";
            String defaultYN = "Y";
            String firstImageYN = "Y";
            String areacodeYN = "Y";
            String catcodeYN = "Y";
            String addrinfoYN = "Y";
            String mapinfoYN = "Y";
            String overviewYN = "Y";
            String numOfRows = "3";
            String pageNo = "1";

            /*String serviceType = "detailCommon1";
            String numOfRows = "20";
            String pageNo = "1";
            String mobileOS = "AND";
            String mobileApp = "AppTest";
            String listYN = "Y";
            String arrange = "O";
            String contentTypeId = "12";//
            String areaCode = "6";
            String sigunguCode = "";
            String cat1 = "";
            String cat2 = "";
            String cat3 = "";*/


            StringBuilder urlBuilder = new StringBuilder(requestUrl);
            urlBuilder.append(URLEncoder.encode(serviceType, "UTF-8"));
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode(mobileOS, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode(mobileApp, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("contentId", "UTF-8") + "=" + URLEncoder.encode(contentid, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" + URLEncoder.encode(contentTypeId, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("defaultYN", "UTF-8") + "=" + URLEncoder.encode(defaultYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("firstImageYN", "UTF-8") + "=" + URLEncoder.encode(firstImageYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("areacodeYN", "UTF-8") + "=" + URLEncoder.encode(areacodeYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("catcodeYN", "UTF-8") + "=" + URLEncoder.encode(catcodeYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("addrinfoYN", "UTF-8") + "=" + URLEncoder.encode(addrinfoYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("mapinfoYN", "UTF-8") + "=" + URLEncoder.encode(mapinfoYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("overviewYN", "UTF-8") + "=" + URLEncoder.encode(overviewYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));



            requestUrl = urlBuilder.toString();

            Log.d("url5", requestUrl);

            InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                    new Response.Listener<InputStream>() {
                        @Override
                        public void onResponse(InputStream response) {
//                            place_xmlParser.parseCommonInfo(response, touristPlace);
//                        Log.d("firstimage1", place.getSubname() + place.getFirstimage());
//                            touristPlace.setValue(touristPlace.getValue());
                            Place place = place_xmlParser.parseCommonInfo_return_Place(response);
                            placeDetail.setValue(place);
//Log.d("place", place.getFirstimage());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error
                        }
                    });

            requestQueue.add(request);
        }
        catch (UnsupportedEncodingException e) {}
    }

    //이거 중간에 상세 띄울떄.
    public void loadCommonInfo2(final String contentid, final MutableLiveData<Place> place) {
        try {

            Place tmp=new Place();
            String requestUrl = "https://apis.data.go.kr/B551011/KorService1/";
            String serviceKey = "RG%2BJOg2NmquDjVNABdeiaNIlMlem1pPi1a4ZWMVVAFuUWsQNCN29nz45kkKunmmqq0IU0aVlpWWHtUsPM6vtkA%3D%3D";

            String serviceType = "detailCommon1";
            String mobileOS = "ETC";
            String mobileApp = "AppTest";
            String contentTypeId = "12";
            String defaultYN = "Y";
            String firstImageYN = "Y";
            String areacodeYN = "Y";
            String catcodeYN = "Y";
            String addrinfoYN = "Y";
            String mapinfoYN = "Y";
            String overviewYN = "Y";
            String numOfRows = "3";
            String pageNo = "1";

            StringBuilder urlBuilder = new StringBuilder(requestUrl);
            urlBuilder.append(URLEncoder.encode(serviceType, "UTF-8"));
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode(mobileOS, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode(mobileApp, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("contentId", "UTF-8") + "=" + URLEncoder.encode(contentid, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" + URLEncoder.encode(contentTypeId, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("defaultYN", "UTF-8") + "=" + URLEncoder.encode(defaultYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("firstImageYN", "UTF-8") + "=" + URLEncoder.encode(firstImageYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("areacodeYN", "UTF-8") + "=" + URLEncoder.encode(areacodeYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("catcodeYN", "UTF-8") + "=" + URLEncoder.encode(catcodeYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("addrinfoYN", "UTF-8") + "=" + URLEncoder.encode(addrinfoYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("mapinfoYN", "UTF-8") + "=" + URLEncoder.encode(mapinfoYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("overviewYN", "UTF-8") + "=" + URLEncoder.encode(overviewYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));

            requestUrl = urlBuilder.toString();

            InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                    new Response.Listener<InputStream>() {
                        @Override
                        public void onResponse(InputStream response) {
                            Place tmp=place_xmlParser.parseCommonInfo_return_Place(response);
                            // place 객체가 수정됨
                            place.setValue(tmp);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // 에러 처리 코드 추가 필요
                        }
                    });

            requestQueue.add(request);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            // 예외 처리 코드 추가 필요
        }
    }


    //이건 코스에서 따라온거 쓸일없다.
    /*public void loadTouristCourseDetails(final String contentId, final MutableLiveData<Course> touristCourse) {
        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/detailInfo1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&MobileOS=ETC&MobileApp=AppTest&contentId=" + contentId + "&contentTypeId=25&numOfRows=10&pageNo=1";
//        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/detailInfo1?serviceKey=YOUR_API_KEY&MobileOS=ETC&MobileApp=AppTest&contentId=" + contentId + "&contentTypeId=25&numOfRows=10&pageNo=1";

        InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                new Response.Listener<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        Course course = place_xmlParser.parseDetail(response);
                        touristCourse.setValue(course);

                        for (TouristCoursePlace place : course.getPlaces()) {
                            loadCommonInfo(place.getSubcontentid(), place, touristCourse);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        requestQueue.add(request);
    }*/


    public void loadFilteredPlaces(final String requestUrl, final MutableLiveData<List<Place>> filteredPlaces) {


        InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                new Response.Listener<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        List<Place> places = place_xmlParser.parseCommon_return_List_Place(response);
                        filteredPlaces.setValue(places);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        requestQueue.add(request);
    }

    public void loadFilteredGps(final String latitude, final String longitude, final MutableLiveData<List<Place>> filteredPlaces,final String contenttype) {
        Log.d("latitude", latitude);
//        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=YOUR_API_KEY&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&listYN=Y&arrange=O&contentTypeId=25&areaCode=" + areaCode;
        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/locationBasedList1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&numOfRows=15&pageNo=1&MobileOS=ETC&MobileApp=AppTest&listYN=Y&arrange=O&mapX="+longitude+"&mapY="+latitude+"&radius=20000&contentTypeId="+contenttype;

//        https://apis.data.go.kr/B551011/KorService1/locationBasedList1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&listYN=Y&arrange=O&mapX=128.6102797&mapY=35.8889217&radius=20000&contentTypeId=25

//        https://apis.data.go.kr/B551011/KorService1/locationBasedList1?serviceKey=&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&listYN=Y&arrange=O&mapX=128.6102797&mapY=35.8889217&radius=20000&contentTypeId=25
//        https://apis.data.go.kr/B551011/KorService1/locationBasedList1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&listYN=Y&arrange=O&mapX=128.6102797&mapY=35.8889217&radius=20000&contentTypeId=25





        InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                new Response.Listener<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        List<Place> places = place_xmlParser.parseCommon_return_List_Place(response);
                        filteredPlaces.setValue(places);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        requestQueue.add(request);
    }


}

package com.example.tourlist.Course;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.InputStream;
import java.util.List;

public class TouristCourseRepository {

    private static TouristCourseRepository instance;
    private RequestQueue requestQueue;
    private Course_XmlParser courseXmlParser;

    private TouristCourseRepository(Application application) {
        requestQueue = Volley.newRequestQueue(application);
        courseXmlParser = new Course_XmlParser();
    }

    public static synchronized TouristCourseRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TouristCourseRepository(application);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void loadTouristCourses(final MutableLiveData<List<TouristCourse>> touristCourses) {
        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?numOfRows=30&pageNo=1&MobileOS=ETC&MobileApp=AppTest&ServiceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&listYN=Y&arrange=A&contentTypeId=25&areaCode=&sigunguCode=&cat1=&cat2=&cat3=";

        InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                new Response.Listener<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        List<TouristCourse> courses = courseXmlParser.parse(response);
                        touristCourses.setValue(courses);
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

    public void loadTouristCourseDetails(final String contentId, final MutableLiveData<TouristCourse> touristCourse) {
        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/detailInfo1?serviceKey=YOUR_API_KEY&MobileOS=ETC&MobileApp=AppTest&contentId=" + contentId + "&contentTypeId=25&numOfRows=10&pageNo=1";

        InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                new Response.Listener<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        TouristCourse course = courseXmlParser.parseDetail(response);
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
    }

    private void loadCommonInfo(final String subcontentid, final TouristCoursePlace place, final MutableLiveData<TouristCourse> touristCourse) {
//        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=YOUR_API_KEY&MobileOS=ETC&MobileApp=AppTest&contentId=" + subcontentid + "&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&addrinfoYN=Y&mapinfoYN=Y";
        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/detailImage1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&MobileOS=ETC&MobileApp=AppTest&contentId=" + subcontentid + "&imageYN=Y&subImageYN=Y&numOfRows=10&pageNo=1";
        //                   https://apis.data.go.kr/B551011/KorService1/detailImage1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&MobileOS=ETC&MobileApp=AppTest&contentId=250121&imageYN=Y&subImageYN=Y&numOfRows=10&pageNo=1

        InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                new Response.Listener<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        courseXmlParser.parseCommonInfo(response, place);
                        Log.d("firstimage1", place.getSubname() + place.getFirstimage());
                        touristCourse.setValue(touristCourse.getValue());
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

    public void loadFilteredCourses(final String areaCode, final MutableLiveData<List<TouristCourse>> filteredCourses) {
        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=YOUR_API_KEY&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&listYN=Y&arrange=O&contentTypeId=25&areaCode=" + areaCode;

        InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                new Response.Listener<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        List<TouristCourse> courses = courseXmlParser.parse(response);
                        filteredCourses.setValue(courses);
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

    public void loadFilteredGps(final String latitude, final String longitude, final MutableLiveData<List<TouristCourse>> filteredCourses) {
        Log.d("latitude", latitude);
//        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=YOUR_API_KEY&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&listYN=Y&arrange=O&contentTypeId=25&areaCode=" + areaCode;
        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/locationBasedList1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&numOfRows=15&pageNo=1&MobileOS=ETC&MobileApp=AppTest&listYN=Y&arrange=O&mapX="+longitude+"&mapY="+latitude+"&radius=20000&contentTypeId=25";

//        https://apis.data.go.kr/B551011/KorService1/locationBasedList1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&listYN=Y&arrange=O&mapX=128.6102797&mapY=35.8889217&radius=20000&contentTypeId=25

//        https://apis.data.go.kr/B551011/KorService1/locationBasedList1?serviceKey=&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&listYN=Y&arrange=O&mapX=128.6102797&mapY=35.8889217&radius=20000&contentTypeId=25
//        https://apis.data.go.kr/B551011/KorService1/locationBasedList1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&listYN=Y&arrange=O&mapX=128.6102797&mapY=35.8889217&radius=20000&contentTypeId=25





        InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                new Response.Listener<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        List<TouristCourse> courses = courseXmlParser.parse(response);
                        filteredCourses.setValue(courses);
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

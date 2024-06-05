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

    public void loadTouristCourses(final MutableLiveData<List<TouristCourse>> touristCourses) {
//        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/detailInfo1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&MobileOS=ETC&MobileApp=AppTest&contentId=1885246&contentTypeId=25&numOfRows=6&pageNo=1";

        //코스 정보 출력(지역기반) //~행복 코스//김해 가야~코스 ...
        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&listYN=Y&arrange=O&contentTypeId=25";

        InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                new Response.Listener<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        //코스들 저장. 코스 하나당 contentid
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

        //xmL: 하나 코스에 서브 장소들

        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/detailInfo1?serviceKey=YOUR_API_KEY&MobileOS=ETC&MobileApp=AppTest&contentId=" + contentId + "&contentTypeId=25&numOfRows=10&pageNo=1";

        InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                new Response.Listener<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        TouristCourse course = courseXmlParser.parseDetail(response);
                        touristCourse.setValue(course);

                        // 각 subcontentid에 대해 공통정보조회 API 요청
                        for (TouristCoursePlace place : course.getPlaces()) {
                            Log.d("subcontentid", place.getSubcontentid());
                            Log.d("", place.getSubcontentid());
//                            loadCommonInfo(place.getSubcontentid(), place, touristCourse);
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
//    https://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&MobileOS=ETC&MobileApp=AppTest&contentId=128803&contentTypeId=12&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&numOfRows=10&pageNo=1

//    https://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=&MobileOS=ETC&MobileApp=AppTest&contentId=1260275&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&numOfRows=10&pageNo=1
private void loadCommonInfo(final String subcontentid, final TouristCoursePlace place, final MutableLiveData<TouristCourse> touristCourse) {
    String requestUrl = "https://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=YOUR_API_KEY&MobileOS=ETC&MobileApp=AppTest&contentId=" + subcontentid + "&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&addrinfoYN=Y&mapinfoYN=Y";

    InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
            new Response.Listener<InputStream>() {
                @Override
                public void onResponse(InputStream response) {
                    courseXmlParser.parseCommonInfo(response, place);
                    Log.d("firstimage1", place.getSubname() + place.getFirstimage());
                    // 업데이트된 정보를 반영하여 LiveData를 갱신
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


}

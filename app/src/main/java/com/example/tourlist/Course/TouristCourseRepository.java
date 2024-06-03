package com.example.tourlist.Course;

import android.app.Application;
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
    private XmlParser2 xmlParser2;

    private TouristCourseRepository(Application application) {
        requestQueue = Volley.newRequestQueue(application);
        xmlParser2 = new XmlParser2();
    }

    public static synchronized TouristCourseRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TouristCourseRepository(application);
        }
        return instance;
    }

    public void loadTouristCourses(final MutableLiveData<List<TouristCourse>> touristCourses) {
//        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/detailInfo1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&MobileOS=ETC&MobileApp=AppTest&contentId=1885246&contentTypeId=25&numOfRows=6&pageNo=1";

        //코스 정보 출력(지역기반)
        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&listYN=Y&arrange=O&contentTypeId=25";

        InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                new Response.Listener<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        List<TouristCourse> courses = xmlParser2.parse(response);
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
}

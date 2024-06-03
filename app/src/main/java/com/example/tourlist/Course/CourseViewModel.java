package com.example.tourlist.Course;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.InputStream;
import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private MutableLiveData<List<TouristCourse>> touristCourses;
    private RequestQueue requestQueue;
    private XmlParser2 xmlParser2;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        requestQueue = Volley.newRequestQueue(application);
        xmlParser2 = new XmlParser2(); // XML 파서를 초기화
    }

    public LiveData<List<TouristCourse>> getTouristCourses() {
        if (touristCourses == null) {
            touristCourses = new MutableLiveData<>();
            loadTouristPlaces();
        }
        return touristCourses;
    }

    private void loadTouristPlaces() {
        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/detailInfo1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&MobileOS=ETC&MobileApp=AppTest&contentId=1885246&contentTypeId=25&numOfRows=6&pageNo=1";

        InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                new Response.Listener<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        List<TouristCourse> courses = xmlParser2.parse(response); // XML 파서를 사용하여 파싱
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

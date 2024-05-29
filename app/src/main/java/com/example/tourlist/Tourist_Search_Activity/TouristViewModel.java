package com.example.tourlist.Tourist_Search_Activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TouristViewModel extends AndroidViewModel {


    private MutableLiveData<List<TouristAttraction>> touristAttractions;
    private RequestQueue requestQueue;

    public TouristViewModel(@NonNull Application application) {
        super(application);
        requestQueue = Volley.newRequestQueue(application);
    }

    public LiveData<List<TouristAttraction>> getTouristAttractions() {
        if (touristAttractions == null) {
            touristAttractions = new MutableLiveData<>();
            loadTouristPlaces();
        }
        return touristAttractions;
    }

    private void loadTouristPlaces() {
        String url = "https://www.data.go.kr/download/15021141/standard.do";
        String apiKey = "M4q3CWc0OP6VctrSKmKMdcNJAY3CWOj5XmhvM7WF2GkyXgdKb2IpCrGO8LRWl9Wl9986gSB%2Bi6t29viXcyV58g%3D%3D";
        String requestUrl = url + "?dataType=xml&ServiceKey=" + apiKey + "&pageNo=1&numOfRows=100";

        StringRequest request = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<TouristAttraction> attractions = processXmlResponse(response);
                        touristAttractions.setValue(attractions);
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

    private List<TouristAttraction> processXmlResponse(String response) {
        List<TouristAttraction> attractions = new ArrayList<>();

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
                                default:

                            }
                        }
                        eventType = parser.next();
                    }
//                    attractions.add(new TouristAttraction(placeName[0], address[0]));
                    attractions.add(new TouristAttraction(placeName[0], latitude[0], longitude[0], address[0], description[0], phone[0]));
//                    new TouristPlace(placeName[0], latitude[0], longitude[0], address[0], description[0], phone[0])
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return attractions;
    }
}



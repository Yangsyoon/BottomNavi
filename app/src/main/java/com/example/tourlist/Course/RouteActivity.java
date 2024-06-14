package com.example.tourlist.Course;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class RouteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<TouristCoursePlace> placesList = new ArrayList<>();
        placesList.add(new TouristCoursePlace("동대구역", 35.87703, 128.62834));
        placesList.add(new TouristCoursePlace("경북대학교", 35.89028, 128.61023));
        placesList.add(new TouristCoursePlace("꽃보라동산", 35.86948, 128.59106));

        if (placesList == null || placesList.size() < 2) {
            Toast.makeText(RouteActivity.this, "경로에 포함할 장소가 충분하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String url = generateMultiStopRouteURL(placesList);
            openNaverMapWithTransitRoute(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(RouteActivity.this, "URL 생성 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateMultiStopRouteURL(List<TouristCoursePlace> placesList) throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder("nmap://route/public?");
        TouristCoursePlace startPlace = placesList.get(0);
        TouristCoursePlace endPlace = placesList.get(placesList.size() - 1);

        urlBuilder.append("slat=").append(startPlace.getLatitude())
                .append("&slng=").append(startPlace.getLongitude())
                .append("&sname=").append(URLEncoder.encode(startPlace.getSubname(), "UTF-8"))
                .append("&secoords=").append(startPlace.getLatitude()).append(",").append(startPlace.getLongitude());

        urlBuilder.append("&dlat=").append(endPlace.getLatitude())
                .append("&dlng=").append(endPlace.getLongitude())
                .append("&dname=").append(URLEncoder.encode(endPlace.getSubname(), "UTF-8"))
                .append("&decoords=").append(endPlace.getLatitude()).append(",").append(endPlace.getLongitude());

        // 경유지 추가
        for (int i = 1; i < placesList.size() - 1; i++) {
            TouristCoursePlace waypoint = placesList.get(i);
            urlBuilder.append("&v").append(i).append("lat=").append(waypoint.getLatitude())
                    .append("&v").append(i).append("lng=").append(waypoint.getLongitude())
                    .append("&v").append(i).append("name=").append(URLEncoder.encode(waypoint.getSubname(), "UTF-8"))
                    .append("&v").append(i).append("ecoords=").append(waypoint.getLatitude()).append(",").append(waypoint.getLongitude());
        }

        urlBuilder.append("&appname=com.example.tourlist");
        return urlBuilder.toString();
    }

    private void openNaverMapWithTransitRoute(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if (list == null || list.isEmpty()) {
            Toast.makeText(this, "네이버 지도 앱이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nhn.android.nmap")));
        } else {
            startActivity(intent);
        }
    }
}
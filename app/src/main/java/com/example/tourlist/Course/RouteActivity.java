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


//        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("nmap://route/car?slat=37.3595953&slng=127.1053971&sname=%EA%B7%B8%EB%A6%B0%ED%8C%A9%ED%86%A0%EB%A6%AC&secoords=37.359761,127.10527&dlng=127.1267772&dlat=37.4200267&dname=%EC%84%B1%EB%82%A8%EC%8B%9C%EC%B2%AD&decoords=37.4189564,127.1256827&v1lng=126.9522394&v1lat=37.464007&v1name=%20%EC%84%9C%EC%9A%B8%EB%8C%80%ED%95%99%EA%B5%90&v1ecoords=37.466358,126.948357&appname=com.example.myapp")));
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

//        if (list == null || list.isEmpty()) {
//            Toast.makeText(this, "네이버 지도 앱이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nhn.android.nmap")));
//        } else {
//            startActivity(intent);
//
//        }
    }
}
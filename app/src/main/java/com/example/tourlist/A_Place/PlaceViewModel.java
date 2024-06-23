package com.example.tourlist.A_Place;

import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class PlaceViewModel extends ViewModel {
    private MutableLiveData<String> blogSearchResult = new MutableLiveData<>();

    public LiveData<String> getBlogSearchResult() {
        return blogSearchResult;
    }

    public void searchBlog(String query) {
        new BlogSearchTask().execute(query);
    }

    private class BlogSearchTask extends AsyncTask<String, Void, String> {

        private static final String CLIENT_ID = "50ZEXUvqEHSEMmaLJkij"; // 애플리케이션 클라이언트 아이디
        private static final String CLIENT_SECRET = "3GD0ZPu680"; // 애플리케이션 클라이언트 시크릿


        private static final String BASE_API_URL = "https://openapi.naver.com/v1/search/blog?query=";

        @Override
        protected String doInBackground(String... strings) {
            String query = strings[0];
            String encodedQuery = encodeQuery(query);
            if (encodedQuery == null) {
                return null;
            }

            String apiUrl = BASE_API_URL + encodedQuery + "&display=10&start=1&sort=sim";
            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("X-Naver-Client-Id", CLIENT_ID);
            requestHeaders.put("X-Naver-Client-Secret", CLIENT_SECRET);

            return get(apiUrl, requestHeaders);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                blogSearchResult.setValue(result);
            } else {
                blogSearchResult.setValue("검색 실패");
            }
        }

        private String encodeQuery(String query) {
            try {
                return URLEncoder.encode(query, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }

        private String get(String apiUrl, Map<String, String> requestHeaders) {
            HttpURLConnection con = connect(apiUrl);
            try {
                con.setRequestMethod("GET");
                for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                    con.setRequestProperty(header.getKey(), header.getValue());
                }

                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                    return readBody(con.getInputStream());
                } else { // 오류 발생
                    return readBody(con.getErrorStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                con.disconnect();
            }
        }

        private HttpURLConnection connect(String apiUrl) {
            try {
                URL url = new URL(apiUrl);
                return (HttpURLConnection) url.openConnection();
            } catch (MalformedURLException e) {
                throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
            } catch (IOException e) {
                throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
            }
        }

        private String readBody(InputStream body) {
            InputStreamReader streamReader = new InputStreamReader(body);
            try (BufferedReader lineReader = new BufferedReader(streamReader)) {
                StringBuilder responseBody = new StringBuilder();
                String line;
                while ((line = lineReader.readLine()) != null) {
                    responseBody.append(line);
                }
                return responseBody.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}

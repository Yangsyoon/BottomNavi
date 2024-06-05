package com.example.tourlist.Course;

import java.util.List;

public class TouristCourse {

    private String content_id;
    private String course_title;
    private List<TouristCoursePlace> places;

    // 기본 생성자
    public TouristCourse() {
    }

    // 생성자 추가
    public TouristCourse(String content_id, String course_title) {
        this.content_id = content_id;
        this.course_title = course_title;
        this.places = places;
    }
    public TouristCourse(String content_id, String course_title, List<TouristCoursePlace> places) {
        this.content_id = content_id;
        this.course_title = course_title;
        this.places = places;
    }

    // Getter 메소드
    public String getContent_id() {
        return content_id;
    }

    public String getCourse_title() {
        return course_title;
    }

    public List<TouristCoursePlace> getPlaces() {
        return places;
    }

    // Setter 메소드
    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public void setPlaces(List<TouristCoursePlace> places) {
        this.places = places;
    }
}

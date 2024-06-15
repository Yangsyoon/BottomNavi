package com.example.tourlist.A_Course;

import java.util.List;

public class Course {

    private String content_id;
    private String course_title;
    private String areacode;
    private List<TouristCoursePlace> places;

    // 기본 생성자
    public Course() {
    }

    // 생성자 추가
    public Course(String content_id, String course_title) {
        this.content_id = content_id;
        this.course_title = course_title;
    }

    public Course(String content_id, String course_title, String areacode, List<TouristCoursePlace> places) {
        this.content_id = content_id;
        this.course_title = course_title;
        this.areacode = areacode;
        this.places = places;
    }

    public Course(String content_id, String course_title, String areacode) {
        this.content_id = content_id;
        this.course_title = course_title;
        this.areacode = areacode;
    }

    // Getter 메소드
    public String getContent_id() {
        return content_id;
    }

    public String getCourse_title() {
        return course_title;
    }

    public String getAreacode() {
        return areacode;
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

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public void setPlaces(List<TouristCoursePlace> places) {
        this.places = places;
    }
}

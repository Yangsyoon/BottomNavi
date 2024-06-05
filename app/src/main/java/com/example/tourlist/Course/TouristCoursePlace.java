package com.example.tourlist.Course;

public class TouristCoursePlace {

    private String subcontentid;
    private String subname;
    private String subdetailoverview;
    private String subdetailimg;
    private String subdetailalt;
    private String firstimage;
    private String areacode;
    private String sigungucode;
    private String addr1;
    private double mapx;
    private double mapy;

    // 기본 생성자
    public TouristCoursePlace() {
    }

    // 필요한 필드들을 받는 생성자 추가
    public TouristCoursePlace(String subcontentid, String subname, String subdetailoverview, String subdetailimg, String subdetailalt, String firstimage, String areacode, String sigungucode, String addr1, double mapx, double mapy) {
        this.subcontentid = subcontentid;
        this.subname = subname;
        this.subdetailoverview = subdetailoverview;
        this.subdetailimg = subdetailimg;
        this.subdetailalt = subdetailalt;
        this.firstimage = firstimage;
        this.areacode = areacode;
        this.sigungucode = sigungucode;
        this.addr1 = addr1;
        this.mapx = mapx;
        this.mapy = mapy;
    }

    // Getter 및 Setter 메소드
    public String getSubcontentid() {
        return subcontentid;
    }

    public void setSubcontentid(String subcontentid) {
        this.subcontentid = subcontentid;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getSubdetailoverview() {
        return subdetailoverview;
    }

    public void setSubdetailoverview(String subdetailoverview) {
        this.subdetailoverview = subdetailoverview;
    }

    public String getSubdetailimg() {
        return subdetailimg;
    }

    public void setSubdetailimg(String subdetailimg) {
        this.subdetailimg = subdetailimg;
    }

    public String getSubdetailalt() {
        return subdetailalt;
    }

    public void setSubdetailalt(String subdetailalt) {
        this.subdetailalt = subdetailalt;
    }

    public String getFirstimage() {
        return firstimage;
    }

    public void setFirstimage(String firstimage) {
        this.firstimage = firstimage;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getSigungucode() {
        return sigungucode;
    }

    public void setSigungucode(String sigungucode) {
        this.sigungucode = sigungucode;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public double getMapx() {
        return mapx;
    }

    public void setMapx(double mapx) {
        this.mapx = mapx;
    }

    public double getMapy() {
        return mapy;
    }

    public void setMapy(double mapy) {
        this.mapy = mapy;
    }
}

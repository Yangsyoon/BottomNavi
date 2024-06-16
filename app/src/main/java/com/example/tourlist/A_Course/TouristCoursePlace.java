package com.example.tourlist.A_Course;

public class TouristCoursePlace {

    private String subcontentid;
    private String contenttypeid;
    private String subname;
    private String subdetailoverview;
    private String subdetailimg;
    private String subdetailalt;
    private String firstimage;
    private String firstimage2;
    private String areacode;
    private String sigungucode;
    private String cat1;
    private String cat2;
    private String cat3;
    private String addr1;
    private String addr2;
    private String zipcode;
    private String tel;
    private String telname;
    private String homepage;
    private String booktour;
    private String cpyrhtDivCd;
    private String createdtime;
    private String modifiedtime;
    private double mapx;
    private double mapy;
    private String mlevel;

    // 기본 생성자
    public TouristCoursePlace() {
    }

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

    // 필요한 필드들을 받는 생성자 추가
    public TouristCoursePlace(String subcontentid, String contenttypeid, String subname, String subdetailoverview, String subdetailimg, String subdetailalt, String firstimage, String firstimage2, String areacode, String sigungucode, String cat1, String cat2, String cat3, String addr1, String addr2, String zipcode, String tel, String telname, String homepage, String booktour, String cpyrhtDivCd, String createdtime, String modifiedtime, double mapx, double mapy, String mlevel) {
        this.subcontentid = subcontentid;
        this.contenttypeid = contenttypeid;
        this.subname = subname;
        this.subdetailoverview = subdetailoverview;
        this.subdetailimg = subdetailimg;
        this.subdetailalt = subdetailalt;
        this.firstimage = firstimage;
        this.firstimage2 = firstimage2;
        this.areacode = areacode;
        this.sigungucode = sigungucode;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.zipcode = zipcode;
        this.tel = tel;
        this.telname = telname;
        this.homepage = homepage;
        this.booktour = booktour;
        this.cpyrhtDivCd = cpyrhtDivCd;
        this.createdtime = createdtime;
        this.modifiedtime = modifiedtime;
        this.mapx = mapx;
        this.mapy = mapy;
        this.mlevel = mlevel;
    }

    // Getter 및 Setter 메소드
    public String getSubcontentid() {
        return subcontentid;
    }

    public void setSubcontentid(String subcontentid) {
        this.subcontentid = subcontentid;
    }

    public String getContenttypeid() {
        return contenttypeid;
    }

    public void setContenttypeid(String contenttypeid) {
        this.contenttypeid = contenttypeid;
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

    public String getFirstimage2() {
        return firstimage2;
    }

    public void setFirstimage2(String firstimage2) {
        this.firstimage2 = firstimage2;
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

    public String getCat1() {
        return cat1;
    }

    public void setCat1(String cat1) {
        this.cat1 = cat1;
    }

    public String getCat2() {
        return cat2;
    }

    public void setCat2(String cat2) {
        this.cat2 = cat2;
    }

    public String getCat3() {
        return cat3;
    }

    public void setCat3(String cat3) {
        this.cat3 = cat3;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTelname() {
        return telname;
    }

    public void setTelname(String telname) {
        this.telname = telname;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getBooktour() {
        return booktour;
    }

    public void setBooktour(String booktour) {
        this.booktour = booktour;
    }

    public String getCpyrhtDivCd() {
        return cpyrhtDivCd;
    }

    public void setCpyrhtDivCd(String cpyrhtDivCd) {
        this.cpyrhtDivCd = cpyrhtDivCd;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    public String getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(String modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public String getMlevel() {
        return mlevel;
    }

    public void setMlevel(String mlevel) {
        this.mlevel = mlevel;
    }
}

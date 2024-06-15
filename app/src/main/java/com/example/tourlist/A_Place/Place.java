package com.example.tourlist.A_Place;

public class Place {

    private String contentid;
    private String contenttypeid;
    private String title;
    private String createdtime;
    private String modifiedtime;
    private String tel;
    private String homepage;
    private String booktour;
    private String firstimage;
    private String firstimage2;
    private String cpyrhtDivCd;
    private String areacode;
    private String sigungucode;
    private String cat1;
    private String cat2;
    private String cat3;
    private String addr1;
    private String zipcode;
    private double mapx;
    private double mapy;
    private String mlevel;
    private String overview;

    // 기본 생성자
    public Place() {
    }

    // 필요한 필드들을 받는 생성자 추가
    public Place(String contentid, String contenttypeid, String title, String createdtime, String modifiedtime, String tel, String homepage, String booktour, String firstimage, String firstimage2, String cpyrhtDivCd, String areacode, String sigungucode, String cat1, String cat2, String cat3, String addr1, String zipcode, double mapx, double mapy, String mlevel, String overview) {
        this.contentid = contentid;
        this.contenttypeid = contenttypeid;
        this.title = title;
        this.createdtime = createdtime;
        this.modifiedtime = modifiedtime;
        this.tel = tel;
        this.homepage = homepage;
        this.booktour = booktour;
        this.firstimage = firstimage;
        this.firstimage2 = firstimage2;
        this.cpyrhtDivCd = cpyrhtDivCd;
        this.areacode = areacode;
        this.sigungucode = sigungucode;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.addr1 = addr1;
        this.zipcode = zipcode;
        this.mapx = mapx;
        this.mapy = mapy;
        this.mlevel = mlevel;
        this.overview = overview;
    }

    // Getter 메소드
    public String getContentid() {
        return contentid;
    }

    public String getContenttypeid() {
        return contenttypeid;
    }

    public String getTitle() {
        return title;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public String getModifiedtime() {
        return modifiedtime;
    }

    public String getTel() {
        return tel;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getBooktour() {
        return booktour;
    }

    public String getFirstimage() {
        return firstimage;
    }

    public String getFirstimage2() {
        return firstimage2;
    }

    public String getCpyrhtDivCd() {
        return cpyrhtDivCd;
    }

    public String getAreacode() {
        return areacode;
    }

    public String getSigungucode() {
        return sigungucode;
    }

    public String getCat1() {
        return cat1;
    }

    public String getCat2() {
        return cat2;
    }

    public String getCat3() {
        return cat3;
    }

    public String getAddr1() {
        return addr1;
    }

    public String getZipcode() {
        return zipcode;
    }

    public double getMapx() {
        return mapx;
    }

    public double getMapy() {
        return mapy;
    }

    public String getMlevel() {
        return mlevel;
    }

    public String getOverview() {
        return overview;
    }

    // Setter 메소드
    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

    public void setContenttypeid(String contenttypeid) {
        this.contenttypeid = contenttypeid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    public void setModifiedtime(String modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public void setBooktour(String booktour) {
        this.booktour = booktour;
    }

    public void setFirstimage(String firstimage) {
        this.firstimage = firstimage;
    }

    public void setFirstimage2(String firstimage2) {
        this.firstimage2 = firstimage2;
    }

    public void setCpyrhtDivCd(String cpyrhtDivCd) {
        this.cpyrhtDivCd = cpyrhtDivCd;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public void setSigungucode(String sigungucode) {
        this.sigungucode = sigungucode;
    }

    public void setCat1(String cat1) {
        this.cat1 = cat1;
    }

    public void setCat2(String cat2) {
        this.cat2 = cat2;
    }

    public void setCat3(String cat3) {
        this.cat3 = cat3;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public void setMapx(double mapx) {
        this.mapx = mapx;
    }

    public void setMapy(double mapy) {
        this.mapy = mapy;
    }

    public void setMlevel(String mlevel) {
        this.mlevel = mlevel;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}

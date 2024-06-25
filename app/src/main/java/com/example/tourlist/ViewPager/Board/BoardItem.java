package com.example.tourlist.ViewPager.Board;
public class BoardItem {

    private String title;
    private String content;
    private String userId;
    private long timestamp;
    private String imageUrl;

    public BoardItem() {
        // Default constructor required for calls to DataSnapshot.getValue(BoardItem.class)
    }

    // Getter and setter methods

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

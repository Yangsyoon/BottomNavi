package com.example.tourlist.ViewPager;

public class Post {
    private String title;
    private String content;
    private String imageUrl;
    private String userId;
    private long timestamp;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String title, String content, String imageUrl, String userId, long timestamp) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    // Getters and setters
}

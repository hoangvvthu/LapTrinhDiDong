package com.example.baitap08;

import com.google.firebase.firestore.DocumentId;
import java.io.Serializable;

// 1. Implement Serializable
public class VideoShort implements Serializable {
    @DocumentId
    private String documentId;
    private String videoUrl;
    private String userEmail;
    private String userAvatarUrl;
    private int likes;

    public VideoShort() {
        // Default constructor required
    }

    public VideoShort(String videoUrl, String userEmail, String userAvatarUrl, int likes) {
        this.videoUrl = videoUrl;
        this.userEmail = userEmail;
        this.userAvatarUrl = userAvatarUrl;
        this.likes = likes;
    }

    // Getters and Setters

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}

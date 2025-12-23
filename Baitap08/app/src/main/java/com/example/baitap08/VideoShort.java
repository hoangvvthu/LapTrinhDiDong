package com.example.baitap08;

import com.google.firebase.firestore.DocumentId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VideoShort implements Serializable {
    @DocumentId
    private String documentId;
    private String videoUrl;
    private String userEmail;
    private String userAvatarUrl;
    private int likes;
    private int dislikes;
    private List<String> likedBy = new ArrayList<>();    // Danh sách UID đã like
    private List<String> dislikedBy = new ArrayList<>(); // Danh sách UID đã dislike

    public VideoShort() {}

    public VideoShort(String videoUrl, String userEmail, String userAvatarUrl, int likes, int dislikes) {
        this.videoUrl = videoUrl;
        this.userEmail = userEmail;
        this.userAvatarUrl = userAvatarUrl;
        this.likes = likes;
        this.dislikes = dislikes;
        this.likedBy = new ArrayList<>();
        this.dislikedBy = new ArrayList<>();
    }

    // Getters and Setters
    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getUserAvatarUrl() { return userAvatarUrl; }
    public void setUserAvatarUrl(String userAvatarUrl) { this.userAvatarUrl = userAvatarUrl; }
    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }
    public int getDislikes() { return dislikes; }
    public void setDislikes(int dislikes) { this.dislikes = dislikes; }
    public List<String> getLikedBy() { return likedBy; }
    public void setLikedBy(List<String> likedBy) { this.likedBy = likedBy; }
    public List<String> getDislikedBy() { return dislikedBy; }
    public void setDislikedBy(List<String> dislikedBy) { this.dislikedBy = dislikedBy; }
}

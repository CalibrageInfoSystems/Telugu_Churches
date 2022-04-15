package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created model class for API to request like/dislike
 */

public class LikeDisLikeRequestModel {
    @SerializedName("eventId")
    @Expose
    private Integer eventId;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("like")
    @Expose
    private Boolean like;
    @SerializedName("disLike")
    @Expose
    private Boolean disLike;

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

    public Boolean getDisLike() {
        return disLike;
    }

    public void setDisLike(Boolean disLike) {
        this.disLike = disLike;
    }

}
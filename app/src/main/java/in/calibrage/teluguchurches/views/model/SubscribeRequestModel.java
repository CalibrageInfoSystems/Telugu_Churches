package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created model class for API for subscribe request
 */
public class SubscribeRequestModel {

    @SerializedName("isSubscribed")
    @Expose
    private Integer isSubscribed;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("churchId")
    @Expose
    private Integer churchId;
    @SerializedName("authorId")
    @Expose
    private Integer authorId;

    public Integer getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(Integer isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getChurchId() {
        return churchId;
    }

    public void setChurchId(Integer churchId) {
        this.churchId = churchId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }
}

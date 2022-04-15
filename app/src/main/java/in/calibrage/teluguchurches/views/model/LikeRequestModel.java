package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created model class for API to request like/dislike
 */

public class LikeRequestModel {
    @SerializedName("postId")
    @Expose
    private Integer postId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("like1")
    @Expose
    private Boolean like1;
    @SerializedName("disLike")
    @Expose
    private Boolean disLike;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("parentCommentId")
    @Expose
    private Integer parentCommentId;
    @SerializedName("churchId")
    @Expose
    private Integer churchId;

    public Integer getChurchId() {
        return churchId;
    }

    public void setChurchId(Integer churchId) {
        this.churchId = churchId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Integer parentCommentId) {
        this.parentCommentId = parentCommentId;
    }


    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getLike1() {
        return like1;
    }

    public void setLike1(Boolean like1) {
        this.like1 = like1;
    }

    public Boolean getDisLike() {
        return disLike;
    }

    public void setDisLike(Boolean disLike) {
        this.disLike = disLike;
    }

}

package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created model class for API like/dislike response
 */

public class LikeResponseModel {
    @SerializedName("listResult")
    @Expose
    private Object listResult;
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("affectedRecords")
    @Expose
    private Integer affectedRecords;
    @SerializedName("totalRecords")
    @Expose
    private Integer totalRecords;
    @SerializedName("endUserMessage")
    @Expose
    private String endUserMessage;
    @SerializedName("validationErrors")
    @Expose
    private List<Object> validationErrors = null;
    @SerializedName("exception")
    @Expose
    private Object exception;

    public Object getListResult() {
        return listResult;
    }

    public void setListResult(Object listResult) {
        this.listResult = listResult;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Integer getAffectedRecords() {
        return affectedRecords;
    }

    public void setAffectedRecords(Integer affectedRecords) {
        this.affectedRecords = affectedRecords;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public String getEndUserMessage() {
        return endUserMessage;
    }

    public void setEndUserMessage(String endUserMessage) {
        this.endUserMessage = endUserMessage;
    }

    public List<Object> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<Object> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public Object getException() {
        return exception;
    }

    public void setException(Object exception) {
        this.exception = exception;
    }

    public class LikeResult {

        @SerializedName("postId")
        @Expose
        private Integer postId;
        @SerializedName("userId")
        @Expose
        private Integer userId;
        @SerializedName("like1")
        @Expose
        private Boolean like1;
        @SerializedName("disLike")
        @Expose
        private Boolean disLike;

        public Integer getPostId() {
            return postId;
        }

        public void setPostId(Integer postId) {
            this.postId = postId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
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

    public class Result {

        @SerializedName("likeResult")
        @Expose
        private List<LikeResult> likeResult = null;
        @SerializedName("likeCount")
        @Expose
        private Integer likeCount;
        @SerializedName("dislikeCount")
        @Expose
        private Integer dislikeCount;

        public List<LikeResult> getLikeResult() {
            return likeResult;
        }

        public void setLikeResult(List<LikeResult> likeResult) {
            this.likeResult = likeResult;
        }

        public Integer getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(Integer likeCount) {
            this.likeCount = likeCount;
        }

        public Integer getDislikeCount() {
            return dislikeCount;
        }

        public void setDislikeCount(Integer dislikeCount) {
            this.dislikeCount = dislikeCount;
        }

    }
}

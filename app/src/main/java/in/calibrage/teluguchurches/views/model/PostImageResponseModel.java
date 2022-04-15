package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created model class for API images post response
 */

public class PostImageResponseModel {
    @SerializedName("listResult")
    @Expose
    private List<ListResult> listResult = null;
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

    public List<ListResult> getListResult() {
        return listResult;
    }

    public void setListResult(List<ListResult> listResult) {
        this.listResult = listResult;
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
    public class ListResult {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("desc")
        @Expose
        private String desc;
        @SerializedName("categoryId")
        @Expose
        private Integer categoryId;
        @SerializedName("embededUrl")
        @Expose
        private String embededUrl;
        @SerializedName("mediaTypeId")
        @Expose
        private Integer mediaTypeId;
        @SerializedName("postTypeId")
        @Expose
        private Integer postTypeId;
        @SerializedName("userId")
        @Expose
        private Integer userId;
        @SerializedName("postImage")
        @Expose
        private String postImage;
        @SerializedName("embedId")
        @Expose
        private String embedId;
        @SerializedName("mediaType")
        @Expose
        private String mediaType;
        @SerializedName("postType")
        @Expose
        private String postType;
        @SerializedName("categoryName")
        @Expose
        private String categoryName;
        @SerializedName("likeCount")
        @Expose
        private Integer likeCount;
        @SerializedName("disLikeCount")
        @Expose
        private Integer disLikeCount;
        @SerializedName("commentCount")
        @Expose
        private Integer commentCount;
        @SerializedName("viewCount")
        @Expose
        private Object viewCount;
        @SerializedName("createdByUser")
        @Expose
        private String createdByUser;
        @SerializedName("updatedByUser")
        @Expose
        private String updatedByUser;
        @SerializedName("postUpdatedDate")
        @Expose
        private String postUpdatedDate;

        @SerializedName("churchId")
        @Expose
        private Integer churchId;
        @SerializedName("churchName")
        @Expose
        private String churchName;

        public Integer getChurchId() {
            return churchId;
        }

        public void setChurchId(Integer churchId) {
            this.churchId = churchId;
        }

        public String getChurchName() {
            return churchName;
        }

        public void setChurchName(String churchName) {
            this.churchName = churchName;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public String getEmbededUrl() {
            return embededUrl;
        }

        public void setEmbededUrl(String embededUrl) {
            this.embededUrl = embededUrl;
        }

        public Integer getMediaTypeId() {
            return mediaTypeId;
        }

        public void setMediaTypeId(Integer mediaTypeId) {
            this.mediaTypeId = mediaTypeId;
        }

        public Integer getPostTypeId() {
            return postTypeId;
        }

        public void setPostTypeId(Integer postTypeId) {
            this.postTypeId = postTypeId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getPostImage() {
            return postImage;
        }

        public void setPostImage(String postImage) {
            this.postImage = postImage;
        }

        public String getEmbedId() {
            return embedId;
        }

        public void setEmbedId(String embedId) {
            this.embedId = embedId;
        }

        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }

        public String getPostType() {
            return postType;
        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public Integer getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(Integer likeCount) {
            this.likeCount = likeCount;
        }

        public Integer getDisLikeCount() {
            return disLikeCount;
        }

        public void setDisLikeCount(Integer disLikeCount) {
            this.disLikeCount = disLikeCount;
        }

        public Integer getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(Integer commentCount) {
            this.commentCount = commentCount;
        }

        public Object getViewCount() {
            return viewCount;
        }

        public void setViewCount(Object viewCount) {
            this.viewCount = viewCount;
        }

        public String getCreatedByUser() {
            return createdByUser;
        }

        public void setCreatedByUser(String createdByUser) {
            this.createdByUser = createdByUser;
        }

        public String getUpdatedByUser() {
            return updatedByUser;
        }

        public void setUpdatedByUser(String updatedByUser) {
            this.updatedByUser = updatedByUser;
        }

        public String getPostUpdatedDate() {
            return postUpdatedDate;
        }

        public void setPostUpdatedDate(String postUpdatedDate) {
            this.postUpdatedDate = postUpdatedDate;
        }

    }

}

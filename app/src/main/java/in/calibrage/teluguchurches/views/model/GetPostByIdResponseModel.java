package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created model class for API to get postbyId
 */

public class GetPostByIdResponseModel {
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

    public class PostDetail {

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
        @SerializedName("churchId")
        @Expose
        private Object churchId;
        @SerializedName("fileName")
        @Expose
        private Object fileName;
        @SerializedName("fileLocation")
        @Expose
        private Object fileLocation;
        @SerializedName("fileExtention")
        @Expose
        private String fileExtention;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;
        @SerializedName("createdByUserId")
        @Expose
        private Integer createdByUserId;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("updatedByUserId")
        @Expose
        private Integer updatedByUserId;
        @SerializedName("updatedDate")
        @Expose
        private String updatedDate;
        @SerializedName("htmlDesc")
        @Expose
        private Object htmlDesc;
        @SerializedName("eventId")
        @Expose
        private Object eventId;
        @SerializedName("viewCount")
        @Expose
        private Integer viewCount;
        @SerializedName("postImage")
        @Expose
        private Object postImage;
        @SerializedName("mediaType")
        @Expose
        private String mediaType;
        @SerializedName("postType")
        @Expose
        private String postType;
        @SerializedName("categoryName")
        @Expose
        private String categoryName;
        @SerializedName("isLike")
        @Expose
        private Integer isLike;
        @SerializedName("isDisLike")
        @Expose
        private Integer isDisLike;
        @SerializedName("likeCount")
        @Expose
        private Integer likeCount;
        @SerializedName("disLikeCount")
        @Expose
        private Integer disLikeCount;
        @SerializedName("commentCount")
        @Expose
        private Integer commentCount;
        @SerializedName("parentCommentId")
        @Expose
        private Object parentCommentId;
        @SerializedName("createdByUser")
        @Expose
        private String createdByUser;
        @SerializedName("updatedByUser")
        @Expose
        private String updatedByUser;
        @SerializedName("postShortTitle")
        @Expose
        private String postShortTitle;

        public String getPostShortTitle() {
            return postShortTitle;
        }

        public void setPostShortTitle(String postShortTitle) {
            this.postShortTitle = postShortTitle;
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

        public Object getChurchId() {
            return churchId;
        }

        public void setChurchId(Object churchId) {
            this.churchId = churchId;
        }

        public Object getFileName() {
            return fileName;
        }

        public void setFileName(Object fileName) {
            this.fileName = fileName;
        }

        public Object getFileLocation() {
            return fileLocation;
        }

        public void setFileLocation(Object fileLocation) {
            this.fileLocation = fileLocation;
        }

        public String getFileExtention() {
            return fileExtention;
        }

        public void setFileExtention(String fileExtention) {
            this.fileExtention = fileExtention;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public Integer getCreatedByUserId() {
            return createdByUserId;
        }

        public void setCreatedByUserId(Integer createdByUserId) {
            this.createdByUserId = createdByUserId;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Integer getUpdatedByUserId() {
            return updatedByUserId;
        }

        public void setUpdatedByUserId(Integer updatedByUserId) {
            this.updatedByUserId = updatedByUserId;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }

        public Object getHtmlDesc() {
            return htmlDesc;
        }

        public void setHtmlDesc(Object htmlDesc) {
            this.htmlDesc = htmlDesc;
        }

        public Object getEventId() {
            return eventId;
        }

        public void setEventId(Object eventId) {
            this.eventId = eventId;
        }

        public Integer getViewCount() {
            return viewCount;
        }

        public void setViewCount(Integer viewCount) {
            this.viewCount = viewCount;
        }

        public Object getPostImage() {
            return postImage;
        }

        public void setPostImage(Object postImage) {
            this.postImage = postImage;
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

        public Integer getIsLike() {
            return isLike;
        }

        public void setIsLike(Integer isLike) {
            this.isLike = isLike;
        }

        public Integer getIsDisLike() {
            return isDisLike;
        }

        public void setIsDisLike(Integer isDisLike) {
            this.isDisLike = isDisLike;
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

        public Object getParentCommentId() {
            return parentCommentId;
        }

        public void setParentCommentId(Object parentCommentId) {
            this.parentCommentId = parentCommentId;
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

    }

    public class Result {

        @SerializedName("postDetails")
        @Expose
        private List<PostDetail> postDetails = null;
        @SerializedName("commentDetails")
        @Expose
        private List<CommentDetail> commentDetails = null;
        @SerializedName("replyDetails")
        @Expose
        private List<ReplyDetail> replyDetails = null;

        public List<PostDetail> getPostDetails() {
            return postDetails;
        }

        public void setPostDetails(List<PostDetail> postDetails) {
            this.postDetails = postDetails;
        }

        public List<CommentDetail> getCommentDetails() {
            return commentDetails;
        }

        public void setCommentDetails(List<CommentDetail> commentDetails) {
            this.commentDetails = commentDetails;
        }
        public List<ReplyDetail> getReplyDetails() {
            return replyDetails;
        }

        public void setReplyDetails(List<ReplyDetail> replyDetails) {
            this.replyDetails = replyDetails;
        }

    }

    public class CommentDetail {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("postId")
        @Expose
        private Integer postId;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("parentCommentId")
        @Expose
        private Integer parentCommentId;
        @SerializedName("userId")
        @Expose
        private Integer userId;
        @SerializedName("commentByUser")
        @Expose
        private String commentByUser;
        @SerializedName("userImage")
        @Expose
        private Object userImage;
        @SerializedName("replyCount")
        @Expose
        private Integer replyCount;

        public Integer getPos() {
            return pos;
        }

        public void setPos(Integer pos) {
            this.pos = pos;
        }

        private Integer pos;

        public Integer getId() {
            return id;
        }

        public Integer getReplyCount() {
            return replyCount;
        }

        public void setReplyCount(Integer replyCount) {
            this.replyCount = replyCount;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getPostId() {
            return postId;
        }

        public void setPostId(Integer postId) {
            this.postId = postId;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Integer getParentCommentId() {
            return parentCommentId;
        }

        public void setParentCommentId(Integer parentCommentId) {
            this.parentCommentId = parentCommentId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getCommentByUser() {
            return commentByUser;
        }

        public void setCommentByUser(String commentByUser) {
            this.commentByUser = commentByUser;
        }

        public Object getUserImage() {
            return userImage;
        }

        public void setUserImage(Object userImage) {
            this.userImage = userImage;
        }

    }

    public class ReplyDetail {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("postId")
        @Expose
        private Integer postId;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("parentCommentId")
        @Expose
        private Integer parentCommentId;
        @SerializedName("userId")
        @Expose
        private Integer userId;
        @SerializedName("commentByUser")
        @Expose
        private String commentByUser;
        @SerializedName("userImage")
        @Expose
        private String userImage;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getPostId() {
            return postId;
        }

        public void setPostId(Integer postId) {
            this.postId = postId;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Integer getParentCommentId() {
            return parentCommentId;
        }

        public void setParentCommentId(Integer parentCommentId) {
            this.parentCommentId = parentCommentId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getCommentByUser() {
            return commentByUser;
        }

        public void setCommentByUser(String commentByUser) {
            this.commentByUser = commentByUser;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

    }
}

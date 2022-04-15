package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created model class for API to get upcoming events
 */

public class GetUpComingEventsModel {
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
    public class EventDetail {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("startDate")
        @Expose
        private String startDate;
        @SerializedName("endDate")
        @Expose
        private String endDate;
        @SerializedName("churchId")
        @Expose
        private Integer churchId;
        @SerializedName("churchName")
        @Expose
        private String churchName;
        @SerializedName("registrationNumber")
        @Expose
        private String registrationNumber;
        @SerializedName("contactNumber")
        @Expose
        private String contactNumber;
        @SerializedName("fileLocation")
        @Expose
        private String fileLocation;
        @SerializedName("fileName")
        @Expose
        private String fileName;
        @SerializedName("fileExtention")
        @Expose
        private String fileExtention;
        @SerializedName("eventImage")
        @Expose
        private String eventImage;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;
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

        @SerializedName("authorId")
        @Expose
        private Integer authorId;

        @SerializedName("authorName")
        @Expose
        private String authorName;

        private String mobileNumber;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        private String description;

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }



        @SerializedName("eventShortTitle")
        @Expose
        private String eventShortTitle;

        public String getEventShortTitle() {
            return eventShortTitle;
        }

        public void setEventShortTitle(String eventShortTitle) {
            this.eventShortTitle = eventShortTitle;
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

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

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

        public String getRegistrationNumber() {
            return registrationNumber;
        }

        public void setRegistrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
        }

        public String getContactNumber() {
            return contactNumber;
        }
        public Integer getAuthorId() {
            return authorId;
        }

        public void setAuthorId(Integer authorId) {
            this.authorId = authorId;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }
        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getFileLocation() {
            return fileLocation;
        }

        public void setFileLocation(String fileLocation) {
            this.fileLocation = fileLocation;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileExtention() {
            return fileExtention;
        }

        public void setFileExtention(String fileExtention) {
            this.fileExtention = fileExtention;
        }

        public String getEventImage() {
            return eventImage;
        }

        public void setEventImage(String eventImage) {
            this.eventImage = eventImage;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
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

    }
    public class Result {

        @SerializedName("eventDetails")
        @Expose
        private List<EventDetail> eventDetails;
        @SerializedName("commentDetails")
        @Expose
        private List<CommentDetail> commentDetails ;

        public List<EventDetail> getEventDetails() {
            return eventDetails;
        }

        public void setEventDetails(List<EventDetail> eventDetails) {
            this.eventDetails = eventDetails;
        }

        public List<CommentDetail> getCommentDetails() {
            return commentDetails;
        }

        public void setCommentDetails(List<CommentDetail> commentDetails) {
            this.commentDetails = commentDetails;
        }

    }
    public class CommentDetail {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("eventId")
        @Expose
        private Integer eventId;
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

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getEventId() {
            return eventId;
        }

        public void setEventId(Integer eventId) {
            this.eventId = eventId;
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

        public Integer getReplyCount() {
            return replyCount;
        }

        public void setReplyCount(Integer replyCount) {
            this.replyCount = replyCount;
        }
    }
}





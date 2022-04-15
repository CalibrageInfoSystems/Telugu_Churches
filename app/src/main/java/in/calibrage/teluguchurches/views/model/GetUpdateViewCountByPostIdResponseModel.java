package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created model class for API to update vies count for posts
 */
public class GetUpdateViewCountByPostIdResponseModel {

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
        private String htmlDesc;
        @SerializedName("eventId")
        @Expose
        private Integer eventId;
        @SerializedName("viewCount")
        @Expose
        private Integer viewCount;

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

        public String getHtmlDesc() {
            return htmlDesc;
        }

        public void setHtmlDesc(String htmlDesc) {
            this.htmlDesc = htmlDesc;
        }

        public Integer getEventId() {
            return eventId;
        }

        public void setEventId(Integer eventId) {
            this.eventId = eventId;
        }

        public Integer getViewCount() {
            return viewCount;
        }

        public void setViewCount(Integer viewCount) {
            this.viewCount = viewCount;
        }

    }
}

package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created model class for API to get event info of church
 */

public class GetEventsForTodayResponseModel {

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

        @SerializedName("eventDate")
        @Expose
        private String eventDate;
        @SerializedName("eventsCount")
        @Expose
        private Integer eventsCount;
        @SerializedName("churchId")
        @Expose
        private Integer churchId;
        @SerializedName("authorId")
        @Expose
        private Object authorId;
        @SerializedName("eventName")
        @Expose
        private String eventName;
        @SerializedName("churchName")
        @Expose
        private String churchName;
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

        public String getEventDate() {
            return eventDate;
        }

        public void setEventDate(String eventDate) {
            this.eventDate = eventDate;
        }

        public Integer getEventsCount() {
            return eventsCount;
        }

        public void setEventsCount(Integer eventsCount) {
            this.eventsCount = eventsCount;
        }

        public Integer getChurchId() {
            return churchId;
        }

        public void setChurchId(Integer churchId) {
            this.churchId = churchId;
        }

        public Object getAuthorId() {
            return authorId;
        }

        public void setAuthorId(Object authorId) {
            this.authorId = authorId;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getChurchName() {
            return churchName;
        }

        public void setChurchName(String churchName) {
            this.churchName = churchName;
        }

        public String getContactNumber() {
            return contactNumber;
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
    }
}

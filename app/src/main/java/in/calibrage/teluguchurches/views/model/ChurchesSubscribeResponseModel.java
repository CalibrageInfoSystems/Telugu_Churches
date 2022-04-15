package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created model class for API to get subscibed church response
 */

public class ChurchesSubscribeResponseModel {
    @SerializedName("listResult")
    @Expose
    private Object listResult;
    @SerializedName("result")
    @Expose
    private SubscribeResponseModel.Result result;
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

    public SubscribeResponseModel.Result getResult() {
        return result;
    }

    public void setResult(SubscribeResponseModel.Result result) {
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
    public class Result {

        @SerializedName("isSubscribed")
        @Expose
        private Integer isSubscribed;
        @SerializedName("subscriptionDetails")
        @Expose
        private List<SubscribeResponseModel.Result.SubscriptionDetail> subscriptionDetails = null;

        public Integer getIsSubscribed() {
            return isSubscribed;
        }

        public void setIsSubscribed(Integer isSubscribed) {
            this.isSubscribed = isSubscribed;
        }

        public List<SubscribeResponseModel.Result.SubscriptionDetail> getSubscriptionDetails() {
            return subscriptionDetails;
        }

        public void setSubscriptionDetails(List<SubscribeResponseModel.Result.SubscriptionDetail> subscriptionDetails) {
            this.subscriptionDetails = subscriptionDetails;
        }

        public class SubscriptionDetail {

            @SerializedName("userId")
            @Expose
            private Integer userId;
            @SerializedName("churchId")
            @Expose
            private Object churchId;
            @SerializedName("authorId")
            @Expose
            private Integer authorId;

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

            public Integer getAuthorId() {
                return authorId;
            }

            public void setAuthorId(Integer authorId) {
                this.authorId = authorId;
            }

        }
    }
}

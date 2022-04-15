package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created model class for API for particular pastor details response
 */

public class GetAuthorsResponse {

    @SerializedName("listResult")
    @Expose
    private List<ListResult> listResult = null;
    @SerializedName("result")
    @Expose
    private Object result;
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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
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

    public class ListResult {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("firstName")
        @Expose
        private String firstName;
        @SerializedName("middleName")
        @Expose
        private String middleName;
        @SerializedName("lastName")
        @Expose
        private String lastName;
        @SerializedName("authorName")
        @Expose
        private String authorName;
        @SerializedName("authorContactNumbar")
        @Expose
        private String authorContactNumbar;
        @SerializedName("authorEmail")
        @Expose
        private String authorEmail;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("genderTypeId")
        @Expose
        private Integer genderTypeId;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("countryId")
        @Expose
        private Integer countryId;
        @SerializedName("countryName")
        @Expose
        private String countryName;
        @SerializedName("stateId")
        @Expose
        private Integer stateId;
        @SerializedName("stateName")
        @Expose
        private String stateName;
        @SerializedName("districtId")
        @Expose
        private Integer districtId;
        @SerializedName("districtName")
        @Expose
        private String districtName;
        @SerializedName("mandalId")
        @Expose
        private Integer mandalId;
        @SerializedName("mandalName")
        @Expose
        private String mandalName;
        @SerializedName("villageName")
        @Expose
        private String villageName;
        @SerializedName("pinCode")
        @Expose
        private Integer pinCode;
        @SerializedName("churchImage")
        @Expose
        private String churchImage;
        @SerializedName("userImage")
        @Expose
        private String userImage;
        @SerializedName("userName")
        @Expose
        private String userName;
        @SerializedName("churchId")
        @Expose
        private Integer churchId;
        @SerializedName("churchName")
        @Expose
        private String churchName;
        @SerializedName("registrationNumber")
        @Expose
        private String registrationNumber;
        @SerializedName("churchAddress")
        @Expose
        private String churchAddress;
        @SerializedName("createdByUser")
        @Expose
        private String createdByUser;
        @SerializedName("updatedByUser")
        @Expose
        private String updatedByUser;

        public Integer getIsSubscribed() {
            return isSubscribed;
        }

        public void setIsSubscribed(Integer isSubscribed) {
            this.isSubscribed = isSubscribed;
        }

        @SerializedName("isSubscribed")
        @Expose
        private Integer isSubscribed;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getAuthorContactNumbar() {
            return authorContactNumbar;
        }

        public void setAuthorContactNumbar(String authorContactNumbar) {
            this.authorContactNumbar = authorContactNumbar;
        }

        public String getAuthorEmail() {
            return authorEmail;
        }

        public void setAuthorEmail(String authorEmail) {
            this.authorEmail = authorEmail;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public Integer getGenderTypeId() {
            return genderTypeId;
        }

        public void setGenderTypeId(Integer genderTypeId) {
            this.genderTypeId = genderTypeId;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Integer getCountryId() {
            return countryId;
        }

        public void setCountryId(Integer countryId) {
            this.countryId = countryId;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public Integer getStateId() {
            return stateId;
        }

        public void setStateId(Integer stateId) {
            this.stateId = stateId;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public Integer getDistrictId() {
            return districtId;
        }

        public void setDistrictId(Integer districtId) {
            this.districtId = districtId;
        }

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public Integer getMandalId() {
            return mandalId;
        }

        public void setMandalId(Integer mandalId) {
            this.mandalId = mandalId;
        }

        public String getMandalName() {
            return mandalName;
        }

        public void setMandalName(String mandalName) {
            this.mandalName = mandalName;
        }

        public String getVillageName() {
            return villageName;
        }

        public void setVillageName(String villageName) {
            this.villageName = villageName;
        }

        public Integer getPinCode() {
            return pinCode;
        }

        public void setPinCode(Integer pinCode) {
            this.pinCode = pinCode;
        }

        public String getChurchImage() {
            return churchImage;
        }

        public void setChurchImage(String churchImage) {
            this.churchImage = churchImage;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
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

        public String getChurchAddress() {
            return churchAddress;
        }

        public void setChurchAddress(String churchAddress) {
            this.churchAddress = churchAddress;
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

}


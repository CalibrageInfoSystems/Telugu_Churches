package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created model class for API for churches list response
 */

public class GetAllChurchesResponseModel {

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
    private List<String> validationErrors = null;
    @SerializedName("exception")
    @Expose
    private String exception;

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

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public Object getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public static class ListResult {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("registrationNumber")
        @Expose
        private String registrationNumber;
        @SerializedName("address1")
        @Expose
        private String address1;
        @SerializedName("address2")
        @Expose
        private String address2;
        @SerializedName("landMark")
        @Expose
        private String landMark;
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
        @SerializedName("villageId")
        @Expose
        private Integer villageId;
        @SerializedName("villageName")
        @Expose
        private String villageName;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("mission")
        @Expose
        private String mission;
        @SerializedName("vision")
        @Expose
        private String vision;
        @SerializedName("contactNumber")
        @Expose
        private String contactNumber;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("websiteAddress")
        @Expose
        private String websiteAddress;
        @SerializedName("openingTime")
        @Expose
        private String  openingTime;
        @SerializedName("closingTime")
        @Expose
        private String  closingTime;
        @SerializedName("churchImage")
        @Expose
        private String churchImage;
        @SerializedName("userImage")
        @Expose
        private String userImage;
        @SerializedName("pasterUserId")
        @Expose
        private Integer pasterUserId;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;
        @SerializedName("pasterUser")
        @Expose
        private String pasterUser;
        @SerializedName("createdByUser")
        @Expose
        private String createdByUser;
        @SerializedName("updatedByUser")
        @Expose
        private String updatedByUser;
        @SerializedName("updatedDate")
        @Expose
        private String updatedDate;
        @SerializedName("churchId")
        @Expose
        private Integer churchId;

        @SerializedName("isSubscribed")
        @Expose
        private Integer isSubscribed;
        private Integer pinCode;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRegistrationNumber() {
            return registrationNumber;
        }

        public void setRegistrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getLandMark() {
            return landMark;
        }

        public void setLandMark(String landMark) {
            this.landMark = landMark;
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

        public Integer getVillageId() {
            return villageId;
        }

        public void setVillageId(Integer villageId) {
            this.villageId = villageId;
        }

        public String getVillageName() {
            return villageName;
        }

        public void setVillageName(String villageName) {
            this.villageName = villageName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMission() {
            return mission;
        }

        public void setMission(String mission) {
            this.mission = mission;
        }

        public String getVision() {
            return vision;
        }

        public void setVision(String vision) {
            this.vision = vision;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getWebsiteAddress() {
            return websiteAddress;
        }

        public void setWebsiteAddress(String websiteAddress) {
            this.websiteAddress = websiteAddress;
        }

        public String  getOpeningTime() {
            return openingTime;
        }

        public void setOpeningTime(String  openingTime) {
            this.openingTime = openingTime;
        }

        public String  getClosingTime() {
            return closingTime;
        }

        public void setClosingTime(String  closingTime) {
            this.closingTime = closingTime;
        }

        public String getChurchImage() {
            return churchImage;
        }

        public void setChurchImage(String churchImage) {
            this.churchImage = churchImage;
        }

        public Object getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public Integer getPasterUserId() {
            return pasterUserId;
        }

        public void setPasterUserId(Integer pasterUserId) {
            this.pasterUserId = pasterUserId;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public String getPasterUser() {
            return pasterUser;
        }

        public void setPasterUser(String pasterUser) {
            this.pasterUser = pasterUser;
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

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }

        public Integer getPinCode() {
            return pinCode;
        }

        public void setPinCode(Integer pinCode) {
            this.pinCode = pinCode;
        }

        public Integer getChurchId() {
            return churchId;
        }

        public void setChurchId(Integer churchId) {
            this.churchId = churchId;
        }

        public Integer getIsSubscribed() {
            return isSubscribed;
        }

        public void setIsSubscribed(Integer isSubscribed) {
            this.isSubscribed = isSubscribed;
        }

    }
}

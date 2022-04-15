package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created model class for API login response
 */

public class GetLoginResponseModel {

    @SerializedName("userDetails")
    @Expose
    private UserDetails userDetails;
    @SerializedName("userActivityRights")
    @Expose
    private UserActivityRights userActivityRights;

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public UserActivityRights getUserActivityRights() {
        return userActivityRights;
    }

    public void setUserActivityRights(UserActivityRights userActivityRights) {
        this.userActivityRights = userActivityRights;
    }


    public class ListResult {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("userName")
        @Expose
        private String userName;
        @SerializedName("mobileNumber")
        @Expose
        private String mobileNumber;
        @SerializedName("gender")
        @Expose
        private Object gender;
        @SerializedName("dob")
        @Expose
        private Object dob;
        @SerializedName("roleId")
        @Expose
        private Integer roleId;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("roleName")
        @Expose
        private String roleName;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public Object getGender() {
            return gender;
        }

        public void setGender(Object gender) {
            this.gender = gender;
        }

        public Object getDob() {
            return dob;
        }

        public void setDob(Object dob) {
            this.dob = dob;
        }

        public Integer getRoleId() {
            return roleId;
        }

        public void setRoleId(Integer roleId) {
            this.roleId = roleId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

    }

    public class UserActivityRights {

        @SerializedName("listResult")
        @Expose
        private List<Object> listResult = null;
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

        public List<Object> getListResult() {
            return listResult;
        }

        public void setListResult(List<Object> listResult) {
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

    }


    public class UserDetails {

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

    }
}

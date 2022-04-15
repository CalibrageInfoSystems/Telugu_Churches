package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created model class for API to for pastors list request
 */

public class GetAdminRequestModel {
    @SerializedName("pasterUserId")
    @Expose
    private Integer pasterUserId;
    @SerializedName("pageIndex")
    @Expose
    private Integer pageIndex;
    @SerializedName("pageSize")
    @Expose
    private Integer pageSize;
    @SerializedName("sortbyColumnName")
    @Expose
    private String sortbyColumnName;
    @SerializedName("sortDirection")
    @Expose
    private String sortDirection;
    @SerializedName("searchName")
    @Expose
    private String searchName;
    private String fromDate;
    private String toDate;

    @SerializedName("churchId")
    @Expose
    private Integer churchId;
    @SerializedName("month")
    @Expose
    private Integer month;
    @SerializedName("year")
    @Expose
    private Integer year;

    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("uid")
    @Expose
    private Integer uid;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getChurchId() {
        return churchId;
    }

    public void setChurchId(Integer churchId) {
        this.churchId = churchId;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getPasterUserId() {
        return pasterUserId;
    }

    public void setPasterUserId(Integer pasterUserId) {
        this.pasterUserId = pasterUserId;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortbyColumnName() {
        return sortbyColumnName;
    }

    public void setSortbyColumnName(String sortbyColumnName) {
        this.sortbyColumnName = sortbyColumnName;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}


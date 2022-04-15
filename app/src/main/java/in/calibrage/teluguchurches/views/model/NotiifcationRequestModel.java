package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created model class for API for notification request
 */

public class NotiifcationRequestModel {


    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("sortbyColumnName")
    @Expose
    private String sortbyColumnName;
    @SerializedName("sortDirection")
    @Expose
    private String sortDirection;
    @SerializedName("pageIndex")
    @Expose
    private Integer pageIndex;
    @SerializedName("pageSize")
    @Expose
    private Integer pageSize;
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
}

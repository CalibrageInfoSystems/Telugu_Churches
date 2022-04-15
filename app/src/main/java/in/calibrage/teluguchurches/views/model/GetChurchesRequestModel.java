package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created model class for API to get churhes
 */

public class GetChurchesRequestModel {

    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("pageIndex")
    @Expose
    private Integer pageIndex;
    @SerializedName("pageSize")
    @Expose
    private Integer pageSize;
    @SerializedName("uid")
    @Expose
    private Integer uid;
    @SerializedName("sortbyColumnName")
    @Expose
    private String sortbyColumnName;
    @SerializedName("sortDirection")
    @Expose
    private String sortDirection;
    @SerializedName("searchName")
    @Expose
    private String searchName;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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

}

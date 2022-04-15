package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created model class for API for particular category details response
 */

public class GetCategoryRequestModel {

    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("isActive")
    @Expose
    private Integer isActive;
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

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("itemId")
    @Expose
    private Integer itemId;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }



    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
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

}


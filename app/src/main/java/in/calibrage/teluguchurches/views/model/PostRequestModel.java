package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created model class for API images post request
 */

public class PostRequestModel {
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
    @SerializedName("authorId")
    @Expose
    private Integer authorId;
    @SerializedName("mediaTypeId")
    @Expose
    private Integer mediaTypeId;
    @SerializedName("churchId")
    @Expose
    private Integer churchId;

    public Integer getChurchId() {
        return churchId;
    }

    public void setChurchId(Integer churchId) {
        this.churchId = churchId;
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

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getMediaTypeId() {
        return mediaTypeId;
    }

    public void setMediaTypeId(Integer mediaTypeId) {
        this.mediaTypeId = mediaTypeId;
    }
}

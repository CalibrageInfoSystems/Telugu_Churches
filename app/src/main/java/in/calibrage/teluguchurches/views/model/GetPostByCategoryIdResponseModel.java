package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created model class for API to get post by categoryId response
 */

public class GetPostByCategoryIdResponseModel {

    @SerializedName("listResult")
    @Expose
    private Object listResult;
    @SerializedName("result")
    @Expose
    private Result result;
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


    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
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

        @SerializedName("images")
        @Expose
        private List<Image> images = null;
        @SerializedName("videos")
        @Expose
        private List<Video> videos = null;
        @SerializedName("audios")
        @Expose
        private List<Audio> audios = null;
        @SerializedName("documents")
        @Expose
        private List<Document> documents = null;

        public List<Image> getImages() {
            return images;
        }

        public void setImages(List<Image> images) {
            this.images = images;
        }

        public List<Video> getVideos() {
            return videos;
        }

        public void setVideos(List<Video> videos) {
            this.videos = videos;
        }

        public List<Audio> getAudios() {
            return audios;
        }

        public void setAudios(List<Audio> audios) {
            this.audios = audios;
        }

        public List<Document> getDocuments() {
            return documents;
        }

        public void setDocuments(List<Document> documents) {
            this.documents = documents;
        }

    }

    public class Image {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("desc")
        @Expose
        private String desc;
        @SerializedName("categoryId")
        @Expose
        private Integer categoryId;
        @SerializedName("embededUrl")
        @Expose
        private Object embededUrl;
        @SerializedName("mediaTypeId")
        @Expose
        private Integer mediaTypeId;
        @SerializedName("postTypeId")
        @Expose
        private Integer postTypeId;
        @SerializedName("userId")
        @Expose
        private Integer userId;
        @SerializedName("churchId")
        @Expose
        private Integer churchId;
        @SerializedName("fileName")
        @Expose
        private String fileName;
        @SerializedName("fileLocation")
        @Expose
        private String fileLocation;
        @SerializedName("fileExtention")
        @Expose
        private String fileExtention;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;
        @SerializedName("createdByUserId")
        @Expose
        private Integer createdByUserId;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("updatedByUserId")
        @Expose
        private Integer updatedByUserId;
        @SerializedName("updatedDate")
        @Expose
        private String updatedDate;
        @SerializedName("postImage")
        @Expose
        private String postImage;
        @SerializedName("mediaType")
        @Expose
        private String mediaType;
        @SerializedName("postType")
        @Expose
        private String postType;
        @SerializedName("categoryName")
        @Expose
        private String categoryName;
        @SerializedName("createdByUser")
        @Expose
        private String createdByUser;
        @SerializedName("updatedByUser")
        @Expose
        private String updatedByUser;

        @SerializedName("isLike")
        @Expose
        private Boolean isLike;
        @SerializedName("isDisLike")
        @Expose
        private Boolean isDisLike;
        @SerializedName("likeCount")
        @Expose
        private Integer likeCount;
        @SerializedName("disLikeCount")
        @Expose
        private Integer disLikeCount;
        @SerializedName("commentCount")
        @Expose
        private Integer commentCount;
        @SerializedName("parentCommentId")
        @Expose
        private Integer parentCommentId;

        public Boolean getIsLike() {
            return isLike;
        }

        public void setIsLike(Boolean isLike) {
            this.isLike = isLike;
        }

        public Boolean getIsDisLike() {
            return isDisLike;
        }

        public void setIsDisLike(Boolean isDisLike) {
            this.isDisLike = isDisLike;
        }

        public Integer getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(Integer likeCount) {
            this.likeCount = likeCount;
        }

        public Integer getDisLikeCount() {
            return disLikeCount;
        }

        public void setDisLikeCount(Integer disLikeCount) {
            this.disLikeCount = disLikeCount;
        }

        public Integer getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(Integer commentCount) {
            this.commentCount = commentCount;
        }

        public Integer getParentCommentId() {
            return parentCommentId;
        }

        public void setParentCommentId(Integer parentCommentId) {
            this.parentCommentId = parentCommentId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public Object getEmbededUrl() {
            return embededUrl;
        }

        public void setEmbededUrl(Object embededUrl) {
            this.embededUrl = embededUrl;
        }

        public Integer getMediaTypeId() {
            return mediaTypeId;
        }

        public void setMediaTypeId(Integer mediaTypeId) {
            this.mediaTypeId = mediaTypeId;
        }

        public Integer getPostTypeId() {
            return postTypeId;
        }

        public void setPostTypeId(Integer postTypeId) {
            this.postTypeId = postTypeId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getChurchId() {
            return churchId;
        }

        public void setChurchId(Integer churchId) {
            this.churchId = churchId;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileLocation() {
            return fileLocation;
        }

        public void setFileLocation(String fileLocation) {
            this.fileLocation = fileLocation;
        }

        public String getFileExtention() {
            return fileExtention;
        }

        public void setFileExtention(String fileExtention) {
            this.fileExtention = fileExtention;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public Integer getCreatedByUserId() {
            return createdByUserId;
        }

        public void setCreatedByUserId(Integer createdByUserId) {
            this.createdByUserId = createdByUserId;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Integer getUpdatedByUserId() {
            return updatedByUserId;
        }

        public void setUpdatedByUserId(Integer updatedByUserId) {
            this.updatedByUserId = updatedByUserId;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }

        public String getPostImage() {
            return postImage;
        }

        public void setPostImage(String postImage) {
            this.postImage = postImage;
        }

        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }

        public String getPostType() {
            return postType;
        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
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

    public class Audio {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("desc")
        @Expose
        private String desc;
        @SerializedName("categoryId")
        @Expose
        private Integer categoryId;
        @SerializedName("embededUrl")
        @Expose
        private String embededUrl;
        @SerializedName("mediaTypeId")
        @Expose
        private Integer mediaTypeId;
        @SerializedName("postTypeId")
        @Expose
        private Integer postTypeId;
        @SerializedName("userId")
        @Expose
        private Integer userId;
        @SerializedName("churchId")
        @Expose
        private Integer churchId;
        @SerializedName("fileName")
        @Expose
        private String fileName;
        @SerializedName("fileLocation")
        @Expose
        private String fileLocation;
        @SerializedName("fileExtention")
        @Expose
        private String fileExtention;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;
        @SerializedName("createdByUserId")
        @Expose
        private Integer createdByUserId;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("updatedByUserId")
        @Expose
        private Integer updatedByUserId;
        @SerializedName("updatedDate")
        @Expose
        private String updatedDate;
        @SerializedName("postImage")
        @Expose
        private String postImage;
        @SerializedName("mediaType")
        @Expose
        private String mediaType;
        @SerializedName("postType")
        @Expose
        private String postType;
        @SerializedName("categoryName")
        @Expose
        private String categoryName;
        @SerializedName("createdByUser")
        @Expose
        private String createdByUser;
        @SerializedName("updatedByUser")
        @Expose
        private String updatedByUser;

        @SerializedName("isLike")
        @Expose
        private Boolean isLike;
        @SerializedName("isDisLike")
        @Expose
        private Boolean isDisLike;
        @SerializedName("likeCount")
        @Expose
        private Integer likeCount;
        @SerializedName("disLikeCount")
        @Expose
        private Integer disLikeCount;
        @SerializedName("commentCount")
        @Expose
        private Integer commentCount;
        @SerializedName("parentCommentId")
        @Expose
        private Integer parentCommentId;
        @SerializedName("embedId")
        @Expose
        private String embedId;

        public Boolean getIsLike() {
            return isLike;
        }

        public void setIsLike(Boolean isLike) {
            this.isLike = isLike;
        }

        public Boolean getIsDisLike() {
            return isDisLike;
        }

        public void setIsDisLike(Boolean isDisLike) {
            this.isDisLike = isDisLike;
        }

        public Integer getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(Integer likeCount) {
            this.likeCount = likeCount;
        }

        public Integer getDisLikeCount() {
            return disLikeCount;
        }

        public void setDisLikeCount(Integer disLikeCount) {
            this.disLikeCount = disLikeCount;
        }

        public Integer getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(Integer commentCount) {
            this.commentCount = commentCount;
        }

        public Integer getParentCommentId() {
            return parentCommentId;
        }

        public void setParentCommentId(Integer parentCommentId) {
            this.parentCommentId = parentCommentId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public String getEmbededUrl() {
            return embededUrl;
        }

        public void setEmbededUrl(String embededUrl) {
            this.embededUrl = embededUrl;
        }

        public Integer getMediaTypeId() {
            return mediaTypeId;
        }

        public void setMediaTypeId(Integer mediaTypeId) {
            this.mediaTypeId = mediaTypeId;
        }

        public Integer getPostTypeId() {
            return postTypeId;
        }

        public void setPostTypeId(Integer postTypeId) {
            this.postTypeId = postTypeId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getChurchId() {
            return churchId;
        }

        public void setChurchId(Integer churchId) {
            this.churchId = churchId;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileLocation() {
            return fileLocation;
        }

        public void setFileLocation(String fileLocation) {
            this.fileLocation = fileLocation;
        }

        public String getFileExtention() {
            return fileExtention;
        }

        public void setFileExtention(String fileExtention) {
            this.fileExtention = fileExtention;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public Integer getCreatedByUserId() {
            return createdByUserId;
        }

        public void setCreatedByUserId(Integer createdByUserId) {
            this.createdByUserId = createdByUserId;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Integer getUpdatedByUserId() {
            return updatedByUserId;
        }

        public void setUpdatedByUserId(Integer updatedByUserId) {
            this.updatedByUserId = updatedByUserId;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }

        public String getPostImage() {
            return postImage;
        }

        public void setPostImage(String postImage) {
            this.postImage = postImage;
        }

        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }

        public String getPostType() {
            return postType;
        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
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
        public String getEmbedId() {
            return embedId;
        }

        public void setEmbedId(String embedId) {
            this.embedId = embedId;
        }
    }

    public class Video {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("desc")
        @Expose
        private String desc;
        @SerializedName("categoryId")
        @Expose
        private Integer categoryId;
        @SerializedName("embededUrl")
        @Expose
        private String embededUrl;
        @SerializedName("mediaTypeId")
        @Expose
        private Integer mediaTypeId;
        @SerializedName("postTypeId")
        @Expose
        private Integer postTypeId;
        @SerializedName("userId")
        @Expose
        private Integer userId;
        @SerializedName("churchId")
        @Expose
        private Object churchId;
        @SerializedName("fileName")
        @Expose
        private Object fileName;
        @SerializedName("fileLocation")
        @Expose
        private Object fileLocation;
        @SerializedName("fileExtention")
        @Expose
        private Object fileExtention;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;
        @SerializedName("createdByUserId")
        @Expose
        private Integer createdByUserId;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("updatedByUserId")
        @Expose
        private Integer updatedByUserId;
        @SerializedName("updatedDate")
        @Expose
        private String updatedDate;
        @SerializedName("postImage")
        @Expose
        private Object postImage;
        @SerializedName("mediaType")
        @Expose
        private String mediaType;
        @SerializedName("postType")
        @Expose
        private String postType;
        @SerializedName("categoryName")
        @Expose
        private String categoryName;
        @SerializedName("createdByUser")
        @Expose
        private String createdByUser;
        @SerializedName("updatedByUser")
        @Expose
        private String updatedByUser;

        @SerializedName("isLike")
        @Expose
        private Boolean isLike;
        @SerializedName("isDisLike")
        @Expose
        private Boolean isDisLike;
        @SerializedName("likeCount")
        @Expose
        private Integer likeCount;
        @SerializedName("disLikeCount")
        @Expose
        private Integer disLikeCount;
        @SerializedName("commentCount")
        @Expose
        private Integer commentCount;
        @SerializedName("parentCommentId")
        @Expose
        private Integer parentCommentId;

        public Boolean getIsLike() {
            return isLike;
        }

        public void setIsLike(Boolean isLike) {
            this.isLike = isLike;
        }

        public Boolean getIsDisLike() {
            return isDisLike;
        }

        public void setIsDisLike(Boolean isDisLike) {
            this.isDisLike = isDisLike;
        }

        public Integer getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(Integer likeCount) {
            this.likeCount = likeCount;
        }

        public Integer getDisLikeCount() {
            return disLikeCount;
        }

        public void setDisLikeCount(Integer disLikeCount) {
            this.disLikeCount = disLikeCount;
        }

        public Integer getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(Integer commentCount) {
            this.commentCount = commentCount;
        }

        public Integer getParentCommentId() {
            return parentCommentId;
        }

        public void setParentCommentId(Integer parentCommentId) {
            this.parentCommentId = parentCommentId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public String getEmbededUrl() {
            return embededUrl;
        }

        public void setEmbededUrl(String embededUrl) {
            this.embededUrl = embededUrl;
        }

        public Integer getMediaTypeId() {
            return mediaTypeId;
        }

        public void setMediaTypeId(Integer mediaTypeId) {
            this.mediaTypeId = mediaTypeId;
        }

        public Integer getPostTypeId() {
            return postTypeId;
        }

        public void setPostTypeId(Integer postTypeId) {
            this.postTypeId = postTypeId;
        }

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

        public Object getFileName() {
            return fileName;
        }

        public void setFileName(Object fileName) {
            this.fileName = fileName;
        }

        public Object getFileLocation() {
            return fileLocation;
        }

        public void setFileLocation(Object fileLocation) {
            this.fileLocation = fileLocation;
        }

        public Object getFileExtention() {
            return fileExtention;
        }

        public void setFileExtention(Object fileExtention) {
            this.fileExtention = fileExtention;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public Integer getCreatedByUserId() {
            return createdByUserId;
        }

        public void setCreatedByUserId(Integer createdByUserId) {
            this.createdByUserId = createdByUserId;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Integer getUpdatedByUserId() {
            return updatedByUserId;
        }

        public void setUpdatedByUserId(Integer updatedByUserId) {
            this.updatedByUserId = updatedByUserId;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }

        public Object getPostImage() {
            return postImage;
        }

        public void setPostImage(Object postImage) {
            this.postImage = postImage;
        }

        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }

        public String getPostType() {
            return postType;
        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
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


    public class Document {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("desc")
        @Expose
        private String desc;
        @SerializedName("categoryId")
        @Expose
        private Integer categoryId;
        @SerializedName("embededUrl")
        @Expose
        private Object embededUrl;
        @SerializedName("mediaTypeId")
        @Expose
        private Integer mediaTypeId;
        @SerializedName("postTypeId")
        @Expose
        private Integer postTypeId;
        @SerializedName("userId")
        @Expose
        private Object userId;
        @SerializedName("churchId")
        @Expose
        private Integer churchId;
        @SerializedName("fileName")
        @Expose
        private String fileName;
        @SerializedName("fileLocation")
        @Expose
        private String fileLocation;
        @SerializedName("fileExtention")
        @Expose
        private String fileExtention;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;
        @SerializedName("createdByUserId")
        @Expose
        private Integer createdByUserId;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("updatedByUserId")
        @Expose
        private Integer updatedByUserId;
        @SerializedName("updatedDate")
        @Expose
        private String updatedDate;
        @SerializedName("postImage")
        @Expose
        private String postImage;
        @SerializedName("mediaType")
        @Expose
        private String mediaType;
        @SerializedName("postType")
        @Expose
        private String postType;
        @SerializedName("categoryName")
        @Expose
        private String categoryName;
        @SerializedName("createdByUser")
        @Expose
        private String createdByUser;
        @SerializedName("updatedByUser")
        @Expose
        private String updatedByUser;

        @SerializedName("isLike")
        @Expose
        private Boolean isLike;
        @SerializedName("isDisLike")
        @Expose
        private Boolean isDisLike;
        @SerializedName("likeCount")
        @Expose
        private Integer likeCount;
        @SerializedName("disLikeCount")
        @Expose
        private Integer disLikeCount;
        @SerializedName("commentCount")
        @Expose
        private Integer commentCount;
        @SerializedName("parentCommentId")
        @Expose
        private Integer parentCommentId;

        public Boolean getIsLike() {
            return isLike;
        }

        public void setIsLike(Boolean isLike) {
            this.isLike = isLike;
        }

        public Boolean getIsDisLike() {
            return isDisLike;
        }

        public void setIsDisLike(Boolean isDisLike) {
            this.isDisLike = isDisLike;
        }

        public Integer getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(Integer likeCount) {
            this.likeCount = likeCount;
        }

        public Integer getDisLikeCount() {
            return disLikeCount;
        }

        public void setDisLikeCount(Integer disLikeCount) {
            this.disLikeCount = disLikeCount;
        }

        public Integer getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(Integer commentCount) {
            this.commentCount = commentCount;
        }

        public Integer getParentCommentId() {
            return parentCommentId;
        }

        public void setParentCommentId(Integer parentCommentId) {
            this.parentCommentId = parentCommentId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public Object getEmbededUrl() {
            return embededUrl;
        }

        public void setEmbededUrl(Object embededUrl) {
            this.embededUrl = embededUrl;
        }

        public Integer getMediaTypeId() {
            return mediaTypeId;
        }

        public void setMediaTypeId(Integer mediaTypeId) {
            this.mediaTypeId = mediaTypeId;
        }

        public Integer getPostTypeId() {
            return postTypeId;
        }

        public void setPostTypeId(Integer postTypeId) {
            this.postTypeId = postTypeId;
        }

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public Integer getChurchId() {
            return churchId;
        }

        public void setChurchId(Integer churchId) {
            this.churchId = churchId;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileLocation() {
            return fileLocation;
        }

        public void setFileLocation(String fileLocation) {
            this.fileLocation = fileLocation;
        }

        public String getFileExtention() {
            return fileExtention;
        }

        public void setFileExtention(String fileExtention) {
            this.fileExtention = fileExtention;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public Integer getCreatedByUserId() {
            return createdByUserId;
        }

        public void setCreatedByUserId(Integer createdByUserId) {
            this.createdByUserId = createdByUserId;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Integer getUpdatedByUserId() {
            return updatedByUserId;
        }

        public void setUpdatedByUserId(Integer updatedByUserId) {
            this.updatedByUserId = updatedByUserId;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }

        public String getPostImage() {
            return postImage;
        }

        public void setPostImage(String postImage) {
            this.postImage = postImage;
        }

        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }

        public String getPostType() {
            return postType;
        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
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


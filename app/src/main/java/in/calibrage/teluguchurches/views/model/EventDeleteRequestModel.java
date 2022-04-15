package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created model class for API to delete the event comments
 */

public class EventDeleteRequestModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("eventId")
    @Expose
    private Integer eventId;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("churchId")
    @Expose
    private Object churchId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
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

}

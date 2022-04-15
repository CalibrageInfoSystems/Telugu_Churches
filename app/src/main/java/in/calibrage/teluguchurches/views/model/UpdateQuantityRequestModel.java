package in.calibrage.teluguchurches.views.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created model class for API to update quantity request
 */
public class UpdateQuantityRequestModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("itemId")
    @Expose
    private Integer itemId;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("userId")
    @Expose
    private Integer userId;

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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}

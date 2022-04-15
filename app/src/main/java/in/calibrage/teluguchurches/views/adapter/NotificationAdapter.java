package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.views.model.GetNotificationResponseModel;

/**
 * This adapter is helpfull to show notifications in Notifications view
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;
    GetNotificationResponseModel dataList;
    private OnCartChangedListener onCartChangedListener;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param context
     * @param dataList
     */
    public NotificationAdapter(Context context, GetNotificationResponseModel dataList) {
        this.context = context;
        this.dataList = dataList;

    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_notification, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, final int position) {
        holder.titleText.setText(dataList.getResult().getNotificationsList().get(position).getName());
        holder.descriptionText.setText(dataList.getResult().getNotificationsList().get(position).getDesc());
        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCartChangedListener.setCartClickListener("itemPosition", position);
            }
        });

    }

    // items count
    @Override
    public int getItemCount() {
        return dataList.getResult().getNotificationsList().size();
    }


    /*
     * initialising viewholder to recycler view
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText, descriptionText;
        private LinearLayout mainLay;

        public ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            mainLay = itemView.findViewById(R.id.mainLay);
        }
    }

    /**
     * @param onCartChangedListener
     */
    public void setOnCartChangedListener(OnCartChangedListener onCartChangedListener) {
        this.onCartChangedListener = onCartChangedListener;
    }

    /**
     * Container Activity must implement this interface
     * you can define any parameter as per your requirement
     */
    public interface OnCartChangedListener {
        void setCartClickListener(String status, int position);
    }
}

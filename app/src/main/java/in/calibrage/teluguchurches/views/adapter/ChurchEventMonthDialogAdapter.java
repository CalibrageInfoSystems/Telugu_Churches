package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.views.model.GetEventByDateAndChurchIdResponseModel;

/**
 * This adapter is helpfull to show the particular church evnets information
 */
public class ChurchEventMonthDialogAdapter extends RecyclerView.Adapter<ChurchEventMonthDialogAdapter.ViewHolder> {

    Context mContext;
    GetEventByDateAndChurchIdResponseModel events = new GetEventByDateAndChurchIdResponseModel();

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param mContext
     * @param events
     */
    public ChurchEventMonthDialogAdapter(Context mContext, GetEventByDateAndChurchIdResponseModel events) {
        this.mContext = mContext;
        this.events = events;
    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.adapter_dialog, null);
        ChurchEventMonthDialogAdapter.ViewHolder mh = new ChurchEventMonthDialogAdapter.ViewHolder(v);
        return mh;
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.eventText.setText(events.getListResult().get(position).getEventTitle());
        holder.startDateText.setText(CommonUtil.formatDateTimeUi(events.getListResult().get(position).getStartDate()));
        holder.endDateText.setText(CommonUtil.formatDateTimeUi(events.getListResult().get(position).getEndDate()));
    }

    // items count
    @Override
    public int getItemCount() {
        return (null != events ? events.getListResult().size() : 0);
    }

    /*
     * initialising viewholder to recycler view
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView eventText, startDateText, endDateText;
        private RelativeLayout titleLay;

        public ViewHolder(View itemView) {
            super(itemView);
            eventText = itemView.findViewById(R.id.eventText);
            startDateText = itemView.findViewById(R.id.startDateText);
            endDateText = itemView.findViewById(R.id.endDateText);
        }
    }
}

package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.views.model.GetEventDetailsInfoByChurchIdMonthYear;

public class EventDetailsInfoByChurchIdMonthYearAdapter extends RecyclerView.Adapter<EventDetailsInfoByChurchIdMonthYearAdapter.ViewHolder> {

    Context mContext;
    GetEventDetailsInfoByChurchIdMonthYear events;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param mContext
     * @param events
     */
    public EventDetailsInfoByChurchIdMonthYearAdapter(Context mContext, GetEventDetailsInfoByChurchIdMonthYear events) {
        this.events = events;
        this.mContext = mContext;


    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.churchevent_adapter, parent, false);
        return new EventDetailsInfoByChurchIdMonthYearAdapter.ViewHolder(view);
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.churchNameText.setText((CommonUtil.fromHtml("<b><font color=\"#ffffff\">" + mContext.getString(R.string.church_name) + " " + " : " + " " + "</font><b>"
                + events.getListResult().get(position).getChurchName())));
        holder.eventText.setText(CommonUtil.fromHtml("<b><font color=\"#ffffff\">" + mContext.getString(R.string.event_name) + " " + " : " + " " + "</font><b>"
                + events.getListResult().get(position).getTitle()));
        holder.numberText.setText((CommonUtil.fromHtml("<b><font color=\"#ffffff\">" + mContext.getString(R.string.mobile_number_event) + " " + " : " + " " + "</font><b>"
                + events.getListResult().get(position).getContactNumber())));
        String from = "<b><font color='#ffffff'>" + mContext.getString(R.string.event_from) + " " + " : " + " " + " </font>  <b><font color='#ffffff'>"
                + CommonUtil.formatDateTimeUi(events.getListResult().get(position).getStartDate()) + "</font>";
        String to = "<b><font color='#ffffff'>" + mContext.getString(R.string.event_to) + " " + " : " + " " + " </font>  <b><font color='#ffffff'>" +
                CommonUtil.formatDateTimeUi(events.getListResult().get(position).getEndDate()) + "</font>";

        holder.openTimeText.setText(Html.fromHtml(from));
        holder.closeTimeText.setText(Html.fromHtml(to));
        // holder.openTimeText.setText(Html.fromHtml(from ));
    }

    // items count
    @Override
    public int getItemCount() {
        return events.getListResult().size();
    }

    /*
     * initialising viewholder to recycler view
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView churchNameText, eventText, numberText,
                openTimeText, closeTimeText;

        public ViewHolder(View itemView) {
            super(itemView);
            churchNameText = itemView.findViewById(R.id.churchNameText);
            eventText = itemView.findViewById(R.id.eventText);
            numberText = itemView.findViewById(R.id.numberText);
            openTimeText = itemView.findViewById(R.id.openTimeText);
            closeTimeText = itemView.findViewById(R.id.closeTimeText);
        }
    }
}

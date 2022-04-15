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
import in.calibrage.teluguchurches.views.model.GetResponceEvents;
import in.calibrage.teluguchurches.views.model.GetUpComingEvents;

// This adapter is helpfull to show events by month  in Church Detailview

public class EventsByMonthAdapter extends RecyclerView.Adapter<EventsByMonthAdapter.ViewHolder> {
    private Context context;
    GetResponceEvents dataList = new GetResponceEvents();
    GetUpComingEvents datalistevents = new GetUpComingEvents();


    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param context
     * @param datalistevents
     */
    public EventsByMonthAdapter(Context context, GetUpComingEvents datalistevents) {
        this.dataList = dataList;
        this.context = context;
        this.datalistevents = datalistevents;

    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public EventsByMonthAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_adapter, parent, false);
        return new EventsByMonthAdapter.ViewHolder(view);
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(EventsByMonthAdapter.ViewHolder holder, int position) {

        holder.churchNameText.setText((CommonUtil.fromHtml("<b><font color=\"#ffffff\">" + "Church Name: " + "</font><b>" + datalistevents.getListResult().get(position).getChurchName())));
        holder.eventText.setText(CommonUtil.fromHtml("<b><font color=\"#ffffff\">" + "Event Name: " + "</font><b>" + datalistevents.getListResult().get(position).getTitle()));
        holder.numberText.setText((CommonUtil.fromHtml("<b><font color=\"#ffffff\">" + "Mobile Number: " + "</font><b>" + datalistevents.getListResult().get(position).getContactNumber())));
        String from = "<b><font color='#ffffff'>" + "Event From: " + " </font>  <b><font color='#ffffff'>" + CommonUtil.dateFormat(datalistevents.getListResult().get(position).getStartDate()) + "</font>";
        String to = "<b><font color='#ffffff'>" + " To: " + " </font>  <b><font color='#ffffff'>" + CommonUtil.dateFormat(datalistevents.getListResult().get(position).getEndDate()) + "</font>";

        holder.openTimeText.setText(Html.fromHtml(from + " " + " " + to));

    }

    // items count
    @Override
    public int getItemCount() {
        return datalistevents.getListResult().size();
    }

    /*
     * initialising viewholder to recycler view
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView churchNameText, eventText, numberText, openTimeText;

        public ViewHolder(View itemView) {
            super(itemView);
            churchNameText = itemView.findViewById(R.id.churchNameText);
            eventText = itemView.findViewById(R.id.eventText);
            numberText = itemView.findViewById(R.id.numberText);
            openTimeText = itemView.findViewById(R.id.openTimeText);
        }
    }
}

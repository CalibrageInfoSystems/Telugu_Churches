package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.calibrage.teluguchurches.R;

/**
 * This adapter is helpfull to show bible chapters name
 */

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.ViewHolder> {
    Context context;
    private OnCartChangedListener onCartChangedListener;
    ArrayList<String> chapterscount = new ArrayList<>();
    ArrayList<String> chapterList = new ArrayList<>();

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *  @param context
     * @param chapterList
     * @param chapterscount
     */

    public ChapterListAdapter(Context context, ArrayList<String> chapterList, ArrayList<String> chapterscount) {
        this.context = context;
        this.chapterList = chapterList;
        this.chapterscount = chapterscount;
    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public ChapterListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bible_study_adapter, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(ChapterListAdapter.ViewHolder holder, final int position) {
        holder.chapterListText.setText(chapterList.get(position));
        holder.chapterCountText.setText(chapterscount.get(position));
        holder.mainrlty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCartChangedListener.setCartClickListener(position, "click");
            }
        });


    }

    // items count
    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    /*
     * initialising viewholder to recycler view
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView chapterListText, chapterCountText;
        RelativeLayout mainrlty;

        public ViewHolder(View itemView) {
            super(itemView);
            chapterListText = itemView.findViewById(R.id.chapterListText);
            chapterCountText =  itemView.findViewById(R.id.chapterCountText);
            mainrlty = itemView.findViewById(R.id.mainrlty);
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
        void setCartClickListener(int position, String status);
    }

}
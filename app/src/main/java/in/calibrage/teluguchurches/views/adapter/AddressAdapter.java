package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.views.model.AddressResponseModel;

// This adapter is helpfull to show Address  in Address view


public class AddressAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<T> myDataSet;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount, subscibeval, churchId, authorId;
    private boolean loading;
    View v;
    private OnLoadMoreListener onLoadMoreListener;
    ArrayList<AddressResponseModel.ListResult> addressResponse;
    private OnCartChangedListener onCartChangedListener;
    private RadioButton lastCheckedRB = null;
    public static int mSelectedItem = -1;


    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *  @param context
     * @param myDataSet
     */

    public AddressAdapter(Context context, List<T> myDataSet, RecyclerView recyclerView) {
        this.myDataSet = myDataSet;
        this.context = context;

        addressResponse = (ArrayList<AddressResponseModel.ListResult>) myDataSet;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }

    }

    /*
    * creating viewholder to recycler view here
    * */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_address, parent, false);
            vh = new TextViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder).text_cusName.setText(addressResponse.get(position).getFullName());
            ((TextViewHolder) holder).text_address.setText(addressResponse.get(position).getAddressLine1() + ", " + addressResponse.get(position).getAddressLine2());
            ((TextViewHolder) holder).text_phoneNumber.setText(addressResponse.get(position).getMobileNumber());
            ((TextViewHolder) holder).stateText.setText(addressResponse.get(position).getStateName() + ", " + addressResponse.get(position).getCountryName());
            ((TextViewHolder) holder).pincodeText.setText(String.valueOf(addressResponse.get(position).getPinCode()));

            ((TextViewHolder) holder).radio_button_select.setChecked(mSelectedItem == position);

            ((TextViewHolder) holder).convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSelectedItem = position;
                    notifyDataSetChanged();
                    onCartChangedListener.setCartClickListener("itemView", position);
                }
            });
            ((TextViewHolder) holder).radio_button_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSelectedItem = position;
                    notifyDataSetChanged();
                    onCartChangedListener.setCartClickListener("itemView", position);
                }
            });
            ((TextViewHolder) holder).imageView_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCartChangedListener.setCartClickListener("edit", position);
                }
            });
            ((TextViewHolder) holder).imageView_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCartChangedListener.setCartClickListener("delete", position);
                }
            });

        }
    }

    //interface for view more functionality
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    @Override
    public int getItemViewType(int position) {
        return myDataSet.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    // items count
    @Override
    public int getItemCount() {
        return this.myDataSet.size();
    }


    /*
     * initialising viewholder to recycler view
     * */
    public static class TextViewHolder extends RecyclerView.ViewHolder {
        public TextView text_cusName, text_address, text_phoneNumber, stateText, pincodeText;
        private View convertView;
        private RadioButton radio_button_select;
        private ImageView imageView_edit, imageView_delete;


        public TextViewHolder(View v) {
            super(v);
            convertView = v;
            text_cusName =  v.findViewById(R.id.text_cusName);
            pincodeText =  v.findViewById(R.id.pincodeText);
            stateText =  v.findViewById(R.id.stateText);
            text_address =  v.findViewById(R.id.text_address);
            text_phoneNumber =  v.findViewById(R.id.text_phoneNumber);
            radio_button_select = v.findViewById(R.id.radio_button_select);
            imageView_edit =  v.findViewById(R.id.imageView_edit);
            imageView_delete = v.findViewById(R.id.imageView_delete);


        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

    /**
     * Container Activity must implement this interface
     * you can define any parameter as per your requirement
     */
    public interface OnCartChangedListener {
        void setCartClickListener(String status, int position);

    }

    /**
     * @param onCartChangedListener
     */
    public void setOnCartChangedListener(OnCartChangedListener onCartChangedListener) {
        this.onCartChangedListener = onCartChangedListener;
    }
}

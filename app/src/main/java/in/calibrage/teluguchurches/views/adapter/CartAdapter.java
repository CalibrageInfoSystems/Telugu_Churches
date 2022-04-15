package in.calibrage.teluguchurches.views.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.views.model.CartResponseModel;
import in.calibrage.teluguchurches.views.model.UpdateQuantityResponseModel;

/**
 * This adapter is helpfull to show the cart list in cart screen
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private CartResponseModel dataList;
    private String cart;
    private UpdateQuantityResponseModel mResponse;
    private OnChangedListener onChangedListener;
    private int currentSelectedPosition = RecyclerView.NO_POSITION;
    private int quantityPrice;
    private Double price;
    private double result;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *  @param context
     * @param dataList
     * @param cart
     */

    public CartAdapter(Context context, CartResponseModel dataList, String cart) {
        this.context = context;
        this.dataList = dataList;
        this.cart = cart;

    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_shopping, null);
        CartAdapter.ViewHolder viewHolder = new CartAdapter.ViewHolder(view);
        return viewHolder;
    }

    /*
     * binding data to viewholder from API here
     * */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bookText.setText(dataList.getListResult().get(position).getItemName());
        holder.priceText.setText(CommonUtil.fromHtml("₹ " + dataList.getListResult().get(position).getPrice()));
        holder.authorText.setText(dataList.getListResult().get(position).getAuthor());
        if (dataList.getListResult().get(position).getQuantity() != null) {
            holder.quantityValue.setText(Integer.toString(dataList.getListResult().get(position).getQuantity()));
        }
        holder.quantityValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.quantityValue.setVisibility(View.GONE);
                holder.quantityText.setVisibility(View.VISIBLE);
                holder.quantityText.setText(Integer.toString(dataList.getListResult().get(position).getQuantity()));
            }
        });
        Glide.with(context).load(dataList.getListResult().get(position).getItemImage())
                .fitCenter()
                .error(R.drawable.loginimage)
                .into(holder.itemImg);
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangedListener.setClickListener("delete", position, " ", dataList.getListResult().get(position).getItemId());
            }
        });

        try {
            quantityPrice = Integer.parseInt("" + holder.quantityValue.getText());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        price = dataList.getListResult().get(position).getPrice();

        result = (quantityPrice * price);
        holder.totalpriceText.setText("₹ " + result + " ");

        // added text changed listner to maintain the update button
        holder.quantityText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!holder.quantityText.getText().toString().equals("") || holder.quantityText.getText().toString() != null) {
                    currentSelectedPosition = position;
                    if (currentSelectedPosition == position) {
                        holder.updateQuantityBtn.setVisibility(View.VISIBLE);
                        holder.updateQuantityBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onChangedListener.setClickListener("updatequantity", position,
                                        holder.quantityText.getText().toString(), dataList.getListResult().get(position).getItemId());

                            }
                        });

                    } else {
                        holder.updateQuantityBtn.setVisibility(View.GONE);
                    }
                } else {
                    holder.updateQuantityBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String x = "";
                x = s.toString();
                if (x.startsWith("0")) {
                    holder.quantityText.setText("");

                } else if (x.startsWith(".")) {
                    holder.quantityText.setText("");
                }

                try {
                    quantityPrice = Integer.parseInt("" + holder.quantityText.getText());
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }

                price = dataList.getListResult().get(position).getPrice();
                result = (quantityPrice * price);
                holder.totalpriceText.setText("₹ " + result + " ");

                if (holder.quantityText.getText().toString().equals("")) {
                    holder.updateQuantityBtn.setVisibility(View.GONE);
                    holder.totalpriceText.setText("₹ " + "0");
                } else {
                    currentSelectedPosition = position;
                    if (currentSelectedPosition == position || holder.quantityText.getText().toString().equals("")) {
                        holder.updateQuantityBtn.setVisibility(View.VISIBLE);
                        holder.updateQuantityBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onChangedListener.setClickListener("updatequantity", position, holder.quantityText.getText().toString(), dataList.getListResult().get(position).getItemId());

                            }
                        });

                    }
                }

            }
        });
        holder.quantityText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (holder.quantityText.getText().toString().equals("")) {
                        holder.updateQuantityBtn.setVisibility(View.GONE);
                        holder.totalpriceText.setText("₹ " + "0");
                    } else {
                        currentSelectedPosition = position;
                        if (currentSelectedPosition == position) {
                            holder.updateQuantityBtn.setVisibility(View.VISIBLE);
                            holder.updateQuantityBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onChangedListener.setClickListener("updatequantity", position, holder.quantityText.getText().toString(), dataList.getListResult().get(position).getItemId());
                                }
                            });

                        }
                    }
                }
                return false;
            }
        });
    }

    // items count
    @Override
    public int getItemCount() {
        return dataList.getListResult().size();
    }

    /*
     * initialising viewholder to recycler view
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView deleteImg, itemImg;
        private RelativeLayout mainLay;
        private EditText quantityText;
        private TextView priceText, bookText, authorText, totalpriceText, totalprice, quantityValue;
        private Button updateQuantityBtn;


        public ViewHolder(View v) {
            super(v);
            mainLay = v.findViewById(R.id.mainLay);
            quantityText = v.findViewById(R.id.quantityText);
            priceText = v.findViewById(R.id.priceText);
            bookText = v.findViewById(R.id.bookText);
            authorText = v.findViewById(R.id.authorText);
            totalpriceText = v.findViewById(R.id.totalpriceText);
            quantityValue = v.findViewById(R.id.quantityValue);
            totalprice = v.findViewById(R.id.totalprice);
            itemImg = v.findViewById(R.id.itemImg);
            deleteImg = v.findViewById(R.id.deleteImg);
            updateQuantityBtn = v.findViewById(R.id.updateQuantityBtn);
            deleteImg.setVisibility(View.VISIBLE);
            updateQuantityBtn.setVisibility(View.GONE);
            quantityValue.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param onChangedListener
     */
    public void setOnChangedListener(OnChangedListener onChangedListener) {
        this.onChangedListener = onChangedListener;


    }

    /**
     * Container Activity must implement this interface
     * you can define any parameter as per your requirement
     */
    public interface OnChangedListener {
        void setClickListener(String status, int position, String quantity, int itemId);
    }

}

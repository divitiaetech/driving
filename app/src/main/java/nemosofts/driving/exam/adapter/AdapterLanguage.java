package nemosofts.driving.exam.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.item.ItemLanguage;


public class AdapterLanguage extends RecyclerView.Adapter {

    private final Context mContext;
    private final RecyclerItemClickListener listener;
    private final ArrayList<ItemLanguage> arrayList;
    private final int VIEW_ITEM = 1;
    private int pos = -1;

    public AdapterLanguage(Context context, ArrayList<ItemLanguage> arrayList, RecyclerItemClickListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
        mContext = context;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cat_text;
        public RelativeLayout rl_lan_bg;
        MyViewHolder(View view) {
            super(view);
            cat_text = itemView.findViewById(R.id.cat_text);
            rl_lan_bg = itemView.findViewById(R.id.rl_lan_bg);
        }

        public void bind(final ItemLanguage item, final RecyclerItemClickListener listener){
            itemView.setOnClickListener(v -> listener.onClickListener(item, getLayoutPosition()));
        }
    }

    @SuppressLint("StaticFieldLeak")
    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private static ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false);
            return new MyViewHolder(itemView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progressbar, parent, false);
            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            final ItemLanguage listltem = arrayList.get(position);

            ((MyViewHolder) holder).cat_text.setText(listltem.getLanguageName());


            if (pos == position){
                ((MyViewHolder) holder).cat_text.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                ((MyViewHolder) holder).rl_lan_bg.setBackgroundResource(R.drawable.card_lan_select);
            }else {
                if (Setting.Dark_Mode){
                    ((MyViewHolder) holder).cat_text.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                }else {
                    ((MyViewHolder) holder).cat_text.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                }
                ((MyViewHolder) holder).rl_lan_bg.setBackgroundResource(R.drawable.card_lan);
            }

            ((MyViewHolder) holder).bind(listltem, listener);

        } else {
            if (getItemCount() == 1) {
                ProgressViewHolder.progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public long getItemId(int id) {
        return id;
    }


    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }

    public boolean isHeader(int position) {
        return position == arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_PROG = 0;
        return isHeader(position) ? VIEW_PROG : VIEW_ITEM;
    }

    public interface RecyclerItemClickListener{
        void onClickListener(ItemLanguage item, int position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void select(int position) {
        pos = position;
        notifyDataSetChanged();
    }
}
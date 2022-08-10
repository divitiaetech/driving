package nemosofts.driving.exam.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.item.ItemCat;

public class AdapterCategory extends RecyclerView.Adapter {

    private final RecyclerItemClickListener listener;
    private final ArrayList<ItemCat> arrayList;
    private final int VIEW_ITEM = 1;

    public AdapterCategory(ArrayList<ItemCat> arrayList, RecyclerItemClickListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cat_text;
        public ImageView cat_image;

        MyViewHolder(View view) {
            super(view);
            cat_text = itemView.findViewById(R.id.cat_text);
            cat_image = itemView.findViewById(R.id.item_img);
        }

        public void bind(final ItemCat itemCat, final RecyclerItemClickListener listener){
            itemView.setOnClickListener(v -> listener.onClickListener(itemCat, getLayoutPosition()));
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        @SuppressLint("StaticFieldLeak")
        private static ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
            return new MyViewHolder(itemView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progressbar, parent, false);
            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            final ItemCat listltem = arrayList.get(position);

            ((MyViewHolder) holder).cat_text.setText(listltem.getCategory_name());

            if (Setting.Dark_Mode){
                Picasso.get()
                        .load(arrayList.get(position).getCategory_image())
                        .placeholder(R.drawable.placeholder_night)
                        .into(((MyViewHolder) holder).cat_image);
            }else {
                Picasso.get()
                        .load(arrayList.get(position).getCategory_image())
                        .placeholder(R.drawable.placeholder_light)
                        .into(((MyViewHolder) holder).cat_image);
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

    public void hideHeader() {
        try {
            ProgressViewHolder.progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface RecyclerItemClickListener{
        void onClickListener(ItemCat itemCat, int position);
    }
}
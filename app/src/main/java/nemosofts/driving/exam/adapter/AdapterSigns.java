package nemosofts.driving.exam.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.item.ItemSigns;

public class AdapterSigns extends RecyclerView.Adapter {

    private final ArrayList<ItemSigns> arrayList;
    private final int columnWidth;
    private final int VIEW_ITEM = 1;

    public AdapterSigns(Context context, ArrayList<ItemSigns> arrayList) {
        this.arrayList = arrayList;
        columnWidth = getColumnWidth(2, 5,context);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;

        MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.iv_signs);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
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
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_signs, parent, false);
            return new MyViewHolder(itemView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progressbar, parent, false);
            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).imageView.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth, columnWidth));

            if (Setting.Dark_Mode){
                Picasso.get()
                        .load(arrayList.get(position).getSignsImage())
                        .placeholder(R.drawable.placeholder_night)
                        .into(((MyViewHolder) holder).imageView);
            }else {
                Picasso.get()
                        .load(arrayList.get(position).getSignsImage())
                        .placeholder(R.drawable.placeholder_light)
                        .into(((MyViewHolder) holder).imageView);
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


    public void hideHeader() {
        try {
            ProgressViewHolder.progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isHeader(int position) {
        return position == arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_PROG = 0;
        return isHeader(position) ? VIEW_PROG : VIEW_ITEM;
    }


    public int getColumnWidth(int column, int grid_padding, Context context) {
        Resources r = context.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, grid_padding, r.getDisplayMetrics());
        return (int) ((getScreenWidth(context) - ((column + 1) * padding)) / column);
    }

    public int getScreenWidth(Context context) {
        int columnWidth;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        final Point point = new Point();
        point.x = display.getWidth();
        point.y = display.getHeight();
        columnWidth = point.x;
        return columnWidth;
    }

}
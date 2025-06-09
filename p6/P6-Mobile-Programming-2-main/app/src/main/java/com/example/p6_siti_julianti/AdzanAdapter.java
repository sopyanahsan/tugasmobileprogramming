package com.example.p6_siti_julianti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdzanAdapter extends RecyclerView.Adapter<AdzanAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(AdzanModel adzan);
    }

    private List<AdzanModel> adzanList;
    private Context context;
    private OnItemClickListener listener;

    public AdzanAdapter(List<AdzanModel> adzanList, Context context, OnItemClickListener listener) {
        this.adzanList = adzanList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdzanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adzan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdzanAdapter.ViewHolder holder, int position) {
        AdzanModel adzan = adzanList.get(position);
        holder.tvTitle.setText(adzan.getCountry());

        // Cari resource drawable bendera berdasarkan nama flag di AdzanModel
        int flagResId = context.getResources().getIdentifier(adzan.getFlag(), "drawable", context.getPackageName());
        if (flagResId != 0) {
            holder.imgFlag.setImageResource(flagResId);
        } else {
            holder.imgFlag.setImageResource(android.R.color.transparent); // atau gambar default
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(adzan));
    }

    @Override
    public int getItemCount() {
        return adzanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFlag;
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFlag = itemView.findViewById(R.id.imgFlag);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}


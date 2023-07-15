package com.creapptors.funolympic.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creapptors.funolympic.R;
import com.creapptors.funolympic.main_more.FunOlympics;
import com.creapptors.funolympic.model.funOlympics;

import java.util.List;

public class FunOlympicAdapter extends RecyclerView.Adapter<FunOlympicAdapter.FunOlympicViewHolder> {
    private List<funOlympics> funOlympicList;
    private Context context;

    public FunOlympicAdapter(List<funOlympics> funOlympicList, Context context) {
        this.funOlympicList = funOlympicList;
        this.context = context;
    }

    @NonNull
    @Override
    public FunOlympicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.funolympic_lister, parent, false);
        return new FunOlympicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FunOlympicViewHolder holder, int position) {
        funOlympics funOlympics = funOlympicList.get(position);

        holder.textViewTitle.setText(funOlympics.getTitle());
        holder.textViewDate.setText(funOlympics.getFo_date());
    }

    @Override
    public int getItemCount() {
        return funOlympicList.size();
    }

    static class FunOlympicViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDate;

        FunOlympicViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.list_fo_name);
            textViewDate = itemView.findViewById(R.id.list_fon_date);
        }
    }
}

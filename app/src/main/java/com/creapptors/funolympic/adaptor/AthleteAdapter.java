package com.creapptors.funolympic.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creapptors.funolympic.R;
import com.creapptors.funolympic.main_more.Athletes;
import com.creapptors.funolympic.model.Athlete;

import java.util.List;

public class AthleteAdapter extends RecyclerView.Adapter<AthleteAdapter.AthleteViewHolder> {
    private List<Athlete> athleteList;
    private Context context;

    public AthleteAdapter(List<Athlete> athleteList, Context context) {
        this.athleteList = athleteList;
        this.context = context;
    }

    @NonNull
    @Override
    public AthleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.athlete_view, parent, false);
        return new AthleteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AthleteViewHolder holder, int position) {
        Athlete athlete = athleteList.get(position);

        holder.textViewUsername.setText(athlete.getUsername());
        holder.textViewSport.setText(athlete.getEmail());
    }

    @Override
    public int getItemCount() {
        return athleteList.size();
    }

    static class AthleteViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsername, textViewSport;

        AthleteViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUsername = itemView.findViewById(R.id.Atl_Atl_name);
            textViewSport = itemView.findViewById(R.id.Atl_Atl_sport);
        }
    }
}

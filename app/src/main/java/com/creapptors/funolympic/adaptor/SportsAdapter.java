package com.creapptors.funolympic.adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creapptors.funolympic.R;
import com.creapptors.funolympic.model.Sport;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.SportViewHolder> {
    private List<Sport> sportList;
    private Context context;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public SportsAdapter(List<Sport> sportList, Context context) {
        this.sportList = sportList;
        this.context = context;
    }

    @NonNull
    @Override
    public SportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sports_lister, parent, false);
        return new SportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SportViewHolder holder, int position) {
        Sport sport = sportList.get(position);
        holder.textViewTitle.setText(sport.getTitle());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Handle long press sport here
                // You can show a confirmation dialog and delete the sport if confirmed
                showDeleteConfirmationDialog(sport);
                return true;
            }

            private void showDeleteConfirmationDialog(Sport sid) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete sport");
                builder.setMessage("Are you sure you want to delete this sport?");

                // Add positive button
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the sport here
                        deleteSport(sport.getSportId());
                    }

                    private void deleteSport(String sid) {
                        firebaseFirestore.collection("Sport").whereEqualTo("sportId", sid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Delete the document
                                        document.getReference().delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // Document deleted successfully
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Failed to delete document
                                                    }
                                                });
                                    }
                                } else {
                                    // Error occurred while fetching documents
                                }
                            }
                        });
                    }

                });

                // Add negative button
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel the deletion
                        dialog.dismiss();
                    }
                });

                // Create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        });
    }

    @Override
    public int getItemCount() {
        return sportList.size();
    }

    static class SportViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewId;

        SportViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.list_sport_name);
        }
    }
}

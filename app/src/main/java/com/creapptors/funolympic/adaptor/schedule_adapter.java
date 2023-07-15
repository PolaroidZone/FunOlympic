package com.creapptors.funolympic.adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creapptors.funolympic.R;
import com.creapptors.funolympic.admin_functions.AddEvent;
import com.creapptors.funolympic.model.Event;
import com.creapptors.funolympic.model.Video;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ConcurrentModificationException;
import java.util.List;

public class schedule_adapter extends RecyclerView.Adapter<schedule_adapter.ViewHolder> {

    private Context context;
    private List<Event> eventList;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public schedule_adapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public schedule_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull schedule_adapter.ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.title.setText(event.getTitle());
        holder.date.setText(event.getDate());
        //add a long press to delete function
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Handle long press event here
                // You can show a confirmation dialog and delete the event if confirmed
                showDeleteConfirmationDialog(event);
                return true;
            }

            private void showDeleteConfirmationDialog(Event event) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Event");
                builder.setMessage("Are you sure you want to delete this event?");

                // Add positive button
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the event here
                        deleteEvent(event.getEid());
                    }

                    private void deleteEvent(String eid) {
                        firebaseFirestore.collection("Event").whereEqualTo("eid", eid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Evt_sc_Title);
            date = itemView.findViewById(R.id.Atl_sc_name);
        }
    }
}

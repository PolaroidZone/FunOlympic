package com.creapptors.funolympic.adaptor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creapptors.funolympic.R;
import com.creapptors.funolympic.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private List<User> userList;
    private Context context;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public UsersAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.username.setText(user.getUsername());
        holder.email.setText(user.getEmail());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showUserOptionsMenu(user);
                return true;
            }

            private void showUserOptionsMenu(User user) {
                PopupMenu popupMenu = new PopupMenu(context, holder.itemView);
                popupMenu.getMenuInflater().inflate(R.menu.user_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_reset_password:
                                // Handle reset password option
                                resetPassword(user);
                                return true;
                            case R.id.menu_delete_user:
                                // Handle delete user option
                                showDeleteConfirmationDialog(user);
                                return true;
                            default:
                                return false;
                        }
                    }

                    private void showDeleteConfirmationDialog(User user) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Delete Event");
                        builder.setMessage("Are you sure you want to delete this event?");
                        // Add positive button
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete the event here
                                deleteAccount(user.getUid());
                            }

                            private void deleteAccount(String uid) {
                                ProgressDialog dialog = new ProgressDialog(context);
                                dialog.setTitle("Deleting user account");
                                dialog.setMessage("please wait patiently...");
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                                firebaseFirestore.collection("user").whereEqualTo("uid", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                                                dialog.dismiss();
                                                                Toast.makeText(context, "Go to the firebase console to finish deleting this account", Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                // Failed to delete document
                                                                dialog.dismiss();
                                                                Toast.makeText(context, "Failed to delete account: "+2, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        } else {
                                            dialog.dismiss();
                                            Toast.makeText(context, "Failed to delete Account", Toast.LENGTH_SHORT).show();
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

                    private void resetPassword(User user) {
                        // Implement the logic to reset the password for the user
                        String email = user.getEmail();
                        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Password reset email sent successfully
                                            Toast.makeText(context, "Password reset email sent to " + email, Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Failed to send password reset email
                                            Toast.makeText(context, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                });

                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView username, email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.usernameItem);
            email = itemView.findViewById(R.id.userEmailItem);
        }
    }
}

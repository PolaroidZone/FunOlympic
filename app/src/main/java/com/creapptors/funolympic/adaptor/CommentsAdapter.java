package com.creapptors.funolympic.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creapptors.funolympic.R;
import com.creapptors.funolympic.model.Comment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<Comment> commentList;


    public CommentsAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.bind(comment);
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an option menu that shows a delete option
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.comment_menu, popupMenu.getMenu());

                // Set click listener for menu items
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_delete:
                                // Handle the delete option
                                deleteComment(comment);
                                return true;
                            default:
                                return false;
                        }
                    }

                    private void deleteComment(Comment comment) {
                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        firebaseFirestore.collection("Comment")
                                .document(comment.getCid())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Comment deleted successfully
                                        Toast.makeText(view.getContext(), "Comment deleted", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Failed to delete comment
                                        Toast.makeText(view.getContext(), "Failed to delete comment", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                // Show the popup menu
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView commentText, user;
        ImageView more;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.comment_item_msg);
            user = itemView.findViewById(R.id.commenter);
            more = itemView.findViewById(R.id.imageViewComOptions);
        }

        public void bind(Comment comment) {
            commentText.setText(comment.getDetails());
            user.setText(comment.getUid());
        }
    }
}

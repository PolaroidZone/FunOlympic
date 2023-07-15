package com.creapptors.funolympic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.creapptors.funolympic.adaptor.CommentsAdapter;
import com.creapptors.funolympic.model.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BroadCast extends AppCompatActivity {

    ImageView optionsView;
    VideoView videoView;
    ImageButton imageButton;
    TextView vTitle, vDesc;
    EditText wComment;
    RecyclerView recyclerView;

    PopupMenu popupMenu;
    MenuInflater inflater;

    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_cast);

        Objects.requireNonNull(getSupportActionBar()).hide();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        optionsView = findViewById(R.id.imageViewBrOptions);
        videoView = findViewById(R.id.videoViewBroadCast);
        imageButton = findViewById(R.id.imageButtonComB);
        vTitle = findViewById(R.id.videoTitleB);
        vDesc = findViewById(R.id.videoDescB);
        wComment = findViewById(R.id.editTextTextComB);
        recyclerView = findViewById(R.id.commentRecyclerView);

        String video = getIntent().getStringExtra("vid");
        String event = getIntent().getStringExtra("eid");
        String title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("desc");

        loadVideoToPlayer(video, event, title, desc);

        imageButton.setOnClickListener(view -> {
            if(wComment.getText().toString().trim() == ""){
            } else {
                Timestamp timestamp = Timestamp.now();
                String time = String.valueOf(timestamp);
                searchUsername(firebaseAuth.getUid());
                //String details = wComment.getText().toString().trim();
                //setComment(uid, event, details, time);
                //wComment.setText("");
            }
        });

        optionsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Comment> commentList = new ArrayList<>();
        CommentsAdapter commentsAdapter = new CommentsAdapter(commentList);
        recyclerView.setAdapter(commentsAdapter);
        firebaseFirestore.collection("Comment")
                .whereEqualTo("eid", event)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Comment comment = document.toObject(Comment.class);
                                commentList.add(comment);
                            }
                            commentsAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(BroadCast.this, "Failed to retrieve comments", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        dialog = new ProgressDialog(this);
        dialog.setTitle("Deleting event");
        dialog.setMessage("please wait patiently...");
        dialog.setCanceledOnTouchOutside(false);

        optionsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu = new PopupMenu(BroadCast.this, view);
                inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.comment_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_delete:
                                deleteEvent(event);
                                dialog.show();
                                return true;
                            default:
                                return false;
                        }
                    }

                    private void deleteEvent(String eid) {
                        firebaseFirestore.collection("Event").whereEqualTo("eid", eid).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                document.getReference().delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                // Event deleted successfully
                                                                dialog.dismiss();
                                                                deleteVideo(eid);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                // Error deleting event
                                                                dialog.dismiss();
                                                                Toast.makeText(BroadCast.this, "Event delete error 1 with code: "+e, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        } else {
                                            // Error getting documents
                                            Toast.makeText(BroadCast.this, "Event delete error 1", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        finish();
                                    }
                                });
                    }


                    private void deleteVideo(String eid) {
                        dialog.setTitle("Deleting event step 2");
                        dialog.setMessage("please wait patiently almost there...");
                        dialog.show();
                        firebaseFirestore.collection("Video").whereEqualTo("eid", eid).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                document.getReference().delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                // Event deleted successfully
                                                                dialog.dismiss();
                                                                deleteComment(eid);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                // Error deleting event
                                                                dialog.dismiss();
                                                                Toast.makeText(BroadCast.this, "Video delete error with code: "+e, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        } else {
                                            // Error getting documents
                                            dialog.dismiss();
                                            Toast.makeText(BroadCast.this, "Video delete error 1", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                    private void deleteComment(String event) {
                        dialog.setTitle("Deleting event, last step");
                        dialog.setMessage("please wait patiently...");
                        dialog.show();
                        firebaseFirestore.collection("Comment").whereEqualTo("eid", event).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            boolean commentExists = !Objects.requireNonNull(task.getResult()).isEmpty();

                                            if (commentExists){
                                                dialog.dismiss();
                                                finish();
                                            }else {
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    document.getReference().delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    // Event deleted successfully
                                                                    dialog.dismiss();
                                                                    finish();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    // Error deleting event
                                                                    dialog.dismiss();
                                                                    finish();
                                                                    Toast.makeText(BroadCast.this, "Comment delete error 1 with code:" +e, Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            }

                                        } else {
                                            // Error getting documents
                                            dialog.dismiss();
                                            finish();
                                            Toast.makeText(BroadCast.this, "Comment delete error 1", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                    }
                });
                popupMenu.show();
            }
        });

    }

    private void searchUsername(String uid) {
        //return the username from firestore
        DocumentReference documentRef = firebaseFirestore.collection("admin").document(uid);

        // Retrieve the username from the document
        documentRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String username = document.getString("username");
                    String details = wComment.getText().toString().trim();
                    Timestamp timestamp = Timestamp.now();
                    String time = String.valueOf(timestamp);
                    String event = getIntent().getStringExtra("eid");
                    // Handle the retrieved username
                    setComment(username, event, details, time);
                    wComment.setText("");
                    // You can update a variable or perform further operations here
                    // Return the username or use it as needed
                } else {
                    Toast.makeText(this, "failed1", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "failed2", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadVideoToPlayer(String video, String event, String title, String desc) {
        vTitle.setText(title);
        vDesc.setText(desc);
        //add code that loads the video link to the video player
        //add video controls
        // Set up media controller
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Set the video URI and start playback
        Uri videoUri = Uri.parse(video);
        videoView.setVideoURI(videoUri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });
    }

    private void setComment(String uid, String eid, String details, String timeStamp){
        String cid = uid+timeStamp;
        Comment comment = new Comment(cid, uid, eid, details);
        firebaseFirestore.collection("Comment")
                .document(cid)
                .set(comment)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                        }else {
                            Toast.makeText(BroadCast.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BroadCast.this, "Failed with error code: "+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
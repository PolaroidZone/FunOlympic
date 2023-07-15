package com.creapptors.funolympic.admin_functions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.creapptors.funolympic.R;
import com.creapptors.funolympic.model.Event;
import com.creapptors.funolympic.model.Video;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AddEvent extends AppCompatActivity {

    TextView videoName, videoUr;
    VideoView videoView;
    Button datePicker, submit;
    EditText eventTitle, eventDesc, eventDate;
    Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    ImageView back;
    ImageButton upload;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    Calendar calendar;
    ProgressDialog progressDialog;

    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_PICK_VIDEO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Objects.requireNonNull(getSupportActionBar()).hide();
        
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        calendar = Calendar.getInstance();
        progressDialog = new ProgressDialog(this);
        
        spinner = findViewById(R.id.spinner);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        loadSports();

        videoUr = findViewById(R.id.videoUri);
        videoName = findViewById(R.id.videoName);
        videoView = findViewById(R.id.videoViewEventUpload);
        eventTitle = findViewById(R.id.set_event_title);
        eventDesc = findViewById(R.id.set_event_desc);
        eventDate = findViewById(R.id.EditEventDate);
        eventDate.setClickable(false);
        eventDate.setFocusable(false);

        upload = findViewById(R.id.add_event_video);
        upload.setOnClickListener(view ->{
            checkStoragePermissions();
        });
        datePicker = findViewById(R.id.setEventDate);
        datePicker.setOnClickListener(view -> {
            setDate();
        });
        submit = findViewById(R.id.new_event_submit);
        submit.setOnClickListener(view -> {
            String title = eventTitle.getText().toString().trim();
            String desc = eventDesc.getText().toString().trim();
            String date = eventDate.getText().toString().trim();
            String sport = spinner.getSelectedItem().toString().trim();
            scheduleEvent(title, desc, date, sport);
            checkSchedule();
        });
        back = findViewById(R.id.new_event_back);
        back.setOnClickListener(view -> {
            String name = videoName.getText().toString();
            String url = videoUr.getText().toString();
            deleteVideoAndRecord(name, url);
            finish();
        });
    }

    private void loadSports() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Getting ready");
        dialog.setMessage("please wait patiently...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        CollectionReference sportCollection = firebaseFirestore.collection("Sport");
        sportCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String sportName = document.getString("title");
                        if (sportName != null) {
                            spinnerAdapter.add(sportName);
                            dialog.dismiss();
                        }
                    }
                }
            } else {
                dialog.dismiss();
                Toast.makeText(this, "Error loading the spinner", Toast.LENGTH_SHORT).show();
                // Handle error
            }
        });
    }



    // Check for the required permission(s) before accessing local storage
    private void checkStoragePermissions() {
        // Check if the permission is already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        } else {
            // Permission is already granted, proceed with accessing local storage
            // Call the method to get the selected video from local storage
            getSelectedVideo();
        }
    }

    // Handle the permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, proceed with accessing local storage
                // Call the method to get the selected video from local storage
                getSelectedVideo();
            } else {
                // Permission is denied, handle it appropriately (e.g., show an error message)
                Toast.makeText(this, "Permission denied. Cannot access local storage.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getSelectedVideo() {
        // Create an intent to pick a video file
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(intent, REQUEST_PICK_VIDEO);
//        // Check if there is a suitable activity to handle the intent
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            // Start the intent to pick a video file
//
//        } else {
//            // No suitable activity found, show an error message
//            Toast.makeText(this, "No video picker app found.", Toast.LENGTH_SHORT).show();
//        }
    }

    // Handle the result of the video file selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_VIDEO && resultCode == RESULT_OK) {
            if (data != null) {
                // Get the selected video URI
                Uri selectedVideoUri = data.getData();
                if (selectedVideoUri != null) {
                    // Do something with the selected video URI
                    // For example, display the video in the VideoView
                    videoView.setVideoURI(selectedVideoUri);
                    videoView.start();
                    String vidName = getVideoNameFromUri(selectedVideoUri);
                    //uploadBroadcast(vidName, selectedVideoUri);

                    // Generate thumbnail image
                    Bitmap thumbnail = null;
                    try {
                        thumbnail = generateThumbnail(selectedVideoUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (thumbnail != null) {
                        // Upload the video and thumbnail
                        uploadBroadcast(vidName, selectedVideoUri, thumbnail);
                    }
                }
            }
        }
    }

    private Bitmap generateThumbnail(Uri videoUri) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(getApplicationContext(), videoUri);
            // Extract the first frame as the thumbnail
            Bitmap thumbnail = retriever.getFrameAtTime();
            // Resize the thumbnail if needed
            // thumbnail = resizeBitmap(thumbnail, width, height);
            return thumbnail;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return null;
    }

    private String getVideoNameFromUri(Uri videoUri) {
        String videoName = "";
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Video.Media.DISPLAY_NAME};
            cursor = getContentResolver().query(videoUri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                videoName = cursor.getString(columnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return videoName;
    }

    private void uploadBroadcast(String fileName, Uri selectedVideoUri, Bitmap thumbnail) {
        // Get the selected video from local storage
        progressDialog.setTitle("Uploading video");
        progressDialog.setMessage("Your image is uploading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        // Create a Firebase Storage reference for the video
        StorageReference storageRef = firebaseStorage.getReference().child("Videos").child(fileName);

        // Upload the video to Firebase Storage
        UploadTask uploadTask = storageRef.putFile(selectedVideoUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Get the download URL of the uploaded video
            Task<Uri> downloadUrlTask = taskSnapshot.getStorage().getDownloadUrl();
            downloadUrlTask.addOnSuccessListener(uri -> {
                String videoUrl = uri.toString(); // Get the video URL
                // Create a video object with the necessary data
                Video video = new Video(videoUrl, "null", fileName, "null");
                //video.setVideoUrl(videoUrl);
                // Store the video object in the firestore "Video" collection
                firebaseFirestore.collection("Video")
                        .document(fileName)
                        .set(video)
                        .addOnSuccessListener(aVoid -> {
                            // Video uploaded and saved successfully
                            // Update the videoName TextView with the video ID or URL
                            progressDialog.dismiss();
                            videoName.setText(fileName);
                            videoUr.setText(videoUrl);
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(this, "Error creating record", Toast.LENGTH_SHORT).show();
                        });
            });
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(this, "Error creating video", Toast.LENGTH_SHORT).show();
        });
    }

    private void setDate() {
        //ignore this method
        //picks the date and coverts it to a string value named Date
        //Date is then set the ed2 EditText to be displayed
        // Get the current date
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog to pick a date
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddEvent.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Create a formatted date string
                        String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year);
                        eventDate.setText(selectedDate);
                    }
                }, day, month, year);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void deleteVideoAndRecord(String videoId, String videoUrl) {
        // Check if video record exists before proceeding with deletion
        firebaseFirestore.collection("Video")
                .document(videoId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Video record exists, proceed with deletion
                            // Delete video from Firebase Storage
                            StorageReference storageRef = firebaseStorage.getReferenceFromUrl(videoUrl);
                            storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    // Video deleted successfully
                                    // Now delete video record from Firestore
                                    firebaseFirestore.collection("Video")
                                            .document(videoId)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    // Video record deleted successfully
                                                    Toast.makeText(AddEvent.this, "Video and record deleted.", Toast.LENGTH_SHORT).show();
                                                    // Additional actions after successful deletion
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to delete video record
                                                    Toast.makeText(AddEvent.this, "Failed to delete video record.", Toast.LENGTH_SHORT).show();
                                                    // Handle the failure accordingly
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to delete video
                                    Toast.makeText(AddEvent.this, "Failed to delete video.", Toast.LENGTH_SHORT).show();
                                    // Handle the failure accordingly
                                }
                            });
                        } else {
                            // Video record doesn't exist, finish the activity
                            finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to check video record existence
                        Toast.makeText(AddEvent.this, "Failed to check video record existence.", Toast.LENGTH_SHORT).show();
                        // Handle the failure accordingly
                    }
                });
    }


    private void checkSchedule() {

    }

    private void scheduleEvent(String title, String desc, String date, String sport) {
        progressDialog.setTitle("Scheduling event");
        progressDialog.setMessage("Your image is uploading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String eid = title+date;
        Event event = new Event(eid, "null", "null", sport, title, desc, date);
        firebaseFirestore.collection("Event")
                .document(title)
                .set(event)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            String vTitle = videoName.getText().toString();
                            String eTitle = title+" Video Broadcast";
                            String vid = videoUr.getText().toString();
                            checkForVideo(vid, eid, vTitle, desc, eTitle);
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(AddEvent.this, "", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddEvent.this, "failed to create event", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private void checkForVideo(String videoId, String eid,String title, String desc, String newTitle) {
        // Check if video record exists before proceeding with deletion
        progressDialog.setTitle("Scheduling event");
        progressDialog.setMessage("Your image is uploading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        firebaseFirestore.collection("Video")
                .document(title)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Video record exists, proceed with
                            // update record
                            firebaseFirestore.collection("Video")
                                    .document(title)
                                    .update(
                                            "description", desc,
                                            "eid", eid,
                                            "title", newTitle
                                    ).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                progressDialog.dismiss();
                                                finish();
                                            }else {
                                                progressDialog.dismiss();
                                                Toast.makeText(AddEvent.this, "Error uopdating video record", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(AddEvent.this, "Error finding video whith error code: "+e, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            // Video record doesn't exist, finish the activity
                            finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to check video record existence
                        Toast.makeText(AddEvent.this, "Failed to check video record existence.", Toast.LENGTH_SHORT).show();
                        // Handle the failure accordingly
                    }
                });
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        String name = videoName.getText().toString();
//        String url = videoUr.getText().toString();
//        deleteVideoAndRecord(name, url);
//    }
}
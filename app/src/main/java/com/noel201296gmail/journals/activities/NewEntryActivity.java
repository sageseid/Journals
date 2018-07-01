package com.noel201296gmail.journals.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.noel201296gmail.journals.R;
import com.noel201296gmail.journals.activities.model.EntriesModel;



public class NewEntryActivity extends AppCompatActivity {

    private Button btnSave;
    private EditText txtTitle, txtContent;
    private FirebaseAuth auth;
    private DatabaseReference entryDatabase;
    private String noteID;
    private String  key;
    private String gNoteTitle;
    private String gNoteContent;
    private boolean isExist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(isNetworkStatusAvialable (getApplicationContext())) {
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection detected. Please connect to the internet", Toast.LENGTH_LONG).show();
        }

        try {
            noteID       = getIntent().getStringExtra("noteId");
            key          = getIntent().getStringExtra("userkey");
            gNoteTitle   = getIntent().getStringExtra("title");
            gNoteContent = getIntent().getStringExtra("content");

            if (!noteID.trim().equals("")) {
                isExist = true;
            } else {
                isExist = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        btnSave    = findViewById(R.id.save_entry_btn);
        txtTitle   = findViewById(R.id.entry_title);
        txtContent = findViewById(R.id.entry_content);

        auth = FirebaseAuth.getInstance();
        entryDatabase = FirebaseDatabase.getInstance().getReference().child("Entries").child(auth.getCurrentUser().getUid());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title   = txtTitle.getText().toString().trim();
                String content = txtContent.getText().toString().trim();

                if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
                    Toast.makeText(getApplicationContext(), "please fill in the empty fields", Toast.LENGTH_LONG).show();
                } else {
                    createEntry(title, content);
                }
            }
        });
        txtContent.setText(gNoteContent);
        txtTitle.setText(gNoteTitle);
    }

    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
            {
                return netInfos.isConnected();
            }
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_entry_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_note_delete_btn:
                if (isExist) {
                    Toast.makeText(this, "Delete functionality is still under construction", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Nothing to delete", Toast.LENGTH_SHORT).show();
                } if (isExist) {
                Toast.makeText(this, "Delete functionality is still under construction", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Nothing to delete", Toast.LENGTH_SHORT).show();
            }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteNote() {
//
        entryDatabase.getRef().child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(NewEntryActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                    noteID = "no";
                    finish();
                } else {
                    Log.e("NewNoteActivity", task.getException().toString());
                    Toast.makeText(NewEntryActivity.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void createEntry(String title, String content) {
        if (auth.getCurrentUser() != null) {

            if (isExist) {
                // UPDATE A NOTE
                final EntriesModel entriesModel = new EntriesModel();
                entriesModel.setNoteTitle(txtTitle.getText().toString().trim());
                entriesModel.setNoteContent(txtContent.getText().toString().trim());
                entriesModel.setNoteTime(String.valueOf(ServerValue.TIMESTAMP));

                entryDatabase.child(key).setValue(entriesModel);

                Toast.makeText(this, "Note updated in database", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
            } else {
                final DatabaseReference newEntry = entryDatabase.push();
                final EntriesModel entriesModel = new EntriesModel();
                entriesModel.setNoteTitle(title);
                entriesModel.setNoteContent(content);
                entriesModel.setNoteTime(String.valueOf(ServerValue.TIMESTAMP));

                Thread mainThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        newEntry.setValue(entriesModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(NewEntryActivity.this, " new note added to database", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(NewEntryActivity.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                mainThread.start();
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, "USER IS NOT SIGNED IN", Toast.LENGTH_SHORT).show();
        }


    }
}
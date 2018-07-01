package com.noel201296gmail.journals.activities;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.noel201296gmail.journals.R;
import com.noel201296gmail.journals.activities.adapter.EntryAdapter;
import com.noel201296gmail.journals.activities.model.EntriesModel;

import java.util.ArrayList;
import java.util.List;

public  class HomeActivity extends AppCompatActivity {



    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private RecyclerView mNotesList;
    private DatabaseReference EntryDatabase;
    private static FirebaseDatabase firebaseDatabase;
    private List<EntriesModel> entries;
    private String noteId;
    private  EntryAdapter mAdapter;
    public static ArrayList<String> mUserKey = new ArrayList<String>() ;
    private FloatingActionButton AddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (firebaseDatabase == null) {
            firebaseDatabase=FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
        }

        auth = FirebaseAuth.getInstance();
        AddNote = findViewById(R.id.fab_add_new_button);
        mNotesList = findViewById(R.id.recycler_view);
        mNotesList.setHasFixedSize(true);
        mNotesList.setLayoutManager(new LinearLayoutManager(this));

        if(isNetworkStatusAvialable (getApplicationContext())) {
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection detected. Please connect to the internet", Toast.LENGTH_LONG).show();
        }

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            EntryDatabase = FirebaseDatabase.getInstance().getReference().child("Entries").child(user.getUid());
        }

        EntryDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                entries = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                   noteId = snapshot.getKey();
                   mUserKey.add(noteId);
                    EntriesModel entriesModels =snapshot.getValue(EntriesModel.class);
                    entries.add(entriesModels);
                }
                mNotesList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mAdapter = new EntryAdapter(entries,getApplicationContext(),noteId, new EntryAdapter.RecyclerViewItemClick() {
                    @Override
                    public void OnItemClickListener(EntryAdapter.EntryViewHolder holder, int position) {
                        String userKey = mUserKey.get(position);

                        EntriesModel aEntrymodel = entries.get(position);
                        Intent intent = new Intent(getApplicationContext(), NewEntryActivity.class);
                        intent.putExtra("noteId",noteId);
                        intent.putExtra("title", aEntrymodel.getNoteTitle());
                        intent.putExtra("content", aEntrymodel.getNoteContent());
                        intent.putExtra("userkey", userKey);
                        startActivity(intent);
                    }
                });
                mNotesList.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // launch login activity
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        AddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewEntryActivity.class);
                startActivity(intent);
            }
        });

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
        inflater.inflate(R.menu.home_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void signOut() {
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }


}
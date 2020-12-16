package com.king.jazakikapuadmin;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProductsActivity extends AppCompatActivity implements ProductsAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private ProductsAdapter mAdapter;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Upload> mUploads;
    private SearchView searchView;
    SweetAlertDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();



        mAdapter = new ProductsAdapter(getApplicationContext(),mUploads);
        mRecyclerView.setAdapter(mAdapter );
        mAdapter.setOnItemClickListener(ProductsActivity.this);



        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper();
        pDialog.setTitleText("Loading");
        pDialog.setContentText("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                    Collections.reverse(mUploads);
                }
                mAdapter.notifyDataSetChanged();
                pDialog.dismissWithAnimation();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProductsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismissWithAnimation();
            }
        });
    }

    public void onItemClick(int position) {
        //Intent intent = new Intent(getApplicationContext(),DetailsActivity.class);
        //intent.putExtra("url", mUploads.get(position).getmImageUrl());
        //intent.putExtra("txt", mUploads.get(position).getmPhoneNumber());
        //startActivity(intent);
    }

    public void onWhatEverClick(int position) {
        Toast.makeText(this, "Whatever Click at position: "+position, Toast.LENGTH_SHORT).show();

    }

    public void onDeleteClick(int position) {
        Upload selectedItem = mUploads.get(position);
        final String selecetedKey = selectedItem.getKey();
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImage_url());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selecetedKey).removeValue();
                Toast.makeText(ProductsActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search_deposits).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search");
        //or
        //searchView.setQueryHint(getString(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                List<Upload> virtualList = new ArrayList<>();
                for (Upload upload:mUploads){
                    String item = "Hey";
                    if (item.contains(newText)){
                        virtualList.add(upload);
                        mAdapter.setSearchOperation(virtualList);
                    }else {
                        mAdapter.setSearchOperation(mUploads);
                        Toast.makeText(ProductsActivity.this, "No Item Found", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
        return true;
    }
}

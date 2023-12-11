package com.openclassrooms.realestatemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.edit.EditPropertyActivity;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    MainActivityViewModel viewModel;

    private TextView textViewMain;
    private TextView textViewQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.createProperty)
                .setOnClickListener(__ -> startActivity(new Intent(this, EditPropertyActivity.class)));

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        setupPropertyList();
    }
        //this.textViewMain = findViewById(R.id.activity_second_activity_text_view_main);
        // this.textViewMain = findViewById(R.id.activity_main_activity_text_view_main);
        // this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);

    private void setupPropertyList() {
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final PropertyListAdapter adapter = new PropertyListAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        viewModel.getProperties().observe(this, adapter::updateList);
    }
   /* private void configureTextViewQuantity(){
        int quantity = Utils.convertDollarToEuro(100);
        this.textViewQuantity.setTextSize(20);
       // this.textViewQuantity.setText(quantity);
        this.textViewQuantity.setText(Integer.toString(quantity));
    }*/

}
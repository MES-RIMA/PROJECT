package com.openclassrooms.realestatemanager.activities;
import static com.openclassrooms.realestatemanager.details.PropertyDetailActivity.LAYOUT_ORIENTATION_KEY;
import static com.openclassrooms.realestatemanager.details.PropertyDetailActivity.PROPERTY_ID_ARG_KEY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.details.PropertyDetailActivity;
import com.openclassrooms.realestatemanager.details.PropertyDetailFragment;
import com.openclassrooms.realestatemanager.edit.EditPropertyActivity;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.ui.PropertyListFragment;
import com.openclassrooms.realestatemanager.ui.PropertyListViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    PropertyDetailFragment propertyDetailFragment;
    private boolean isLargeScreen;
    private int currentSelectedPropertyID;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    public MainActivity() {
    }

    private TextView textViewMain;
    private TextView textViewQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        findViewById(R.id.createProperty)
                .setOnClickListener(__ -> startActivity(new Intent(this, EditPropertyActivity.class)));
        isLargeScreen = findViewById(R.id.detailFragmentContainer) != null;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
    }
    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.editProperty) {
            startEditActivity();
            return true;
        } else if (item.getItemId() == R.id.filter) {
            showFilterDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViews() {
        setupListFragment();
        if (isLargeScreen) {
            setupDetailFragment();
        }
        setupNavigationDrawer();
    }

    private void setupListFragment() {
        final PropertyListFragment propertyListFragment = PropertyListFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.listFragmentContainer, propertyListFragment)
                .commit();
    }
    private void setupNavigationDrawer(){
        final DrawerLayout drawerLayout = findViewById(R.id.root);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        final NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(MenuItem menuItem){
        if (menuItem.getItemId() == R.id.map){
            // TODO implement
            return true;
        }
        return false;
    }


    //this.textViewMain = findViewById(R.id.activity_second_activity_text_view_main);
    // this.textViewMain = findViewById(R.id.activity_main_activity_text_view_main);
    // this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);
    private void setupDetailFragment() {
        final Bundle args = new Bundle();
        args.putInt(LAYOUT_ORIENTATION_KEY, LinearLayout.HORIZONTAL);
        final PropertyDetailFragment propertyDetailFragment = PropertyDetailFragment.newInstance(args);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.detailFragmentContainer, propertyDetailFragment)
                .commit();
    }
    public void onPropertySelected(Property property) {
        if (isLargeScreen) {
            propertyDetailFragment.setProperty(property);
            currentSelectedPropertyID = property.getId();
        } else {
            startDetailActivity(property.getId());
        }
    }
    private void startDetailActivity(int propertyId){
        final Bundle bundle = new Bundle();
        bundle.putInt(PROPERTY_ID_ARG_KEY, propertyId);
        final Intent startDetailIntent = new Intent(this, PropertyDetailActivity.class);
        startDetailIntent.putExtras(bundle);
        startActivity(startDetailIntent);
    }
    /* private void configureTextViewQuantity(){
         int quantity = Utils.convertDollarToEuro(100);
         this.textViewQuantity.setTextSize(20);
        // this.textViewQuantity.setText(quantity);
         this.textViewQuantity.setText(Integer.toString(quantity));
     }*/
    private void startEditActivity() {
        if(currentSelectedPropertyID == 0){
            showSnackBar("No property have been selected yet");
            return;
        }
        final Bundle args = new Bundle();
        args.putInt(PROPERTY_ID_ARG_KEY, currentSelectedPropertyID);
        final Intent editPropertyIntent = new Intent(this, EditPropertyActivity.class);
        editPropertyIntent.putExtras(args);
        startActivity(editPropertyIntent);
    }

    private void showFilterDialog() {}

    private void showSnackBar(String message){
        final View root = findViewById(R.id.root);
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show();
    }
}
package com.openclassrooms.realestatemanager.edit;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityEditPropertyBinding;
import com.openclassrooms.realestatemanager.databinding.PhotoDescriptionEditorLayoutBinding;
import com.openclassrooms.realestatemanager.models.Photo;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.ui.PhotoListAdapter;

import java.util.ArrayList;

public class EditPropertyActivity extends AppCompatActivity {
    public static final String PROPERTY_ID_KEY = "PROPERTY_ID_KEY";

    private ActivityEditPropertyBinding binding;
    private EditPropertyViewModel viewModel;
    private PhotoListAdapter photoListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_property);
        viewModel = new ViewModelProvider(this).get(EditPropertyViewModel.class);
        setEditMode(); // Create || Update
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.saveAction) {
            saveProperty();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveProperty() {
        viewModel.persist();
        Snackbar.make(
                        binding.getRoot(),
                        getString(R.string.property_successfully_saved_msg),
                        Snackbar.LENGTH_SHORT)
                .show();
    }

    private void setEditMode() {
        final int propertyId = getIntent().getIntExtra(PROPERTY_ID_KEY, 0);
        if(propertyId != 0) {
            viewModel.updateProperty(propertyId).observe(this, binding::setProperty);
            binding.toolbar.setTitle(R.string.update_property_txt);
        } else {
            binding.setProperty(viewModel.createNewProperty());
            binding.toolbar.setTitle(R.string.create_property_txt);
        }
    }

    private void setupViews() {
        setupTypeSelector();
        setupAgentSelector();
        setupPhotoList();
        showPointOfInterests();
        binding.setOnPointOfInterestAdded(this::createPointOfInterest);
        binding.pickPhoto.setOnClickListener(__ -> pickAnImage());
    }

    private void setupTypeSelector() {
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Property.Type.names());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.typeSpinner.setAdapter(adapter);
    }

    private void setupAgentSelector() {
        final AgentSpinnerAdapter adapter = new AgentSpinnerAdapter(this, viewModel.getAllAgents());
        binding.agentSelector.setAdapter(adapter);
    }

    private void setupPhotoList() {
        photoListAdapter = new PhotoListAdapter(new ArrayList<>());
        binding.photoRecyclerView.setAdapter(photoListAdapter);
        binding.photoRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        viewModel.getPropertyPhotos().observe(this, photoListAdapter::updateList);
    }

    private void showPointOfInterests() {
        viewModel
                .getAllPointsOfInterests()
                .observe(
                        this,
                        pointOrInterestList -> {
                            TextView pointOfInterestView;
                            boolean isSelected;
                            for (final Property.PointOfInterest pointOfInterest : pointOrInterestList) {
                                isSelected = viewModel.containsPointOfInterest(pointOfInterest.getId());
                                pointOfInterestView = getPointOfInterestView(isSelected);
                                pointOfInterestView.setText(pointOfInterest.getName());
                                pointOfInterestView.setTag(pointOfInterest.getId());
                            }
                        });
    }

    private void createPointOfInterest(String pointOfInterest) {
        if (pointOfInterest.length() < 3) return;
        viewModel
                .createPointOfInterest(new Property.PointOfInterest(pointOfInterest))
                .observe(
                        this,
                        pointOfInterestId -> {
                            final TextView pointOfInterestView = getPointOfInterestView(false);
                            pointOfInterestView.setText(pointOfInterest);
                            pointOfInterestView.setTag(pointOfInterestId);
                            binding.pointOfInterestsContainer.addView(pointOfInterestView);
                            binding.addPointOfInterest.setVisibility(View.GONE);
                        });
    }

    private TextView getPointOfInterestView(boolean isChecked) {
        final TextView pointOfInterestView = new CheckedTextView(this);
        setPointOfInterestState(isChecked, pointOfInterestView);
        pointOfInterestView.setPadding(16, 8, 16, 8);
        pointOfInterestView.setMinWidth(100);
        pointOfInterestView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        pointOfInterestView.setTextSize(17);
        pointOfInterestView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        pointOfInterestView.setOnClickListener(this::togglePointOfInterestState);
        return pointOfInterestView;
    }

    private void togglePointOfInterestState(View pointOfInterestView) {
        final int pointOfInterestId = (int) pointOfInterestView.getTag();
        if (viewModel.containsPointOfInterest(pointOfInterestId)) {
            setPointOfInterestState(false, (TextView) pointOfInterestView);
            viewModel.removePointOrInterestFromCurrentProperty(pointOfInterestId);
        } else {
            setPointOfInterestState(true, (TextView) pointOfInterestView);
            viewModel.addPointOfInterestToCurrentProperty(pointOfInterestId);
        }
    }

    private void setPointOfInterestState(boolean isChecked, TextView pointOfInterestView) {
        if (isChecked) {
            pointOfInterestView.setBackgroundResource(R.drawable.checked);
            final Drawable checkedDrawable =
                    ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_check_24, null);
            pointOfInterestView.setCompoundDrawables(null, null, checkedDrawable, null);
        } else {
            pointOfInterestView.setBackgroundResource(R.drawable.unchecked);
        }
    }
    private void pickAnImage() {
        ImagePicker.with(this).crop().saveDir(getExternalFilesDir(Environment.DIRECTORY_DCIM)).start();
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                final Photo photo = new Photo();
                photo.setUrl(data.getData().toString());
                showPhotoDescriptionEditor(photo);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Snackbar.make(binding.getRoot(), ImagePicker.getError(data), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void showPhotoDescriptionEditor(Photo photo) {
        final PhotoDescriptionEditorLayoutBinding photoDescLayout =
                PhotoDescriptionEditorLayoutBinding.inflate(getLayoutInflater(), binding.root, false);

        Glide.with(photoDescLayout.getRoot())
                .load(photo.getUrl())
                .centerCrop()
                .into(photoDescLayout.photo);

        new AlertDialog.Builder(this)
                .setTitle(R.string.photo_desc_dialog_title)
                .setView(photoDescLayout.getRoot())
                .setPositiveButton(
                        R.string.set_txt,
                        ((dialog, which) -> {
                            photo.setDescription(photoDescLayout.photoDescription.getText().toString());
                            viewModel.createPhotoAndAddToCurrentProperty(photo);
                            Log.d("PHOTO_DESCRIPTION", "Desc : " + photo.getDescription());
                        }))
                .create()
                .show();
    }
}
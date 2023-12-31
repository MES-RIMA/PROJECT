package com.openclassrooms.realestatemanager.edit;

import android.content.Intent;
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
import com.openclassrooms.realestatemanager.details.PropertyDetailActivity;
import com.openclassrooms.realestatemanager.models.Photo;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.ui.PhotoListAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditPropertyActivity extends AppCompatActivity {

    private ActivityEditPropertyBinding binding;
    private EditPropertyViewModel viewModel;
    private PhotoListAdapter photoListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_property);
        viewModel = new ViewModelProvider(this).get(EditPropertyViewModel.class);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setEditMode(); // Create || Update

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
        if (viewModel.isPhotoDefined()) {
            viewModel.persist();
            Snackbar.make(
                            binding.getRoot(),
                            getString(R.string.property_successfully_saved_msg),
                            Snackbar.LENGTH_SHORT)
                    .show();
        } else {
            Snackbar.make(
                            binding.getRoot(), getString(R.string.photo_required_msg), Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    private void setEditMode() {
        final int propertyId = getIntent().getIntExtra(PropertyDetailActivity.PROPERTY_ID_ARG_KEY, 0);
        if (propertyId != 0) {
            viewModel.updateProperty(propertyId).observe(this, this::setProperty);
            getSupportActionBar().setTitle(R.string.update_property_txt);
        } else {
            viewModel.createNewProperty().observe(this, this::setProperty);
            getSupportActionBar().setTitle(R.string.update_property_txt);
        }
    }
    private void setProperty(PropertyDataBinding property){
        binding.setProperty(property);
        setupViews();
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
        viewModel.getAllAgents().observe(this, agentList -> {
            final AgentSpinnerAdapter adapter = new AgentSpinnerAdapter(this, agentList);
            binding.agentSelector.setAdapter(adapter);
        });
    }

    private void setupPhotoList() {
        photoListAdapter = new PhotoListAdapter(viewModel.getPropertyPhotos());
        binding.photoRecyclerView.setAdapter(photoListAdapter);
        binding.photoRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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
                                isSelected = viewModel.containsPointOfInterest(pointOfInterest);
                                pointOfInterestView = getPointOfInterestView(isSelected);
                                pointOfInterestView.setText(pointOfInterest.getName());
                                pointOfInterestView.setTag(pointOfInterest);
                                binding.pointOfInterestsContainer.addView(pointOfInterestView);
                            }
                        });
    }

    private void createPointOfInterest(String pointOfInterestName) {
        Log.d("CREATE_POINT_OF_I", "CALLED WITH VALUE : " + pointOfInterestName);
        if (pointOfInterestName.length() < 3) return;
        final Property.PointOfInterest pointOfInterest = new Property.PointOfInterest(pointOfInterestName);
        viewModel
                .createPointOfInterest(pointOfInterest)
                .observe(
                        this,
                        pointOfInterestId -> {
                            if(pointOfInterestId == null || pointOfInterestId.equals(0)) return;
                            final TextView pointOfInterestView = getPointOfInterestView(false);
                            pointOfInterest.setId(pointOfInterestId);
                            pointOfInterestView.setText(pointOfInterestName);
                            pointOfInterestView.setTag(pointOfInterest);
                            binding.addPointOfInterest.setVisibility(View.GONE);
                        });
    }

    private TextView getPointOfInterestView(boolean isChecked) {
        final TextView pointOfInterestView = new CheckedTextView(this);
        setPointOfInterestState(isChecked, pointOfInterestView);
            pointOfInterestView.setPadding(20, 5, 20, 8);
        pointOfInterestView.setMinWidth(100);
        pointOfInterestView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        pointOfInterestView.setTextSize(17);
        pointOfInterestView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        pointOfInterestView.setOnClickListener(this::togglePointOfInterestState);
        return pointOfInterestView;
    }

    private void togglePointOfInterestState(View pointOfInterestView) {
        final Property.PointOfInterest pointOfInterest = (Property.PointOfInterest) pointOfInterestView.getTag();
        if (viewModel.containsPointOfInterest(pointOfInterest)) {
            setPointOfInterestState(false, (TextView) pointOfInterestView);
            viewModel.removePointOrInterestFromCurrentProperty(pointOfInterest);
        } else {
            setPointOfInterestState(true, (TextView) pointOfInterestView);
            viewModel.addPointOfInterestToCurrentProperty(pointOfInterest);
        }
    }

    private void setPointOfInterestState(boolean isChecked, TextView pointOfInterestView) {
        if (isChecked) {
            pointOfInterestView.setBackgroundResource(R.drawable.checked);
            pointOfInterestView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0, 0, R.drawable.ic_baseline_check_24, 0);
        } else {
            pointOfInterestView.setBackgroundResource(R.drawable.unchecked);
            pointOfInterestView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
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
                            if (photoDescLayout.mainCheckBox.isChecked()) {
                                viewModel.setMainPhoto(photo);
                            }
                            viewModel.addPhotoToCurrentProperty(photo);
                            photoListAdapter.updateList(viewModel.getPropertyPhotos());
                        }))
                .create()
                .show();
    }
}
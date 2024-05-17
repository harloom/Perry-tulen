package com.ferry.tulen.presentation.business;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.util.Pair;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ferry.tulen.R;
import com.ferry.tulen.presentation.business.bottomsheet.BottomSheetCallback;
import com.ferry.tulen.presentation.business.bottomsheet.BottomSheetFragment;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.type.DateTime;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class OrderWorkerCreateActivity extends AppCompatActivity {
    private static final int PERMISSION_ID = 44;
    private FusedLocationProviderClient fusedLocationClient;


    ///value

    private EditText etDescription;
    private String tipePengerjaan;
    private  Location locationNow;

    private Date startDate;
    private Date endDate;

    private  Uri imageUri;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_created);

        Objects.requireNonNull(getSupportActionBar()).hide();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets navigationBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), navigationBars.bottom);
                return insets;
            }
        });

        etDescription = findViewById(R.id.textJob);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        findViewById(R.id.btnAmbilLokasi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });


        Calendar minDate = Calendar.getInstance();
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Pilih Pengerjaan");
        builder.setSelection(Pair.create(MaterialDatePicker.todayInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()));
        builder.setPositiveButtonText("OK");
        builder.setNegativeButtonText("Cancel");
        builder.setCalendarConstraints(new CalendarConstraints.Builder()
                .setStart(minDate.getTimeInMillis())
                .build());

        MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                Date first = new Date(selection.first);
                Calendar calennderNow = Calendar.getInstance();
                calennderNow.add(Calendar.DAY_OF_YEAR, -1);
                Date now = calennderNow.getTime();

                if (now.before(first)) {
                    Toast.makeText(OrderWorkerCreateActivity.this, "Selected date range: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date(selection.first)) + " - " + new SimpleDateFormat("yyyy-MM-dd").format(new Date(selection.second)), Toast.LENGTH_SHORT).show();
                    startDate =  new Date(selection.first);
                    endDate = new Date(selection.second);
                    setValueJadwal();
                } else {
                    Toast.makeText(OrderWorkerCreateActivity.this, "Error date range: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date(selection.first)) + " - " + new SimpleDateFormat("yyyy-MM-dd").format(new Date(selection.second)), Toast.LENGTH_SHORT).show();

                }
            }
        });


        findViewById(R.id.btnPilihJadwal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.show(getSupportFragmentManager(), "date_picker");
            }
        });

        BottomSheetFragment bottomSheet = BottomSheetFragment.newInstance();
        findViewById(R.id.btnPilihType).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet.show(getSupportFragmentManager(), "BottomSheet");
            }
        });

        bottomSheet.setBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onOption1Clicked() {
                tipePengerjaan = "Borongan";
                setValueTipePengerjaan();
            }

            @Override
            public void onOption2Clicked() {
                tipePengerjaan = "Harian";
                setValueTipePengerjaan();
            }
        });


        findViewById(R.id.lnImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ambilFoto();
            }
        });

        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOrder(view);
            }
        });

    }

    void sendOrder(View view ){
        ///
    }


    void ambilFoto() {
        ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                //Final image resolution will be less than 1080 x 1080(Optional)
                .start();

    }

    void setValueTipePengerjaan() {
        TextView setPengerjaan = findViewById(R.id.resTipePekerjaan);
        setPengerjaan.setText(tipePengerjaan);
    }

    @SuppressLint("SimpleDateFormat")
    void setValueJadwal(){
        TextView setJadwal = findViewById(R.id.resJadwal);
        setJadwal.setText(String.format("Mulai %s - %s", new SimpleDateFormat("yyyy-MM-dd").format(startDate), new SimpleDateFormat("yyyy-MM-dd").format(endDate)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri uri = data.getData();
            Toast.makeText(this, "Set : " + uri.toString(), Toast.LENGTH_LONG).show();
            // Use Uri object instead of File to avoid storage permissions
//            imgProfile.setImageURI(uri);

            ImageView imageView = findViewById(R.id.imageView);
            imageUri = uri;

            imageView.setImageURI(uri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {
                            // Logic to handle location object
                            System.out.println("Debug: location " + location.getLatitude() + ", Long : " + location.getLongitude());

                            locationNow = location;
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
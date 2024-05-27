package com.ferry.tulen.presentation.business;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.airbnb.lottie.LottieAnimationView;
import com.ferry.tulen.R;
import com.ferry.tulen.datasources.SharedPreferences.SharedPreferenceHelper;
import com.ferry.tulen.datasources.firebase.OrderDataSource;
import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.Order;
import com.ferry.tulen.datasources.models.WorkMan;
import com.ferry.tulen.presentation.business.bottomsheet.BottomSheetCallback;
import com.ferry.tulen.presentation.business.bottomsheet.BottomSheetFragment;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;


public class OrderWorkerCreateActivity extends AppCompatActivity {
    private static final int PERMISSION_ID = 44;
    private FusedLocationProviderClient fusedLocationClient;


    public  static int REQUEST_CODE_WORK_MAN = 19;

    ///value

    private WorkMan selectedWorkMan;
    private EditText etDescription;
    private String tipePengerjaan;
    private  Location locationNow;

    private String pekerjaan;

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

        TextView perkerjanTxt = findViewById(R.id.resPerkerjaan);
        Intent intent = getIntent();

        pekerjaan = intent.getStringExtra("pekerjaan");

        perkerjanTxt.setText(pekerjaan);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        findViewById(R.id.btnAmbilLokasi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });


        findViewById(R.id.btnPilihTukang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderWorkerCreateActivity.this, ChooseWorkManActivity.class);
                someActivityResultLauncher.launch(intent);
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
               uploadImageBeforeSubmit(view,imageUri);

            }
        });

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // Extract the result from the Intent
                        int resultCode = data.getIntExtra("result_code", -1);
                        if (resultCode == OrderWorkerCreateActivity.REQUEST_CODE_WORK_MAN) {
                            WorkMan workMan = (WorkMan) data.getParcelableExtra("workMan");
                            System.out.println("debug: Workman "  + workMan.getFullName());
                            selectedWorkMan = workMan;

                            setValueWorkMan();
                        }
                    }
                }
            });


    void testSendOrder(View view){
        LottieAnimationView animationView =  findViewById(R.id.loadingAnimation);
        animationView.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity after the delay
                animationView.setVisibility(View.GONE);
                goToSuccess();
            }
        }, 3000);
    }

    void goToSuccess(){
        Intent intent = new Intent(OrderWorkerCreateActivity.this, SuccessOrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    void sendOrder(View view, String fileNameImage ){

        /// validation
        if(locationNow == null){
            Snackbar.make(view,"Silahkan Ambil Coordinate terlebih dahulu", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.RED)
                    .show();
            return;
        }


        LottieAnimationView animationView =  findViewById(R.id.loadingAnimation);
        animationView.setVisibility(View.VISIBLE);

        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(this);
        OrderDataSource orderDataSource =  OrderDataSource.getInstance(FirebaseFirestore.getInstance());
        String idUser = sharedPreferenceHelper.getString(SharedPreferenceHelper.KEY_ID_USER,"");


        // geo hash
        String hash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(locationNow.getLatitude(), locationNow.getLongitude()));



        Order order = new Order(
                null, /// new create set null
                idUser,selectedWorkMan.getId(),tipePengerjaan,etDescription.getText().toString(),pekerjaan,
                fileNameImage,startDate,endDate,hash,locationNow.getLatitude(),locationNow.getLongitude(),0
        );


        orderDataSource.createdOrder(order, new ResultListener<String>() {
            @Override
            public void onSuccess(String result) {
                animationView.setVisibility(View.GONE);
                goToSuccess();
            }

            @Override
            public void onError(Throwable error) {
                animationView.setVisibility(View.GONE);

                Snackbar.make(view,error.getMessage(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(Color.RED)
                        .show();
            }
        });

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

    void setValueWorkMan() {

        findViewById(R.id.mainHeader).setVisibility(View.VISIBLE);
        findViewById(R.id.cardView).setVisibility(View.VISIBLE);

        TextView namaTukang = findViewById(R.id.namatukang);
        namaTukang.setText(selectedWorkMan.getFullName());

        TextView jesniTukang = findViewById(R.id.jenisTukang);
        jesniTukang.setText(selectedWorkMan.getJob());

        /// set alamat
        TextView addressTukang = findViewById(R.id.addressTukang);
        addressTukang.setText(selectedWorkMan.getAddress());

        // Coordinate

    }


    @SuppressLint("SimpleDateFormat")
    void setValueJadwal(){
        TextView setJadwal = findViewById(R.id.resJadwal);
        setJadwal.setText(String.format("Mulai %s - %s", new SimpleDateFormat("yyyy-MM-dd").format(startDate), new SimpleDateFormat("yyyy-MM-dd").format(endDate)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == OrderWorkerCreateActivity.REQUEST_CODE_WORK_MAN){
            System.out.println("debug: Workman onActivityResult overide ");
        }else{
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                Uri uri = data.getData();
                ImageView imageView = findViewById(R.id.imageView);
                imageUri = uri;
                imageView.setImageURI(uri);
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
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
//                            System.out.println("Debug: location " + location.getLatitude() + ", Long : " + location.getLongitude());

                            locationNow = location;

                           TextView txtLocation =  findViewById(R.id.resLocation);
                           txtLocation.setText("Long : " + locationNow.getLongitude() + " lat : " + locationNow.getLatitude());
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


    private  void uploadImageBeforeSubmit(View view , Uri imageFile){




        LottieAnimationView animationView =  findViewById(R.id.loadingAnimation);
        animationView.setVisibility(View.VISIBLE);


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());
        String randomString = UUID.randomUUID().toString();
        String fileName = "file_" + timestamp + "_" + randomString + ".jpg";


        StorageReference spaceRef = storageRef.child(fileName);


        // Create a File object from the file path
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] data = baos.toByteArray();
//

// Create an upload task using the byte array
        UploadTask uploadTask = spaceRef.putBytes(data);

// Add listeners for the upload task
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                animationView.setVisibility(View.GONE);

                Snackbar.make(view,"Upload Gagal", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(Color.RED)
                        .show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...

                animationView.setVisibility(View.GONE);
                sendOrder(view,fileName);
            }
        });
    }

}
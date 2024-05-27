package com.ferry.tulen.datasources.firebase;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.ferry.tulen.datasources.listener.ResultListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StorageDownload {

    private static StorageDownload instance;
    private FirebaseStorage storage;

    private StorageDownload(FirebaseStorage storage) {
        this.storage = storage;
    }

    public static StorageDownload getInstance(FirebaseStorage storage) {
        if (instance == null) {
            instance = new StorageDownload(storage);
        }
        return instance;
    }

    public  void getUrlDownload(String fileName , ResultListener<Uri> resultListener){
        StorageReference storageRef = storage.getReference();
        StorageReference reference = storageRef.child(fileName);

        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                resultListener.onSuccess(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultListener.onError(e);
            }
        });

    }


}

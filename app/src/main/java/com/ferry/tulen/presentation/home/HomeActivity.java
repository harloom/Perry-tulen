package com.ferry.tulen.presentation.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ferry.tulen.R;
import com.ferry.tulen.datasources.firebase.CategoryDataSource;
import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.core.FirestoreClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Objects.requireNonNull(getSupportActionBar()).hide();

        CategoryDataSource dataSource = CategoryDataSource.getInstance(FirebaseFirestore.getInstance());

        dataSource.getAllCategory(new ResultListener<List<Category>>() {
            @Override
            public void onSuccess(List<Category> result) {
                for (Category doc : result) {
                    System.out.println("debug: result " + doc.toString());
                }
            }
            @Override
            public void onError(Throwable e) {
                System.err.println("debug: Error category: " + e.getMessage());
            }
        });

    }


//    public class FirestoreHelper {
//
//        private FirebaseFirestore db;
//
//        public FirestoreHelper() {
//            db = FirebaseFirestore.getInstance();
//        }
//
//        public void addUser(String name, String email) {
//            DocumentReference userRef = db.collection("users").document();
//            User user = new User(userRef.getId(), name, email);
//            userRef.set(user);
//        }
//    }
//
//    public class User {
//        private String id;
//        private String name;
//        private String email;
//
//        public User(String id, String name, String email) {
//            this.id = id;
//            this.name = name;
//            this.email = email;
//        }
//
//        // Getters and setters
//    }
}
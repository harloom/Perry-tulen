package com.ferry.tulen.datasources.firebase;

import androidx.annotation.NonNull;

import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryDataSource {
    private static CategoryDataSource instance;
    private FirebaseFirestore db;

    private CategoryDataSource(FirebaseFirestore db) {
        this.db = db;
    }

    public static CategoryDataSource getInstance(FirebaseFirestore db) {
        if (instance == null) {
            instance = new CategoryDataSource(db);
        }
        return instance;
    }

    public void getAllCategory(ResultListener<List<Category>> listener) {
        // Implementation to get all categories from FirebaseFirestore
        // and call the listener.onSuccess() or listener.onError() method
        db.collection(CollectionName.CATEGORY).get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        /// handle value
                        List<Category> categories = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Category category = Category.fromMap(doc.getData());
                            categories.add(category);
                        }
                        listener.onSuccess(categories);
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onError(e);
            }
        });
    }
}
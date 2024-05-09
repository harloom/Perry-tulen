package com.ferry.tulen.datasources.firebase;

import androidx.annotation.NonNull;

import com.ferry.tulen.datasources.models.Category;
import com.ferry.tulen.datasources.models.GuardUser;
import com.ferry.tulen.datasources.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

public class InitializeDbTest {

    private FirebaseFirestore db;

    public InitializeDbTest() {
        db = FirebaseFirestore.getInstance();
    }

    public void initialObject() {
//        createCategory();
//        createUser();
    }

    // model category
    private void createCategory() {
        Category c = new Category(
                "1", "testCategory", CollectionName.base64Example
        );
        db.collection(CollectionName.CATEGORY).add(c.toMap()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("debug: Category added successfully.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.err.println("debug: Error adding category: " + e.getMessage());
            }
        });
    }

    private void createUser() {

        WriteBatch batch =  db.batch();
        User user = new User("USER_1", "harloom.developer@gmail.com", "Harloom", "Jalan Aku sayang kamu", "08238888888", "Software Engineer");
        GuardUser guardUser = new GuardUser(user.getId(),"test1234");
         batch.set(db.collection(CollectionName.USER).document(),user.toMap());
        batch.set(db.collection(CollectionName.GUARD_USER).document(),guardUser.toMap());

       batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void unused) {
               System.out.println("debug: User AND GUARD added successfully.");
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               System.out.println("debug: User AND GUARD added Failure.");
           }
       });


    }
}

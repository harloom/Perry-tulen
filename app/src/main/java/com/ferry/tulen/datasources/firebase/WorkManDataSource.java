package com.ferry.tulen.datasources.firebase;

import androidx.annotation.NonNull;

import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.Category;
import com.ferry.tulen.datasources.models.WorkMan;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class WorkManDataSource {
    private static WorkManDataSource instance;
    private FirebaseFirestore db;

    private WorkManDataSource(FirebaseFirestore db) {
        this.db = db;
    }

    public static WorkManDataSource getInstance(FirebaseFirestore db) {
        if (instance == null) {
            instance = new WorkManDataSource(db);
        }
        return instance;
    }

    public void getListTopWorkMan(ResultListener<List<WorkMan>> resultListener){
        db.collection(CollectionName.WORK_MAN).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<WorkMan> workManLists = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    WorkMan workMan = WorkMan.fromMap(doc.getData());
                    workManLists.add(workMan);
                }

                resultListener.onSuccess(workManLists);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultListener.onError(e);
            }
        });
    }

    public void getListByFiler(String pekerjaan, ResultListener<List<WorkMan>> resultListener){

        System.out.println("debug: pekerjaan " + pekerjaan);
        db.collection(CollectionName.WORK_MAN).whereEqualTo("job",pekerjaan).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<WorkMan> workManLists = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    WorkMan workMan = WorkMan.fromMap(doc.getData());
                    workManLists.add(workMan);
                }

                resultListener.onSuccess(workManLists);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultListener.onError(e);
            }
        });
    }
}

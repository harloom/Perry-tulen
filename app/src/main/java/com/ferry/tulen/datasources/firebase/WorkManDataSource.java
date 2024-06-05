package com.ferry.tulen.datasources.firebase;

import androidx.annotation.NonNull;

import com.ferry.tulen.datasources.SharedPreferences.SharedPreferenceHelper;
import com.ferry.tulen.datasources.alogritma.SlopeOneWorkMan;
import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.Category;
import com.ferry.tulen.datasources.models.Rating;
import com.ferry.tulen.datasources.models.WorkMan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public void getListTopWorkMan(ResultListener<List<WorkMan>> resultListener) {
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

    public void getListByFiler(String idUser, String pekerjaan, ResultListener<List<WorkMan>> resultListener) {

        System.out.println("debug: pekerjaan " + pekerjaan);
        db.collection(CollectionName.WORK_MAN).whereEqualTo("job", pekerjaan).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<WorkMan> workManLists = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    WorkMan workMan = WorkMan.fromMap(doc.getData());
                    workManLists.add(workMan);
                }

                getRatingData(idUser,workManLists,resultListener);

//                resultListener.onSuccess(workManLists);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultListener.onError(e);
            }
        });
    }

    private void getRatingData(String idUser, List<WorkMan> dataSet, ResultListener<List<WorkMan>> resultListener) {


        db.collection(CollectionName.RATING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Rating> ratings = new ArrayList<>();
                if (!task.isSuccessful()) {
                    return;
                } else {
                    List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                        Rating rating = Rating.fromMap(documentSnapshot.getData());
                        ratings.add(rating);
                    }
                }

                System.out.println("debug idUser : " + idUser + " Rating seize : " + ratings.size());
                getDataSetAlgoritma(idUser,dataSet,ratings,resultListener);

            }
        });

    }

    private void getDataSetAlgoritma(String idUser, List<WorkMan> workManList, List<Rating> ratingList, ResultListener<List<WorkMan>> resultListener) {

        Map<String, Map<String, Double>> ratingsDataSet = new HashMap<>();

        /// filter rating  inclue idUser;
        List<Rating> filteredRatingByUser = ratingList.stream()
                .filter(f -> idUser.contains(f.getIdUser()))
                .collect(Collectors.toList());

//        List<Rating> filteredRatingNoIncludeByUser = ratingList.stream()
//                .filter(f -> !idUser.contains(f.getIdUser()))
//                .collect(Collectors.toList());

        System.out.println("debug filter rating no inclue idUser");


        Map<String, Map<String, Double>> data = new HashMap<>();

        /// for each workman
        for (WorkMan man : workManList) {
            List<Rating> filteredRatingNoIncludeByUser = ratingList.stream()
                    .filter(f -> !man.getId().contains(f.getIdUser()))
                    .collect(Collectors.toList());
            System.out.println("debug set workman : " + man.getId() +" size : " +filteredRatingNoIncludeByUser.size());

            /// asve id to data set
            data.put(man.getId(), new HashMap<>());

            for (Rating rating : filteredRatingNoIncludeByUser) {
//                save rating dan id workman to data set
                data.get(man.getId()).put(rating.getIdWorkman(),rating.getRating());

                System.out.println("debug set : " + man.getId() +"workman :" +rating.getIdWorkman() + "  rating "+ rating.getRating());
            }
        }

        System.out.println("debug after for each");


        SlopeOneWorkMan slopeOneWorkMan = new SlopeOneWorkMan();

        slopeOneWorkMan.update(data);

//        // Create a sample user preferences
        Map<String, Double> userprefs = new HashMap<>();
        for (Rating rating : filteredRatingByUser) {
            // Create a sample user preferences
            userprefs.put(rating.getIdWorkman(),rating.getRating());

            System.out.println("debug rating user pref : " + rating.getIdWorkman() + " rating "+ rating.getRating());
        }
        System.out.println("debug after userprefs");

//        // Predict ratings for the user
        Map<String, Double> preds = slopeOneWorkMan.predict(userprefs);

        System.out.println("debug preds " + preds.entrySet().size());
        for (Map.Entry<String, Double> entry : preds.entrySet()) {

            /// update data rating ....
            int index = getIndex(workManList, entry.getKey());
            if(index != -1){
                workManList.get(index).setScore(entry.getValue());

                System.out.println("debug Predicted rating for item  " +  entry.getKey() + ": " +  workManList.get(index).getFullName() + ": " + entry.getValue());
            }

        }
        sortByScoreDesc(workManList);
        resultListener.onSuccess(workManList);
    }

    public static void sortByScoreDesc(List<WorkMan> list) {
        list.sort(Comparator.comparingDouble((WorkMan e) -> e.getScore()!= null? e.getScore() : 0).reversed());
    }
    private static int getIndex(List<WorkMan> list, String targetId) {
        for (int i = 0; i < list.size(); i++) {
            if (Objects.equals(list.get(i).getId(), targetId)) {
                return i;
            }
        }
        return -1; // not found
    }
}

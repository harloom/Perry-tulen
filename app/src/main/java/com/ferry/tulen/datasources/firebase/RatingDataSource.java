package com.ferry.tulen.datasources.firebase;

import androidx.annotation.NonNull;

import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.Order;
import com.ferry.tulen.datasources.models.OrderWithUser;
import com.ferry.tulen.datasources.models.Rating;
import com.ferry.tulen.datasources.models.WorkMan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class RatingDataSource {

    private static RatingDataSource instance;
    private FirebaseFirestore db;

    private RatingDataSource(FirebaseFirestore db) {
        this.db = db;
    }

    public static RatingDataSource getInstance(FirebaseFirestore db) {
        if (instance == null) {
            instance = new RatingDataSource(db);
        }
        return instance;
    }

    public  void checkRatingHave(String idOrder, ResultListener< List<Rating>> resultListener ){
        db.collection(CollectionName.RATING).whereEqualTo("idOrder", idOrder).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    List<Rating> ratings = new ArrayList<>();
                    resultListener.onSuccess(ratings);
                }else{
                    List<Rating> ratings = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Rating rating = Rating.fromMap(doc.getData());
                        ratings.add(rating);
                    }
                    resultListener.onSuccess(ratings);
                }
            }
        });
    }


    public void setRatingWorkman(OrderWithUser order, double star, String ulasan,ResultListener<String> resultListener){
        WriteBatch batch = db.batch();

        Rating rating = new Rating(order.getUser().getUser().getId(),order.getWorkMan().getWorkMan().getId(),star,ulasan,order.getOrder().getId());
        batch.set( db.collection(CollectionName.RATING).document(),rating.toMap());
        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
//                 resultListener.onSuccess("Successfully");
                calculatorRatingWorkman(rating.getIdWorkman(), order.getWorkMan().getIdDocument(), resultListener);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultListener.onError(new Throwable("Terjadi kesalahan"));

            }
        });

    }

    private  void calculatorRatingWorkman (String idWorkman, String idDocWorkman , ResultListener resultListener){
        db.collection(CollectionName.RATING).whereEqualTo("idWorkman", idWorkman).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Rating> ratings = new ArrayList<>();

                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            Rating rating = Rating.fromMap(doc.getData());
                            double star = rating.getRating();
                            ratings.add(rating);
                        }

                        /// calculator rating
                        double sum = 0;
                        for (Rating rating : ratings) {
                            sum += rating.getRating();
                        }
                        double meanRating = sum / ratings.size();

                        // update to User;
                        updateTableWorkman(idDocWorkman,meanRating,resultListener);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        resultListener.onError(new Throwable("Terjadi kesalahan"));
                    }
                });
    }

    private  void updateTableWorkman(String idDocumentWorkman, double meanRating,ResultListener<String> resultListener){
        WorkMan workMan = new WorkMan();
        workMan.setRating(meanRating);;
        WriteBatch batch = db.batch();
        DocumentReference docRef = db.collection(CollectionName.WORK_MAN).document(idDocumentWorkman);
        batch.update(docRef,workMan.toUpdateRating());

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                resultListener.onSuccess("OK");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultListener.onError(e);
            }
        });

    }

}

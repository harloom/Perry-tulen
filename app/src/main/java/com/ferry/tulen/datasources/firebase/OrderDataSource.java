package com.ferry.tulen.datasources.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.Category;
import com.ferry.tulen.datasources.models.NotificationWorker;
import com.ferry.tulen.datasources.models.Order;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDataSource {

    private static OrderDataSource instance;
    private FirebaseFirestore db;

    private OrderDataSource(FirebaseFirestore db) {
        this.db = db;
    }

    public static OrderDataSource getInstance(FirebaseFirestore db) {
        if (instance == null) {
            instance = new OrderDataSource(db);
        }
        return instance;
    }


    public void createdOrder(Order order, ResultListener<String> resultListener){
        WriteBatch batch = db.batch();
        Date dateNow = new Date();
        NotificationWorker notificationWorker = new NotificationWorker(
                order.getIdUser(),order.getIdWorkman(),"Kamu mendapatkan pesanan",dateNow);

        batch.set( db.collection(CollectionName.ORDER).document(),order.createMap());

        batch.set(db.collection(CollectionName.NOTIFICATION_WORKER).document(), notificationWorker);

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("debug: User AND GUARD added successfully.");
                resultListener.onSuccess("Successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("debug: User AND GUARD added Failure.");
                resultListener.onError(new Throwable("Terjadi kesalahan"));

            }
        });
    }

    public  void getListOrderListener(String idWorker, ResultListener<List<Order>> resultListener){
        db.collection(CollectionName.ORDER).whereEqualTo("idWorkman",idWorker).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
//                    Log.w(TAG, "Listen failed.", error);
                    return;
                }

                if (value != null) {
                    // Process the query snapshot here
                    List<Order> orders = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Order order = Order.toObject(doc.getData());
                        orders.add(order);
                    }

                    resultListener.onSuccess(orders);
                } else {
//                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    public  void getListOrder(String idWorker, ResultListener<List<Order>> resultListener){
        db.collection(CollectionName.ORDER).whereEqualTo("idWorkman",idWorker).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Order> orders = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    Order order = Order.toObject(doc.getData());
                    orders.add(order);
                }

                resultListener.onSuccess(orders);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

}

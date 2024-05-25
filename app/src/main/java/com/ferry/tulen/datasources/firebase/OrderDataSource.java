package com.ferry.tulen.datasources.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.NotificationWorker;
import com.ferry.tulen.datasources.models.Order;
import com.ferry.tulen.datasources.models.OrderWithUser;
import com.ferry.tulen.datasources.models.User;
import com.ferry.tulen.datasources.models.UserWithIdDocument;
import com.ferry.tulen.datasources.models.WorkMainWithIdDocument;
import com.ferry.tulen.datasources.models.WorkMan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

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

    public  void getOrderDetailWithUser(String idOrder , ResultListener<OrderWithUser> resultListener){
        db.collection(CollectionName.ORDER).document(idOrder).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(!task.isSuccessful()){

                    /// gagal
                    return;
                }
                DocumentSnapshot result = task.getResult();
                if (result == null || result.getData() == null) {
                    // data kosong
                    resultListener.onError(new Throwable("Data Tidak ada"));
                    return;
                }

                Order order = Order.toObject(idOrder, result.getData());
                OrderWithUser orderWithUser = new OrderWithUser();
                orderWithUser.setOrder(order);
                /// get User Order
                getUser(orderWithUser,resultListener);
            }
        });
    }

    public  void getOrderDetailFinishWithUser(String idOrder , ResultListener<OrderWithUser> resultListener){
        db.collection(CollectionName.ORDER_FINISH).document(idOrder).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(!task.isSuccessful()){

                    /// gagal
                    return;
                }
                DocumentSnapshot result = task.getResult();
                if (result == null || result.getData() == null) {
                    // data kosong
                    resultListener.onError(new Throwable("Data Tidak ada"));
                    return;
                }

                Order order = Order.toObject(idOrder, result.getData());
                OrderWithUser orderWithUser = new OrderWithUser();
                orderWithUser.setOrder(order);
                /// get User Order
                getUser(orderWithUser,resultListener);
            }
        });
    }

    private  void getUser(OrderWithUser orderWithUser, ResultListener<OrderWithUser> resultListener ){
        db.collection(CollectionName.USER).whereEqualTo( "id", orderWithUser.getOrder().getIdUser()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(!task.isSuccessful()){
                    orderWithUser.setUser(null);
                }else{
                    if (!task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        User user = User.fromMap(document.getData());
                        UserWithIdDocument userWithIdDocument = new UserWithIdDocument(document.getId(),user);
                        orderWithUser.setUser(userWithIdDocument);

                    }
                }
                /// get workman
                getWorkman(orderWithUser,resultListener);
            }
        });
    }

    private  void getWorkman(OrderWithUser orderWithUser, ResultListener<OrderWithUser> resultListener ){
        db.collection(CollectionName.WORK_MAN).whereEqualTo( "id", orderWithUser.getOrder().getIdWorkman()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(!task.isSuccessful()){
                    orderWithUser.setWorkMan(null);
                }else{
                    if (!task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        WorkMan workMan = WorkMan.fromMap(document.getData());
                        WorkMainWithIdDocument workMainWithIdDocument = new WorkMainWithIdDocument(document.getId(),workMan);
                        orderWithUser.setWorkMan(workMainWithIdDocument);

                    }
                }
                resultListener.onSuccess(orderWithUser);
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
                        Order order = Order.toObject(doc.getId(),doc.getData());
                        orders.add(order);
                    }

                    resultListener.onSuccess(orders);
                } else {
//                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    public  void getListOrderWithIdWorker(String idWorker, ResultListener<List<Order>> resultListener){
        db.collection(CollectionName.ORDER).whereEqualTo("idWorkman",idWorker).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Order> orders = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    Order order = Order.toObject(doc.getId(),doc.getData());
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

    public  void getListOrderWithIdUser(String idUser, ResultListener<List<Order>> resultListener){
        db.collection(CollectionName.ORDER).whereEqualTo("idUser",idUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Order> orders = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    Order order = Order.toObject(doc.getId(),doc.getData());
                    orders.add(order);
                }

                resultListener.onSuccess(orders);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultListener.onError(e);
            }
        });


    }

    public  void getListOrderFinishWithIdUser(String idUser, ResultListener<List<Order>> resultListener){
        db.collection(CollectionName.ORDER_FINISH).whereEqualTo("idUser",idUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Order> orders = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    Order order = Order.toObject(doc.getId(),doc.getData());
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


    public  void finishOrder(String idOrder , ResultListener<Boolean> resultListener){
        getOrderDetailWithUser(idOrder, new ResultListener<OrderWithUser>() {
            @Override
            public void onSuccess(OrderWithUser result) {
                /// order detail move to collection
                WriteBatch batch = db.batch();
//                Date dateNow = new Date();
                Order orderFinish  = result.getOrder();
                batch.set(db.collection(CollectionName.ORDER_FINISH).document(),orderFinish.createMap());
                batch.delete(db.collection(CollectionName.ORDER).document(orderFinish.getId()));

                batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("debug: User AND GUARD added successfully.");
                        resultListener.onSuccess(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        resultListener.onError(new Throwable("Terjadi kesalahan"));

                    }
                });
            }

            @Override
            public void onError(Throwable error) {
                    resultListener.onError(error);
            }
        });
    }

    public  void confirmOrder(String idOrder , ResultListener<Boolean> resultListener){
        getOrderDetailWithUser(idOrder, new ResultListener<OrderWithUser>() {
            @Override
            public void onSuccess(OrderWithUser result) {

                DocumentReference docRef = db.collection(CollectionName.ORDER).document(idOrder);

                /// order detail move to collection
                WriteBatch batch = db.batch();
                Order order  = result.getOrder();
                order.setStatusTransaction(1);
                batch.update(docRef,order.toTransactionUpdate());
                batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("debug: User AND GUARD added successfully.");
                        resultListener.onSuccess(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        resultListener.onError(new Throwable("Terjadi kesalahan"));

                    }
                });
            }

            @Override
            public void onError(Throwable error) {
                resultListener.onError(error);
            }
        });
    }

    public  void rejectOrder(String idOrder , ResultListener<Boolean> resultListener){
        getOrderDetailWithUser(idOrder, new ResultListener<OrderWithUser>() {
            @Override
            public void onSuccess(OrderWithUser result) {

                DocumentReference docRef = db.collection(CollectionName.ORDER).document(idOrder);

                /// order detail move to collection
                WriteBatch batch = db.batch();
                Order order  = result.getOrder();
                order.setStatusTransaction(-1);
                batch.update(docRef,order.toTransactionUpdate());
                batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("debug: User AND GUARD added successfully.");
                        resultListener.onSuccess(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        resultListener.onError(new Throwable("Terjadi kesalahan"));

                    }
                });
            }

            @Override
            public void onError(Throwable error) {
                resultListener.onError(error);
            }
        });
    }

}

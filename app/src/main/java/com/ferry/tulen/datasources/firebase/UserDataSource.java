package com.ferry.tulen.datasources.firebase;

import androidx.annotation.NonNull;

import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.User;
import com.ferry.tulen.datasources.models.UserWithIdDocument;
import com.ferry.tulen.datasources.models.WorkMainWithIdDocument;
import com.ferry.tulen.datasources.models.WorkMan;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

public class UserDataSource {

    private static UserDataSource instance;

    private FirebaseFirestore db;

    private UserDataSource(FirebaseFirestore db) {
        this.db = db;
    }

    public static UserDataSource getInstance(FirebaseFirestore db) {
        if (instance == null) {
            instance = new UserDataSource(db);
        }
        return instance;
    }


    public void setUser(String idUser, String fullName, String address, String phoneNumber, String job, ResultListener<Boolean> resultListener) {
            getDataUser(idUser, new ResultListener<UserWithIdDocument>() {
                @Override
                public void onSuccess(UserWithIdDocument result) {

                    /// save result
                    WriteBatch batch = db.batch();
                    DocumentReference docRef = db.collection(CollectionName.USER).document(result.getIdDocument());

                    User user = result.getUser();

                    user.setFullName(fullName);
                    user.setJob(job);
                    user.setAddress(address);
                    user.setPhoneNumber(phoneNumber);
                    batch.update(docRef,user.toUpdateMap());

                    batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            resultListener.onSuccess(true);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            resultListener.onError(e);
                        }
                    });

                }
                @Override
                public void onError(Throwable error) {
                        resultListener.onError(error);
                }
            });

    }

    public void setWorkerAddressAndPrice(String idUser, String address, String minPrice, String maxPrice, ResultListener<Boolean> resultListener) {
        getWorkMan(idUser, new ResultListener<WorkMainWithIdDocument>() {
            @Override
            public void onSuccess(WorkMainWithIdDocument result) {

                /// save result
                WriteBatch batch = db.batch();
                DocumentReference docRef = db.collection(CollectionName.WORK_MAN).document(result.getIdDocument());

                WorkMan workMan = result.getWorkMan();
                workMan.setAddress(address);
                workMan.setPriceMin(minPrice);
                workMan.setPriceMax(maxPrice);
                batch.update(docRef,workMan.toUpdateAddressAndPrice());

                batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        resultListener.onSuccess(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        resultListener.onError(e);
                    }
                });

            }
            @Override
            public void onError(Throwable error) {
                resultListener.onError(error);
            }
        });

    }

    public void setWorkMan(String idUser, String fullName, String address, String phoneNumber, String job, ResultListener<Boolean> resultListener) {
        getWorkMan(idUser, new ResultListener<WorkMainWithIdDocument>() {
            @Override
            public void onSuccess(WorkMainWithIdDocument result) {

                /// save result
                WriteBatch batch = db.batch();
                DocumentReference docRef = db.collection(CollectionName.WORK_MAN).document(result.getIdDocument());

                WorkMan workMan = result.getWorkMan();

                workMan.setFullName(fullName);
                workMan.setJob(job);
                workMan.setAddress(address);
                workMan.setPhoneNumber(phoneNumber);
                batch.update(docRef,workMan.toMapUpdate());

                batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        resultListener.onSuccess(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        resultListener.onError(e);
                    }
                });

            }
            @Override
            public void onError(Throwable error) {
                resultListener.onError(error);
            }
        });

    }


    public void getDataUser(String idUser, ResultListener<UserWithIdDocument> resultListener) {
        db.collection(CollectionName.USER).whereEqualTo(
                "id", idUser
        ).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                    User user = User.fromMap(document.getData());
                    resultListener.onSuccess(new UserWithIdDocument(document.getId(),
                            user));
                } else {
                    // Handle no document found
                    resultListener.onError(new Throwable("User tidak ditemukan"));
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultListener.onError(e);
            }
        });
    }

    public void getWorkMan(String idUser, ResultListener<WorkMainWithIdDocument> resultListener) {
        db.collection(CollectionName.WORK_MAN).whereEqualTo(
                "id", idUser
        ).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                    WorkMan workMan = WorkMan.fromMap(document.getData());
                    resultListener.onSuccess(new WorkMainWithIdDocument(document.getId(),workMan));
                } else {
                    // Handle no document found
                    resultListener.onError(new Throwable("Pekerja tidak ditemukan"));
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultListener.onError(e);
            }
        });
    }

}

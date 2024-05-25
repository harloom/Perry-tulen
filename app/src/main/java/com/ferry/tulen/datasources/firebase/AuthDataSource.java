package com.ferry.tulen.datasources.firebase;

import android.content.Context;

import androidx.annotation.NonNull;

import com.ferry.tulen.datasources.SharedPreferences.SharedPreferenceHelper;
import com.ferry.tulen.datasources.listener.ResultListener;
import com.ferry.tulen.datasources.models.GuardUser;
import com.ferry.tulen.datasources.models.GuardWorkMan;
import com.ferry.tulen.datasources.models.TokenNotification;
import com.ferry.tulen.datasources.models.User;
import com.ferry.tulen.datasources.models.WorkMan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.Objects;


public class AuthDataSource {


    private static AuthDataSource instance;

    private FirebaseFirestore db;

    private AuthDataSource(FirebaseFirestore db) {
        this.db = db;
    }

    public static AuthDataSource getInstance(FirebaseFirestore db) {
        if (instance == null) {
            instance = new AuthDataSource(db);
        }
        return instance;
    }

    public  void logout(Context context, ResultListener<Boolean> resultListener){
        try {
            SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(context);
            sharedPreferenceHelper.remove(SharedPreferenceHelper.KEY_ID_USER);
            sharedPreferenceHelper.remove(SharedPreferenceHelper.KEY_TYPE_LOGIN);
            resultListener.onSuccess(true);
        }catch (Exception e){
            resultListener.onError(e);
        }
    }

    public void registerUser(String email, String password, ResultListener<String> resultListener) {
        checkUsageEmail(1, email, new ResultListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result.equals(true)) {
                    resultListener.onError(new Throwable("Email Sudah Terdaftar"));
                    return;
                }

                WriteBatch batch = db.batch();
                String generateId = "USER_" + System.currentTimeMillis();
                User user = new User(generateId, email, "", "", "", "");
                GuardUser guardUser = new GuardUser(user.getId(), password);
                batch.set(db.collection(CollectionName.USER).document(), user.toMap());
                batch.set(db.collection(CollectionName.GUARD_USER).document(), guardUser.toMap());

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
            @Override
            public void onError(Throwable error) {
                resultListener.onError(new Throwable("Terjadi kesalahan"));
            }
        });


    }


    public void registerWorkMan(String email, String password, ResultListener<String> resultListener) {
        checkUsageEmail(2,email, new ResultListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result.equals(true)) {
                    resultListener.onError(new Throwable("Email Tukang Sudah Terdaftar"));
                    return;
                }

                WriteBatch batch = db.batch();
                String generateId = "WORKMAN_" + System.currentTimeMillis();
                WorkMan user = new WorkMan(generateId, email, "", "", "", "",0.0);
                GuardWorkMan guardWorkMan = new GuardWorkMan(user.getId(), password);
                batch.set(db.collection(CollectionName.WORK_MAN).document(), user.toMap());
                batch.set(db.collection(CollectionName.GUARD_WORK_MAN).document(), guardWorkMan.toMap());

                batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("debug: Workman AND GUARD added successfully.");
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
            @Override
            public void onError(Throwable error) {
                resultListener.onError(new Throwable("Terjadi kesalahan"));
            }
        });


    }

    public void login(int typeLogin, String email, String password, ResultListener resultListener) {

        if (typeLogin == 1) {
            db.collection(CollectionName.USER).whereEqualTo("email", email).get().addOnCompleteListener(
                    new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()) {
                                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                    User user = User.fromMap(document.getData());
                                    comparePasswordUser(user, password, resultListener);
                                } else {
                                    // Handle no document found
                                    resultListener.onError(new Throwable("Email/password salah"));
                                }
                            } else {
                                resultListener.onError(new Throwable("Terjadi kesalahan, coba lagi"));
                            }
                        }
                    }
            );
        } else if (typeLogin == 2) {
            db.collection(CollectionName.WORK_MAN).whereEqualTo("email", email).get().addOnCompleteListener(
                    new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()) {
                                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                    WorkMan workMan = WorkMan.fromMap(document.getData());
                                    comparePasswordWorkMan(workMan, password, resultListener);
                                    // Do something with the document
                                } else {
                                    // Handle no document found
                                    resultListener.onError(new Throwable("Email/password salah"));
                                }
                            } else {
                                resultListener.onError(new Throwable("Terjadi kesalahan, coba lagi"));
                            }
                        }
                    }
            );
        } else {

            /// not access
        }


    }

    private void checkUsageEmail(int typeLogin, String email, ResultListener<Boolean> resultListener) {

        String collectionName = typeLogin == 1 ? CollectionName.USER : CollectionName.WORK_MAN;

        db.collection(collectionName).whereEqualTo("email", email).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot.isEmpty()) {

                                System.out.println("debug: email is Empty");
                                resultListener.onSuccess(false);
                                // Do something with the document
                            } else {
                                // Handle  document found
                                System.out.println("debug: email is Not Empty");

//                                true bearti ada gunakan
                                resultListener.onSuccess(true);
                            }
                        } else {
                            resultListener.onError(new Throwable("Terjadi kesalahan, coba lagi"));
                        }
                    }
                }
        );
    }

    private void comparePasswordUser(User user, String password, ResultListener<User> resultListener) {
        db.collection(CollectionName.GUARD_USER).whereEqualTo("idUser", user.getId()).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            GuardUser guardUser = GuardUser.fromMap(document.getData());

                            if (password.equals(Objects.requireNonNull(guardUser).getPassword())) {
                                resultListener.onSuccess(user);
                            } else {
                                resultListener.onError(new Throwable("Email/password salah"));
                            }

                            // Do something with the document
                        } else {
                            // Handle no document found
                            resultListener.onError(new Throwable("Email/password salah"));
                        }
                    } else {
                        resultListener.onError(new Throwable("Terjadi kesalahan, coba lagi"));
                    }
                }
        );
    }

    private void comparePasswordWorkMan(WorkMan workMan, String password, ResultListener<WorkMan> resultListener) {
        db.collection(CollectionName.GUARD_WORK_MAN).whereEqualTo("idUser", workMan.getId()).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            GuardWorkMan guardWorkMan = GuardWorkMan.fromMap(document.getData());

                            if (password.equals(Objects.requireNonNull(guardWorkMan).getPassword())) {
                                resultListener.onSuccess(workMan);
                            } else {
                                resultListener.onError(new Throwable("Email/password salah"));
                            }

                            // Do something with the document
                        } else {
                            // Handle no document found
                            resultListener.onError(new Throwable("Email/password salah"));
                        }
                    } else {
                        resultListener.onError(new Throwable("Terjadi kesalahan, coba lagi"));
                    }
                }
        );
    }

    public void setToken(String id, String token, ResultListener<String> resultListener){
        db.collection(CollectionName.TOKEN_NOTIF).document(id).set(new TokenNotification(
                token
        ).setTokenMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
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

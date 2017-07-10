package com.vishesh.doctorprofile;

import io.reactivex.Observable;

/**
 * Created by vishesh on 10/7/17.
 */

public class Model {

    private Observable<Profile> profileObservable;

    /*Observable<Profile> profileObservable() {

        PublishSubject<Boolean> networkChangeSubject = PublishSubject.create();

        profileObservable = Observable.create(new ObservableOnSubscribe<Profile>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Profile> e) throws Exception {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("docProfile");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Profile profile = dataSnapshot.getValue(Profile.class);
                        e.onNext(profile);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Throwable throwable = new Throwable(databaseError.getMessage());
                        e.onError(throwable);
                    }
                });
            }
        });
        return networkChangeSubject;
    }*/
}

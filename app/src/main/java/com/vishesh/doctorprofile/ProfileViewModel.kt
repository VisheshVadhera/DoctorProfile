package com.vishesh.doctorprofile

import com.google.firebase.database.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import java.util.*

/**
 * Created by vishesh on 10/7/17.
 */
class ProfileViewModel {

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var networkConnectivityObservable: Observable<Boolean>

    fun initialize() {
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.setLogLevel(Logger.Level.DEBUG)
    }

    fun createProfileObservable(): Observable<Profile> {

        val profileObservable = Observable.create<Profile> { e ->
            val databaseReference = firebaseDatabase.getReference("docProfile")

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val profile = dataSnapshot.getValue(Profile::class.java)
                    e?.onNext(profile)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    e?.onError(Throwable(databaseError.message))
                }
            })
        }
        return profileObservable
    }

    fun connectivityToastObservable(): Observable<Boolean> {
        val observables = Arrays.asList<Observable<out Any>>(createNetworkConnectedObservable().toFlowable(BackpressureStrategy.LATEST).toObservable(),
                createNetworkNotConnectedObservable().toFlowable(BackpressureStrategy.LATEST)?.toObservable())

        return Observable.combineLatest(observables, { objects ->
            val connectedDate = objects[0] as Date;
            val notConnectedDate = objects[1] as Date;
            connectedDate.after(notConnectedDate)
        }).filter({ isConnected -> isConnected == true })
    }

    fun noConnectivityToastObservable(): Observable<Boolean> {
        return createNetworkNotConnectedObservable()
                .map { true }
    }

    private fun createNetworkConnectedObservable(): Observable<Date> {
        return networkConnectivityObservable
                .filter({ connected -> connected == true })
                .map { Date() }
    }

    private fun createNetworkNotConnectedObservable(): Observable<Date> {
        return networkConnectivityObservable
                .filter { connected -> connected == false }
                .map { Date() }
    }

    fun createNetworkConnectivityObservable(): Observable<Boolean> {
        var databaseReference = firebaseDatabase.getReference(".info/connected")

        networkConnectivityObservable = Observable.create<Boolean> { e ->
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val connected = snapshot.getValue(Boolean::class.java)
                    println(" Connected " + connected);
                    if (connected) {
                        e.onNext(true)
                    } else {
                        e.onNext(false)
                    }
                }
            })
        }
        return networkConnectivityObservable;
    }
}
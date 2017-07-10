package com.vishesh.doctorprofile

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity() : AppCompatActivity() {

    lateinit var viewModel: ProfileViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        viewModel = ProfileViewModel();

        viewModel.initialize();

        viewModel.createProfileObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ profile -> showProfile(profile) },
                        { e -> e.message?.let { showError(it) } })

        viewModel.createNetworkConnectivityObservable()

        viewModel.connectivityToastObservable()
                .subscribe({
                    Toast.makeText(this, "Connected to the internet", Toast.LENGTH_SHORT).show()
                })

        viewModel.noConnectivityToastObservable()
                .subscribe({ Toast.makeText(this, "Trying to connect to the internet", Toast.LENGTH_SHORT).show() })
    }

    private fun showProfile(profile: Profile) {
        setErrorViewVisibility(View.GONE)
        setProfileDataVisibility(View.VISIBLE)
        setProfileData(profile)
    }

    private fun showError(message: String) {
        setErrorViewVisibility(View.VISIBLE)
        setProfileDataVisibility(View.GONE)
        this.text_error.setText(message);
    }

    private fun setProfileDataVisibility(visibility: Int) {
        this.text_first_name.visibility = visibility
        this.text_last_name.visibility = visibility
        this.text_age.visibility = visibility
    }

    private fun setProfileData(profile: Profile) {
        this.text_first_name.setText(profile?.firstName)
        this.text_last_name.setText(profile?.lastName)
        this.text_age.setText(profile?.age.toString())
    }

    private fun setErrorViewVisibility(visibility: Int) {
        this.text_error.visibility = visibility
    }
}

package com.app.fr.getmymask.ui.authentification.register

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.app.fr.getmymask.Constants

import com.app.fr.getmymask.R
import com.app.fr.getmymask.core.BaseFragment
import com.app.fr.getmymask.helpers.SharedPref
import com.app.fr.getmymask.helpers.extensions.isValidEmail
import com.app.fr.getmymask.ui.around.AroundActivity
import com.app.fr.getmymask.ui.authentification.HomeAuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.btn_register
import kotlinx.android.synthetic.main.fragment_login.et_email
import kotlinx.android.synthetic.main.fragment_login.et_password
import kotlinx.android.synthetic.main.fragment_register.*
import org.jetbrains.anko.toast

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : BaseFragment() {

    private lateinit var homeAuthViewModel: HomeAuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeAuthViewModel = ViewModelProviders.of(activity!!).get(HomeAuthViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        homeAuthViewModel.getResponseRegister().observe(viewLifecycleOwner, Observer { response ->
            if (response == 200) {
                activity.toast("Inscription reussie")
                homeAuthViewModel.loginUser(et_email.text.toString(), et_password.text.toString())
            } else {
                activity.hideProgressDialog()
                activity.toast("L'adresse email exist deja")
            }
        })
        homeAuthViewModel.getResponseLogin().observe(viewLifecycleOwner, Observer { response ->
            if (response){
                activity.hideProgressDialog()
                saveCredentials(et_email.text.toString(), et_password.text.toString())
                startAroundActivity()
            }
        })
        btn_register.setOnClickListener {
            if (!checkFieldValid()) {
                activity.toast("L'adresse email est incorrecte") }
            else {
                activity.showProgressDialog()
                homeAuthViewModel.registerUser(et_email.text.toString(),et_password.text.toString())
            }

        }
        iv_back.setOnClickListener {
            findNavController().navigateUp()
        }


    }
    private fun saveCredentials(email: String, password: String) {
        SharedPref.putString(activity, Constants.EMAIL_KEY, email)
        SharedPref.putString(activity, Constants.PASSWORD_KEY, password)
    }

    private fun startAroundActivity() {
        val intent = Intent(activity, AroundActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun checkFieldValid(): Boolean {
        return if (et_email!!.text.toString().isEmpty()
            || et_password!!.text.toString().isEmpty()
        ) false else et_email!!.text.toString().isValidEmail()
    }


}

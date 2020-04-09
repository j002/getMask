package com.app.fr.getmymask.ui.authentification.login


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import org.jetbrains.anko.toast


class LoginFragment : BaseFragment() {

    private lateinit var homeAuthViewModel: HomeAuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeAuthViewModel = ViewModelProviders.of(activity!!).get(HomeAuthViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeAuthViewModel.getResponseLogin().observe(viewLifecycleOwner, Observer { response ->
            if (response){
                activity.hideProgressDialog()
                saveCredentials(et_email.text.toString(), et_password.text.toString())
                startAroundActivity()
            }else{
                activity.hideProgressDialog()
                activity.toast("Email ou mot de passe incorrectes verrifier vos identifiants ou créer un compte ")

            }


        })
        btn_login.setOnClickListener {

            if (!checkFieldValid()) {
                activity.toast("L'adresse email est incorrecte") }
               else {
                activity.showProgressDialog()
                homeAuthViewModel.loginUser(et_email.text.toString(),et_password.text.toString())
            }

        }
        btn_register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

    }

    override fun onResume() {
        super.onResume()
        //  setHint()

    }

    private fun startAroundActivity() {
        val intent = Intent(activity, AroundActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }


    private fun saveCredentials(email: String, password: String) {
        SharedPref.putString(activity, Constants.EMAIL_KEY, email)
        SharedPref.putString(activity, Constants.PASSWORD_KEY, password)
    }


    private fun checkFieldValid(): Boolean {
        return if (et_email!!.text.toString().isEmpty()
            || et_password!!.text.toString().isEmpty()
        ) false else et_email!!.text.toString().isValidEmail()
    }


}

package com.example.tiendavirtualapp.cliente

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tiendavirtualapp.Constantes
import com.example.tiendavirtualapp.R
import com.example.tiendavirtualapp.databinding.ActivityLoginTelefonoBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class LoginTelefonoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginTelefonoBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth

    private var forceResendingToken : ForceResendingToken ?= null
    private lateinit var mCallback : OnVerificationStateChangedCallbacks
    private var mVerification : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginTelefonoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.rlTelefono.visibility = View.VISIBLE
        binding.rlCodigoVerificacion.visibility = View.GONE

        phoneLoginCallbacks()

        binding.btnEnviarCodigo.setOnClickListener{
            validarData()
        }

        binding.btnVerificarCod.setOnClickListener{
            val otp = binding.etCodVer.text.toString().trim()
            if (otp.isEmpty()){
                binding.etCodVer.error = "Ingrese código"
                binding.etCodVer.requestFocus()
            }else if(otp.length < 6){
                binding.etCodVer.error = "El código debe contener 6 car."
                binding.etCodVer.requestFocus()
            }else{
                verificarCodigoTelefono(otp)
            }
        }

        binding.tvReenviarCod.setOnClickListener {
            if (forceResendingToken != null){
                reenviarCodigoVerificacion()
            }else{
                Toast.makeText(this, "No se puede reenviar el código", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun verificarCodigoTelefono(otp: String) {
        progressDialog.setMessage("Verificando código")
        progressDialog.show()

        val credencial = PhoneAuthProvider.getCredential(mVerification!!, otp)
        signInWithPhoneAuthCredencial(credencial)

    }

    private fun signInWithPhoneAuthCredencial(credencial: PhoneAuthCredential) {
        progressDialog.setMessage("Ingresando")
        progressDialog.show()

        firebaseAuth.signInWithCredential(credencial)
            .addOnSuccessListener { authResult ->
                if (authResult.additionalUserInfo!!.isNewUser){
                    guardarInformacion()
                }else{
                    startActivity(Intent(this, MainActivityCliente::class.java))
                    finishAffinity()
                }
            }

    }

    private fun guardarInformacion() {
        progressDialog.setMessage("Guardando información")
        progressDialog.show()

        val uid = firebaseAuth.uid
        val tiempoReg = Constantes().obtenerTiempoD()

        val datosCliente = HashMap<String, Any>()

        datosCliente["uid"] = "${uid}"
        datosCliente["nombres"] = ""
        datosCliente["telefono"] = "${codigoTelnumeroTel}"
        datosCliente["tRegistro"] = tiempoReg
        datosCliente["imagen"] = ""
        datosCliente["tipoUsuario"] = "Cliente"

        val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
        reference.child(uid!!)
            .setValue(datosCliente)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityCliente::class.java))
                finishAffinity()
            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()
                Toast.makeText(
                    this@LoginTelefonoActivity,
                    "${e.message}",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun reenviarCodigoVerificacion() {
        progressDialog.setMessage("Enviando código a ${numeroTelefono}")
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(codigoTelnumeroTel)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallback)
            .setForceResendingToken(forceResendingToken!!)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private var codigoTelefono = "" //+57
    private var numeroTelefono = "" //3207894561
    private var codigoTelnumeroTel = "" //+57 3207894561

    private fun validarData() {
        codigoTelefono = binding.telCodePicker.selectedCountryCodeWithPlus
        numeroTelefono = binding.etTelefonoC.text.toString().trim()
        codigoTelnumeroTel = codigoTelefono + numeroTelefono

        if (numeroTelefono.isEmpty()){
            binding.etTelefonoC.error = "Ingrese número telefónico"
            binding.etTelefonoC.requestFocus()
        }else{
            verificarNumeroTelefonico()
        }
    }

    private fun verificarNumeroTelefonico() {
        progressDialog.setMessage("Enviando código a ${numeroTelefono}")
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(codigoTelnumeroTel)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallback)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun phoneLoginCallbacks() {

        mCallback = object : OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(verificationId: String, token: ForceResendingToken) {
                mVerification = verificationId
                forceResendingToken = token

                progressDialog.dismiss()
                binding.rlTelefono.visibility = View.GONE
                binding.rlCodigoVerificacion.visibility = View.VISIBLE

                Toast.makeText(this@LoginTelefonoActivity, "Enviando código ${codigoTelnumeroTel}", Toast.LENGTH_SHORT).show()

            }
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                signInWithPhoneAuthCredencial(phoneAuthCredential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                progressDialog.dismiss()
                Toast.makeText(
                    this@LoginTelefonoActivity,
                    "Falló la verificación debido a ${e.message}",
                    Toast.LENGTH_SHORT).show()
            }

        }




















    }
}
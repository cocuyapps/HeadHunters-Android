package pe.com.headhunters.activities.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import pe.com.headhunters.R
import pe.com.headhunters.activities.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var txtUserName: EditText
    private lateinit var txtPassword: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var loginBtn: Button
    private lateinit var toRegisterBtn: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtUserName = findViewById(R.id.txtUserName)
        txtPassword = findViewById(R.id.txtPassword)
        progressBar = findViewById(R.id.progressBar)
        toRegisterBtn = findViewById(R.id.toRegisterBtn)
        loginBtn = findViewById(R.id.login)

        auth = FirebaseAuth.getInstance()
    }
    fun forgotPassword(view: View){

    }

    fun ventanaRegister(view: View){
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    fun login(view: View){
        loginUser()
    }

    private fun loginUser(){
        val user:String = txtUserName.text.toString()
        val password:String = txtPassword.text.toString()

        if(!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)){
            progressBar.visibility = View.VISIBLE
            loginBtn.visibility = View.GONE
            toRegisterBtn.visibility = View.GONE
            auth.signInWithEmailAndPassword(user, password)
                .addOnCompleteListener(this){
                        task ->

                    if(task.isSuccessful){
                        action()
                    }
                    else{
                        progressBar.visibility = View.GONE
                        loginBtn.visibility = View.VISIBLE
                        toRegisterBtn.visibility = View.VISIBLE
                        Toast.makeText(this,"Authentication error", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun action(){
        startActivity(Intent(this, MainActivity::class.java))
    }

}

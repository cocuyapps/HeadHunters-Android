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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import pe.com.headhunters.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var txtUserName: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var registerBtn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //layout views
        txtUserName = findViewById(R.id.txtUserName)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        registerBtn = findViewById(R.id.registerBtn)
        progressBar = findViewById(R.id.progressBar)
        //firebase concern
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        //Reference to read and write in a location
        dbReference = database.reference.child("User")

    }

    fun ventanaLogin(view: View){
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun register(view: View){
        createNewAccount()
    }

    private fun createNewAccount(){
        val username: String = txtUserName.text.toString()
        val email: String = txtEmail.text.toString()
        val password: String = txtPassword.text.toString()

        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            progressBar.visibility = View.VISIBLE
            registerBtn.setVisibility(View.GONE);
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){
                        task ->

                    if(task.isComplete){
                        val user: FirebaseUser?=auth.currentUser
                        verifyEmail(user)

                        val userBD=dbReference.child(user?.uid.toString())

                        userBD.child("UserName").setValue(username)
                        action()
                    } else {
                        progressBar.visibility = View.GONE
                        registerBtn.visibility = View.VISIBLE
                    }
                }
        }
    }

    private fun verifyEmail(user: FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                    task ->

                if(task.isComplete){
                    Toast.makeText(this,"Email enviado", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this,"Error al enviar el Email", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun action(){
        startActivity(Intent(this, LoginActivity::class.java))
    }
}

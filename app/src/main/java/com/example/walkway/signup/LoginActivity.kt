package com.example.walkway.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.walkway.R
import com.example.walkway.SignUpActivity
import com.example.walkway.map.MainMapActivity
import com.example.walkway.model.entitiy.login.LoginResponse
import com.example.walkway.model.entitiy.login.LoginModel
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : AppCompatActivity() {


    var loginModel: LoginModel = LoginModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)


        //login 버튼을 클릭하면 다음과 같은 동작을 하겠다.
        login.setOnClickListener {
            check()
        }

        //signUp 버튼 눌렀을 떄

        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

       }


    fun check(){

        loginModel.Login(emailEditText.text.toString(),passwordTextview.text.toString()){

                isSuccess: Int, data: LoginResponse? ->


                if(data!!.statusCode==200){

                    login.setOnClickListener {

                        val intent = Intent(this,MainMapActivity::class.java)
                        startActivity(intent)
                    }

                }else{

                    Toast.makeText(applicationContext,"ID 또는 Password가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
                }



        }

    }
}
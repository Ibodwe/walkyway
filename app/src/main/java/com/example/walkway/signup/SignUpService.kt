package com.example.walkway.signup


//function for signUp text field verification
object SignUpService{


    //this
    fun checkEmailFormat(email : String) : Boolean{
        val regexEmail = Regex("^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]*\$")

        val result = regexEmail.matches(email)

        return result
    }

    fun checkPasswordFormat(password : String) : Boolean{

        val regexPassword= Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@\$!%^*#?&=*().]{8,}")

        val result = regexPassword.matches(password)

        return result
    }



}
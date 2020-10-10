package com.example.walkway

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.walkway.map.MainMapActivity
import com.example.walkway.signup.SignUpService
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,
    TextView.OnEditorActionListener {


    lateinit var emailWatcher: TextWatcher
    lateinit var passWordWatcher: TextWatcher
    lateinit var passWordCheckWatcher: TextWatcher
    lateinit var nickNameWatcher: TextWatcher
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var pictureIntent : Intent

    var isEmailProper = false
    var isPasswordProper = false
    var isPasswordReProper = false
    var isNickNameProper = false
    var isBirthProper: Boolean? = null
    var isGenderProper: Boolean? = null
    var isConditionPrpoer = false
    var isAgreementMarketingProper = false


    val cal = Calendar.getInstance()

    lateinit var radioGroupLisenter: RadioGroup.OnCheckedChangeListener

    //password 비교를 위한 변수
    private var passWord: CharSequence = ""



    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)



        initWatcher()

        birthField.setOnClickListener(this)
        birthField.setOnFocusChangeListener(this)

        agreementMarketingCheckBox.setOnClickListener(this)

        signupBtn.setOnClickListener(this)

    }

    // radioGroup 리스너
    // checkbox listener  필요


    fun initWatcher() {

        initEmailTextWatcher(emailFieldLayout)
        initCheckPasswordTextWatcher(passwordCheckFieldLayout)
        initPasswordTextWatcher(passwordFieldLayout)
        initNicknameTextWatcher(nickNameFieldLayout)

        emailField.addTextChangedListener(emailWatcher)
        passwordField.addTextChangedListener(passWordWatcher)
        passwordCheckField.addTextChangedListener(passWordCheckWatcher)
        nickNameField.addTextChangedListener(nickNameWatcher)

    }


    override fun onClick(v: View?) {

        Log.d("v click", v!!.id.toString())

        when (v.id) {

            R.id.birthField -> {

                DatePickerDialog(
                    this@SignUpActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()

            }


            R.id.signupBtn -> {


                startActivity(Intent(this,MainMapActivity::class.java))

            }


        }

        checkSignupBtnIsEnable()

    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {

        if (v!!.getId() == R.id.birthField && actionId == EditorInfo.IME_ACTION_DONE) {

            Toast.makeText(applicationContext, "test keyBoard", Toast.LENGTH_SHORT).show()
        }

        return false
    }


    override fun onFocusChange(v: View?, hasFocus: Boolean) {

        when (v!!.id) {

        }
    }


    fun initEmailTextWatcher(emailLayout: TextInputLayout) {

        emailWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEmailProper = SignUpService.checkEmailFormat(s.toString())

                if (isEmailProper) {
                    emailLayout.error = null
                    emailLayout.helperText = "이메일 형식과 일치합니다."

                } else {
                    emailLayout.error = "이메일 형식과 일치하여야 합니다."
                }
                checkSignupBtnIsEnable()
            }
        }

    }

    fun initPasswordTextWatcher(passWordLayout: TextInputLayout) {

        passWordWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isPasswordProper = SignUpService.checkPasswordFormat(s.toString())

                passWord = s.toString()

                if (isPasswordProper) {
                    passWordLayout.error = null
                    passWordLayout.helperText = "사용 가능합니다."


                } else {
                    passWordLayout.error = "최소한 1개의 소문자, 대문자, 숫자를 포함한 8자리 이상 이여야합니다. "
                }
                checkSignupBtnIsEnable()
            }
        }

    }

    fun initCheckPasswordTextWatcher(passwordCheckFieldLayout: TextInputLayout) {

        passWordCheckWatcher = object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                isPasswordReProper = s.toString().equals(passWord)

                Log.d("sString", s.toString())
                Log.d("firstPasswd", passWord.toString())



                if (isPasswordReProper) {
                    passwordCheckFieldLayout.error = null
                    passwordCheckFieldLayout.helperText = "일치합니다."

                } else {
                    passwordCheckFieldLayout.error = "일치하지 않습니다."
                }
                checkSignupBtnIsEnable()
            }
        }

    }


    fun initNicknameTextWatcher(nickNameField: TextInputLayout) {

        nickNameWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                if (s!!.length >= 8) {
                    isNickNameProper = false
                    nickNameField.error = "닉네임은 8글자를 넘을 수 없습니다."

                } else {
                    isNickNameProper = true
                    nickNameField.helperText = "사용가능 합니다."
                }

                checkSignupBtnIsEnable()
            }
        }

    }


    fun checkSignupBtnIsEnable() {

        signupBtn.isEnabled = isConditionPrpoer &&
                isEmailProper &&
                isPasswordProper &&
                isPasswordReProper &&
                isNickNameProper &&
                isBirthProper!! && isGenderProper != null
    }

    private fun updateDateInView() {
        val myFormat = "yyyy/MM/dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.KOREA)

        val currentTime = Calendar.getInstance()


        cal.get(Calendar.YEAR)


        //가입하는 사람의 생년월일이 100살 이하 14세 이하는 서버에서 로직 점검

        if (currentTime.get(Calendar.YEAR) - 100 < cal.get(Calendar.YEAR)) {
            birthField.setText(sdf.format(cal.getTime()))
            isBirthProper = true

            hideKeyboard()

        } else {
            Toast.makeText(applicationContext, "생년월일을 다시 선택 해주세요", Toast.LENGTH_SHORT).show()
        }


    }

    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(birthField.windowToken, 0)
    }

}
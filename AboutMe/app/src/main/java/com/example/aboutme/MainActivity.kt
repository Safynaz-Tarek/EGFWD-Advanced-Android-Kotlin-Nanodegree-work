package com.example.aboutme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.aboutme.databinding.ActivityMainBinding
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val myName: MyName = MyName("Safynaz Tarek")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.myName = myName
        binding.doneButton.setOnClickListener {
            addNickName(it)
        }
    }

    private fun addNickName(view:View) {
        // View here is the button
        binding.apply {
//            nicknameText.text = nicknameEdit.text
            //In order to refresh the UI with new Data
            myName?.nickName = nicknameEdit.text.toString()
            invalidateAll()
            nicknameEdit.visibility = View.GONE
            view.visibility = View.GONE
            nicknameText.visibility = View.VISIBLE
        }
        //To hide the keyboard after the user is done typing
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
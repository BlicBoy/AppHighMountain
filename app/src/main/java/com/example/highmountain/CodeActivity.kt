package com.example.highmountain

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.SparseArray
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.highmountain.databinding.ActivityCodeBinding
import com.example.highmountain.ui.PREF_CODE
import kotlinx.android.synthetic.main.activity_code.*


class CodeActivity : AppCompatActivity(), View.OnClickListener {

    //lateinit var binding : ActivityCodeBinding

    private var keyValues = SparseArray<String>()
    var code = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code)

        //binding = ActivityCodeBinding.inflate(layoutInflater)
        //supportActionBar?.hide()
        //setContentView(binding.root)
       //
        //binding.buttonEntrar.setOnClickListener {
        //  if (this@CodeActivity.PREF_CODE == binding.editTextNumberPasswordInicio.text.toString()){
        //      startActivity(Intent(this@CodeActivity,MainActivity::class.java))
        //  }else{
        //      Toast.makeText(baseContext, "Erro",Toast.LENGTH_LONG).show()
        //  }
        //}

        if(Build.VERSION.SDK_INT >= 21) {
            editTexts.showSoftInputOnFocus = false
        }
        else if (Build.VERSION.SDK_INT >= 11) {
            editTexts.setRawInputType(InputType.TYPE_CLASS_TEXT)
            editTexts.setTextIsSelectable(true)
        }
        else {
            editTexts.setRawInputType(InputType.TYPE_NULL)
            editTexts.isFocusable = true
        }

        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        button0.setOnClickListener(this)

        delete.setOnClickListener(this)

        keyValues.put(R.id.button0, "0")
        keyValues.put(R.id.button1, "1")
        keyValues.put(R.id.button2, "2")
        keyValues.put(R.id.button3, "3")
        keyValues.put(R.id.button4, "4")
        keyValues.put(R.id.button5, "5")
        keyValues.put(R.id.button6, "6")
        keyValues.put(R.id.button7, "7")
        keyValues.put(R.id.button8, "8")
        keyValues.put(R.id.button9, "9")

        delete.setOnClickListener {
            var lenght : Int = editTexts.text?.length? : 0

            if (lenght > 0) {
                editTexts.text?.delete(lenght-1, lenght)
                code.deleteCharAt(lenght-1)
            }
        }



    }

    override fun onClick(p0: View?) {
        var value : String = keyValues.get(v!!.id)

        if (code.length < 4) {
            code.append(value)
            editTexts.setText(code)
        }
        else {
            Toast.makeText(this,"Ultrapassou o limite de números", Toast.LENGTH_SHORT).show()
        }
    }
}
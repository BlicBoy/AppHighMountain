package com.example.highmountain;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class CodeActivity extends AppCompatActivity implements View.OnClickListener{

    View view1, view2, view3, view4;
    MaterialCardView button1, button2, button3, button4, button5, button6, button7, button8, button9, button0;
    ImageView delete;

    ArrayList<String> numbers_list = new ArrayList<>();
    String passCode = "";
    String num1, num2, num3, num4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        inicializeComponents();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void inicializeComponents() {
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        button0 = findViewById(R.id.button0);

        delete = findViewById(R.id.delete);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button0.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                numbers_list.add("1");
                passNumber(numbers_list);
                break;
            case R.id.button2:
                numbers_list.add("2");
                passNumber(numbers_list);
                break;
            case R.id.button3:
                numbers_list.add("3");
                passNumber(numbers_list);
                break;
            case R.id.button4:
                numbers_list.add("4");
                passNumber(numbers_list);
                break;
            case R.id.button5:
                numbers_list.add("5");
                passNumber(numbers_list);
                break;
            case R.id.button6:
                numbers_list.add("6");
                passNumber(numbers_list);
                break;
            case R.id.button7:
                numbers_list.add("7");
                passNumber(numbers_list);
                break;
            case R.id.button8:
                numbers_list.add("8");
                passNumber(numbers_list);
                break;
            case R.id.button9:
                numbers_list.add("9");
                passNumber(numbers_list);
                break;
            case R.id.button0:
                numbers_list.add("0");
                passNumber(numbers_list);
                break;
            case R.id.delete:
                numbers_list.clear();
                passNumber(numbers_list);
                break;
        }
    }

    private void passNumber(ArrayList<String> numbers_list) {

        if (numbers_list.size() == 0) {
            view1.setBackgroundResource(R.drawable.bg_view_grey_oval);
            view2.setBackgroundResource(R.drawable.bg_view_grey_oval);
            view3.setBackgroundResource(R.drawable.bg_view_grey_oval);
            view4.setBackgroundResource(R.drawable.bg_view_grey_oval);
        } else {
            switch (numbers_list.size()) {
                case 1:
                    num1 = numbers_list.get(0);
                    view1.setBackgroundResource(R.drawable.bg_view_blue_oval);
                    break;
                case 2:
                    num2 = numbers_list.get(0);
                    view2.setBackgroundResource(R.drawable.bg_view_blue_oval);
                    break;
                case 3:
                    num3 = numbers_list.get(0);
                    view3.setBackgroundResource(R.drawable.bg_view_blue_oval);
                    break;
                case 4:
                    num4 = numbers_list.get(0);
                    view4.setBackgroundResource(R.drawable.bg_view_blue_oval);
                    passCode = num1 + num2 + num3 + num4;
                    if (getPassCode().length() == 0) {
                        savedPassCode(passCode);
                        } else {
                        matchPassCode();
                    }
                    break;
            }
        }
    }

    private void matchPassCode() {
        if (getPassCode().equals(passCode)) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Toast.makeText(this, "Pin Incorreto", Toast.LENGTH_SHORT).show();
        }
    }

    private SharedPreferences.Editor savedPassCode(String passCode) {
        SharedPreferences preferences = getSharedPreferences("passcode_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("passcode", passCode);
        editor.commit();

        return editor;
    }

    private String getPassCode() {
        SharedPreferences preferences = getSharedPreferences("passcode_pref", Context.MODE_PRIVATE);
        return preferences.getString("passcode", "");
    }
}
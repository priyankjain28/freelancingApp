package com.peeru.task.freelancingapp.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Utility {
    public static String dotToStarConverter(String s){
        return String.valueOf(s).replace(".", "*");
    }
    public static String dateTimeCoversion(String createDate) {
        try {
            long yourmilliseconds = Long.valueOf(createDate);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
            Date resultdate = new Date(yourmilliseconds);
            return sdf.format(resultdate).toString();

        } catch (Exception e) {
            return "";
        }
    }

    public static void textEventListner(EditText editText, Button button) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
   }
}

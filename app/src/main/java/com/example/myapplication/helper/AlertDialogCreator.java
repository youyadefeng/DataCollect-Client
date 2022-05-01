package com.example.myapplication.helper;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;

public class AlertDialogCreator {
    public static AlertDialog MakeDialog(Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(R.layout.layout_loading_dialog);
        return builder.create();
    }
}

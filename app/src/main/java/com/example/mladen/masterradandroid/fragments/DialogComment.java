package com.example.mladen.masterradandroid.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.mladen.masterradandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DialogComment extends AppCompatDialogFragment {

    public static final int DATEPICKER_FRAGMENT = 1;
    @BindView(R.id.title_comment) EditText editCommentTitle;
    @BindView(R.id.content_comment) EditText editCommentContent;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog, null);

        ButterKnife.bind(this, view);

        builder.setView(view)
                .setNegativeButton("Одбаци", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Пошаљи", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String commentTitle = editCommentTitle.getText().toString();
                        String commentContent = editCommentContent.getText().toString();

                        Intent in = new Intent()
                                .putExtra("title", commentTitle)
                                .putExtra("content", commentContent);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, in);
                        dismiss();
                    }
                });
        return builder.create();
    }
}

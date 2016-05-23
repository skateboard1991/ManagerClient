package com.skateboard.managerclient.ui.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.skateboard.managerclient.K;

/**
 * Created by skateboard on 16-5-20.
 */
public class ProgressDialogFragment extends DialogFragment
{

    private String message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null && bundle.containsKey(K.PROGRESS_MESSAGE))
        {
            message=bundle.getString(K.PROGRESS_MESSAGE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        ProgressDialog dialog=new ProgressDialog(getContext());
        dialog.setMessage(message);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }
}


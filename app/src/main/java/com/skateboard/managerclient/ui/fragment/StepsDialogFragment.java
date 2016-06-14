package com.skateboard.managerclient.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.skateboard.managerclient.R;

/**
 * Created by skateboard on 16-6-1.
 */
public class StepsDialogFragment extends DialogFragment implements View.OnClickListener,AdapterView.OnItemSelectedListener
{
    private View customView;
    private Button confirm;
    private Button cancel;
    private AppCompatSpinner stepsSpinner;
    private OnButtonClickListener buttonClickListener;
    private String step;


    public void setOnButtonClickListener(OnButtonClickListener listener)
    {
        this.buttonClickListener=listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        initDialogView();
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setView(customView);
        return builder.create();
    }

    private void initDialogView()
    {
        customView= LayoutInflater.from(getContext()).inflate(R.layout.fragment_stepsdialog,null);
        stepsSpinner= (AppCompatSpinner) customView.findViewById(R.id.steps_spinner);
        ArrayAdapter adapter=ArrayAdapter.createFromResource(getContext(),R.array.steps,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stepsSpinner.setAdapter(adapter);
        stepsSpinner.setOnItemSelectedListener(this);
        confirm= (Button) customView.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        cancel= (Button) customView.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.confirm:
                if(buttonClickListener!=null)
                buttonClickListener.onConfirmClick(step);
                break;
            case R.id.cancel:
                if(buttonClickListener!=null)
                buttonClickListener.onCancelClick();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
                step= (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    public interface OnButtonClickListener
    {
        void onConfirmClick(String step);
        void onCancelClick();
    }

}

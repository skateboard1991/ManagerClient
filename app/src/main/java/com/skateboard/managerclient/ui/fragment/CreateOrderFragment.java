package com.skateboard.managerclient.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.skateboard.managerclient.K;
import com.skateboard.managerclient.R;
import com.skateboard.managerclient.network.base.BaseListener;
import com.skateboard.managerclient.network.base.RequestHolder;
import com.skateboard.managerclient.network.request.AddOrderRequest;

import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Created by skateboard on 16-5-25.
 */
public class CreateOrderFragment extends Fragment implements View.OnClickListener
{

    private TextInputLayout orderNumberInp;
    private TextInputLayout typeInp;
    private TextInputLayout voltageInp;
    private TextInputLayout sizeInp;
    private TextInputLayout amountInp;
    private TextInputLayout noteInp;
    private Button confirm;

    private String orderNumber;
    private String type;
    private String voltage;
    private String size;
    private String amount;
    private String note;
    private String state;

    private ProgressDialogFragment loading;

    private HashMap<String, String> params=new HashMap<String,String>();

    private OnOrderCreatedListener onOrderCreatedListener;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try
        {
            onOrderCreatedListener= (OnOrderCreatedListener) context;
        }
        catch(ClassCastException e)
        {
            throw new ClassCastException("Your activity must implements OnCreatedListener");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.fragment_create_order,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view)
    {
//        toolbar.setTitle(getString(R.string.create_order_title));
        orderNumberInp = (TextInputLayout)view.findViewById(R.id.order_number_input);
        orderNumberInp.setHint(getString(R.string.order_number_item));
        orderNumberInp.setErrorEnabled(true);
        typeInp = (TextInputLayout) view.findViewById(R.id.type_input);
        typeInp.setHint(getString(R.string.order_type_item));
        typeInp.setErrorEnabled(true);
        voltageInp = (TextInputLayout) view.findViewById(R.id.voltage_input);
        voltageInp.setHint(getString(R.string.order_voltage_item));
        voltageInp.setErrorEnabled(true);
        sizeInp = (TextInputLayout) view.findViewById(R.id.size_input);
        sizeInp.setHint(getString(R.string.order_size_item));
        sizeInp.setErrorEnabled(true);
        amountInp = (TextInputLayout)view. findViewById(R.id.amount_input);
        amountInp.setHint(getString(R.string.order_amount_item));
        amountInp.setErrorEnabled(true);
        noteInp = (TextInputLayout)view. findViewById(R.id.note_input);
        noteInp.setHint(getString(R.string.order_note_item));
        noteInp.setErrorEnabled(true);
        confirm = (Button) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
    }


    private void addOrder()
    {
        checkEmpty();
        postOrderInfo();
    }

    private void checkEmpty()
    {
        orderNumber = orderNumberInp.getEditText().getText().toString();
        type = typeInp.getEditText().getText().toString();
        voltage = voltageInp.getEditText().getText().toString();
        size = sizeInp.getEditText().getText().toString();
        amount = amountInp.getEditText().getText().toString();
        note = noteInp.getEditText().getText().toString();
        if (shouldShowError(orderNumber, orderNumberInp, getString(R.string.order_number_empty_message)))
        {
            return;
        } else if (shouldShowError(type, typeInp, getString(R.string.type_empty_message)))
        {
            return;
        } else if (shouldShowError(voltage, voltageInp, getString(R.string.voltage_empty_message)))
        {
            return;
        } else if (shouldShowError(size, sizeInp, getString(R.string.size_empty_message)))
        {
            return;
        } else if (shouldShowError(amount, amountInp, getString(R.string.amount_empty_message)))
        {
            return;
        }
    }

    private boolean shouldShowError(String data, TextInputLayout input, String message)
    {
        if (TextUtils.isEmpty(data))
        {
            input.setError(message);
            return true;
        } else
        {
            return false;
        }
    }

    private void postOrderInfo()
    {
        holder.execute();
    }

    private RequestHolder holder = new RequestHolder()
    {
        @Override
        public Request newRequest()
        {
            initParams();
            return new AddOrderRequest(Request.Method.POST, K.ADDORDER_PATH, new ResponseListener(), params);
        }
    };

    private void initParams()
    {
        params.put(K.DBORDERNUMBER, orderNumber);
        params.put(K.DBTYPE, type);
        params.put(K.DBAMOUNT, amount);
        if (!TextUtils.isEmpty(note))
            params.put(K.DBNOTE, note);
        if (!TextUtils.isEmpty(state))
            params.put(K.DBSTATE, state);
        params.put(K.DBVOLTAGE, voltage);
        params.put(K.DBSIZE, size);
    }

    private class ResponseListener extends BaseListener<String>
    {
        @Override
        public void onErrorResponse(VolleyError error)
        {
            super.onErrorResponse(error);
            loading.dismiss();
            Toast.makeText(getContext(), getString(R.string.create_order_failed), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String response)
        {
            super.onResponse(response);
            loading.dismiss();
            System.out.println("charset name "+ Charset.defaultCharset().displayName());
            if ("success".equalsIgnoreCase(response))
            {
                Toast.makeText(getContext(), getString(R.string.create_order_success), Toast.LENGTH_SHORT).show();
                onOrderCreatedListener.onOrderCreated(orderNumber);
            }
            else
            {
                Toast.makeText(getContext(), getString(R.string.create_order_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.confirm:
                showProgressDialog();
                addOrder();
                break;
        }
    }

    private void showProgressDialog()
    {
        Bundle bundle=new Bundle();
        bundle.putString(K.PROGRESS_MESSAGE,getString(R.string.creating_order_message));
        loading=new ProgressDialogFragment();
        loading.setArguments(bundle);
        loading.show(getChildFragmentManager(),null);
    }

    public interface OnOrderCreatedListener
    {
        void onOrderCreated(String orderNumber);
    }
}

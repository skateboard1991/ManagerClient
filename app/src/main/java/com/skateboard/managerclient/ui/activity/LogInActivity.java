package com.skateboard.managerclient.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.skateboard.managerclient.K;
import com.skateboard.managerclient.R;
import com.skateboard.managerclient.network.base.BaseListener;
import com.skateboard.managerclient.network.base.RequestHolder;
import com.skateboard.managerclient.network.request.SignInRequest;
import com.skateboard.managerclient.ui.fragment.TipDialogFragment;
import com.skateboard.managerclient.xiaomi.XiaoMiPushManager;

import java.util.HashMap;

/**
 * Created by skateboard on 16-5-23.
 */
public class LogInActivity extends BaseActivity implements View.OnClickListener
{
    private String userName;
    private String passWord;
    private TextInputLayout userNameInp;
    private TextInputLayout passWordInp;
    private Button signIn;
    private SignInRequest request;
    private FrameLayout loadingLayout;
    private BroadcastReceiver onPushReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initOnPushReceiver();
        registetOnPushReceiver();
    }

    private void initView()
    {
        initLoadingLayout();
        initToolbar();
        initInput();
    }

    private void initOnPushReceiver()
    {
        onPushReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                if (K.SET_USER_ACCOUNT_SUCCESS.equalsIgnoreCase(intent.getAction()))
                {
                    hideLoadingLayout();
                    openMainActivity();
                    Toast.makeText(LogInActivity.this, getString(R.string.signin_success), Toast.LENGTH_SHORT).show();
                } else if (K.SET_USER_ACCOUNT_FAILED.equalsIgnoreCase(intent.getAction()))
                {
                    new TipDialogFragment().show(getSupportFragmentManager(), null);
                    return;
                }
            }
        };

    }

    private void registetOnPushReceiver()
    {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(K.PUSH_REGISTER_FAILED);
        intentFilter.addAction(K.PUSH_REGISTER_SUCCESS);
        intentFilter.addAction(K.SET_USER_ACCOUNT_FAILED);
        intentFilter.addAction(K.SET_USER_ACCOUNT_SUCCESS);
        registerReceiver(onPushReceiver, intentFilter);
    }


    private void initLoadingLayout()
    {
        loadingLayout= (FrameLayout) findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.GONE);
    }

    private void initToolbar()
    {
        toolbar.setNavigationIcon(null);
        toolbar.setTitle(getString(R.string.signin));
    }

    private void initInput()
    {
        userNameInp = (TextInputLayout) findViewById(R.id.username);
        userNameInp.setErrorEnabled(true);
        passWordInp = (TextInputLayout) findViewById(R.id.password);
        passWordInp.setErrorEnabled(true);
        signIn = (Button) findViewById(R.id.signin);
        signIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.signin:
                signIn();
                break;
        }
    }

    private void signIn()
    {
        if (!isInforError())
        {
            postSignInInfo();
            setPushUserAccount();
        }
    }


    private boolean isInforError()
    {
        userName = userNameInp.getEditText().getEditableText().toString();
        if (TextUtils.isEmpty(userName))
        {
            userNameInp.setError(getString(R.string.username_empty_message));
            return true;
        }
        passWord = passWordInp.getEditText().getEditableText().toString();
        if (TextUtils.isEmpty(passWord))
        {
            passWordInp.setError(getString(R.string.password_empty_message));
            return true;
        }

        return false;
    }

    private void setPushUserAccount()
    {
         XiaoMiPushManager.setUserAccount(getApplication().getApplicationContext(),userName);
    }

    private void postSignInInfo()
    {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", userName);
        params.put("password", passWord);
        request = new SignInRequest(Request.Method.POST, K.LOGIN_PATH, new Listener(),params);
        requestHolder.execute();
    }

    private RequestHolder requestHolder = new RequestHolder()
    {
        @Override
        public Request newRequest()
        {
            loadingLayout.setVisibility(View.VISIBLE);
            return request;
        }
    };

    private class Listener extends BaseListener<String>
    {
        @Override
        public void onErrorResponse(VolleyError error)
        {
            super.onErrorResponse(error);
            hideLoadingLayout();

        }

        @Override
        public void onResponse(String response)
        {
            super.onResponse(response);

            if ("success".equalsIgnoreCase(response))
            {
                savehasSignIn();
//                XiaoMiPushManager.registerPush(LogInActivity.this.getApplicationContext());
                XiaoMiPushManager.setUserAccount(LogInActivity.this.getApplicationContext(), K.USER_ACCOUNT);

            }
            else
            {
                hideLoadingLayout();
                Toast.makeText(LogInActivity.this, getString(R.string.invalid_user), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void hideLoadingLayout()
    {
        loadingLayout.setVisibility(View.GONE);
    }

    private void savehasSignIn()
    {
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(K.HAS_SIGNED_IN, true).commit();
    }

    private void openMainActivity()
    {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(onPushReceiver);
    }
}

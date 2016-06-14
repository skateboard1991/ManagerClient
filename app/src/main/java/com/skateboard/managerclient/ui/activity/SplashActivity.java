package com.skateboard.managerclient.ui.activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.skateboard.managerclient.K;
import com.skateboard.managerclient.R;
import com.skateboard.managerclient.bean.Orders;
import com.skateboard.managerclient.network.base.BaseListener;
import com.skateboard.managerclient.network.base.RequestHolder;
import com.skateboard.managerclient.network.request.OrderListRequest;
import com.skateboard.managerclient.ui.fragment.TextDialogFragment;

/**
 * Created by skateboard on 16-5-18.
 */
public class SplashActivity extends AppCompatActivity
{
    private ImageView splashImg;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkInternet();
        initSplashImg();
        getOrdersData();
    }

    private void initSplashImg()
    {
        splashImg = (ImageView) findViewById(R.id.splash_img);
        Glide.with(this).load(K.SPLASHIMG_PATH).into(splashImg);
    }

    private void getOrdersData()
    {
        holder.execute();
    }

    private RequestHolder holder = new RequestHolder()
    {
        @Override
        public Request newRequest()
        {
            return new OrderListRequest(Request.Method.GET, K.ORDERS_PATH, new Listener());
        }
    };

    private class Listener extends BaseListener<Orders>
    {
        @Override
        public void onErrorResponse(VolleyError error)
        {
            super.onErrorResponse(error);
        }

        @Override
        public void onResponse(final Orders response)
        {
            super.onResponse(response);
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    enterNextActivity();
                }
            }, 3000);
        }
    }

    private void enterNextActivity()
    {
          if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(K.HAS_SIGNED_IN,false))
          {
              openMainActivity();
          }
        else
          {
              openSignInActivity();
          }
    }

    private void openMainActivity()
    {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openSignInActivity()
    {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkInternet()
    {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info == null || !info.isAvailable())
        {
            new TextDialogFragment().show(getSupportFragmentManager(), null);
        }
    }

}

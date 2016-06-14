package com.skateboard.managerclient.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.skateboard.managerclient.K;
import com.skateboard.managerclient.R;
import com.skateboard.managerclient.bean.Orders;
import com.skateboard.managerclient.network.base.BaseListener;
import com.skateboard.managerclient.network.base.RequestHolder;
import com.skateboard.managerclient.network.request.OrderListRequest;
import com.skateboard.managerclient.ui.fragment.OrderListFragment;
import com.skateboard.managerclient.xiaomi.XiaoMiPushManager;

public class MainActivity extends BaseActivity implements OnClickListener
{

    private FrameLayout loadingLayout;
    private FloatingActionButton addOrderBtn;
    private OrderListFragment orderListFragment;
    private BroadcastReceiver onMessageArrivedReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initOnMessageArrivedReceiver();
    }

    private void initView()
    {
        initToolbar();
        initLoadingLayout();
        fetchData();
        initAddOrderBtn();
    }

    private void initOnMessageArrivedReceiver()
    {
        onMessageArrivedReceiver=new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                     loadingLayout.setVisibility(View.VISIBLE);
                     fetchData();
            }
        };
        IntentFilter intentFilter=new IntentFilter(K.ON_MESSAGE_ARRIVED);
        registerReceiver(onMessageArrivedReceiver,intentFilter);
    }

    private void initToolbar()
    {
       toolbar.setTitle(getString(R.string.order_list));
       toolbar.setNavigationIcon(null);
    }

    private void initLoadingLayout()
    {
        loadingLayout= (FrameLayout) findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    private void fetchData()
    {
        holder.execute();
    }

    private void showOrderList(Orders response)
    {
        orderListFragment=new OrderListFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable(K.ORDER_LIST,response);
        orderListFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,orderListFragment).commit();
    }


    private void initAddOrderBtn()
    {
        addOrderBtn = (FloatingActionButton) findViewById(R.id.add_order);
        addOrderBtn.setOnClickListener(this);
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
            hideLoading();
        }

        @Override
        public void onResponse(final Orders response)
        {
            super.onResponse(response);
            hideLoading();
            showOrderList(response);
        }
    }

    private void hideLoading()
    {
        loadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.add_order:
                showCreateOrderActivity();
                break;
        }
    }

    private void showCreateOrderActivity()
    {
        Intent intent=new Intent(this,CreateOrderActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.account_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.account_state:
                showLogIn(item);
                break;
        }
        return true;
    }

    private void showLogIn(MenuItem item)
    {
        if(getString(R.string.signout).equalsIgnoreCase(item.getTitle().toString()))
        {
            XiaoMiPushManager.unsetUserAccount(MainActivity.this.getApplicationContext(),K.USER_ACCOUNT);
//            XiaoMiPushManager.unRegisterPush(MainActivity.this.getApplicationContext());
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(K.HAS_SIGNED_IN,false).commit();
            Intent intent=new Intent(this,LogInActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unRegisterOnMessageArrivedReceiver();
    }

    private void unRegisterOnMessageArrivedReceiver()
    {
        unregisterReceiver(onMessageArrivedReceiver);
    }
}

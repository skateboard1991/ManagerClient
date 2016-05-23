package com.skateboard.managerclient.ui.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.skateboard.managerclient.R;
import com.skateboard.managerclient.ui.fragment.OrderListFragment;

public class MainActivity extends BaseActivity implements OnClickListener
{

    private FloatingActionButton addOrderBtn;
    private OrderListFragment orderListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView()
    {
        initToolbar();
        showOrderList();
        initAddOrderBtn();
    }

    private void initToolbar()
    {
       toolbar.setTitle(getString(R.string.order_list));
       toolbar.setNavigationIcon(null);
    }

    private void showOrderList()
    {
        orderListFragment=new OrderListFragment();
        orderListFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.container,orderListFragment).commit();
    }


    private void initAddOrderBtn()
    {
        addOrderBtn = (FloatingActionButton) findViewById(R.id.add_order);
        addOrderBtn.setOnClickListener(this);
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
}

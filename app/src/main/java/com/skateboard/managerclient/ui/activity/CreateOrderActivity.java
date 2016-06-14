package com.skateboard.managerclient.ui.activity;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.skateboard.managerclient.K;
import com.skateboard.managerclient.R;
import com.skateboard.managerclient.ui.fragment.CreateOrderFragment;
import com.skateboard.managerclient.ui.fragment.CreateOrderFragment.OnOrderCreatedListener;
import com.skateboard.managerclient.ui.fragment.FlowListFragment;


public class CreateOrderActivity extends BaseActivity implements OnOrderCreatedListener
{

    private CreateOrderFragment orderFragment;
    private FlowListFragment flowListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        setToolbarTitle(getString(R.string.create_order_title));
        dealIntent();
    }

    private void dealIntent()
    {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null && bundle.containsKey(K.ORDERNUMBER))
        {
            String orderNumber=bundle.getString(K.ORDERNUMBER);
            showFlowListFragment(orderNumber);
        }
        else
        {
            showCreateOrderFragment();
        }
    }

    private void setToolbarTitle(String title)
    {
        toolbar.setTitle(title);
    }

    private void showCreateOrderFragment()
    {
        orderFragment=new CreateOrderFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,orderFragment).commit();
    }

    @Override
    public void onOrderCreated(String orderNumber)
    {
        setToolbarTitle(getString(R.string.create_flow_title));
        showFlowListFragment(orderNumber);
    }

    private void showFlowListFragment(String orderNumber)
    {
      Bundle bundle=new Bundle();
      bundle.putString("ordernumber",orderNumber);
      flowListFragment=new FlowListFragment();
      flowListFragment.setArguments(bundle);
      getSupportFragmentManager().beginTransaction().replace(R.id.container,flowListFragment).commit();
    }

}

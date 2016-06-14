package com.skateboard.managerclient.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.skateboard.managerclient.K;
import com.skateboard.managerclient.R;
import com.skateboard.managerclient.bean.Order;
import com.skateboard.managerclient.bean.OrderState;
import com.skateboard.managerclient.bean.States;
import com.skateboard.managerclient.network.base.BaseListener;
import com.skateboard.managerclient.network.base.RequestHolder;
import com.skateboard.managerclient.network.request.OrderDetailRequest;
import com.skateboard.managerclient.network.request.ProcessStateRequest;

import java.util.ArrayList;


/**
 * Created by skateboard on 16-5-23.
 */
public class OrderDetailActivity extends BaseActivity
{

    private int requestNum;
    private TextView date, orderNumber, type, voltage, size, amount, state, note;
    private FrameLayout loadingContainer;
    private RecyclerView stateList;
    private String inOrderNumber;
    private ArrayList<OrderState> states;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        dealIntent();
        initView();
        fetchOrderData();
        fetchOrderProcessData();
    }

    private void dealIntent()
    {
        inOrderNumber = getIntent().getStringExtra(K.DBORDERNUMBER);
    }

    private void initView()
    {
        initToolbar();
        initInfoDetail();
        initLoading();
        initStateList();
    }

    private void initToolbar()
    {
        toolbar.setTitle(getString(R.string.order_process));
    }

    private void initInfoDetail()
    {
        date = (TextView) findViewById(R.id.date);
        orderNumber = (TextView) findViewById(R.id.order_number);
        type = (TextView) findViewById(R.id.type);
        voltage = (TextView) findViewById(R.id.voltage);
        size = (TextView) findViewById(R.id.size);
        amount = (TextView) findViewById(R.id.amount);
        state = (TextView) findViewById(R.id.state);
        note = (TextView) findViewById(R.id.note);
    }

    private void initLoading()
    {
        loadingContainer = (FrameLayout) findViewById(R.id.loading_container);
        loadingContainer.setVisibility(View.VISIBLE);
    }

    private void initStateList()
    {
        stateList = (RecyclerView) findViewById(R.id.state_list);
        stateList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchOrderData()
    {
        holder.execute();
    }

    private void fetchOrderProcessData()
    {
        stateHolder.execute();
    }

    private RequestHolder holder = new RequestHolder()
    {
        @Override
        public Request newRequest()
        {
            String url = K.ORDER_DETAIL_PATH + "/?ordernumber=" + inOrderNumber;
            return new OrderDetailRequest(Request.Method.GET, url, new Listener());
        }
    };

    private class Listener extends BaseListener<Order>
    {
        @Override
        public void onErrorResponse(VolleyError error)
        {
            super.onErrorResponse(error);
            requestNum++;
            hideLoading();

        }

        @Override
        public void onResponse(Order response)
        {
            super.onResponse(response);
            requestNum++;
            hideLoading();
            setOrderData(response);
        }
    }

    private void setOrderData(Order order)
    {
        date.setText(order.getDATE());
        orderNumber.setText(order.getORDERNUMBER());
        type.setText(order.getTYPE());
        voltage.setText(order.getVOLTAGE());
        size.setText(order.getSIZE());
        amount.setText(order.getAMOUNT());
        state.setText(order.getSIZE());
        note.setText(order.getNOTE());
    }

    private void hideLoading()
    {
        if(requestNum>=2)
        loadingContainer.setVisibility(View.GONE);
    }

    private RequestHolder stateHolder = new RequestHolder()
    {
        @Override
        public Request newRequest()
        {
            String url = K.ORDER_STATE_PATH + "?ordernumber=" + inOrderNumber;
            return new ProcessStateRequest(Request.Method.GET, url, new StateListener());
        }
    };

    private class StateListener extends BaseListener<States>
    {
        @Override
        public void onErrorResponse(VolleyError error)
        {
            super.onErrorResponse(error);
            requestNum++;
            hideLoading();
        }

        @Override
        public void onResponse(States response)
        {
            super.onResponse(response);
            requestNum++;
            hideLoading();
            setAndRefreshData(response);
        }
    }

    private void setAndRefreshData(States response)
    {
        states = response.getStates();
        if(stateList.getAdapter()==null)
        {
            stateList.setAdapter(new StateAdapter());
        }
        else
        {
            stateList.getAdapter().notifyDataSetChanged();
        }
    }

    private class StateAdapter extends RecyclerView.Adapter<StateItemViewHolder>
    {

        @Override
        public StateItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_item, parent, false);
            return new StateItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(StateItemViewHolder holder, int position)
        {
            holder.date.setText(states.get(position).getDATE());
            holder.process.setText(states.get(position).getSTATE());
        }

        @Override
        public int getItemCount()
        {
            return states.size();
        }
    }

    private class StateItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView process;
        TextView date;

        public StateItemViewHolder(View itemView)
        {
            super(itemView);
            process = (TextView) itemView.findViewById(R.id.state);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }


}

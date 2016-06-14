package com.skateboard.managerclient.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.skateboard.managerclient.K;
import com.skateboard.managerclient.R;
import com.skateboard.managerclient.bean.Order;
import com.skateboard.managerclient.bean.Orders;
import com.skateboard.managerclient.network.base.BaseListener;
import com.skateboard.managerclient.network.base.RequestHolder;
import com.skateboard.managerclient.network.request.OrderListRequest;
import com.skateboard.managerclient.ui.activity.CreateOrderActivity;
import com.skateboard.managerclient.ui.activity.OrderDetailActivity;

import java.util.ArrayList;

/**
 * Created by skateboard on 16-5-18.
 */
public class OrderListFragment extends Fragment implements OnRefreshListener
{

    private RecyclerView orderList;
    private ArrayList<Order> orders;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        dealIntent();
    }

    private void dealIntent()
    {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(K.ORDER_LIST))
        {
            orders = ((Orders) bundle.getSerializable(K.ORDER_LIST)).getORDER_LIST();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initRefreshLayout(view);
        initOrderList(view);
    }

    private void initRefreshLayout(View view)
    {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        refreshLayout.setOnRefreshListener(this);
    }

    private void initOrderList(View view)
    {
        orderList = (RecyclerView) view.findViewById(R.id.order_list);
        orderList.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderList.setAdapter(new OrdersAdapter());
    }

    @Override
    public void onRefresh()
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
            refreshLayout.setRefreshing(false);
        }

        @Override
        public void onResponse(final Orders response)
        {
            super.onResponse(response);
            refreshLayout.setRefreshing(false);
            orders = response.getORDER_LIST();
            orderList.getAdapter().notifyDataSetChanged();
            Toast.makeText(getActivity(), "refresh successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private class OrdersAdapter extends RecyclerView.Adapter<OrderItemHolder>
    {

        @Override
        public OrderItemHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View orderItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
            OrderItemHolder orderItemHolder = new OrderItemHolder(orderItemView);
            return orderItemHolder;
        }

        @Override
        public void onBindViewHolder(OrderItemHolder holder, final int position)
        {
            holder.date.setText(orders.get(position).getDATE() + "");
            holder.orderNumber.setText(orders.get(position).getORDERNUMBER() + "");
            if ("NULL".equalsIgnoreCase(orders.get(position).getSTATE()))
            {
               holder.state.setText(R.string.not_create_step);
               holder.state.setClickable(true);
               holder.state.setOnClickListener(new View.OnClickListener()
               {
                   @Override
                   public void onClick(View v)
                   {
                       Intent intent=new Intent(OrderListFragment.this.getActivity(), CreateOrderActivity.class);
                       Bundle bundle=new Bundle();
                       bundle.putString(K.ORDERNUMBER,orders.get(position).getORDERNUMBER()+"");
                       intent.putExtras(bundle);
                       startActivity(intent);
                   }
               });
            } else
            {
                holder.state.setText(orders.get(position).getSTATE());
                holder.state.setClickable(false);
            }
            holder.itemView.setOnClickListener(new OnItemClickListener(position));
        }

        @Override
        public int getItemCount()
        {
            return orders.size();
        }
    }


    private class OnItemClickListener implements View.OnClickListener
    {
        int position;

        OnItemClickListener(int position)
        {
            this.position = position;
        }

        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
            intent.putExtra(K.DBORDERNUMBER, orders.get(position).getORDERNUMBER());
            startActivity(intent);
        }
    }

    private class OrderItemHolder extends RecyclerView.ViewHolder
    {
        TextView date;
        TextView state;
        TextView orderNumber;

        public OrderItemHolder(View itemView)
        {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.order_date);
            orderNumber = (TextView) itemView.findViewById(R.id.order_number);
            state = (TextView) itemView.findViewById(R.id.order_state);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }
}

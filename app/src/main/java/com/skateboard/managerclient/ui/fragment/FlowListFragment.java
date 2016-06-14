package com.skateboard.managerclient.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.skateboard.managerclient.network.base.BaseListener;
import com.skateboard.managerclient.network.base.RequestHolder;
import com.skateboard.managerclient.network.request.StepsRequest;
import com.skateboard.managerclient.ui.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by skateboard on 16-5-25.
 */
public class FlowListFragment extends Fragment implements  View.OnLongClickListener
{
    private FloatingActionButton sync;
    private RecyclerView flowList;
    private ArrayList<String> data = new ArrayList<>();
    private ArrayList<String> chooseSteps=new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private int clickNum;
    private ProgressDialogFragment loading;
    private HashMap<String,String> params;
    private String orderNumber;
    private RecyclerView chooseStepList;
    private TextView chooseStepTitle;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initChooseSteps();
        getOrderNumber();
    }

    private void initChooseSteps()
    {
        String[] steps=getResources().getStringArray(R.array.steps);
        for(String step:steps)
        {
            chooseSteps.add(step);
        }
    }

    private void getOrderNumber()
    {
        Bundle bundle=getArguments();
        if(bundle!=null && bundle.containsKey("ordernumber"))
        {
            orderNumber=bundle.getString("ordernumber");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_flow_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initChooseStepTitle(view);
        initChooseStepList(view);
        initFlowList(view);
        initAddButton(view);
    }

    private void initChooseStepTitle(View view)
    {
        chooseStepTitle= (TextView) view.findViewById(R.id.step_title);
        chooseStepTitle.setText(R.string.choose_step);
    }

    private void initChooseStepList(View view)
    {
        chooseStepList= (RecyclerView) view.findViewById(R.id.step_list);
        chooseStepList.setLayoutManager(new LinearLayoutManager(getContext()));
        chooseStepList.setAdapter(new ChooseStepAdapter());
        chooseStepList.addOnItemTouchListener(new OnItemClickListener(chooseStepList, new OnItemClickListener.OnItemClickLisntere()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                clickNum++;
                data.add(chooseSteps.get(position));
                adapter.notifyItemInserted(adapter.getItemCount());
                flowList.scrollToPosition(adapter.getItemCount() - 1);
            }

            @Override
            public void onItemLongClick(View view, int position)
            {

            }
        }));
    }

    private class ChooseStepAdapter extends RecyclerView.Adapter<StepItemHolder>
    {



        @Override
        public StepItemHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item,parent,false);
            return new StepItemHolder(view);
        }

        @Override
        public void onBindViewHolder(StepItemHolder holder, int position)
        {
            holder.step.setText(chooseSteps.get(position));
        }

        @Override
        public int getItemCount()
        {

            return chooseSteps.size();
        }
    }

    private class StepItemHolder extends RecyclerView.ViewHolder
    {
        TextView step;

        public StepItemHolder(View itemView)
        {
            super(itemView);
            step= (TextView) itemView.findViewById(R.id.step);
        }

    }

    private void initAddButton(View view)
    {
        sync = (FloatingActionButton) view.findViewById(R.id.sync);
        sync.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                  params=new HashMap<String, String>();
                  params.put("ordernumber",orderNumber);
                  StringBuilder builder=new StringBuilder();
                  for(String item:data)
                  {
                      builder.append(item).append("/");
                  }
                  params.put("steplist",builder.toString());
                  showProgressDialog();
                  new FlowListRequestHolder(params).execute();
            }
        });
    }

    private void showProgressDialog()
    {
        Bundle bundle=new Bundle();
        bundle.putString(K.PROGRESS_MESSAGE,getString(R.string.creating_order_message));
        loading=new ProgressDialogFragment();
        loading.setArguments(bundle);
        loading.show(getChildFragmentManager(),null);
    }

    private class FlowListRequestHolder extends RequestHolder
    {
        HashMap<String,String> params;

        FlowListRequestHolder(HashMap<String,String> params)
        {
              this.params=params;
        }

        @Override
        public Request newRequest()
        {

            return new StepsRequest(Request.Method.POST, K.ADD_STEPS_PATH,new RequestListener(),params);
        }
    }


    private class RequestListener extends BaseListener<String>
    {
        @Override
        public void onErrorResponse(VolleyError error)
        {
            super.onErrorResponse(error);
            dismissLoading();
        }

        @Override
        public void onResponse(String response)
        {
            super.onResponse(response);
            dismissLoading();
            if("success".equalsIgnoreCase(response))
            {
                Toast.makeText(getActivity(),getString(R.string.sync_success),Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
            else
            {
                Toast.makeText(getActivity(), getString(R.string.sync_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dismissLoading()
    {
        loading.dismiss();
    }

    private void initFlowList(View view)
    {
        flowList = (RecyclerView) view.findViewById(R.id.flow_list);
        flowList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FlowListAdapter();
        flowList.setAdapter(adapter);
        flowList.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public boolean onLongClick(View v)
    {
        return false;
    }

    private class FlowListAdapter extends RecyclerView.Adapter
    {

        final int DIVIDER = 0;
        final int DES = 1;

        @Override
        public int getItemViewType(int position)
        {
            if (position % 2 == 0)
            {
                return DES;
            } else
            {
                return DIVIDER;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            if (viewType == DES)
            {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.flowdes_item, parent, false);
                return new FlowDesHolder(itemView);
            } else
            {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.flowdivider_item, parent, false);
                return new FlowDividerHolder(itemView);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
        {
            if (getItemViewType(position) == DES)
            {
                ((FlowDesHolder) holder).flowDes.setText(data.get(clickNum-1));
                holder.itemView.setOnLongClickListener(FlowListFragment.this);
            }

        }

        @Override
        public int getItemCount()
        {
            int dataSize = data.size();
            if (dataSize > 1)
            {
                return (dataSize - 1) * 2 + 1;
            }

            return dataSize;

        }
    }

    private class FlowDesHolder extends RecyclerView.ViewHolder
    {

        TextView flowDes;

        public FlowDesHolder(View itemView)
        {
            super(itemView);
            flowDes = (TextView) itemView.findViewById(R.id.des_text);
        }
    }

    private class FlowDividerHolder extends RecyclerView.ViewHolder
    {
        public FlowDividerHolder(View itemView)
        {
            super(itemView);
        }
    }

}

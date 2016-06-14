package com.skateboard.managerclient.ui.listener;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by skateboard on 16-6-9.
 */
public class OnItemClickListener implements RecyclerView.OnItemTouchListener
{

    private RecyclerView recyclerView;
    private GestureDetector gestureDetector;
    private OnItemClickLisntere listener;

    public interface OnItemClickLisntere
    {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public OnItemClickListener(RecyclerView recyclerView, OnItemClickLisntere listener)
    {
        this.recyclerView = recyclerView;
        this.listener = listener;
        initGestureDetector();
    }

    private void initGestureDetector()
    {
        gestureDetector = new GestureDetector(recyclerView.getContext(), onGestureListener);
    }

    private GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener()
    {
        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childView != null)
            {
                int position = recyclerView.getChildAdapterPosition(childView);
                listener.onItemClick(childView, position);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e)
        {
            View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childView != null)
            {
                int position = recyclerView.getChildAdapterPosition(childView);
                listener.onItemLongClick(childView, position);
            }
        }
    };

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
    {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e)
    {
//        gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
    {

    }
}

package com.skateboard.managerclient.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.skateboard.managerclient.R;

/**
 * Created by skateboard on 16-5-20.
 */
    public class BaseActivity extends AppCompatActivity
    {
        protected Toolbar toolbar;

        @Override
        public void onContentChanged()
        {
            super.onContentChanged();
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null)
            {
                toolbar.setTitle("");
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

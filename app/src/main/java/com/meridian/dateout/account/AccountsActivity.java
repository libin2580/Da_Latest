package com.meridian.dateout.account;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.meridian.dateout.R;

import static com.meridian.dateout.Constants.analytics;

public class AccountsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        analytics.setCurrentScreen(AccountsActivity.this, AccountsActivity.this.getLocalClassName(), null /* class override */);

        
    }
}

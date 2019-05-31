package com.cxc.sqlitetest.db;

import android.app.Application;

public class DbApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!DbContext.isInitialized()) {
            DbContext.init(getApplicationContext());
        }
    }
}

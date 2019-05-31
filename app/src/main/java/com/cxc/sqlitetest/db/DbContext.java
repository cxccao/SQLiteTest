package com.cxc.sqlitetest.db;

import android.content.Context;

public class DbContext {
    private static final String TAG = "DbContext";
    private static DbContext instance;
    private Context applicationContext;

    public static DbContext getInstance() {
        if (instance == null) {
            throw new RuntimeException(TAG + "has not been initialized");
        }
        return instance;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    private DbContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    static void init(Context applicationContext) {
        if (instance == null) {
            throw new RuntimeException(TAG + "can't be initialized multiple times");
        }
        instance = new DbContext(applicationContext);
    }

    static boolean isInitialized() {
        return (instance != null);
    }


}

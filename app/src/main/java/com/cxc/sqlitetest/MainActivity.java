package com.cxc.sqlitetest;

import android.os.strictmode.SqliteObjectLeakedViolation;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cxc.sqlitetest.db.Db;
import com.cxc.sqlitetest.db.DbAdapter;
import com.cxc.sqlitetest.db.DbDAO;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView showSQLMsg;
    private EditText inputSQLMsg;
    private ListView showDataListView;
    private DbDAO dbDAO;
    private List<Db> dbList = new ArrayList<>();
    private DbAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbDAO=new DbDAO(this);
        if(!dbDAO.isExist()){
            dbDAO.initTable();
        }
        init();
        dbList=dbDAO.getAllData();
        if (dbList != null) {
            dbAdapter=new DbAdapter(this, dbList);
            showDataListView.setAdapter(dbAdapter);
        }


    }

    private void init(){
        Button executeBtn=findViewById(R.id.executeBtn);
        SQLBtnOnclickListener sqlBtnOnclickListener=new SQLBtnOnclickListener();
        executeBtn.setOnClickListener(sqlBtnOnclickListener);
        inputSQLMsg=findViewById(R.id.inputSQLMsg);
        showSQLMsg=findViewById(R.id.showSQLMsg);
        showDataListView=findViewById(R.id.showDbListView);
        showDataListView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.show_item_layout,null), null, false);

    }

    private void refreshDbList(){
        if(dbList!=null) {
            dbList.clear();
            dbList.addAll(dbDAO.getAllData());
            dbAdapter.notifyDataSetChanged();
        }
    }

    class SQLBtnOnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.executeBtn:
                    showSQLMsg.setVisibility(View.GONE);
                    String sql=inputSQLMsg.getText().toString();
                    if(!TextUtils.isEmpty(sql)){
                        dbDAO.executeSQL(sql);
                    }else {
                        Toast.makeText(MainActivity.this, R.string.inputSQL,Toast.LENGTH_SHORT).show();
                    }
                    refreshDbList();
                    break;
                 default:
                     break;
            }
        }
    }

}

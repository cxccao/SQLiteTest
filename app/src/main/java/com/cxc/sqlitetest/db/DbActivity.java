package com.cxc.sqlitetest.db;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cxc.sqlitetest.R;

import java.util.ArrayList;
import java.util.List;

public class DbActivity extends AppCompatActivity {
    private String TAG="DbActivity";

    private TextView showSQLMsg;
    private EditText inputSQLMsg;
    private ListView showDataListView;
    private DbDAO dbDAO;
    private List<Db> dbList = new ArrayList<>();
    private DbAdapter dbAdapter;

    private int choiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbDAO = new DbDAO(this);
        firstRun();
        init();
        dbList = dbDAO.getAllData();
        if (dbList != null) {
            dbAdapter = new DbAdapter(this, dbList);
            showDataListView.setAdapter(dbAdapter);
        }
    }

    private void init() {
        Button executeBtn = findViewById(R.id.executeBtn);
        Button insertBtn = findViewById(R.id.insertBtn);
        Button deleteBtn = findViewById(R.id.deleteBtn);
        Button updateBtn = findViewById(R.id.updateBtn);
        Button queryBtn1 = findViewById(R.id.queryBtn1);
        Button queryBtn2 = findViewById(R.id.queryBtn2);
        Button queryBtn3 = findViewById(R.id.queryBtn3);

        SQLBtnOnclickListener sqlBtnOnclickListener = new SQLBtnOnclickListener();

        executeBtn.setOnClickListener(sqlBtnOnclickListener);
        insertBtn.setOnClickListener(sqlBtnOnclickListener);
        deleteBtn.setOnClickListener(sqlBtnOnclickListener);
        updateBtn.setOnClickListener(sqlBtnOnclickListener);
        queryBtn1.setOnClickListener(sqlBtnOnclickListener);
        queryBtn2.setOnClickListener(sqlBtnOnclickListener);
        queryBtn3.setOnClickListener(sqlBtnOnclickListener);

        inputSQLMsg = findViewById(R.id.inputSQLMsg);
        showSQLMsg = findViewById(R.id.showSQLMsg);
        showDataListView = findViewById(R.id.showDbListView);
        showDataListView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.show_item_layout, null), null, false);

    }

    private void refreshDbList() {
        if (dbList != null) {
            dbList.clear();
            dbList.addAll(dbDAO.getAllData());
            dbAdapter.notifyDataSetChanged();
        }
    }

    private void insertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DbActivity.this);
        builder.setTitle("插入数据");
        View view = LayoutInflater.from(DbActivity.this).inflate(R.layout.updatadialog, null);
        builder.setView(view);

        final EditText editText1 = view.findViewById(R.id.et1);
        final EditText editText2 = view.findViewById(R.id.et2);
        final EditText editText3 = view.findViewById(R.id.et3);
        final EditText editText4 = view.findViewById(R.id.et4);

        builder.setPositiveButton("确定", (dialog, which) -> {
            int id = Integer.parseInt(editText1.getText().toString());
            String name = editText2.getText().toString();
            int price = Integer.parseInt(editText3.getText().toString());
            String country = editText4.getText().toString();
            dbDAO.insertData(id, name, price, country);
            showSQLMsg.setVisibility(View.VISIBLE);
            showSQLMsg.setText("新增一条数据：\n添加数据（" + id + "，\"" + name + "\", " + price + ", \"" + country + "\")\ninsert into Db(Id,Name,Price, Country) values (" + id + "，\"" + name + "\", " + price + ", \"" + country + "\")");
            refreshDbList();
        }).setNegativeButton("取消", (dialog, which) -> {

        });
        builder.show();
    }

    private void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DbActivity.this);
        builder.setTitle("删除数据");
        List<String> dbList = dbDAO.getAllId();
        final String[] ids = dbList.toArray(new String[dbList.size()]);
        builder.setSingleChoiceItems(ids, -1, new choice()).setPositiveButton("确定", (dialog, which) -> {
            int id = Integer.parseInt(ids[choiceId]);
            dbDAO.deleteData(id);
            showSQLMsg.setVisibility(View.VISIBLE);
            showSQLMsg.setText("删除一条数据：\n删除Id为" + id + "的数据\ndelete from Db where Id = " + id);
            refreshDbList();
        }).setNegativeButton("取消", (dialog, which) -> {
        });
        builder.show();
    }

    private void updateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DbActivity.this);
        builder.setTitle("更新数据");
        List<String> dbList = dbDAO.getAllId();
        final String[] ids = dbList.toArray(new String[dbList.size()]);
        builder.setSingleChoiceItems(ids, -1, new choice()).setPositiveButton("确定", (dialog, which) -> {
            int id = Integer.parseInt(ids[choiceId]);
            AlertDialog.Builder builder1 = new AlertDialog.Builder(DbActivity.this);
            builder1.setTitle("更新数据");
            View view = LayoutInflater.from(DbActivity.this).inflate(R.layout.updatadialog, null);
            builder1.setView(view);

            final EditText editText1 = view.findViewById(R.id.et1);
            editText1.setText(String.valueOf(id));
            editText1.setFocusable(false);
            final EditText editText2 = view.findViewById(R.id.et2);
            final EditText editText3 = view.findViewById(R.id.et3);
            final EditText editText4 = view.findViewById(R.id.et4);

            builder1.setPositiveButton("确定", (dialog1, which1) -> {
                int id1 = Integer.parseInt(editText1.getText().toString());
                String name = editText2.getText().toString();
                int price = Integer.parseInt(editText3.getText().toString());
                String country = editText4.getText().toString();
                dbDAO.updateData(id1, name, price, country);
                showSQLMsg.setVisibility(View.VISIBLE);
                showSQLMsg.setText("Id为" + id1 + "的数据被更新\nupdate Db set Name = \"" + name + "\", Price = \"" + price + "\", Country = \"" + country + "\" where Id = " + id1);
                refreshDbList();
            }).setNegativeButton("取消", (dialog1, which1) -> {

            });
            builder1.show();
        }).setNegativeButton("取消", (dialog, which) -> {
        });
        builder.show();
    }

    private void query1Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DbActivity.this);
        builder.setTitle("提供者查询");
        final List<String> nameList = dbDAO.getAllName();
        final String[] ids = nameList.toArray(new String[nameList.size()]);
        builder.setSingleChoiceItems(ids, -1, new choice()).setPositiveButton("确定", (dialog, which) -> {
            String name = ids[choiceId];
            showSQLMsg.setVisibility(View.VISIBLE);
            dbList.clear();
            dbList.addAll(dbDAO.getDataByName(name));
            dbAdapter.notifyDataSetChanged();
            showSQLMsg.setText("数据查询：\n此处将用户名为\"" + name + "\"的信息提取出来\nselect * from Db where Name = '" + name + "' ");
        }).setNegativeButton("取消", (dialog, which) -> {
        });
        builder.show();
    }

    private void query2Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DbActivity.this);
        builder.setTitle("价格区间查询");
        View view = LayoutInflater.from(DbActivity.this).inflate(R.layout.intervaldialog, null);
        builder.setView(view);

        final EditText editText1 = view.findViewById(R.id.firstNum);
        final EditText editText2 = view.findViewById(R.id.secondNum);

        builder.setPositiveButton("确定", (dialog, which) -> {
                String num1 = editText1.getText().toString();
                String num2 = editText2.getText().toString();
                String[] interval = new String[]{num1, num2};
                showSQLMsg.setVisibility(View.VISIBLE);
                dbList.clear();
                dbList.addAll(dbDAO.getDataByIntervalPrice(interval));
                dbAdapter.notifyDataSetChanged();
                showSQLMsg.setText("数据查询：\n此处将价格区间为\"" + num1 + "\"到\"" + num2 + "\"的信息提取出来\nselect * from Db where Price between " + num1 + " and " + num2);

        }).setNegativeButton("取消", (dialog, which) -> {

        });
        builder.show();
    }

    public void firstRun(){
        SharedPreferences sharedPreferences=getSharedPreferences(TAG, MODE_PRIVATE);
        boolean first = sharedPreferences.getBoolean("first", true);
        if (first) {
            sharedPreferences.edit().putBoolean("first",false).apply();
            if (!dbDAO.isExist()) {
                dbDAO.initTable();
            }
            Toast.makeText(DbActivity.this, R.string.firstRun, Toast.LENGTH_LONG).show();

        }else {

        }
    }

    class SQLBtnOnclickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.executeBtn:
                    showSQLMsg.setVisibility(View.GONE);
                    String sql = inputSQLMsg.getText().toString();
                    if (!TextUtils.isEmpty(sql)) {
                        if (sql.contains("select") || sql.contains("SELECT")) {
                            Toast.makeText(DbActivity.this, R.string.unsupportedSelect, Toast.LENGTH_SHORT).show();
                        } else {
                            dbDAO.executeSQL(sql);
                        }
                    } else {
                        Toast.makeText(DbActivity.this, R.string.inputSQL, Toast.LENGTH_SHORT).show();
                    }
                    refreshDbList();
                    break;
                case R.id.insertBtn:
                    insertDialog();
                    break;
                case R.id.deleteBtn:
                    deleteDialog();
                    break;
                case R.id.updateBtn:
                    updateDialog();
                    break;
                case R.id.queryBtn1:
                    query1Dialog();
                    break;
                case R.id.queryBtn2:
                    query2Dialog();
                    break;
                case R.id.queryBtn3:
                    showSQLMsg.setVisibility(View.GONE);
                    refreshDbList();
                    break;
                default:
                    break;
            }
        }
    }

    class choice implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            choiceId = which;
        }
    }

}

package com.cxc.sqlitetest.db;

import android.content.Context;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cxc.sqlitetest.R;
import java.util.List;

public class DbAdapter extends BaseAdapter {
    private Context context;
    private List<Db> dbList;

    public DbAdapter(Context context, List<Db> dbList){
        setContext(context);
        setDbList(dbList);
    }

    @Override
    public int getCount() {
        return dbList.size();
    }

    @Override
    public Object getItem(int position) {
        return dbList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Db db = dbList.get(position);
        if(db==null) {
            return null;
        }

        ViewHolder holder=null;

        if (convertView != null) {
            holder = (ViewHolder)convertView.getTag();
        }else {
            convertView =LayoutInflater.from(context).inflate(R.layout.show_item_layout,null);
            holder = new ViewHolder();
            holder.dataIdView=convertView.findViewById(R.id.dataIdView);
            holder.dataNameView = convertView.findViewById(R.id.dataNameView);
            holder.dataPriceView = convertView.findViewById(R.id.dataPriceView);
            holder.dataCountryView=convertView.findViewById(R.id.dataCountryView);
            convertView.setTag(holder);
        }

        holder.dataIdView.setText(String.valueOf(db.Id));
        holder.dataNameView.setText(db.Name);
        holder.dataPriceView.setText(String.valueOf(db.Price));
        holder.dataCountryView.setText(db.Country);
        return convertView;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Db> getDbList() {
        return dbList;
    }

    public void setDbList(List<Db> dbList) {
        this.dbList = dbList;
    }

    static class ViewHolder{
        TextView dataIdView;
        TextView dataNameView;
        TextView dataPriceView;
        TextView dataCountryView;

    }
}

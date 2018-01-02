package com.example.thao.readcontact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by luu.phuong.thao on 10/12/2016.
 */

public class FullContactItemAdapter extends ArrayAdapter<Contacts> implements View.OnClickListener {
    ArrayList<Contacts> contacts;
    Context mContext;
    FullContactItemAdapter(ArrayList<Contacts> contacts, Context mContext){
        super(mContext, R.layout.row_item, contacts);
        this.contacts = contacts;
        this.mContext = mContext;
    }
    class ViewHolder{
        TextView id;
        TextView phone;
        TextView email;
        TextView name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //  Contacts contacts = getItem(position);
        FullContactItemAdapter.ViewHolder viewHolder;
        Contacts contacts = getItem(position);
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.full_contact_item, parent, false);
            viewHolder.id = (TextView)convertView.findViewById(R.id.id);
            viewHolder.email = (TextView)convertView.findViewById(R.id.email);
            viewHolder.phone = (TextView)convertView.findViewById(R.id.phone);
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);

        }
        else{
            viewHolder = (FullContactItemAdapter.ViewHolder)convertView.getTag();
        }
        viewHolder.id.setText(contacts.getId());
        viewHolder.name.setText(contacts.getName());
        viewHolder.phone.setText(contacts.getPhone());
        viewHolder.email.setText(contacts.getEmail());
        return convertView;
    }

    @Override
    public void onClick(View v) {
    }
}

package com.whosup.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whosup.android.whosup.R;

import java.util.List;

public class InviteAdapter extends BaseAdapter {

    protected List<Invite> inviteList;
    private LayoutInflater mInflater;

    public InviteAdapter(Context context, List<Invite> inviteList) {
        mInflater = LayoutInflater.from(context);
        this.inviteList = inviteList;
    }

    @Override
    public int getCount() {
        return inviteList.size();
    }

    @Override
    public Object getItem(int position) {
        return inviteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        //return inviteList.get(position).getUsername();
        return 00000;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {

            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_row, parent, false);

            holder.name = (TextView) convertView.findViewById(R.id.inviteFrom);
            holder.subcategory = (TextView) convertView.findViewById(R.id.invitePlace);
            holder.txtDistance = (TextView) convertView.findViewById(R.id.inviteDistance);
            //holder.imgProfile = (ImageView) convertView.findViewById(R.id.inviterProfilePic);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Invite invite = inviteList.get(position);
        holder.name.setText(invite.getFirstName()+ " "+ invite.getLastName());
        holder.subcategory.setText(invite.getSubcategory());
        holder.txtDistance.setText(invite.getAddress() + " Km");
        //holder.imgProfile.setImageResource(invite.getDrawableId());

        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        TextView subcategory;
        TextView txtDistance;
        ImageView imgProfile;
    }
}

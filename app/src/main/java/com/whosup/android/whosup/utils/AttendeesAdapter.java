/*package com.whosup.android.whosup.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whosup.android.whosup.R;
import com.whosup.android.whosup.ViewInviteActivity;
import com.whosup.listview.Invite;

import java.util.List;

public class AttendeesAdapter extends BaseAdapter {
    protected List<InviteAttend> attendeesList;
    private LayoutInflater mInflater;
    private Context context;

    public AttendeesAdapter(Context context, List<InviteAttend> attendeesList) {
        mInflater = LayoutInflater.from(context);
        this.attendeesList = attendeesList;
        this.context=context;


    }


    @Override
    public int getCount() {
        return attendeesList.size();
    }

    @Override
    public Object getItem(int position) {
        return attendeesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        //return inviteList.get(position).getUsername();
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {

            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_row_my_invites, parent, false);
            holder.subcategory = (TextView) convertView.findViewById(R.id.inviteSubcategory);
            holder.address = (TextView) convertView.findViewById(R.id.inviteLocation);
            holder.meetDay = (TextView) convertView.findViewById(R.id.meetDay);
            holder.meetHour = (TextView) convertView.findViewById(R.id.meetHour);
            holder.imgCategory = (ImageView) convertView.findViewById(R.id.imgCategory);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.pending = (TextView) convertView.findViewById(R.id.pending);
            holder.confirmed = (TextView) convertView.findViewById(R.id.confirmed);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Invite invite = inviteList.get(position);
        holder.subcategory.setText(invite.getSubcategory());
        holder.address.setText(invite.getAddress());
        holder.imgCategory.setImageDrawable(getImage(invite));
        holder.meetDay.setText(Utility.arrangeDate(invite.getMeetDay()));
        holder.meetHour.setText(Utility.arrangeHour(invite.getMeetHour()));

        holder.pending.setText(Utility.getNumberCountStatus(invite, "pending")+"");
        holder.confirmed.setText(Utility.getNumberCountStatus(invite, "confirmed")+"");



        return convertView;
    }

    private static class ViewHolder {
        TextView subcategory;
        TextView address;
        TextView meetDay;
        TextView meetHour;
        ImageView imgCategory;
        TextView status;
        TextView pending;
        TextView confirmed;
    }

}

*/
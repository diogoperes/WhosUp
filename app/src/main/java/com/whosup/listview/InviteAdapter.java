package com.whosup.listview;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whosup.android.whosup.R;
import com.whosup.android.whosup.utils.Utility;

import java.util.List;

public class InviteAdapter extends BaseAdapter {

    protected List<Invite> inviteList;
    private LayoutInflater mInflater;
    private Location myLocation;

    public InviteAdapter(Context context, List<Invite> inviteList, Location myLocation) {
        mInflater = LayoutInflater.from(context);
        this.inviteList = inviteList;
        this.myLocation = myLocation;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {

            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_row, parent, false);

            holder.name = (TextView) convertView.findViewById(R.id.inviteFrom);
            holder.subcategory = (TextView) convertView.findViewById(R.id.inviteSubcategory);
            holder.distance = (TextView) convertView.findViewById(R.id.inviteDistance);
            holder.address = (TextView) convertView.findViewById(R.id.inviteLocation);
            //holder.imgProfile = (ImageView) convertView.findViewById(R.id.inviterProfilePic);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Invite invite = inviteList.get(position);
        holder.name.setText(invite.getFirstName()+ " "+ invite.getLastName());
        holder.subcategory.setText(invite.getSubcategory());
        holder.distance.setText(getDistanceKm(invite) + " Km");
        holder.address.setText(invite.getAddress());
        //holder.imgProfile.setImageResource(invite.getDrawableId());

        return convertView;
    }

    public double getDistanceKm(Invite invite){


        Location inviteLocation = new Location("");
        inviteLocation.setLatitude(Double.parseDouble(invite.getLatitude()));
        inviteLocation.setLongitude(Double.parseDouble(invite.getLongitude()));
        //Log.v("CURRENT LOCATION", "" + myLocation);


        float distanceInMeters = inviteLocation.distanceTo(myLocation);

        //Log.v("DISTANCE TO", "" + distanceInMeters);
        double distanceInKm = distanceInMeters / 1000;
        distanceInKm = Math.round(distanceInKm * 100.0) / 100.0;
        invite.setDistanceFromMe(distanceInKm);
        return distanceInKm;
    }


    private static class ViewHolder {
        TextView name;
        TextView subcategory;
        TextView distance;
        TextView address;
        ImageView imgProfile;
    }
}

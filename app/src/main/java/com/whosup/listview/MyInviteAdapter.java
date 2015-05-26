package com.whosup.listview;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.whosup.android.whosup.utils.Category;
import com.whosup.android.whosup.utils.Data;
import com.whosup.android.whosup.utils.SPreferences;
import com.whosup.android.whosup.utils.Utility;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyInviteAdapter extends BaseAdapter {

    protected List<Invite> inviteList;
    private LayoutInflater mInflater;
    private Context context;

    public MyInviteAdapter(Context context, List<Invite> inviteList) {
        mInflater = LayoutInflater.from(context);
        this.inviteList = inviteList;
        this.context=context;
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
            convertView = mInflater.inflate(R.layout.list_row_my_invites, parent, false);

            holder.name = (TextView) convertView.findViewById(R.id.inviteFrom);
            holder.subcategory = (TextView) convertView.findViewById(R.id.inviteSubcategory);
            holder.distance = (TextView) convertView.findViewById(R.id.inviteDistance);
            holder.address = (TextView) convertView.findViewById(R.id.inviteLocation);
            holder.hostAge = (TextView) convertView.findViewById(R.id.hostAge);
            holder.meetDay = (TextView) convertView.findViewById(R.id.meetDay);
            holder.meetHour = (TextView) convertView.findViewById(R.id.meetHour);
            holder.imgCategory = (ImageView) convertView.findViewById(R.id.imgCategory);
            holder.imgHostGender = (ImageView) convertView.findViewById(R.id.hostGender);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Invite invite = inviteList.get(position);
        holder.name.setText(invite.getFirstName()+ " "+ invite.getLastName());
        holder.subcategory.setText(invite.getSubcategory());
        holder.distance.setText(invite.getDistanceFromMe() + " Km");
        holder.address.setText(invite.getAddress());
        holder.imgCategory.setImageDrawable(getImage(invite));
        holder.meetDay.setText(Utility.arrangeDate(invite.getMeetDay()));
        holder.meetHour.setText(Utility.arrangeHour(invite.getMeetHour()));

        /*
        //today date
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        Date currentDate = null;
        //birthdate of the host formated to date
        Date hostBirthdateFormated = null;
        String hostBirthdate = invite.getBirthday();
        try {
            currentDate = df.parse(formattedDate);
            hostBirthdateFormated = df.parse(hostBirthdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        */

        holder.hostAge.setText(Utility.getDiffYears(invite.getBirthday()) + "Y");

        Drawable genderSymbol = null;
        if(invite.getGender().equals("Female")){
            genderSymbol = context.getResources().getDrawable(R.mipmap.ic_female_symbol);
        }else{
            genderSymbol = context.getResources().getDrawable(R.mipmap.ic_male_symbol);
        }
        holder.imgHostGender.setImageDrawable(genderSymbol);

        return convertView;
    }

    /*public double getDistanceKm(Invite invite){


        Location inviteLocation = new Location("");
        inviteLocation.setLatitude(Double.parseDouble(invite.getLatitude()));
        inviteLocation.setLongitude(Double.parseDouble(invite.getLongitude()));
        //Log.v("CURRENT LOCATION", "" + myLocation);


        float distanceInMeters = inviteLocation.distanceTo(myLocation);

        //Log.v("DISTANCE TO", "" + distanceInMeters);
        double distanceInKm = distanceInMeters / 1000;
        distanceInKm = Math.round(distanceInKm * 100.0) / 100.0;
        invite.setDistanceFromMe(distanceInKm);
        System.out.println("SET DISTANCE FROM ME: " + distanceInKm);
        return distanceInKm;
    }
    */

    private static class ViewHolder {
        TextView name;
        TextView subcategory;
        TextView distance;
        TextView address;
        TextView hostAge;
        TextView meetDay;
        TextView meetHour;
        ImageView imgCategory;
        ImageView imgHostGender;
    }

    public Drawable getImage(Invite invite){
        ArrayList<Category> categoriesList= Data.getInstance().getCategories(context.getApplicationContext());
        int imageResource = 0;

        for (Category category : categoriesList){

            //System.out.println(category.getName() + " == " + invite.getCategory());
            if(category.getName().equals(invite.getCategory())){

                imageResource = context.getResources().getIdentifier(category.getDrawableID(), null, context.getPackageName());

            }

        }
        Drawable res = context.getResources().getDrawable(imageResource);
        return res;
    }




}

package com.whosup.drawer.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.whosup.android.whosup.R;
import com.whosup.android.whosup.UpdateMyProfileActivity;
import com.whosup.android.whosup.utils.SPreferences;
import com.whosup.android.whosup.utils.User;
import com.whosup.listview.Invite;

import java.util.Calendar;

public class ViewProfileFragment extends Fragment {

    private ImageView photoLinkImageView;
    private User user;



    public ViewProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_profile_view, container, false);
        //Intent i = getActivity().getIntent();
        user = (User) getArguments().getSerializable("user");


        TextView usernameTextView = (TextView) rootview.findViewById(R.id.usernameText);
        TextView emailTextView = (TextView) rootview.findViewById(R.id.emailText);
        TextView customPhraseTextView = (TextView) rootview.findViewById(R.id.customPhraseText);
        TextView firstNameTextView = (TextView) rootview.findViewById(R.id.firstNameText);
        TextView lastNameTextView = (TextView) rootview.findViewById(R.id.lastNameText);
        TextView ageTextView = (TextView) rootview.findViewById(R.id.ageText);
        TextView cityCountryTextView = (TextView) rootview.findViewById(R.id.cityCountryText);
        TextView aboutMeText = (TextView) rootview.findViewById(R.id.aboutMeText);
        ImageView gender = (ImageView) rootview.findViewById(R.id.userGender);
        Drawable genderSymbol = null;
        if(user.getGender().equals("Female")){
            genderSymbol = getResources().getDrawable(R.mipmap.ic_female_symbol);
        }else{
            genderSymbol = getResources().getDrawable(R.mipmap.ic_male_symbol);
        }
        gender.setImageDrawable(genderSymbol);



        Button changeProfile = (Button) rootview.findViewById(R.id.change_profile);



        usernameTextView.setText(user.getUsername());
        if(user.isMyProfile()){
            emailTextView.setText(SPreferences.getInstance().getLoginEmail(rootview.getContext()));
        }else{
            emailTextView.setVisibility(View.GONE);
        }
        customPhraseTextView.setText("'"+user.getCustomPhrase()+"'");
        firstNameTextView.setText(user.getFirstName());
        lastNameTextView.setText(user.getLastName());
        cityCountryTextView.setText(user.getCity()+
                ", " + user.getCountry());
        aboutMeText.setText(user.getAboutMe());

        if(user.isMyProfile()){
            System.out.println("IS MY PROFILE, ADD UPDATE PROFILE BUTTON");
            changeProfile.setOnClickListener(new ChangeProfileOnClickListener());
        }else {
            changeProfile.setVisibility(View.GONE);
        }


        String birthdateFromDB = SPreferences.getInstance().getLoginBirthday(rootview.getContext());
        String[] divider = birthdateFromDB.split("-");
        int year = Integer.parseInt(divider[0]);
        int month = Integer.parseInt(divider[1]);
        int day = Integer.parseInt(divider[2]);

        Calendar birthday = Calendar.getInstance();
        birthday.set(year, month, day);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
        if(today.get(Calendar.MONTH) < birthday.get(Calendar.MONTH)) {
            age--;
        } else if (today.get(Calendar.MONTH) == birthday.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) < birthday.get(Calendar.DAY_OF_MONTH)){
            age--;
        }

        ageTextView.setText(age + " years");
        return rootview;


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.view_profile);
    }

    @Override
    public void onPause() {
        getActivity().setTitle(R.string.app_name);
        super.onPause();
    }

    private class ChangeProfileOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), UpdateMyProfileActivity.class);
            startActivity(i);
        }
    }
}

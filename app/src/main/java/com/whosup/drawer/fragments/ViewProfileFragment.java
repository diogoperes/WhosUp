package com.whosup.drawer.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whosup.android.whosup.R;
import com.whosup.android.whosup.utils.SPreferences;

import java.util.Calendar;

public class ViewProfileFragment extends Fragment {

    private ImageView photoLinkImageView;

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

        TextView usernameTextView = (TextView) rootview.findViewById(R.id.usernameText);
        TextView emailTextView = (TextView) rootview.findViewById(R.id.emailText);
        TextView customPhraseTextView = (TextView) rootview.findViewById(R.id.customPhraseText);
        TextView firstNameTextView = (TextView) rootview.findViewById(R.id.firstNameText);
        TextView lastNameTextView = (TextView) rootview.findViewById(R.id.lastNameText);
        TextView ageTextView = (TextView) rootview.findViewById(R.id.ageText);
        TextView cityCountryTextView = (TextView) rootview.findViewById(R.id.cityCountryText);
        TextView aboutMeText = (TextView) rootview.findViewById(R.id.aboutMeText);

        usernameTextView.setText(SPreferences.getInstance().getLoginUsername(rootview.getContext()));
        emailTextView.setText(SPreferences.getInstance().getLoginEmail(rootview.getContext()));
        customPhraseTextView.setText("'"+SPreferences.getInstance().getLoginCustomPhrase(rootview.getContext())+"'");
        firstNameTextView.setText(SPreferences.getInstance().getLoginFirstName(rootview.getContext()));
        lastNameTextView.setText(SPreferences.getInstance().getLoginLastName(rootview.getContext()));
        cityCountryTextView.setText(SPreferences.getInstance().getLoginCity(rootview.getContext()) +
                ", " + SPreferences.getInstance().getLoginCountry(rootview.getContext()));
        aboutMeText.setText(SPreferences.getInstance().getLoginAboutMe(rootview.getContext()));

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
}

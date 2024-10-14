package com.hioxniku.agecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;



public class DetailsAgeFragment extends Fragment {
    TextView details_age, nextBirthday, ListnextBirthday;
    CardView shareMe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vdetails = inflater.inflate(R.layout.detail_frame, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);/*Get data from previous Fragment*/
        String ageDetails = getArguments().getString("your_age");
        int ageDays = getArguments().getInt("your_ageInDays");
        int ageWeeks = getArguments().getInt("your_ageInWeeks");
        int ageMonths = getArguments().getInt("your_ageInMonths");
        String remainingBirthday = getArguments().getString("your_remainingDay", "00") + "\n Remaining";
        String nextBirthdate = getArguments().getString("your_NextBirthdayList");
        String show_ageDetails = ageDetails + "\n Your Age In Total " + ageDays + " Days\n Your Age In Total " + ageWeeks + " Weeks " + "\n Your Age In Total " + ageMonths + " Months";/*set data to the TextView*/
        details_age = vdetails.findViewById(R.id.details_ages_show);
        nextBirthday = vdetails.findViewById(R.id.nextBithdayShowId);
        ListnextBirthday = vdetails.findViewById(R.id.nBirthdayListId);
        shareMe = vdetails.findViewById(R.id.ShareMe);
        details_age.setText("\n" + show_ageDetails + "\n");
        nextBirthday.setText("\n" + remainingBirthday + "\n");
        ListnextBirthday.setText("\n" + nextBirthdate);
        shareMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, details_age.getText() + "\n \n Coming Birthday: " + nextBirthday.getText());
                startActivity(Intent.createChooser(intent, "Choose one"));
            }
        });
        return vdetails;
    }
}

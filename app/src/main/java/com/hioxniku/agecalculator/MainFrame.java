package com.hioxniku.agecalculator;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainFrame extends Fragment {
    CardView AgeCalBtn;
    RelativeLayout BirthdayDatePicker, CurrentDatePicker;
    TextView bithdayinput, currentdayinput;

    public static Bundle CalculateMyAge(Calendar startDate, Calendar endDate) {
        Calendar CurrentDateOfCalendar = Calendar.getInstance();
        Calendar startDate1 = Calendar.getInstance();
        startDate1.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH) + 1, startDate.get(Calendar.DAY_OF_MONTH));
        Calendar endDate1 = Calendar.getInstance();
        endDate1.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH) + 1, endDate.get(Calendar.DAY_OF_MONTH));
        DateCalculator dateCaculator = DateCalculator.calculateAge(startDate1, endDate1);
        StringBuilder birthdayList = new StringBuilder();
        Calendar nowBirthdayDate = Calendar.getInstance();
        nowBirthdayDate.set(CurrentDateOfCalendar.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
        List<Calendar> GetListofNextBirthday = NextBirthdaySet(nowBirthdayDate);
        Calendar nYearOfBirthdayIniation = Calendar.getInstance();
        nYearOfBirthdayIniation.set(GetListofNextBirthday.get(0).get(Calendar.YEAR), GetListofNextBirthday.get(0).get(Calendar.MONTH) + 1, GetListofNextBirthday.get(0).get(Calendar.DAY_OF_MONTH));
        CurrentDateOfCalendar.set(Calendar.MONTH, CurrentDateOfCalendar.get(Calendar.MONTH) + 1);
        Calendar thisYearBirthdayDate = Calendar.getInstance();
        thisYearBirthdayDate.set(CurrentDateOfCalendar.get(Calendar.YEAR), nowBirthdayDate.get(Calendar.MONTH) + 1, nowBirthdayDate.get(Calendar.DAY_OF_MONTH));
        if (thisYearBirthdayDate.get(Calendar.MONTH) > CurrentDateOfCalendar.get(Calendar.MONTH))
            nYearOfBirthdayIniation = (Calendar) thisYearBirthdayDate.clone();
        DateCalculator nextBirthdayCal = DateCalculator.calculateAge(CurrentDateOfCalendar, nYearOfBirthdayIniation);
        String yourBirthdayRemaingin = nextBirthdayCal.getMonth() + " Months " + nextBirthdayCal.getDay() + " Days";
        for (int i = 0; i <= GetListofNextBirthday.size() - 1; i++) {
            SimpleDateFormat formatdate = new SimpleDateFormat("EEEE dd MMMM YYY");
            Calendar dateOfNextBday = GetListofNextBirthday.get(i);
            String dateofnextYears = formatdate.format(dateOfNextBday.getTime());
            birthdayList.append(dateofnextYears + "\n");
        }
        String age = "Age: " + dateCaculator.getYear() + " Years " + dateCaculator.getMonth() + " Months " + dateCaculator.getDay() + " Days";
        int totaldays = dateCaculator.getTotalDay();
        int num_weeks = dateCaculator.getTotalDay() / 7;
        int num_months = dateCaculator.getYear() * 12 + dateCaculator.getMonth();
        Bundle dataPass = new Bundle();
        dataPass.putString("your_NextBirthdayList", String.valueOf(birthdayList));
        dataPass.putString("your_age", age);
        dataPass.putString("your_remainingDay", yourBirthdayRemaingin);
        dataPass.putInt("your_ageInWeeks", num_weeks);
        dataPass.putInt("your_ageInDays", totaldays);
        dataPass.putInt("your_ageInMonths", num_months);
        return dataPass;
    }

    public static List<Calendar> NextBirthdaySet(Calendar NBCal) {
        List<Calendar> Nday = new ArrayList();
        int i = 0;
        while (i <= 5) {
            NBCal.set(Calendar.YEAR, NBCal.get(Calendar.YEAR) + 1);
            Calendar nextYearBirthday = (Calendar) NBCal.clone();
            Nday.add(i, nextYearBirthday);
            i++;
        }
        return Nday;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mainview = inflater.inflate(R.layout.mainframe, container, false);
        AgeCalBtn = mainview.findViewById(R.id.myCalculateBtnCardId);
        BirthdayDatePicker = mainview.findViewById(R.id.bithdayRelativeAreaId);
        CurrentDatePicker = mainview.findViewById(R.id.currentDateRelativeAreaId);
        bithdayinput = mainview.findViewById(R.id.dateofBirthedittextId);
        currentdayinput = mainview.findViewById(R.id.myCurrentAgeEditTextId);/*Initiation admob ads*/
        final Calendar startDate = Calendar.getInstance();
        BirthdayDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetBirthDate(bithdayinput, startDate);
            }
        });
        /* Current date picker*/
        final Calendar endDate = Calendar.getInstance();
        CurrentDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetBirthDate(currentdayinput, endDate);
            }
        }); /*age calculation main button*/
        AgeCalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!new SimpleDateFormat("dd-MM-YYYY").format(startDate.getTime()).equals(new SimpleDateFormat("dd-MM-YYYY").format(Calendar.getInstance().getTime()))) {
                    DetailsAgeFragment detailsAgeFragment = new DetailsAgeFragment();
                    Bundle passCalculationData = CalculateMyAge(startDate, endDate);
                    detailsAgeFragment.setArguments(passCalculationData);
                    FragmentManager age_fragmentManager = getFragmentManager();
                    age_fragmentManager.beginTransaction().replace(R.id.frame_me, detailsAgeFragment).addToBackStack("tag").commit();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                } else
                    Toast.makeText(getContext(), "Valid Birthday Please", Toast.LENGTH_SHORT).show();
            }
        });
        /*mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {}

            @Override
            public void onAdFailedToLoad(int errorCode) {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdOpened() {}

            @Override
            public void onAdLeftApplication() {}

            @Override
            public void onAdClosed() {
                if (!new SimpleDateFormat("dd-MM-YYYY").format(startDate.getTime()).equals(new SimpleDateFormat("dd-MM-YYYY").format(Calendar.getInstance().getTime()))) {
                    DetailsAgeFragment detailsAgeFragment = new DetailsAgeFragment();
                    Bundle passCalculationData = CalculateMyAge(startDate, endDate);
                    detailsAgeFragment.setArguments(passCalculationData);
                    FragmentManager age_fragmentManager = getFragmentManager();
                    age_fragmentManager.beginTransaction().replace(R.id.frame_me, detailsAgeFragment).addToBackStack("tag").commit();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                } else
                    Toast.makeText(getContext(), "Valid Birthday Please", Toast.LENGTH_SHORT).show();
            }
        });*/
        return mainview;
    }

    private void GetBirthDate(final TextView getDate, final Calendar myCalendar) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-YYYY"; /*In which you need put here*/
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                getDate.setText(sdf.format(myCalendar.getTime()));
            }
        };
        new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}

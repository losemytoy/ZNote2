package xyz.time;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * My File Created by xyz on 2018/4/26.
 */

public class DateAndTimePicker {


    private Context mContext;
    private Calendar mCalendar;
    private OnMyTimeChangedListener mListener;


    public DateAndTimePicker(Context context, Calendar calendar) {
        mContext = context;
        this.mCalendar = calendar;

    }

    public void show() {


        ViewPager viewPager;
        TabLayout tabLayout;
        List<View> views;
        DatePicker datePicker = new DatePicker(mContext);

        if(mCalendar==null) {
           mCalendar = Calendar.getInstance();
            mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            mCalendar.setTimeInMillis(System.currentTimeMillis());
        }

        datePicker.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(year, monthOfYear, dayOfMonth);
            }
        });
        TimePicker timePicker = new TimePicker(mContext);
        timePicker.setHour(mCalendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setMinute(mCalendar.get(Calendar.MINUTE));
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY,
                        hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
            }
        });
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.date_time, null, false);
        views = new ArrayList<>();
        views.add(datePicker);
        views.add(timePicker);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyPagerAdapter(views));
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        Dialog dialog = new AlertDialog.Builder(mContext)
                .setTitle(R.string.select_time)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onSelected(mCalendar);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onCanceled();
                    }
                }).create();
        dialog.show();
    }

    private class MyPagerAdapter extends PagerAdapter {
        String[] titles = {mContext.getString(R.string.date), mContext.getString(R.string.time)};
        List<View> views;

        MyPagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    public interface  OnMyTimeChangedListener{
        void onSelected(Calendar calendar);
        void onCanceled();
    }

    public void setOnMyTimeChangedListener(OnMyTimeChangedListener listener) {
             this.mListener=listener;
    }
}

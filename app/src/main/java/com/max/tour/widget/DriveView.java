package com.max.tour.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.amap.api.services.route.DrivePath;
import com.max.tour.R;
import com.max.tour.utils.map.AMapUtil;

public class DriveView extends ConstraintLayout {

    private TextView arriveTime;
    private TextView duration;
    private DrivePath drivePath;

    public DriveView(Context context) {
        super(context);
    }

    public DriveView(Context context, AttributeSet attrs) {
        super(context,attrs);
        initView(context);
    }

    public DriveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.driver_layout, this);
        arriveTime = findViewById(R.id.drive_arrive_time);
        duration    = findViewById(R.id.drive_spend_duration);
    }

    public void setPath(DrivePath drivePath) {
        this.drivePath = drivePath;
        String time = AMapUtil.times(drivePath.getDuration() + System.currentTimeMillis()/1000);

        long minute = (drivePath.getDuration())/60;
        String spendDuration = String.format("%d", minute);
        arriveTime.setText(time);
        duration.setText(spendDuration);
    }
}

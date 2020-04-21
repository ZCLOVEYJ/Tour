package com.max.tour.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.route.DrivePath;
import com.max.tour.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DriveView extends ConstraintLayout implements View.OnClickListener {


    private LinearLayout mLayoutOne;
    private LinearLayout mLayoutTwo;
    private LinearLayout mLayoutThree;
    private TextView mTvTitleOne;
    private TextView mTvTitleTwo;
    private TextView mTvTitleThree;
    private TextView mTvTimeOne;
    private TextView mTvTimeTwo;
    private TextView mTvTimeThree;
    private TextView mTvDistanceOne;
    private TextView mTvDistanceTwo;
    private TextView mTvDistanceThree;
    private TextView mTvStart;

    private ItemClickListener listener;

    List<DrivePath> mDrivePaths = new ArrayList<>();
    private int mRouteSize = 0;


    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }


    public DriveView(Context context) {
        super(context);
    }

    public DriveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DriveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.driver_layout, this);
        mLayoutOne = findViewById(R.id.layout_one);
        mLayoutTwo = findViewById(R.id.layout_two);
        mLayoutThree = findViewById(R.id.layout_three);
        mTvTitleOne = findViewById(R.id.tv_title_one);
        mTvTitleTwo = findViewById(R.id.tv_title_two);
        mTvTitleThree = findViewById(R.id.tv_title_three);
        mTvTimeOne = findViewById(R.id.tv_time_one);
        mTvTimeTwo = findViewById(R.id.tv_time_two);
        mTvTimeThree = findViewById(R.id.tv_time_three);
        mTvDistanceOne = findViewById(R.id.tv_distance_one);
        mTvDistanceTwo = findViewById(R.id.tv_distance_two);
        mTvDistanceThree = findViewById(R.id.tv_distance_three);
        mTvStart = findViewById(R.id.tv_start);
        mLayoutOne.setOnClickListener(this);
        mLayoutTwo.setOnClickListener(this);
        mLayoutThree.setOnClickListener(this);
        mTvStart.setOnClickListener(this);

    }

    public void setPaths(List<DrivePath> drivePaths) {
        if (mDrivePaths.size() > 0) {
            return;
        }
        mDrivePaths.addAll(drivePaths);
        if (mRouteSize > 2) {
            long minute = (drivePaths.get(2).getDuration()) / 60;
            String spendDuration = String.format("%d", minute);
            mTvTimeThree.setText(spendDuration + "分钟");

            float dis = drivePaths.get(2).getDistance() / 1000;
            DecimalFormat format = new DecimalFormat("0.0");
            String str = format.format(dis);
            mTvDistanceThree.setText(str + "公里");

        }
        if (mRouteSize > 1) {
            long minute = (drivePaths.get(1).getDuration()) / 60;
            String spendDuration = String.format("%d", minute);
            mTvTimeTwo.setText(spendDuration + "分钟");

            float dis = drivePaths.get(1).getDistance() / 1000;
            DecimalFormat format = new DecimalFormat("0.0");
            String str = format.format(dis);
            mTvDistanceTwo.setText(str + "公里");
        }
        if (mRouteSize > 0) {
            long minute = (drivePaths.get(0).getDuration()) / 60;
            String spendDuration = String.format("%d", minute);
            mTvTimeOne.setText(spendDuration + "分钟");

            float dis = drivePaths.get(0).getDistance() / 1000;
            DecimalFormat format = new DecimalFormat("0.0");
            String str = format.format(dis);
            mTvDistanceOne.setText(str + "公里");
        }


//        String time = AMapUtil.times(drivePath.getDuration() + System.currentTimeMillis() / 1000);
//        long minute = (drivePath.getDuration()) / 60;
//        String spendDuration = String.format("%d", minute);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_one:
                mTvTitleOne.setEnabled(true);
                mTvTitleTwo.setEnabled(false);
                mTvTitleThree.setEnabled(false);

                mTvTimeOne.setEnabled(true);
                mTvTimeTwo.setEnabled(false);
                mTvTimeThree.setEnabled(false);

                mTvDistanceOne.setEnabled(true);
                mTvDistanceTwo.setEnabled(false);
                mTvDistanceThree.setEnabled(false);
                listener.onItem(0);


                break;
            case R.id.layout_two:
                mTvTitleOne.setEnabled(false);
                mTvTitleTwo.setEnabled(true);
                mTvTitleThree.setEnabled(false);

                mTvTimeOne.setEnabled(false);
                mTvTimeTwo.setEnabled(true);
                mTvTimeThree.setEnabled(false);

                mTvDistanceOne.setEnabled(false);
                mTvDistanceTwo.setEnabled(true);
                mTvDistanceThree.setEnabled(false);

                listener.onItem(1);
                break;
            case R.id.layout_three:
                mTvTitleOne.setEnabled(false);
                mTvTitleTwo.setEnabled(false);
                mTvTitleThree.setEnabled(true);

                mTvTimeOne.setEnabled(false);
                mTvTimeTwo.setEnabled(false);
                mTvTimeThree.setEnabled(true);

                mTvDistanceOne.setEnabled(false);
                mTvDistanceTwo.setEnabled(false);
                mTvDistanceThree.setEnabled(true);
                listener.onItem(2);
                break;
            case R.id.tv_start:
                listener.onItem(3);
                break;
            default:
                break;
        }


    }

    public void setRouteSize(int routeSize) {
        mRouteSize = routeSize;
        if (mRouteSize == 1) {
            mLayoutTwo.setVisibility(GONE);
            mLayoutThree.setVisibility(GONE);
        } else if (mRouteSize == 2) {
            mLayoutTwo.setVisibility(VISIBLE);
            mLayoutThree.setVisibility(GONE);
        } else if (mRouteSize == 3) {
            mLayoutTwo.setVisibility(VISIBLE);
            mLayoutThree.setVisibility(VISIBLE);
        }
    }

    public void clear() {
        mRouteSize = 0;
        mDrivePaths.clear();

    }

    public interface ItemClickListener {

        void onItem(int tag);
    }
}

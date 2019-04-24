package com.example.drmarker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drmarker.Step.DateTimeHelper;
import com.example.drmarker.Step.StepService;
import com.example.drmarker.stepModel.StepModel;
import com.example.drmarker.stepModel.StepTransaction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;


public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private TextView showSteps;
    private View mLayout;
    Switch on_off, foreground_model;
    SharedPreferences sharedPreferences;
    EventBus bus;
    long numSteps;
    boolean isServiceRun;
    boolean isGuest = false;
    boolean isforeground_model;
    public static String uid;
    TextView btn;
    TextView about;
    LineChartView lineChart;
    List<PointValue> mPointValues = new ArrayList<>();
    List<AxisValue> mAxisXValues = new ArrayList<>();
    BottomNavigationView navView;

    public void mybt(View v) {
        showPopupWindow(v);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent fromAbove = getIntent();
        isGuest = fromAbove.getBooleanExtra("guest",false);
        uid= fromAbove.getStringExtra("uid");
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) toolbar.getLayoutParams();
//        params.setMargins(0,getStatusBarHeight(), 0, 0);
//        toolbar.setLayoutParams(params);
        setSupportActionBar(toolbar);
        btn = (TextView) findViewById(R.id.bt);
//        Typeface Font = Typeface.createFromAsset(this.getAssets(), "iconfont.ttf");
        btn.setText("Switches");
        btn.setTextSize(19f);
//        btn.setTypeface(Font);

        Log.d("eee", "on create()");
        showSteps = (TextView) findViewById(R.id.showSteps);
        mLayout = findViewById(R.id.mylayout);
        on_off = (Switch) findViewById(R.id.on_off);
        foreground_model = (Switch) findViewById(R.id.foreground_model);


        sharedPreferences = getSharedPreferences("conf", MODE_PRIVATE);

        detectService();

        bus = EventBus.getDefault();
        bus.register(this);

        Realm realm = Realm.getDefaultInstance();
        StepModel result = realm.where(StepModel.class)
                .equalTo("date", DateTimeHelper.getToday()).equalTo("uid",uid)
                .findFirst();
        numSteps = result == null ? 0 : result.getNumSteps();
        bus.post(true);
        updateShowSteps();
        realm.close();

        drawChart();

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //Do nothing
                    return true;

                case R.id.navigation_monitor:
                    //Do something
                    if (!isGuest){
                        Intent intent_monitor=new Intent(MainActivity.this, InputActivity.class);
                        intent_monitor.putExtra("uid",uid);
                        if (isGuest)intent_monitor.putExtra("guest",true);
                        startActivity(intent_monitor);
                        finish();
                        return true;
                    }else{
                        Toast.makeText(MainActivity.this, "To use monitor, please login", Toast.LENGTH_SHORT).show();
                        return false;
                    }


                case R.id.navigation_forum:
                    //Do something
                    if (!isGuest){
                        Intent intent_forum=new Intent(MainActivity.this, ForumActivity.class);
                        intent_forum.putExtra("uid",getIntent().getStringExtra("uid"));
                        startActivity(intent_forum);
                        finish();
                        return true;

                    }else{
                        Toast.makeText(MainActivity.this, "To use forum, please login", Toast.LENGTH_SHORT).show();
                        return false;
                    }


                case R.id.navigation_me:
                    //Do something

                    if (!isGuest){
                        Intent intent_me=new Intent(MainActivity.this, MeActivity.class);
                        intent_me.putExtra("uid",uid);
                        if (isGuest)intent_me.putExtra("guest",true);
                        startActivity(intent_me);
                        finish();
                        return true;
                    }else {
                        Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                        intent.putExtra("guest",true);
                        intent.putExtra("uid",uid);
                        startActivity(intent);
                        finish();
                        return true;
                    }


            }
            return false;
        }
    };




    public void drawChart() {

        // WeatherChartView mCharView = (WeatherChartView) findViewById(R.id.line_char);
        Date[] days = DateTimeHelper.get6days();

        Realm realm = Realm.getDefaultInstance();

        int[] data = new int[]{0, 0, 0, 0, 0, 0};
        int i = 0;
        for (Date d : days) {
            Log.d("eee","date "+d);
            if (i == 5) {
                data[i] = Integer.parseInt(String.valueOf(numSteps));
            }
            else {
                StepModel result = realm.where(StepModel.class)
                        .equalTo("date", d).equalTo("uid",uid)
                        .findFirst();
                if (result != null) {
                    Log.d("eee","r !null  ");
                    data[i] = Integer.parseInt(String.valueOf(result.getNumSteps()));
                }
            }
            i++;
        }

        realm.close();

        String[] xValues = DateTimeHelper.get6days(true);


        lineChart = (LineChartView) findViewById(R.id.line_chart);
        for (i = 0; i < xValues.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(xValues[i]));
        }

        for (i = 0; i < data.length; i++) {
            mPointValues.add(new PointValue(i, data[i]));
        }
        initLineChart();

    }

    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFFAFA"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<>();
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(false);
        line.setFilled(false);
        line.setHasLabels(true);
//      line.setHasLabelsOnlyForSelected(true);
        line.setHasLines(true);
        line.setHasPoints(true);
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);


        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.WHITE);
        //axisX.setName("date");
        axisX.setTextSize(10);
        axisX.setMaxLabelChars(8);
        axisX.setValues(mAxisXValues);
        data.setAxisXBottom(axisX);
        //data.setAxisXTop(axisX);
        axisX.setHasLines(true);


        Axis axisY = new Axis();

        axisY.setName("");
        // axisY.setTextSize(10);
        axisY.setTextColor(Color.parseColor("#ffffff"));
        data.setAxisYLeft(axisY);
        //data.setAxisYRight(axisY);



        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

//        Viewport v = new Viewport(lineChart.getMaximumViewport());
//        v.left = 0;
//        v.right= 7;
//        lineChart.setCurrentViewport(v);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateSteps(Long num) {
        numSteps = num;
        updateShowSteps();

    }

    public void updateShowSteps() {
        String text = "" + numSteps;

        if (numSteps >= 10000000)
            showSteps.setTextSize(45);

        else if (numSteps >= 1000000)
            showSteps.setTextSize(50);
        else if (numSteps >= 100000)
            showSteps.setTextSize(55);
        else if (numSteps >= 10000) {
            notifyIsUpToStandard( "Excellent, More than 10k steps today!");
            showSteps.setTextSize(60);
        }

        else {
            showSteps.setTextSize(66);
            if (numSteps>=5000) notifyIsUpToStandard("Come on，More closer to 10k steps");
            else notifyIsUpToStandard("You haven't walked today. Go out and exercise!");
        }
        showSteps.setText(text);

    }

    private void notifyIsUpToStandard(String msg)
    {
        MyApplication app = (MyApplication) getApplication();
        if(!app.isShowToast()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            app.setShowToast(true);
        }

    }

    private void showPopupWindow(View view) {


        MyApplication app = (MyApplication) getApplication();
        isServiceRun=app.getServiceRun();

        isforeground_model=sharedPreferences.getBoolean("foreground_model",false);

        View contentView = LayoutInflater.from(this).inflate(
                R.layout.setting_layout, null);
        PopupWindow popupWindow = new PopupWindow(contentView,
                370, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        on_off = (Switch) contentView.findViewById(R.id.on_off);
        foreground_model = (Switch) contentView.findViewById(R.id.foreground_model);
        on_off.setChecked(isServiceRun);
        foreground_model.setChecked(isforeground_model);

        on_off.setOnCheckedChangeListener(this);
        foreground_model.setOnCheckedChangeListener(this);
        about = (TextView) contentView.findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAbout();

            }
        });


//        popupWindow.setTouchable(true);
//
//        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                Log.d("mengdd", "onTouch : ");
//
//                return false;
//
//            }
//        });


//        popupWindow.setBackgroundDrawable(getResources().getDrawable(
//                R.drawable.selectmenu_bg_downward));


        popupWindow.showAsDropDown(view);

    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    public void showAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("Confirm", null);
        builder.setCancelable(true);
        View mview = LayoutInflater.from(this).inflate(R.layout.about_me, null);
        TextView t = (TextView) mview.findViewById(R.id.version_name);
        String s = getVersionName(this);
        if (t != null) {
            t.setText("v" + s);
        }

        builder.setView(mview);
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("eee", "activity stop()");
        bus.post(false);
        if (bus.isRegistered(this))
            bus.unregister(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView.getId() == R.id.on_off) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("switch_on", isChecked);
            editor.apply();
            Intent intent = new Intent(this, StepService.class);
            intent.putExtra("uid",uid);

            if (isChecked) {
                intent.putExtra("isActivity", true);
                if (!bus.isRegistered(this))
                    bus.register(this);
                startService(intent);
                bus.post(true);
            } else {
                editor.putBoolean("foreground_model", isChecked);
                editor.apply();
                foreground_model.setChecked(false);
                if (bus.isRegistered(this))
                    bus.unregister(this);
                stopService(intent);
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new StepTransaction(DateTimeHelper.getToday(), numSteps,uid));
                realm.close();
            }
        } else if (buttonView.getId() == R.id.foreground_model) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("foreground_model", isChecked);
            editor.apply();

            Intent intent = new Intent(this, StepService.class);
            intent.putExtra("uid",uid);
            if (isChecked) {
                editor.putBoolean("switch_on", isChecked);
                editor.apply();
                on_off.setChecked(true);
                intent.putExtra("foreground_model", "on");
                intent.putExtra("isActivity", true);
                if (!bus.isRegistered(this))
                    bus.register(this);
                bus.post(true);
            } else {
                intent.putExtra("foreground_model", "off");
            }
            startService(intent);
        }

    }

    public void detectService() {
        MyApplication app = (MyApplication) getApplication();
        isServiceRun = app.getServiceRun();
        boolean temp = sharedPreferences.getBoolean("switch_on", false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isServiceRun != temp) {
            if (!isServiceRun) {
                Toast.makeText(getApplicationContext(), "The pedestrian service terminated unexpectedly. Please add the application to the whitelist",
                        Toast.LENGTH_LONG).show();
            }
            editor.putBoolean("switch_on", isServiceRun);
            editor.apply();
        }

        temp = sharedPreferences.getBoolean("foreground_model", false);
        if (temp && !isServiceRun) {
            editor.putBoolean("foreground_model", false);
            editor.apply();
            isforeground_model = false;
        } else isforeground_model = temp;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void sdWrite() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                if (this.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Snackbar.make(mLayout, "Apply authority",
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(MainActivity.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            0);
                                }
                            })
                            .show();
                } else {
                    this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }


            }
        }
    }

}

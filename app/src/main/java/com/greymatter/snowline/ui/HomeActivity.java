package com.greymatter.snowline.ui;

import android.app.Dialog;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gms.maps.SupportMapFragment;
import com.greymatter.snowline.db_v2.DBServices;
import com.greymatter.snowline.db_v2.PopulateDBHelper;
import com.greymatter.snowline.ui.helpers.HomeActivityUIHelper;
import com.greymatter.snowline.Handlers.MapHandler;
import com.greymatter.snowline.R;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class HomeActivity extends FragmentActivity{
    private View.OnTouchListener onTouchListener;
    private SupportMapFragment mapFragment;
    private MapHandler mapHandler;
    private PlanningTab planningTab;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.onCreate(savedInstanceState);

        mapHandler = new MapHandler(HomeActivity.this, HomeActivity.this, mapFragment);
        planningTab = new PlanningTab(this, (RelativeLayout)findViewById(R.id.planning_tab), mapHandler);

        mapFragment.getMapAsync(mapHandler);

        setUpUIElements();
        setUpOnTouchEventListener();

        DBServices.initDB(this);
//        PopulateDBHelper.populateGlobalData();
//        final Dialog loadingDialog = generateLoadingDialog();
//        Handler onDBPopulated = new Handler(Looper.getMainLooper()){
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                loadingDialog.dismiss();
//            }
//        };
//        loadingDialog.show();
//        PopulateDBHelper.populateTransitDB(this, onDBPopulated);
    }

    public Dialog generateLoadingDialog() {
        Dialog dialog  = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_dialog);
        ImageView gifImageView = dialog.findViewById(R.id.loading_dialog_image_view);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);
        Glide.with(this)
                .load(R.drawable.loading_anim_iii)
                .placeholder(R.drawable.loading_anim_iii)
                .centerCrop()
                .crossFade()
                .into(imageViewTarget);
        return dialog;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Handler onPermissionDenied = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                final Dialog dialog = new Dialog(HomeActivity.this);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);

                View permDeniedDialog = LayoutInflater.from(HomeActivity.this).inflate(R.layout.permissions_denied_dialog, null);
                Button permGrant = permDeniedDialog.findViewById(R.id.perm_denied_grant_perm);
                Button cancel = permDeniedDialog.findViewById(R.id.perm_denied_cancel);

                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch(v.getId()) {
                            case R.id.perm_denied_grant_perm:
                                mapHandler.requestPermissions();
                                dialog.dismiss();
                                break;
                            case R.id.perm_denied_cancel:
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                permGrant.setOnClickListener(listener);
                cancel.setOnClickListener(listener);

                dialog.setContentView(permDeniedDialog);

                dialog.show();
            }
        };

        mapHandler.onRequestPermissionsResult(requestCode, permissions, grantResults, onPermissionDenied);
    }

    private void setUpUIElements() {
        //HomeActivityUIHelper.setKeyboardVisibilityListener(HomeActivity.this, planningTab);
    }


    @Override
    protected void onStart() {
        mapFragment.onStart();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mapFragment.onStop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        mapFragment.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapFragment.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        PopulateDBHelper.onDestroy();
        DBServices.close();

        mapFragment.onDestroy();
        super.onDestroy();
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        mapFragment.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        mapFragment.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        planningTab.onBackPressed();
        super.onBackPressed();
    }

    private void setUpOnTouchEventListener(){
        onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int)event.getRawX();
                int y = (int)event.getRawY();

                switch (event.getAction()){
                    case ACTION_DOWN:

                        break;
                    case ACTION_MOVE:

                        break;
                    case ACTION_UP:

                        break;
                }

                return false;
            }
        };
    }
}
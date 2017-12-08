/*
 *
 * PlayerActivity.java
 * 
 * Created by Wuwang on 2016/9/29
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.example.vstar.cameraplaydemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;

import com.example.vstar.cameraplaydemo.play.MPlayer;
import com.example.vstar.cameraplaydemo.play.MPlayerException;
import com.example.vstar.cameraplaydemo.play.MinimalDisplay;

import java.io.File;

/**
 * Description:
 */
public class MediaPlayer1Activity extends Activity {

    private EditText mEditAddress;
    private SurfaceView mPlayerView;
    private MPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        initPlayer();
    }

    private void initView(){
        mEditAddress= (EditText) findViewById(R.id.mEditAddress);
        mPlayerView= (SurfaceView) findViewById(R.id.mPlayerView);
    }

    private void initPlayer(){
        player=new MPlayer();
        player.setDisplay(new MinimalDisplay(mPlayerView));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ydzhu","onResume");
        player.setDisplay(new MinimalDisplay(mPlayerView));
        player.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("ydzhu","onPause");
        player.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.onDestroy();
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.mPlay:
                Log.e("ydzhu","onClick---mPlay");
                //String mUrl=mEditAddress.getText().toString();

                //if(mUrl.length()>0){
                //    Log.e("wuwang","播放->"+mUrl);
                    try {
                        //player.setSource(mUrl);
                        String dir = Environment.getExternalStorageDirectory()+"/DCIM/Eye4";
                        File file = new File(dir, "VSTA000004YWFVH_20171207174446.mp4");
                        String mPath = file.getAbsolutePath();
                        player.setSource(mPath);
                        player.play();
                        Log.e("ydzhu","onClick---mPlay---1");
                    } catch (MPlayerException e) {
                        e.printStackTrace();
                    }
               // }
                break;
            case R.id.mPlayerView:
                Log.e("ydzhu","onClick---mPlayerView");
                if(player.isPlaying()){
                    player.pause();
                }else{
                    try {
                        player.play();
                    } catch (MPlayerException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.mType:
                Log.e("ydzhu","onClick---mType");
                player.setCrop(!player.isCrop());
                break;
        }
    }

}

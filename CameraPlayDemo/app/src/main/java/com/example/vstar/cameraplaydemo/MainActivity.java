package com.example.vstar.cameraplaydemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView mainList;
    private MainAdapter mainViewAdapter;
    private ArrayList<String> currentMapValue;
    private String[] listStrAry=new String[]{
            "相机预览---GLSurfaceView+Renderer+SurfaceTexture",
            "基本操作---GLSurfaceView+Renderer",
            "绘制正方形---GLSurfaceView+Renderer",
            "解码播放---GLSurfaceView+Renderer+SurfaceTexture+Surface"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        //currentMapValue.set(0,"相机预览---GLSurfaceView+SurfaceTexture");
        //currentMapValue.set(1,"GLSurfaceView基本操作");
        //currentMapValue.set()
        mainList = (ListView) findViewById(R.id.mainListView);
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG,"initUI---onItemClick---i="+i);
                Log.e(TAG,"initUI---onItemClick---l="+l);
                switch (i){
                    case 0:
                        Intent cameryActivity = new Intent(MainActivity.this, CameraPlayActivity.class);
                        startActivity(cameryActivity);
                        break;
                    case 1:
                        Intent GLTestActivity = new Intent(MainActivity.this, GLSurfaceViewTestActivity.class);
                        startActivity(GLTestActivity);
                        break;
                    case 2:
                        Intent GLDrawActivity = new Intent(MainActivity.this, GLSurfaceViewDrawActivity.class);
                        startActivity(GLDrawActivity);
                        break;
                    case 3:
                        Intent GLDecoderActivity = new Intent(MainActivity.this, DecoderGLSurfaceActivity.class);
                        startActivity(GLDecoderActivity);
                        break;
                    default:
                        break;
                }
            }
        });
        currentMapValue = new ArrayList<String>();
        for(String str:listStrAry) {
            currentMapValue.add(str);
        }
        mainViewAdapter = new MainAdapter();
        //给listview设置数据
        mainList.setAdapter(mainViewAdapter);
        mainViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }

    class MainAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return currentMapValue==null?0:currentMapValue.size();
        }

        @Override
        public String getItem(int position) {
            return currentMapValue.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            //view
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.main_list_item, null);
                holder.itp_time_iv = (TextView) convertView.findViewById(R.id.itp_time_iv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.itp_time_iv.setText(currentMapValue.get(position));

            return convertView;
        }

        class ViewHolder {
            TextView itp_time_iv;
        }
    }
}

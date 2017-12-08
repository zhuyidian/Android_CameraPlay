
package com.example.vstar.cameraplaydemo.SurfaceView;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.vstar.cameraplaydemo.CameraEngineActivity;

import java.util.List;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private static String TAG = "CameraView";

    private SurfaceHolder mSurfaceHolder = null;

    private Camera mCamera = null;
    
    int mPreviewHeight = 800; //
    int mPreviewWidth = 480;
    
    ProcessThread mProcessThread = new ProcessThread();
    private static Object INSTANCE_LOCK = new Object();
    byte[] mTempData;
    int mWidth = 0, mHeight = 0;
    int mFrameRate = 0;
    long mFrameRateStartTime = 0;

    public CameraView(Context context) {
        super(context);
        Log.v(TAG, "CameraView .");
        init_impl();
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.v(TAG, "CameraView ..");
        init_impl();
    }

    public CameraView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Log.v(TAG, "CameraView ...");
        init_impl();
    }

    void init_impl() {
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mProcessThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v(TAG, "surfaceChanged width =" + width + "height =" + height);
        if (holder.getSurface() == null) {
            Log.d(TAG, "holder.getSurface() == null");
            return;
        }
        mSurfaceHolder = holder;

        initCamera();
    }
public boolean ttt = false;
public boolean ttt1 = false;
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();// 开启摄像头（2.3版本后支持多摄像头,需传入参数）
        try {
            Log.v(TAG, "SurfaceHolder.Callback：surface Created");  
            
            mCamera.setPreviewCallback(new PreviewCallback(){

                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    // TODO Auto-generated method stub
//                    YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21,
//                            camera.getParameters().getPreviewSize().width,
//                            camera.getParameters().getPreviewSize().height, null);
//                    ByteArrayOutputStream mJpegOutput = new ByteArrayOutputStream(data.length);
//
//                    yuvimage.compressToJpeg(new Rect(0, 0, mWidth, mHeight), 100, mJpegOutput); 
//                    Bitmap mBitmapIn = BitmapFactory.decodeByteArray( mJpegOutput.toByteArray(), 0, mJpegOutput.size());
//                    CameraEngineActivity.getRender().update(mBitmapIn);
//                    Log.e(TAG, "bbbbb");
                    
                    mWidth = camera.getParameters().getPreviewSize().width;
                    mHeight = camera.getParameters().getPreviewSize().height;
                    int length = data.length;

                    if(mFrameRateStartTime == 0)
                    {
                        mFrameRateStartTime = System.currentTimeMillis();
                    }
                    mFrameRate ++;
                    if(mFrameRate % 30 == 0){
                        long rate = mFrameRate * 1000 / (System.currentTimeMillis() - mFrameRateStartTime);
                        Toast.makeText(CameraEngineActivity.getRender().mContext, "Frame Rate:" + rate, Toast.LENGTH_SHORT)
                        .show();
                    }

                    long start = System.currentTimeMillis();
                    int[] buf = CameraEngineActivity.getImageEngine().decodeYUV420SP(data, mWidth, mHeight);
                    start = System.currentTimeMillis() - start;
                    start = System.currentTimeMillis();
                    CameraEngineActivity.getRender().mFrameBuf = buf;
                    CameraEngineActivity.getRender().update(buf, mWidth, mHeight);
                    start = System.currentTimeMillis() - start;

                }
            });
            
            mCamera.setPreviewDisplay(mSurfaceHolder);// set the surface to be used for live preview

        } catch (Exception ex) {
            if (null != mCamera) {
                mCamera.release();
                mCamera = null;
            }
        }
    }
    
    class ProcessThread extends Thread { 
        public Handler mHandler; 
        
     
        public void run() { 
            Looper.prepare(); //创建本线程的Looper并创建一个MessageQueue
     
            mHandler = new Handler() { 
                public void handleMessage(Message msg) { 
                    // process incoming messages here 
                    switch (msg.what) {
                        case 0:
                            processData();
                            break;
                        default:
                            break;
                    }
                } 
            }; 
       
            Looper.loop(); //开始运行Looper,监听Message Queue 
        } 
        public void processData(){
            synchronized (INSTANCE_LOCK){
                
                int[] buf = CameraEngineActivity.getImageEngine().decodeYUV420SP(mTempData, mWidth, mHeight);
              CameraEngineActivity.getRender().update(buf, mWidth, mHeight);
            }
        }
    } 
    
    static void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {

        final int frameSize = width * height;

        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0)
                    y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }

                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0)
                    r = 0;
                else if (r > 262143)
                    r = 262143;
                if (g < 0)
                    g = 0;
                else if (g > 262143)
                    g = 262143;
                if (b < 0)
                    b = 0;
                else if (b > 262143)
                    b = 262143;

                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v(TAG, "surfaceDestroyed");
        
        if(null != mCamera)
        {
         mCamera.setPreviewCallback(null); //！！这个必须在前，不然退出出错
         mCamera.stopPreview(); 
         mCamera.release();
         mCamera = null;
        }
        
        mSurfaceHolder = null;
    }

    private void initCamera()// surfaceChanged中调用
    {
        Log.i(TAG, "going into initCamera");
 
            mCamera.stopPreview();// stopCamera();
      
        if (null != mCamera) {
            try {
                /* Camera Service settings */
                Camera.Parameters parameters = mCamera.getParameters();
                // parameters.setFlashMode("off"); // 无闪光灯
                parameters.setPictureFormat(PixelFormat.JPEG); // Sets the image format for picture
                                                               // 设定相片格式为JPEG，默认为NV21
               parameters.setPreviewFormat(PixelFormat.YCbCr_420_SP); // Sets the image format for
                                                                       // preview picture，默认为NV21
                /*
                 * 【ImageFormat】JPEG/NV16(YCrCb format，used for Video)/NV21(YCrCb format，used for
                 * Image)/RGB_565/YUY2/YU12
                 */

                // 【调试】获取caera支持的PictrueSize，看看能否设置？？
                List<Size> pictureSizes = mCamera.getParameters().getSupportedPictureSizes();
                List<Size> previewSizes = mCamera.getParameters().getSupportedPreviewSizes();
                List<Integer> previewFormats = mCamera.getParameters().getSupportedPreviewFormats();
                List<Integer> previewFrameRates = mCamera.getParameters().getSupportedPreviewFrameRates();
                Log.i(TAG + "initCamera", "cyy support parameters is ");
                Size psize = null;
                for (int i = 0; i < pictureSizes.size(); i++) {
                    psize = pictureSizes.get(i);
                    Log.e(TAG + "initCamera", "PictrueSize,width: " + psize.width + " height" + psize.height);
                }
                for (int i = 0; i < previewSizes.size(); i++) {
                    psize = previewSizes.get(i);
                    Log.e(TAG + "initCamera", "PreviewSize,width: " + psize.width + " height" + psize.height);
                }
                Integer pf = null;
                for (int i = 0; i < previewFormats.size(); i++) {
                    pf = previewFormats.get(i);
                    Log.e(TAG + "initCamera", "previewformates:" + pf);
                }

                // 设置拍照和预览图片大小
                parameters.setPictureSize(640, 480); // 指定拍照图片的大小
                parameters.setPreviewSize(mPreviewHeight, mPreviewWidth); // 指定preview的大小
                // 这两个属性 如果这两个属性设置的和真实手机的不一样时，就会报错

                // 横竖屏镜头自动调整
                if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    parameters.set("orientation", "portrait"); //
                    //parameters.set("rotation", 90); // 镜头角度转90度（默认摄像头是横拍）
                    mCamera.setDisplayOrientation(90); // 在2.2以上可以使用
                } else// 如果是横屏
                {
                    parameters.set("orientation", "landscape"); //
                    mCamera.setDisplayOrientation(0); // 在2.2以上可以使用
                }

                /* 视频流编码处理 */
                // 添加对视频流处理函数

                // 设定配置参数并开启预览
                mCamera.setParameters(parameters); // 将Camera.Parameters设定予Camera
                
                mCamera.setPreviewCallback(new PreviewCallback(){

                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        // TODO Auto-generated method stub
                        mWidth = camera.getParameters().getPreviewSize().width;
                        mHeight = camera.getParameters().getPreviewSize().height;

                        if(mFrameRateStartTime == 0)
                        {
                            mFrameRateStartTime = System.currentTimeMillis();
                        }
                        mFrameRate ++;
                        if(mFrameRate % 30 == 0){
                            long rate = mFrameRate * 1000 / (System.currentTimeMillis() - mFrameRateStartTime);
                            Toast.makeText(CameraEngineActivity.getRender().mContext, "Frame Rate:" + rate, Toast.LENGTH_SHORT)
                            .show();
                        }
                        long start = System.currentTimeMillis();
                        int[] buf = CameraEngineActivity.getImageEngine().decodeYUV420SP(data, mWidth, mHeight);
                        start = System.currentTimeMillis() - start;

                        start = System.currentTimeMillis();
                        CameraEngineActivity.getRender().mFrameBuf = buf;
                        CameraEngineActivity.getRender().update(buf, mWidth, mHeight);
                        start = System.currentTimeMillis() - start;

                    }
                });
                mCamera.startPreview(); // 打开预览画面

                // 【调试】设置后的图片大小和预览大小以及帧率
                Size csize = mCamera.getParameters().getPreviewSize();
                mPreviewHeight = csize.height; //
                mPreviewWidth = csize.width;
                Log.v(TAG + "initCamera", "after setting, previewSize:width: " + csize.width + " height: "
                        + csize.height);
                csize = mCamera.getParameters().getPictureSize();
                Log.v(TAG + "initCamera", "after setting, pictruesize:width: " + csize.width + " height: "
                        + csize.height);
                Log.v(TAG + "initCamera", "after setting, previewformate is "
                        + mCamera.getParameters().getPreviewFormat());
                Log.v(TAG + "initCamera", "after setting, previewframetate is "
                        + mCamera.getParameters().getPreviewFrameRate());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

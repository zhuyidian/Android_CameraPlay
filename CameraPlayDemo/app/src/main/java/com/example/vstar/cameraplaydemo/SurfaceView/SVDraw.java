package com.example.vstar.cameraplaydemo.SurfaceView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2017/12/8.
 */
/*只不过一个是继承的view一个是surfaceview，将AttributeSetattrs加上。只要处理好谁是顶层的view谁设成透明，
预览视频的surfaceview设成底层，在且要在xml属性文件里设成visible就可以了*/
public class SVDraw extends SurfaceView implements SurfaceHolder.Callback{
    protected SurfaceHolder sh;
    private int mWidth;
    private int mHeight;
    private MyThread thread;

    public SVDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        sh = this.getHolder();
        sh.addCallback(this);
        sh.setFormat(PixelFormat.TRANSPARENT);
        setZOrderOnTop(true);
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int w, int h) {
        mWidth = w;
        mHeight = h;
    }

    public void surfaceCreated(SurfaceHolder arg0) {

    }

    public void surfaceDestroyed(SurfaceHolder arg0) {

    }

    void clearDraw() {
        Canvas canvas = sh.lockCanvas();
        canvas.drawColor(Color.BLUE);
        sh.unlockCanvasAndPost(canvas);
    }
    public void drawLine() {
        //预览视频的时候绘制图像
        /*Canvas canvas = sh.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.RED);
        p.setStyle(Style.STROKE);
        //canvas.drawPoint(100.0f, 100.0f, p);
        canvas.drawLine(0,110, 500, 110, p);
        canvas.drawCircle(110, 110, 10.0f, p);
        sh.unlockCanvasAndPost(canvas);*/

        thread = new MyThread(sh);
        thread.setRun(true);
        thread.start();
    }

    // 绘制线程
    public class MyThread extends Thread {
        private SurfaceHolder holder;
        private boolean run;

        public MyThread(SurfaceHolder holder) {
            this.holder = holder;
            run = true;
        }

        @Override
        public void run() {
            int counter = 0;
            Canvas canvas = null;
            while (run) {
                // 具体绘制工作
                try {
                    // 获取Canvas对象，并锁定之
                    canvas = holder.lockCanvas();
                    // 设定Canvas对象的背景颜色
                    canvas.drawColor(Color.TRANSPARENT);

                    // 创建画笔
                    Paint p = new Paint();
                    // 设置画笔颜色
                    p.setColor(Color.RED);
                    // 设置文字大小
                    p.setTextSize(30);

                    // 创建一个Rect对象rect
                    Rect rect = new Rect(100, 50, 380, 330);
                    // 在canvas上绘制rect
                    canvas.drawRect(rect, p);
                    // 在canvas上显示时间
                    canvas.drawText("Interval = " + (counter++) + " seconds.", 100, 410, p);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        // 解除锁定，并提交修改内容
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        public boolean isRun() {
            return run;
        }

        public void setRun(boolean run) {
            this.run = run;
        }
    }

}

package zhenyuyang.cs190i.cs.ucsb.edu.zhenyu_drawingmultitouch;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.Random;

import static android.os.SystemClock.elapsedRealtime;

public class MainActivity extends AppCompatActivity {
    Bitmap output_final;
    ImageView imageView;
    Canvas canvas = null;
    int canvas_w = 0;
    int canvas_h = 0;
    int strokeWidth = 20;
    boolean ifNewTouch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        float[][] data = new float[10000][2];
        int counter = 0;


        //register button
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("1324", "onClick");
                output_final = Bitmap.createBitmap(canvas_w, canvas_h, Bitmap.Config.ARGB_8888);  //init for one time
                canvas = new Canvas(output_final);  //init for one time
                imageView.setImageBitmap(output_final);
            }
        });

        //register seekbar
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(10);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                strokeWidth = progress + 2;
                //Log.i("1324", "onProgressChanged = " + strokeWidth);
                //seekBarValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });


        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnTouchListener(new ImageView.OnTouchListener() {

            float x_prev = 0;
            float y_prev = 0;
            float x_current = 0;
            float y_current = 0;

            float[] x_current_set = new float[10];
            float[] y_current_set = new float[10];

            float[] x_current_set_prev = new float[10];
            float[] y_current_set_prev = new float[10];

            float[] t_set = new float[10];
            float[] t_set_prev = new float[10];

            float t_prev = 0;
            float t = 0;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //   textView.setText("Touch coordinates : " +
                //         String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
//                Log.i("1324","Touch coordinates : " +
//                        String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
//                Log.i("1324","Touch coordinates2 : " +
//                        String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));

                int numOfFingers = event.getPointerCount();
                int mActivePointerId = 0;
                int pointerIndex = 0;


                //Log.i("1324","numOfFingers = "+numOfFingers);

                t = elapsedRealtime();

                //detect touch   //buggy, sometimes detects wrong downs and ups
//                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
//                    Log.i("1324","down");
//                    x_prev = x_current;
//                    y_prev = y_current;
//                    //Log.i("13245","x_prev = "+x_prev+", x_current = "+x_current);
//                }
//                else if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
//                    Log.i("1324","up");
//                }

//                //Log.i("1324", "t_diff = " + (t - t_prev));
//                if ((t - t_prev) > 100) {
////                    x_prev = x_current;
////                    y_prev = y_current;
//                    ifNewTouch = true;
//                }


                //loop through all fingers
                for (int i = 0; i < numOfFingers; i++) {

                    int k = 0;

                    mActivePointerId = event.getPointerId(i);
                    //Log.i("1324","i = "+i+", ID = "+mActivePointerId);
                    pointerIndex = event.findPointerIndex(mActivePointerId);
                    x_current = event.getX(pointerIndex);
                    y_current = event.getY(pointerIndex);

                    k = mActivePointerId;  //mActivePointerId or i ?

                    x_current_set[k] = x_current;
                    y_current_set[k] = y_current;
                    t_set[k] = elapsedRealtime();


//                    Log.i("1324", "ifNewTouch = " + ifNewTouch);
//                    if (ifNewTouch) {
//                        ifNewTouch = false;
//                        Log.i("1324", "ifNewTouch, i = " + i);
//                        for (int j = 0; j < numOfFingers; j++) {
//                            x_current_set_prev[j] = x_current_set[j];
//                            y_current_set_prev[j] = y_current_set[j];
//                        }
//                    }

                    if ((t_set[k] - t_set_prev[k]) > 100) {
                        x_current_set_prev[k] = x_current_set[k];
                        y_current_set_prev[k] = y_current_set[k];

                    }


                    //Log.i("1324","x_prev = "+x_prev+", x_current = "+x_current);
                    //drawPlot(canvas_w, canvas_h, x_prev, y_prev, x_current, y_current);
                    //drawPlot(canvas_w, canvas_h, x_current_set[i], y_current_set[i], x_current_set_prev[i], y_current_set_prev[i]);
                    drawPlot(canvas_w, canvas_h, x_current_set_prev[k], y_current_set_prev[k], x_current_set[k], y_current_set[k],k);
                    x_current_set_prev[k] = x_current_set[k];
                    y_current_set_prev[k] = y_current_set[k];
                    t_set_prev[k] = t_set[k];
                }


//                Log.i("1324","output_final = "+output_final);
//                Log.i("1324","imageView.getWidth() = "+imageView.getWidth());
//                Log.i("1324","imageView.getHeight() = "+imageView.getHeight());
                if (output_final != null) {
                    imageView.setImageBitmap(output_final);
                    //imageView.setBackgroundResource(R.drawable.test);
                }

                x_prev = event.getX();
                y_prev = event.getY();
                t_prev = t;

                //x_current_set_prev = x_current_set;
                //y_current_set_prev = y_current_set;

                return true;
            }
        });

        imageView.post(new Runnable() {
            @Override
            public void run() {
                canvas_w = imageView.getWidth();
                canvas_h = imageView.getHeight();
                output_final = Bitmap.createBitmap(canvas_w, canvas_h, Bitmap.Config.ARGB_8888);  //init for one time
                canvas = new Canvas(output_final);  //init for one time

            }
        });

//        // set processing data thread
//        Thread dataProcessingThread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    while (!isInterrupted()) {
//                        Thread.sleep(10);  //update every 5 ms
//
//                        output_final = drawPlot(imageView.getWidth(),imageView.getHeight());
//                    }
//                } catch (InterruptedException e) {
//                }
//            }
//        };
//        dataProcessingThread.start();


    }


    void drawPlot(int w, int h, float startX, float startY, float endX, float endY, int colorNumber) {

        if (w > 0 && h > 0) {

            //draw path method,  not smooth
            /*
            Paint paint = new Paint();
            Path path = new Path();
            path.moveTo(startX, startY);
            path.lineTo(endX, endY);
            path.close();
            paint.setStrokeWidth(strokeWidth);
            //paint.setStrokeWidth(2);
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, paint);
*/


            //draw by point, smooth
            Paint paint_c = new Paint();
            paint_c.setStrokeWidth(strokeWidth);

            switch (colorNumber){

                case 0:
                    paint_c.setColor(Color.argb(255,254,67,101));
                    break;
                case 1:
                    paint_c.setColor(Color.argb(255,252,157,154));
                    break;
                case 2:
                    paint_c.setColor(Color.argb(255,249,205,173));
                    break;
                case 3:
                    paint_c.setColor(Color.argb(255,200,200,169));
                    break;
                case 4:
                    paint_c.setColor(Color.argb(255,131,175,155));
                    break;
                case 5:
                    paint_c.setColor(Color.argb(255,130,57,53));
                    break;
                case 6:
                    paint_c.setColor(Color.argb(255,137,190,178));
                    break;
                case 7:
                    paint_c.setColor(Color.argb(255,201,186,131));
                    break;
                case 8:
                    paint_c.setColor(Color.argb(255,222,211,140));
                    break;
                default:
                    paint_c.setColor(Color.argb(255,222,156,83));
                    break;
            }


            paint_c.setStyle(Paint.Style.FILL);

            for (int i = 0; i < 80; i++) {
                float temp_x = (float) (startX + ((endX - startX) / 80.0) * i);
                float temp_y = (float) (startY + ((endY - startY) / 80.0) * i);
                canvas.drawCircle(temp_x, temp_y, strokeWidth / 5, paint_c);
            }


        }

        //return output;


    }


}

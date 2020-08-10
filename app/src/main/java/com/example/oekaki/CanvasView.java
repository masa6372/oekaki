package com.example.oekaki;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CanvasView extends View{
    private ArrayList<Line> lines = new ArrayList<Line>();
    private Line aLine;
    private int currentColor = Color.BLACK;
    public Context context;
    public int lineWidth = 10;
    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    public void undo() {
        if(lines.size() > 0) {
            lines.remove(lines.size() - 1);
        }
        invalidate();
    }
    public void clear() {
        lines.clear();
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        drawAll(canvas);
    }

    public void drawAll(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        for(Line line : lines) {
            paint.setColor(line.getColor());
            paint.setStrokeWidth(lineWidth);
            for(int i=0; i<(line.getPoints().size()-1); i++){
                Point s = line.getPoints().get(i);
                Point e = line.getPoints().get(i+1);
                canvas.drawLine(s.x, s.y, e.x, e.y, paint);
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                aLine = new Line(currentColor);
                lines.add(aLine);
                break;
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getY();
                Point p = new Point(x, y);
                aLine.addPoint(p);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();
        return true;
    }
    public void setColor(int c){
        currentColor = c;
    }

    public void saveFile() {
        Bitmap myBitmap;
        Canvas bitmapCanvas;
        myBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(myBitmap);
        drawAll(bitmapCanvas);

        String status = Environment.getExternalStorageState();
        File storageDir;
        if(status.equals(Environment.MEDIA_MOUNTED)) {
            String storageDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/oekaki";
            storageDir = new File(storageDirPath);
            boolean isDirectoryCreated = storageDir.exists();
            if (!isDirectoryCreated) {
                isDirectoryCreated = storageDir.mkdir();
            }
        } else {
            Toast.makeText(context, "Could not access the storage.", Toast.LENGTH_LONG).show();
            return;
        }
        long msec = System.currentTimeMillis();
        String fname = DateFormat.format("yyyy-MM-dd_kk.mm.ss", msec).toString();
        fname = storageDir.getAbsolutePath() + "/" + fname + ".png";

        try {
            FileOutputStream outstream = new FileOutputStream(fname);
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, outstream);
            outstream.flush();
            outstream.close();
        } catch(IOException e) {
            Toast.makeText(context, "Could not write file.", Toast.LENGTH_LONG).show();
        }
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        contentValues.put(MediaStore.MediaColumns.DATA, fname);
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }
}

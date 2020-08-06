package com.example.oekaki;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;

public class CanvasView extends View{
    private ArrayList<Line> lines = new ArrayList<Line>();
    private Line aLine;
    private int currenColor = Color.BLACK;
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
                aLine = new Line(currenColor);
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
        currenColor = c;
    }
}

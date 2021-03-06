package com.example.oekaki;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    private  CanvasView canvasView;
    public class UndoBtnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v){
            canvasView.undo();
        }
    }
    public class ClearBtnOnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            canvasView.clear();
        }
    }
    public class SaveBtnOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){ /*canvasView.saveFile();*/ }
    }
    public class ColorBtnOnChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkId){
            RadioButton radioButton = (RadioButton) findViewById(checkId);
            String colorStr = radioButton.getText().toString();
            if(colorStr.equals("黒")) {
                canvasView.setColor(Color.BLACK);
            }else if(colorStr.equals("赤")){
                canvasView.setColor(Color.RED);
            }else if(colorStr.equals("緑")){
                canvasView.setColor(Color.GREEN);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        canvasView = (CanvasView) findViewById(R.id.Myview);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.colorRadio);
        radioGroup.setOnCheckedChangeListener(new ColorBtnOnChangeListener());
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new SaveBtnOnClickListener());
        Button clearButton = (Button)findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new ClearBtnOnclickListener());
        Button undoButton = (Button)findViewById(R.id.undoButton);
        undoButton.setOnClickListener(new UndoBtnOnClickListener());
    }
}
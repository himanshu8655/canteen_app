package com.example.nmims_canteen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class Confirm extends AppCompatActivity {
    String value=Canteen.mpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title   
        getSupportActionBar().hide();// hide the title bar  
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//enable full screen

        setContentView(R.layout.activity_confirm);

        PDFView pdfView= (PDFView)findViewById(R.id.pdfView);

     File file=new File(value);
        pdfView.fromFile(file).load();



    }
}

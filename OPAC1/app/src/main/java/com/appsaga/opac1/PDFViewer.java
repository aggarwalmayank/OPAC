package com.appsaga.opac1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class PDFViewer extends AppCompatActivity {
PDFView pdfview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        pdfview=(findViewById(R.id.pdf));
        pdfview.fromAsset("pdf.pdf").load();
    }
}

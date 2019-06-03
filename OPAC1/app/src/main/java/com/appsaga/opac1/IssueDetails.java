package com.appsaga.opac1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class IssueDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_details);

        ArrayList<IssuedBooks> issued_books = (ArrayList<IssuedBooks>)getIntent().getExtras().getSerializable("issued_books");

        ListView issue_list = findViewById(R.id.issue_list);
        IssuedAdapter issuedAdapter = new IssuedAdapter(IssueDetails.this,issued_books);
        issue_list.setAdapter(issuedAdapter);
    }
}

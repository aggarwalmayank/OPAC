package com.appsaga.opac1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class BookDetails extends AppCompatActivity {

    ArrayList<Copies> copies1;
    ListView book_detail;
    BookDetailsAdapter bookDetailsAdapter;
    Button reserve;
    ArrayList<Copies> to_reserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        copies1 = (ArrayList<Copies>) getIntent().getExtras().getSerializable("copies");
        final String id_name = getIntent().getStringExtra("id name");
        book_detail = findViewById(R.id.book_detail_list);
        bookDetailsAdapter = new BookDetailsAdapter(this,copies1);
        book_detail.setAdapter(bookDetailsAdapter);

        reserve = findViewById(R.id.reserve);
        to_reserve = new ArrayList<>();

        for(Copies cp : copies1)
        {
            if(cp.getStatus().equalsIgnoreCase("Issue") && cp.getReserved().equalsIgnoreCase("-") && !cp.getIssued_by().equalsIgnoreCase("-"))
            {
                to_reserve.add(cp);
            }
        }
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BookDetails.this,Reserve.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("toReserve",to_reserve);
                intent.putExtras(bundle);
                intent.putExtra("id name",id_name);
                startActivity(intent);
                finish();
            }
        });
    }
}

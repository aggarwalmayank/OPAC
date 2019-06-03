package com.appsaga.opac1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Books extends AppCompatActivity {

    BookAdapter bookAdapter;
    ArrayList<BookInformation> books;
    ListView book_list;
    HashMap<String, Copies> copies = new HashMap<>();
    //ArrayList<Copies> copy = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        books = (ArrayList<BookInformation>) getIntent().getExtras().getSerializable("books");
        final String id_name = getIntent().getStringExtra("id name");
        bookAdapter = new BookAdapter(this, books);
        book_list = findViewById(R.id.book_list);
        book_list.setAdapter(bookAdapter);

        book_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView name =  view.findViewById(R.id.book_name);
                String value = name.getText().toString();

                ArrayList<Copies> copies1 = new ArrayList<>();
                for (BookInformation bi : books) {
                    if (value.equals(bi.getName())) {

                        copies = bi.getCopies();
                        //copy = bi.getCopy();
                        for(Map.Entry<String,Copies> entry: copies.entrySet())
                        {
                            copies1.add(entry.getValue());
                        }
                        /*Copies copy = copies.get("copy1");
                        Log.d("status_is", copy.getAccession().toString());
                       // Log.d("status_is", copy.get(0).toString());
                        Toast.makeText(Books.this, copy.getAccession(), Toast.LENGTH_SHORT).show();*/
                    }
                }
                Intent intent = new Intent(Books.this,BookDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("copies",copies1);
                intent.putExtras(bundle);
                intent.putExtra("id name",id_name);
                startActivity(intent);
                finish();
            }
        });
    }
}

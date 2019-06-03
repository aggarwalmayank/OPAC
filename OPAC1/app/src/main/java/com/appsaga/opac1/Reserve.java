package com.appsaga.opac1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class Reserve extends AppCompatActivity {


    ArrayList<Copies> to_reserve;
    ListView to_reserve_list;
    ReserveAdapter reserveAdapter;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String bookname;
    String authorname;
    String pub;
    DatabaseReference booksRef;

    String name,rollno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        to_reserve = (ArrayList<Copies>) getIntent().getExtras().getSerializable("toReserve");
        to_reserve_list = findViewById(R.id.to_reserve_list);
        reserveAdapter = new ReserveAdapter(this,to_reserve);
        to_reserve_list.setAdapter(reserveAdapter);
        final String id_name = getIntent().getStringExtra("id name");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        /*SharedPreferences prefs = getSharedPreferences("PREF", MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            String rollno= prefs.getString("rollno", "No name defined");//"No name defined" is the default value.
            String Name = prefs.getString("name", "No name defined"); //0 is the default value.
            Toast.makeText(this, Name, Toast.LENGTH_SHORT).show();
            Log.d("RESERVE",Name);
        }*/
        final Button reserve = findViewById(R.id.complete_reserve);

        booksRef = databaseReference.child("Books");
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<to_reserve_list.getChildCount();i++)
                {
                    View view = to_reserve_list.getChildAt(i);
                    CheckBox checkBox = view.findViewById(R.id.check);
                    if(checkBox.isChecked())
                    {
                        TextView textView = view.findViewById(R.id.accession);
                        final String name = textView.getText().toString();
                        databaseReference.child("Books").child(to_reserve.get(i).getParent_key()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                BookInformation bookInformation = dataSnapshot.getValue(BookInformation.class);
                                bookname=bookInformation.getName();
                                authorname=bookInformation.getAuthor();
                                pub=bookInformation.getPublisher();
                                Toast.makeText(Reserve.this,bookInformation.getName(),Toast.LENGTH_SHORT).show();
                                Log.d("booknameeeeee",bookInformation.getName());
                                SendMail sm = new SendMail(Reserve.this, id_name+"@lnmiit.ac.in", "BOOK RESERVED", "Book Name: "+bookname+"\nAuthor Name: "+authorname+"\nPublisher: "+pub+"\nAccession Number: "+name);
                                sm.execute();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        //Toast.makeText(Reserve.this,"Reserving "+bookname,Toast.LENGTH_SHORT).show();
                        Log.d("Keyyyyyy",to_reserve.get(i).getKey()+"  "+to_reserve.get(i).getParent_key());
                        databaseReference.child("Books").child(to_reserve.get(i).getParent_key()).child("copies").child(to_reserve.get(i).getKey()).child("reserved").setValue(id_name);


                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        },500);
                    }
                }
            }
        });
    }
}

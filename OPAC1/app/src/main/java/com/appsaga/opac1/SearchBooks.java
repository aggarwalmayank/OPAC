package com.appsaga.opac1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.victor.loading.book.BookLoading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchBooks extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Handler handler = new Handler();
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    BookLoading bookload;

    Boolean search_pressed = false;
    Boolean issued_pressed = false;

    private static final String TAG = "ViewDatabase";
    EditText findBooks;
    ImageView search;

    TextView userEmail, userName, bookCD;
    ImageView userPic;
    ArrayList<BookInformation> books;
    String id_name;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_books);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        final DatabaseReference booksRef = databaseReference.child("Books");
        dl = findViewById(R.id.drawer);
        t = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);
        dl.addDrawerListener(t);
        animation = AnimationUtils.loadAnimation(this, R.anim.blink);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bookCD = findViewById(R.id.textView2);
        bookCD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookCD.startAnimation(animation);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SearchBooks.this, PDFViewer.class);
                        startActivity(intent);
                        //finish();
                    }
                }, 500);
            }
        });

        bookload = (BookLoading) findViewById(R.id.bookloading);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        nv = findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.issuedetails) {

                    issued_pressed = true;

                    final ArrayList<IssuedBooks> issued_books = new ArrayList<>();

                    booksRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (issued_pressed == Boolean.TRUE) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                    BookInformation bookInformation = ds.getValue(BookInformation.class);

                                    String name = bookInformation.getName();
                                    String publisher = bookInformation.getPublisher();
                                    String author = bookInformation.getAuthor();

                                    HashMap<String, Copies> hashMap = bookInformation.getCopies();

                                    for (Map.Entry<String, Copies> entry : hashMap.entrySet()) {
                                        if (id_name.equalsIgnoreCase(entry.getValue().getIssued_by())) {
                                            issued_books.add(new IssuedBooks(name, entry.getValue().getAccession(), author, publisher));
                                        }
                                    }
                                }
                                Intent intent = new Intent(SearchBooks.this, IssueDetails.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("issued_books", issued_books);
                                intent.putExtras(bundle);
                                issued_pressed = false;
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //finish();
                } else if (id == R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    if (status.isSuccess()) {
                                        gotoMainActivity();
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Session not close", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

                return true;
            }

        });

        View headerView = nv.getHeaderView(0);
        userEmail = headerView.findViewById(R.id.user_id);
        userName = headerView.findViewById(R.id.user_name);
        userPic = headerView.findViewById(R.id.user_pic);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final ImageView filter = findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                filter.startAnimation(animation);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        spinner.performClick();
                        //finish();
                    }
                }, 500);
            }
        });

        findBooks =

                findViewById(R.id.find_books);

        search =

                findViewById(R.id.search);

        //booksRef.push().child("001").setValue("Iridov");

        search.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                search_pressed = true;

                search.startAnimation(animation);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final String search = findBooks.getText().toString().trim();
                        final String spinner_value = spinner.getSelectedItem().toString();
                        if (findBooks.getText().toString().trim().equalsIgnoreCase("")) {
                            findBooks.setError("Please Enter");
                        } else {
                            bookload.setVisibility(View.VISIBLE);
                            bookload.start();

                            booksRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (search_pressed == Boolean.TRUE) {
                                        books = new ArrayList<>();
                                        books.clear();
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                            BookInformation bookInformation = ds.getValue(BookInformation.class);

                                            if (spinner_value.equalsIgnoreCase("Title")) {

                                                if (search.equalsIgnoreCase(bookInformation.getName()) || bookInformation.getName().toUpperCase().contains(search.toUpperCase())) {

                                                    books.add(bookInformation);
                                                }
                                            } else if (spinner_value.equalsIgnoreCase("Author Name")) {

                                                if (search.equalsIgnoreCase(bookInformation.getAuthor()) || bookInformation.getAuthor().toUpperCase().contains(search.toUpperCase())) {

                                                    books.add(bookInformation);
                                                }
                                            } else if (spinner_value.equalsIgnoreCase("Publisher Name")) {

                                                if (search.equalsIgnoreCase(bookInformation.getPublisher()) || bookInformation.getPublisher().toUpperCase().contains(search.toUpperCase())) {

                                                    books.add(bookInformation);
                                                }
                                            }
                                        }
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                bookload.stop();
                                                bookload.setVisibility(View.INVISIBLE);
                                                Intent intent = new Intent(SearchBooks.this, Books.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("books", books);
                                                intent.putExtras(bundle);
                                                intent.putExtra("id name", id_name);
                                                search_pressed = false;
                                                startActivity(intent);
                                            }
                                        }, 2500);

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                }, 500);


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            id_name = account.getEmail().substring(0, 8);
            Log.d("Nameeeee", account.getEmail().substring(0, 8));
            userName.setText(account.getDisplayName());
            userEmail.setText(account.getEmail());
            /*SharedPreferences.Editor editor = getSharedPreferences("PREF", MODE_PRIVATE).edit();
            editor.putString("rollno", account.getEmail().substring(0,8));
            editor.putString("name",account.getDisplayName());
            editor.apply();*/
            try {
                Glide.with(this).load(account.getPhotoUrl()).into(userPic);
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "image not found", Toast.LENGTH_LONG).show();
            }
        } else {
            finish();
        }
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
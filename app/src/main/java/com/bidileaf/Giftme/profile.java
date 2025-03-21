package com.bidileaf.Giftme;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {

    private static final String WHATSAPP_NUMBER = "+918692008067";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView userEmailOrPhone,username,useraddress;

    CardView addaddress;
    private FirebaseUser user;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /// /


        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();
            retrieveUserInfo(uid);
        } else {
            userEmailOrPhone.setText("User not logged in");
        }





        // Initialize UI elements
        LinearLayout llAboutUs = findViewById(R.id.llAboutUs);
        LinearLayout llGetHelp = findViewById(R.id.llGetHelp);
        userEmailOrPhone = findViewById(R.id.userEmailOrPhone);
        username = findViewById(R.id.UserName);
        addaddress = findViewById(R.id.add_address);
        useraddress = findViewById(R.id.UserAddress);


        // About Us Dialog
        llAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutUsDialog();
            }
        });

        // Get Help - Open WhatsApp
        llGetHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Address");
        }

        addaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddAddressDialog();
            }
        });


        if (currentUser != null) {
            // Retrieve the user's display name from Firebase Authentication
            String name = currentUser.getDisplayName();
            if (name != null && !name.isEmpty()) {
                username.setText(name);
            } else {
                username.setText("Name");
            }
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(profile.this, HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_orders:
                        startActivity(new Intent(profile.this, Yourorder.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_profile:
                        return true;
                }
                return false;
            }
        });

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

    }

    private void openAddAddressDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_add_address, null);

        EditText etHouseNo = view.findViewById(R.id.etHouseNo);
        EditText etArea = view.findViewById(R.id.etArea);
        EditText etPincode = view.findViewById(R.id.etPincode);
        EditText etState = view.findViewById(R.id.etState);
        EditText etContact = view.findViewById(R.id.etContact);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String houseNo = etHouseNo.getText().toString().trim();
                String area = etArea.getText().toString().trim();
                String pincode = etPincode.getText().toString().trim();
                String state = etState.getText().toString().trim();
                String contact = etContact.getText().toString().trim();

                String fullAddress = houseNo + ", " + area + ", " + pincode + ", " + state + ", " + contact;

                useraddress.setText("Address: " + fullAddress);


                if (user != null) {
                    databaseReference.setValue(fullAddress);
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // Open WhatsApp Chat
    private void openWhatsApp() {
        try {
            String url = "https://api.whatsapp.com/send?phone=" + WHATSAPP_NUMBER;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void retrieveUserInfo(String uid) {
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChild("email")) {
                        String email = snapshot.child("email").getValue(String.class);
                        userEmailOrPhone.setText(email);
                    } else if (snapshot.hasChild("phone")) {
                        String phone = snapshot.child("phone").getValue(String.class);
                        userEmailOrPhone.setText("Phone: " + phone);
                    } else {
                        userEmailOrPhone.setText("No email or phone found");
                    }
                } else {
                    userEmailOrPhone.setText("User data not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error fetching user data", error.toException());
                userEmailOrPhone.setText("Error fetching data");
            }
        });
    }

    // Show About Us Dialog
    private void showAboutUsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About Gift Me")
                .setMessage("App Version: 1.0\nParent Company: Bidileaf")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    startActivity(new Intent(profile.this, MainActivity.class));
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
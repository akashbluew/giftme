package com.bidileaf.Giftme;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Yourorder extends AppCompatActivity {

    private TextView orderDetails, orderDate;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_yourorder);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ///


        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("orders");

        // Initialize UI Components
        orderDetails = findViewById(R.id.orderDetails);
        orderDate = findViewById(R.id.orderDate);

        // Get Order Details
        getOrderDetails();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.nav_orders);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(Yourorder.this, HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_orders:
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(Yourorder.this, profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    private void getOrderDetails() {
        String userId = auth.getCurrentUser().getUid(); // Get logged-in user's UID

        databaseReference.child(userId).child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        // Retrieve order details
                        String walletName = orderSnapshot.child("Wallet Name").getValue(String.class);
                        String keychainName = orderSnapshot.child("Keychain Name").getValue(String.class);
                        String orderType = orderSnapshot.child("Order Type").getValue(String.class);
                        String paymentMethod = orderSnapshot.child("Payment Method").getValue(String.class);
                        String address = orderSnapshot.child("Address").getValue(String.class);

                        // Format the order details
                        String orderText = "Wallet Name: " + walletName + "\n" +
                                "Keychain Name: " + keychainName + "\n" +
                                "Order Type: " + orderType + "\n" +
                                "Payment Method: " + paymentMethod + "\n" +
                                "Address: " + address;

                        // Set the text in TextView
                        orderDetails.setText(orderText);
                        break; // Display the first order only
                    }
                } else {
                    orderDetails.setText("No Order Found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                orderDetails.setText("Failed to load order details.");
            }
        });
    }


}
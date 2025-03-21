package com.bidileaf.Giftme;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private ImageView walletCharm;
    private TextView nameOnWallet,address;
    private EditText nameInput;
    private ImageView charm1, charm2, charm3;

    private ImageView keychainCharm, keychainCharm1, keychainCharm2, keychainCharm3;
    private EditText keychainNameInput;
    private TextView keychainNameText;

    private RadioGroup radioGroupOptions,paymentOptionsGroup;
    private Button btnPlaceOrder;
    private String selectedOption = "Only Wallet (Price 299 Rs)";
    private String selectedPaymentMethod = "Cash on Delivery (COD)";

    CardView add_adress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /// /



        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);

        // Set "Home" as selected
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Handle navigation item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        // Already in HomeActivity, do nothing
                        return true;
                    case R.id.nav_orders:
                        startActivity(new Intent(HomeActivity.this, Yourorder.class));
                        overridePendingTransition(0, 0); // No animation
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(HomeActivity.this, profile.class));
                        overridePendingTransition(0, 0); // No animation
                        return true;
                }
                return false;
            }
        });


        // Initializing Views
        walletCharm = findViewById(R.id.charm1); // Wallet charm
        nameOnWallet = findViewById(R.id.nametext); // Name on wallet
        nameInput = findViewById(R.id.nameInput); // EditText where user types name
        charm1 = findViewById(R.id.charm); // First charm option
        charm2 = findViewById(R.id.charm2); // Second charm option
        charm3 = findViewById(R.id.charm3); // Third charm option

        // Initialize Keychain Views
        keychainCharm = findViewById(R.id.keychaincharm);
        keychainCharm1 = findViewById(R.id.keychaincharm1);
        keychainCharm2 = findViewById(R.id.keychaincharm2);
        keychainCharm3 = findViewById(R.id.keychaincharm3);
        keychainNameInput = findViewById(R.id.keychainnameInput);
        keychainNameText = findViewById(R.id.keychainnametext);
        radioGroupOptions = findViewById(R.id.radioGroupOptions);
        paymentOptionsGroup = findViewById(R.id.radioGroupPayment);
        add_adress           =findViewById(R.id.addAddress);
        address =   findViewById(R.id.address);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);



        // Change wallet charm when user clicks a charm
        charm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletCharm.setImageResource(R.drawable.kingkey);
            }
        });

        charm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletCharm.setImageResource(R.drawable.symbol2);
            }
        });

        charm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletCharm.setImageResource(R.drawable.kingcharm);
            }
        });


        keychainCharm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateKeychainCharm(R.drawable.kingkey);
            }
        });

        keychainCharm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateKeychainCharm(R.drawable.symbol2);
            }
        });

        keychainCharm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateKeychainCharm(R.drawable.kingcharm);
            }
        });


        // Update name on wallet as user types
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameOnWallet.setText(s.toString()); // Set text on wallet dynamically
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        // Name Input Logic
        keychainNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                keychainNameText.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        radioGroupOptions.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = findViewById(checkedId);
            selectedOption = selectedButton.getText().toString();
        });

        // Payment Option Selection
        paymentOptionsGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = findViewById(checkedId);
            selectedPaymentMethod = selectedButton.getText().toString();
        });


        add_adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
                View view = inflater.inflate(R.layout.dialog_add_address, null);

                // Get EditTexts from dialog layout
                EditText etHouseNo = view.findViewById(R.id.etHouseNo);
                EditText etArea = view.findViewById(R.id.etArea);
                EditText etPincode = view.findViewById(R.id.etPincode);
                EditText etState = view.findViewById(R.id.etState);
                EditText etContact = view.findViewById(R.id.etContact);

                // Build the dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setView(view);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get input values
                        String houseNo = etHouseNo.getText().toString().trim();
                        String area = etArea.getText().toString().trim();
                        String pincode = etPincode.getText().toString().trim();
                        String state = etState.getText().toString().trim();
                        String contact = etContact.getText().toString().trim();

                        // Create the full address
                        String fullAddress = houseNo + ", " + area + ", " + pincode + ", " + state + ", " + contact;

                        // Set the address to TextView and update visibility
                        address.setText("Address : " + fullAddress);
                        address.setVisibility(View.VISIBLE);
                        add_adress.setVisibility(View.GONE);
                    }
                });

                builder.setNegativeButton("Cancel", null);

                // Show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                placeOrderOnWhatsApp();


            }
        });


    }

    private void updateKeychainCharm(int charmDrawable) {
        keychainCharm.setImageResource(charmDrawable);
    }

    private void placeOrderOnWhatsApp() {
        String name = nameInput.getText().toString().trim();
        String keychainName = keychainNameInput.getText().toString().trim();
        String userAddress = address.getText().toString().trim(); // Get address text

        if (name.isEmpty()) name = "Not provided";
        if (keychainName.isEmpty()) keychainName = "Not provided";
        if (userAddress.isEmpty()) userAddress = "Not provided";

        String message = "Order Details:\n" +
                "Wallet Name: " + name + "\n" +
               /* "Selected Charm: " + selectedCharmName + "\n" +*/
                "Keychain Name: " + keychainName + "\n" +
              /*  "Selected Keychain Charm: " + selectedKeychainCharmName + "\n" +*/
                "Order Type: " + selectedOption + "\n"  +

        "Payment Method: " + selectedPaymentMethod+ "\n" +

        "Address: " + userAddress; // Add address to message

        saveOrderDetails(name, keychainName, selectedOption, selectedPaymentMethod, userAddress);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://wa.me/8692008067?text=" + Uri.encode(message)));
        startActivity(intent);

    }


    public void saveOrderDetails(String name, String keychainName, String selectedOption, String selectedPaymentMethod, String userAddress) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String uid = user.getUid(); // Get user UID
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Orders");

            // Create an order object
            Map<String, String> orderData = new HashMap<>();
            orderData.put("Wallet Name", name);
            orderData.put("Keychain Name", keychainName);
            orderData.put("Order Type", selectedOption);
            orderData.put("Payment Method", selectedPaymentMethod);
            orderData.put("Address", userAddress);

            // Push order details to Firebase
            databaseRef.push().setValue(orderData)
                    .addOnSuccessListener(aVoid -> Log.d("DEBUG", "Order details saved successfully"))
                    .addOnFailureListener(e -> Log.e("DEBUG", "Failed to save order details", e));
        } else {
            Log.e("DEBUG", "User is not authenticated");
        }
    }

}
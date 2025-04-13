package com.bidileaf.Giftme;

import static android.content.ContentValues.TAG;

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
    private ImageView charm1, charm2, charm3,charm4;

    private ImageView keychainCharm, keychainCharm1, keychainCharm2, keychainCharm3,keychainCharm4;
    private EditText keychainNameInput;
    private TextView keychainNameText;

    private RadioGroup radioGroupOptions,paymentOptionsGroup;
    private Button btnPlaceOrder;
    private String selectedOption = "";
    private String selectedPaymentMethod = "Cash on Delivery (COD)";

    CardView add_adress;

    private String selectedWalletCharm = "Charm 1 - king charmcharmc harm";
    private String selectedKeychainCharm = "Charm 4 - bike charm";



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
        charm4 = findViewById(R.id.charm4); // Fourth charm option

        // Initialize Keychain Views
        keychainCharm = findViewById(R.id.keychaincharm);
        keychainCharm1 = findViewById(R.id.keychaincharm1);
        keychainCharm2 = findViewById(R.id.keychaincharm2);
        keychainCharm3 = findViewById(R.id.keychaincharm3);
        keychainCharm4 = findViewById(R.id.keychaincharm4);
        keychainNameInput = findViewById(R.id.keychainnameInput);
        keychainNameText = findViewById(R.id.keychainnametext);
        radioGroupOptions = findViewById(R.id.radioGroupOptions);
        paymentOptionsGroup = findViewById(R.id.radioGroupPayment);
        add_adress           =findViewById(R.id.addAddress);
        address =   findViewById(R.id.address);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        /*int selectedId = radioGroupOptions.getCheckedRadioButtonId();


        // Determine selected option
        if (selectedId == R.id.radio_wallet) {
            selectedOption = "Only Wallet";
            Log.e(TAG, "wallet selected id."  + selectedId);

        } else if (selectedId == R.id.radio_wallet_keychain) {
            selectedOption = "Wallet and Keychain";
            Log.e(TAG, "wallet selected. id "  + selectedId);

        }*/



        // Change wallet charm when user clicks a charm
        charm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletCharm.setImageResource(R.drawable.kingcharm);
                selectedWalletCharm = "Charm 1 - King Charm";
            }
        });

        charm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletCharm.setImageResource(R.drawable.mrcharm);
                selectedWalletCharm = "Charm 2 - Mr. Charm";
            }
        });

        charm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletCharm.setImageResource(R.drawable.infinitylovecharm);
                selectedWalletCharm = "Charm 3 - Infinity Love";
            }
        });

        charm4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                walletCharm.setImageResource(R.drawable.bike);
                selectedWalletCharm = "Charm 4 - Bike";
            }
        });


        keychainCharm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateKeychainCharm(R.drawable.kingcharm);
                selectedKeychainCharm = "Charm 1 - King Charm";
            }
        });

        keychainCharm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateKeychainCharm(R.drawable.mrcharm);
                selectedKeychainCharm = "Charm 2 - Mr. Charm";
            }
        });

        keychainCharm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateKeychainCharm(R.drawable.keychainlovecharm);
                selectedKeychainCharm = "Charm 3 - Infinity Love";
            }
        });

        keychainCharm4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateKeychainCharm(R.drawable.bike);
                selectedKeychainCharm = "Charm 4 - Bike";
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

            Log.e(TAG, selectedOption);
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
                EditText etReceiverName = view.findViewById(R.id.etReceiverName);
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
                        String receiverName = etReceiverName.getText().toString().trim();
                        String houseNo = etHouseNo.getText().toString().trim();
                        String area = etArea.getText().toString().trim();
                        String pincode = etPincode.getText().toString().trim();
                        String state = etState.getText().toString().trim();
                        String contact = etContact.getText().toString().trim();

                        // Create the full address
                        String fullAddress =  houseNo + ", " + area + ", " + pincode + ", " + state + ", " + contact + "\n" + "Name: " + receiverName ;

                        // Set the adc7dcress to TextView and update visibility
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

                String name = nameInput.getText().toString().trim();
                String keychainName = keychainNameInput.getText().toString().trim();
                String userAddress = address.getText().toString().trim(); // Get address text

                // Check if address is empty
                if (userAddress.isEmpty()) {
                    Toast.makeText(HomeActivity.this, "Add address to place order", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }


                // Place order based on selection
                if (selectedOption.equals("Only Wallet (Price 299 Rs)")) {
                    Log.e(TAG, "wallet selected.");
                    placeWalletOrder(name, userAddress);
                } else if (selectedOption.equals("Wallet and Keychain (Price 399 Rs)")) {
                    Log.e(TAG, "wallet keychain selected");
                    placeWalletAndKeychainOrder(name, keychainName, userAddress);
                }


               /* placeOrderOnWhatsApp();*/


            }
        });


    }

    private void placeWalletAndKeychainOrder(String name, String keychainName, String userAddress) {
        if (name.isEmpty()) name = "Not provided";
        if (keychainName.isEmpty()) keychainName = "No keychain needed";
        if (userAddress.isEmpty()) userAddress = "Not provided";

        String message = "Order Details:\n" +
                "Wallet Name: " + name + "\n" +
                "Selected Wallet Charm: " + selectedWalletCharm + "\n" +
                "Keychain Name: " + keychainName + "\n" +
                "Selected Keychain Charm: " + selectedKeychainCharm + "\n" +
                "Order Type: Wallet + Keychain\n" +
                "Payment Method: " + selectedPaymentMethod + "\n" +
                "Address: " + userAddress;
        Log.e(TAG, "placewllaet and keychain works.");
        // Save order details before sending
        saveOrderDetails(name, keychainName, selectedOption, selectedPaymentMethod, userAddress);

        sendOrderToWhatsApp(message);

    }



    private void placeWalletOrder(String name, String userAddress) {
        if (name.isEmpty()) name = "Not provided";
        if (userAddress.isEmpty()) userAddress = "Not provided";

        String message = "Order Details:\n" +
                "Wallet Name: " + name + "\n" +
                "Selected Wallet Charm: " + selectedWalletCharm + "\n" +
                "Order Type: Wallet\n" +
                "Payment Method: " + selectedPaymentMethod + "\n" +
                "Address: " + userAddress;

        Log.e(TAG, "placewllaet works.");

        saveOrderDetails(name, "No keychain needed", selectedOption, selectedPaymentMethod, userAddress);
        sendOrderToWhatsApp(message);
    }

    private void sendOrderToWhatsApp(String message) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://wa.me/8692008067?text=" + Uri.encode(message)));
        startActivity(intent);
    }
    private void updateKeychainCharm(int charmDrawable) {
        keychainCharm.setImageResource(charmDrawable);
    }

 /*   private void placeOrderOnWhatsApp() {
        String name = nameInput.getText().toString().trim();
        String keychainName = keychainNameInput.getText().toString().trim();
        String userAddress = address.getText().toString().trim(); // Get address text

        if (name.isEmpty()) name = "Not provided";
        if (keychainName.isEmpty()) keychainName = "No provided";
        if (userAddress.isEmpty()) userAddress = "Not provided";

        String message = "Order Details:\n" +
                "Wallet Name: " + name + "\n" +
                "Selected Wallet Charm: " + selectedWalletCharm + "\n" +
                "Keychain Name: " + keychainName + "\n" +
               "Selected Keychain Charm: " + selectedKeychainCharm + "\n" +
                "Order Type: " + selectedOption + "\n"  +

        "Payment Method: " + selectedPaymentMethod+ "\n" +

        "Address: " + userAddress; // Add address to message

        saveOrderDetails(name, keychainName, selectedOption, selectedPaymentMethod, userAddress);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://wa.me/8692008067?text=" + Uri.encode(message)));
        startActivity(intent);

    }*/


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
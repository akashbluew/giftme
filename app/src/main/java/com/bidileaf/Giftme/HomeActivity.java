package com.bidileaf.Giftme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    private ImageView walletCharm;
    private TextView nameOnWallet;
    private EditText nameInput;
    private ImageView charm1, charm2, charm3;

    private ImageView keychainCharm, keychainCharm1, keychainCharm2, keychainCharm3;
    private EditText keychainNameInput;
    private TextView keychainNameText;

    private RadioGroup radioGroupOptions;
    private Button btnPlaceOrder;
    private String selectedOption = "Only Wallet (Price 299 Rs)";

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

        if (name.isEmpty()) name = "Not provided";
        if (keychainName.isEmpty()) keychainName = "Not provided";

        String message = "Order Details:\n" +
                "Wallet Name: " + name + "\n" +
               /* "Selected Charm: " + selectedCharmName + "\n" +*/
                "Keychain Name: " + keychainName + "\n" +
              /*  "Selected Keychain Charm: " + selectedKeychainCharmName + "\n" +*/
                "Order Type: " + selectedOption;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://wa.me/8692008067?text=" + Uri.encode(message)));
        startActivity(intent);

    }
}
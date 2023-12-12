package com.example.zakatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    TextView txtTitle;
    EditText txtGoldWeight;
    EditText txtGoldValue;
    RadioButton btnKeep;
    RadioButton btnWear;
    Button btnCalculate;
    Button btnReset;
    Toolbar myToolbar;

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTitle = findViewById(R.id.txtTitle);
        txtGoldWeight = findViewById(R.id.txtGoldWeight);
        txtGoldValue = findViewById(R.id.txtGoldValue);
        btnKeep = findViewById(R.id.btnKeep);
        btnWear = findViewById(R.id.btnWear);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);

        myToolbar = findViewById(R.id.myToolbar);
        //myToolbar = findViewById(R.id.myToolbar);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.app_name);


        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAndDisplayZakat();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void calculateAndDisplayZakat() {
        try {
            double goldWeight = Double.parseDouble(txtGoldWeight.getText().toString());
            double goldValue = Double.parseDouble(txtGoldValue.getText().toString());

            // Check if neither of the radio buttons is checked
            if (!btnKeep.isChecked() && !btnWear.isChecked()) {
                Toast.makeText(this, "Please select type of gold", Toast.LENGTH_SHORT).show();
                return;
            }

            double X = btnKeep.isChecked() ? 85 : (btnWear.isChecked() ? 200 : 0);

            // Handling negative values or non-numeric inputs
            if (goldWeight < 0 || goldValue < 0) {
                throw new NumberFormatException();
            }

            double totalValueOfGold = goldWeight * goldValue;
            double zakatPayable = (goldWeight - X) * goldValue;

            // Checking if zakatPayable is less than 0
            if (zakatPayable < 0) {
                zakatPayable = 0;
            }

            double totalZakat = 0.025 * zakatPayable; // 2.5% as decimal

            // Setting totalZakat to 0 if zakatPayable was set to 0
            if (zakatPayable == 0) {
                totalZakat = 0;
            }

            displayZakatResult(totalValueOfGold, zakatPayable, totalZakat);
        } catch (NumberFormatException e) {
            // Handle non-numeric inputs or negative values
            // For example, display an error message to the user
            Toast.makeText(this, "Please enter valid positive numeric values.", Toast.LENGTH_SHORT).show();
        }
    }




    private void displayZakatResult(double totalValueOfGold, double zakatPayable, double totalZakat) {
        // Display the results in their respective TextViews
        TextView txtTotalValue = findViewById(R.id.txtTotalValue);
        txtTotalValue.setText("RM " + totalValueOfGold);

        TextView txtZakatPayable = findViewById(R.id.txtZakatPayable);
        txtZakatPayable.setText("RM " + zakatPayable);

        TextView txtZakatResult = findViewById(R.id.txtZakatResult);
        txtZakatResult.setText("RM " + totalZakat);
    }

    private void resetFields() {
        txtGoldWeight.setText("");
        txtGoldValue.setText("");
        btnKeep.setChecked(false);
        btnWear.setChecked(false);

        TextView txtTotalValue = findViewById(R.id.txtTotalValue);
        txtTotalValue.setText("RM 0.00");

        TextView txtZakatPayable = findViewById(R.id.txtZakatPayable);
        txtZakatPayable.setText("RM 0.00");

        TextView txtZakatResult = findViewById(R.id.txtZakatResult);
        txtZakatResult.setText("RM 0.00");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        if (item.getItemId() == R.id.item_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Calculate your zakat with this app!");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        } else if (item.getItemId() == R.id.item_aboutUs) {
            Intent aboutUsIntent = new Intent(MainActivity.this, aboutUs.class);
            startActivity(aboutUsIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}

/**
 * IMPORTANT: Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */

package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */

public class MainActivity extends AppCompatActivity {

    int quantity = 1;
    int basePrice = 5;
    int costOfWhippedCream = 0;
    int costOfChocolate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // recovering the instance state onside onCreate

        if (savedInstanceState != null) {
            quantity = savedInstanceState.getInt("amount");
            displayQuantity(quantity);  // push to screen on orientation change
        }
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("amount", quantity);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    /**
     * This method is called when the increment button is clicked.
     */
    public void increment(View view) {
        if (quantity > 99) {
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();
            return;
        } else {
            quantity = quantity + 1;
        }
        displayQuantity(quantity);
    }

    /**
     * This method is called when the decrement button is clicked.
     */
    public void decrement(View view) {
        if (quantity < 2) {
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            return;
        } else {
            quantity = quantity - 1;
        }
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        CheckBox whippedCreamCheckbox = (CheckBox) findViewById(R.id.cream);
        boolean hasWhippedCream = whippedCreamCheckbox.isChecked();

        CheckBox chocolateCheckbox = (CheckBox) findViewById(R.id.chocolate);
        boolean hasChocolate = chocolateCheckbox.isChecked();

        EditText getName = (EditText) findViewById(R.id.nameInput);
        String name = getName.getText().toString();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String dataToEmail = createOrderSummary(price, hasWhippedCream, hasChocolate, name);
        Email(dataToEmail, name);
    }

    /**
     * This method is called to populate an email
     */
    public void Email(String summary, String name) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for: " + name);
        intent.putExtra(Intent.EXTRA_TEXT, (summary));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order.
     * @param hasWhippedCream and hasChocolate passed into to override default values if checked
     * @return total price to submitOrder method
     */
    public int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        if (hasWhippedCream) {
            costOfWhippedCream = 1;
        } else {
            costOfWhippedCream = 0;
        }
        if (hasChocolate) {
            costOfChocolate = 2;
        } else {
            costOfChocolate = 0;
        }
        int toppings = costOfWhippedCream + costOfChocolate;
        return (basePrice + toppings) * quantity;
    }

    /**
     * Create summary of the order.
     *
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate is whether or not the user wants chocolate topping
     * @param price of the order
     * @param addName retrieves the entered name of the user
     * @return text summary
     */
    private String createOrderSummary(int price, boolean addWhippedCream, boolean addChocolate, String addName) {
        String newMessage = getString(R.string.order_summary_name, addName);
        newMessage += "\n" + getString(R.string.addWhippy, addWhippedCream);
        newMessage += "\n" + getString(R.string.addChoco, addChocolate);
        newMessage += "\n" + getString(R.string.order_summary_quantity, quantity);
        newMessage += "\n" + getString(R.string.total, NumberFormat.getCurrencyInstance().format(price));
        newMessage += "\n" + getString(R.string.thanks);
        return newMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.valueOf(numberOfCoffees));
    }
}
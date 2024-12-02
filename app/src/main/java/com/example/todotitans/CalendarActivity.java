package com.example.todotitans;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todotitans.databinding.ActivityCalendarBinding;

public class CalendarActivity extends AppCompatActivity {

    /** The binding object used to access the views defined in the layout file. */
    private ActivityCalendarBinding binding;

    /**
     * Called when the activity is starting. This is where most initialization
     * should occur, including setting up the layout and initializing the back button.
     *
     * @param savedInstanceState If the activity is being reinitialized after being
     *                           previously shut down, this Bundle contains the most recent
     *                           data supplied; otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize and set up the back button
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and return to the previous one
                finish();
            }
        });
    }
}

package space.collabify.collabify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import space.collabify.collabify.*;
import space.collabify.collabify.base.CollabifyActivity;
import space.collabify.collabify.models.Event;
import space.collabify.collabify.models.Role;

/**
 * This file was born on March 11 at 14:00
 */
public class CreateEventActivity extends CollabifyActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }

    public void toDj(View view) {
      mAppManager.getUser().setRole(Role.DJ);
      mAppManager.createEvent(new Event("123"));
      Intent intent = new Intent(this, DjActivity.class);
      startActivity(intent);
    }
}

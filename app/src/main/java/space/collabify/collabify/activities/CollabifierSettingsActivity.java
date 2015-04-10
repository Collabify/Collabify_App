package space.collabify.collabify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import space.collabify.collabify.R;
import space.collabify.collabify.base.BaseSettingsActivity;
import space.collabify.collabify.base.CollabifyActivity;


public class CollabifierSettingsActivity extends CollabifyActivity { //BaseSettingsActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collabifier_settings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                //do nothing, avoid multiple of same activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toAbout(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

}

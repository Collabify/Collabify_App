package space.collabify.android.activities;

import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.R;
import space.collabify.android.collabify.models.domain.Playlist;
import space.collabify.android.fragments.SearchDetailsFragment;
import space.collabify.android.managers.CollabifyCallback;
import space.collabify.android.models.Song;

import java.util.List;


public class DetailedSearchActivity extends PrimaryViewActivity {

    private static final String TAG = DetailedSearchActivity.class.getSimpleName();

    private SearchDetailsFragment mSearchDetailsFragment;

    private ProgressDialog progress;

    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_search);

        SHOW_SETTINGS = true;
        SHOW_LEAVE = true;
        SHOW_LOGOUT = true;

        if(savedInstanceState == null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            mSearchDetailsFragment = new SearchDetailsFragment();
            mSearchDetailsFragment.setmParentActivity(this);
            transaction.replace(R.id.song_details_list_frame, mSearchDetailsFragment, TAG);
            transaction.commit();
        }else {
            // TODO: get search details fragment reference from savedInstanceState?
        }

        Intent intent = getIntent();
        String query = intent.getStringExtra("query");

        this.query = query;

        handleQuery(query);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

    @Override
    public boolean handleQuery(String query) {

        progress = ProgressDialog.show(this, "Performing Search", "Searching tracks...", true);

        mAppManager.getSpotifyService().searchTracks(query, new afterSpotifySearch());

        return true;
    }

    public void setupAddDialog(final String songDescription, final Song song) {
        // prompt to add song
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_song_dialog_title));
        builder.setMessage(songDescription);
        builder.setPositiveButton(getString(R.string.add_song_dialog_positive_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: send song to server

                        dialog.cancel();

                        addSong(song);
                    }
                });
        builder.setNegativeButton(getString(R.string.add_song_dialog_negative_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void addSong(final Song song){
        // TODO: FIX ME!
        progress = ProgressDialog.show(this, "Adding Song", "Adding song to playlist...", true);
        mAppManager.addSong(song, new afterAddSong());
    }

    private class afterAddSong implements CollabifyCallback<Playlist>{

        @Override
        public void success(Playlist playlist, Response response) {
            progress.dismiss();
        }

        @Override
        public void failure(RetrofitError error) {
          progress.dismiss();

            int e = error.getResponse().getStatus();
            if (e == 403) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getBaseContext(), "Too late! Song already on playlist", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else if (e == 401){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getBaseContext(), "You are Blacklisted! You cannot add songs", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else{
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getBaseContext(), "Error adding song to playlist", Toast.LENGTH_LONG).show();
                    }
                });
            }

        }

        @Override
        public void exception(Exception e) {
          progress.dismiss();
          Toast.makeText(getBaseContext(), "Error adding song to playlist", Toast.LENGTH_LONG).show();
        }
    }

    private class afterSpotifySearch extends SpotifyCallback<TracksPager>{

        @Override
        public void failure(SpotifyError spotifyError) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.dismiss();
                    Toast.makeText(getBaseContext(), "Error searching for tracks", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void success(TracksPager tracksPager, Response response) {

            List<Track> tracks = tracksPager.tracks.items;

            final List<Song> songs = new ArrayList<>();

            if(tracks.isEmpty()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Toast.makeText(getBaseContext(), "No tracks found...", Toast.LENGTH_LONG).show();
                        return;
                    }
                });
            }

            for(Track track : tracks){

                String highUrl = "";
                String lowUrl = "";

                if(track.album.images.size() > 0) {
                    highUrl = track.album.images.get(0).url;
                    lowUrl = track.album.images.get(track.album.images.size() - 1).url;
                }

                  String artists = track.artists.get(0).name;
                  if (track.artists.size() > 1) {
                    for (int i = 1; i < track.artists.size() && i < 3; i++) {
                      artists += ", " + track.artists.get(i).name;
                    }
                  }

                Song song = new Song(track.name, artists, track.album.name, 9999, track.id, highUrl,
                  mAppManager.getUser().getId(), mAppManager.getUser().getName());
                song.setLowArtwork(lowUrl);

                songs.add(song);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSearchDetailsFragment.populateSongList(songs);
                }
            });


            progress.dismiss();
        }
    }
}
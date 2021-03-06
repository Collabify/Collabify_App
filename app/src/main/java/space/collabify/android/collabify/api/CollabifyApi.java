package space.collabify.android.collabify.api;

import com.squareup.okhttp.OkHttpClient;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

import space.collabify.android.collabify.models.domain.Event;
import space.collabify.android.collabify.models.domain.EventSettings;
import space.collabify.android.collabify.models.domain.Playlist;
import space.collabify.android.collabify.models.domain.Role;
import space.collabify.android.collabify.models.domain.Song;
import space.collabify.android.collabify.models.domain.User;
import space.collabify.android.collabify.models.domain.UserSettings;
import space.collabify.android.collabify.models.domain.Vote;
import space.collabify.android.collabify.models.network.EventDO;
import space.collabify.android.collabify.models.network.EventRequestDO;
import space.collabify.android.collabify.models.network.RoleDO;
import space.collabify.android.collabify.models.network.SongRequestDO;
import space.collabify.android.collabify.models.network.UserDO;
import space.collabify.android.collabify.models.network.UserRequestDO;

/**
 * Created by ricardolopez on 4/12/15.
 */
public class CollabifyApi {
    private static final int PORT = 1337;
    private static final String COLLABIFY_ENDPOINT = "http://collabify.space:" + PORT;

    private final CollabifyService mCollabifyService;
    private String mCurrentUserId;

    public CollabifyApi() {
        Executor executor = Executors.newSingleThreadExecutor();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(new OkHttpClient()))
                .setExecutors(executor, executor)
                .setEndpoint(COLLABIFY_ENDPOINT)
                .build();
        mCollabifyService = restAdapter.create(CollabifyService.class);
    }

    public void setCurrentUserId(String currentUserId) {
        mCurrentUserId = currentUserId;
    }

    public void clearCurrentUserId() {
        mCurrentUserId = null;
    }


    /*--------------------------------------------------------------------------------------------*
     * User -------------------------------------------------------------------------------- User *
     *--------------------------------------------------------------------------------------------*/


    /**
     * Get a user's info from the server
     *
     * @param userId The user's spotify id
     * @return
     */
    public User getUser(String userId) {
        return mCollabifyService.getUser(userId);
    }

    /**
     * Get a user's info from the server
     *
     * @param userId
     * @param callback
     */
    public void getUser(String userId, Callback<User> callback) {
        mCollabifyService.getUser(userId, callback);
    }

    /**
     * Add a user to the server
     *
     * @param user
     * @return
     * @throws CollabifyApiException
     */
    public User addUser(UserRequestDO user) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        return mCollabifyService.addUser(mCurrentUserId, user);
    }

    /**
     * Add a user to the server
     *
     * @param callback
     * @return
     * @throws CollabifyApiException
     */
    public void addUser(UserRequestDO user, Callback<User> callback) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.addUser(mCurrentUserId, user, callback);
    }

    /**
     * Update the user's showName
     *
     * @param userId
     * @param userSettings
     * @return
     */
    public UserSettings updateUser(String userId, UserSettings userSettings) {
        return mCollabifyService.updateUser(userId, userSettings);
    }

    /**
     * Update the user's showName
     *
     * @param userId
     * @param userSettings
     * @param callback
     * @return
     */
    public void updateUser(String userId, UserSettings userSettings, Callback<UserSettings> callback) {
        mCollabifyService.updateUser(userId, userSettings, callback);
    }

    /**
     * Log out the user and remove him from the server
     *
     * @param userId
     * @throws CollabifyApiException
     */
    public void logoutUser(String userId) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.logoutUser(userId);
    }

    /**
     * Log out the user and remove him from the server
     *
     * @param userId
     * @param callback
     * @throws CollabifyApiException
     */
    public void logoutUser(String userId, ResponseCallback callback) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.logoutUser(userId, callback);
    }


    /*--------------------------------------------------------------------------------------------*
     * Playlist ------------------------------------------------------------------------ Playlist *
     *--------------------------------------------------------------------------------------------*/

    /**
     * Get an event's playlist
     *
     * @param eventId
     * @return
     * @throws CollabifyApiException
     */
    public Playlist getEventPlaylist(String eventId) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        return mCollabifyService.getEventPlaylist(mCurrentUserId, eventId);
    }

    /**
     * Get an event's playlist
     *
     * @param eventId
     * @param callback
     * @throws CollabifyApiException
     */
    public void getEventPlaylist(String eventId, Callback<Playlist> callback) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.getEventPlaylist(mCurrentUserId, eventId, callback);
    }



    /**
     * Reorder an event's playlist
     *
     * @param eventId
     * @param oldIndex
     * @param newIndex
     * @return
     * @throws CollabifyApiException
     */
    public Playlist reorderPlaylist(String eventId, int oldIndex, int newIndex) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        return mCollabifyService.reorderPlaylist(eventId, oldIndex, newIndex);
    }

    /**
     * Reorder an event's playlist
     *
     * @param eventId
     * @param oldIndex
     * @param newIndex
     * @param callback
     * @throws CollabifyApiException
     */
    public void reorderPlaylist(String eventId, int oldIndex, int newIndex, Callback<Playlist> callback) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.reorderPlaylist(eventId, oldIndex, newIndex, callback);
    }

    /*--------------------------------------------------------------------------------------------*
     * Songs ------------------------------------------------------------------------------ Songs *
     *--------------------------------------------------------------------------------------------*/

    /**
     * Place a vote on a song.
     * @param eventId
     * @param songId
     * @param vote
     * @return
     * @throws CollabifyApiException
     */
    public Vote voteOnSong(String eventId, String songId, Vote vote) throws CollabifyApiException {
        if(mCurrentUserId == null || "".equals(mCurrentUserId)){
            throw new CollabifyApiException();
        }

        return mCollabifyService.voteOnSong(eventId, songId, mCurrentUserId, vote);
    }

    /**
     * Place a vote on a song.
     * @param eventId
     * @param songId
     * @param vote
     * @return
     * @throws CollabifyApiException
     */
    public void voteOnSong(String eventId, String songId, Vote vote, Callback<Vote> callback) throws CollabifyApiException {
        if(mCurrentUserId == null || "".equals(mCurrentUserId)){
            throw new CollabifyApiException();
        }

        mCollabifyService.voteOnSong(eventId, songId, mCurrentUserId, vote, callback);
    }

    /**
     * Add a song to an event's playlist
     *
     * @param eventId
     * @param song
     * @return
     * @throws CollabifyApiException
     */
    public Playlist addSong(String eventId, SongRequestDO song) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }
        return mCollabifyService.addSong(mCurrentUserId, eventId, song);
    }

    /**
     * Add a song to an event's playlist
     *
     * @param eventId
     * @param song
     * @param callback
     * @throws CollabifyApiException
     */
    public void addSong(String eventId, SongRequestDO song, Callback<Playlist> callback) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.addSong(mCurrentUserId, eventId, song, callback);
    }

    /**
     * Remove a song from an event's playlist
     *
     * @param eventId
     * @param songId
     * @throws CollabifyApiException
     */
    public void removeSong(String eventId, String songId) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.removeSong(mCurrentUserId, eventId, songId);
    }

    /**
     * Remove a song from an event's playlist
     *
     * @param eventId
     * @param songId
     * @param callback
     * @throws CollabifyApiException
     */
    public void removeSong(String eventId, String songId, ResponseCallback callback) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.removeSong(mCurrentUserId, eventId, songId, callback);
    }

    /**
     * End the current song and advance the queue to the next song (which is already on deck) and
     * get the updated playlist
     *
     * @param eventId
     * @return
     * @throws CollabifyApiException
     */
    public Playlist endCurrentSong(String eventId) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        return mCollabifyService.endCurrentSong(mCurrentUserId, eventId);
    }

    /**
     * End the current song and advance the queue to the next song (which is already on deck) and
     * get the updated playlist
     *
     * @param eventId
     * @param callback
     * @throws CollabifyApiException
     */
    public void endCurrentSong(String eventId, Callback<Playlist> callback) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.endCurrentSong(mCurrentUserId, eventId, callback);
    }

    /*--------------------------------------------------------------------------------------------*
     * Event ------------------------------------------------------------------------------ Event *
     *--------------------------------------------------------------------------------------------*/

    /**
     * Get information on an event
     *
     * @param eventId
     * @return
     * @throws CollabifyApiException
     */
    public EventDO getEvent(String eventId) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        return mCollabifyService.getEvent(eventId, mCurrentUserId);
    }

    /**
     * Get information on an event
     *
     * @param eventId
     * @param callback
     * @throws CollabifyApiException
     */
    public void getEvent(String eventId, Callback<EventDO> callback) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.getEvent(eventId, mCurrentUserId, callback);
    }

    /**
     * Get nearby events
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public List<EventDO> getEvents(String latitude, String longitude) {
        return mCollabifyService.getEvents(latitude, longitude);
    }

    /**
     * Get nearby events
     *
     * @param latitude
     * @param longitude
     * @param callback
     */
    public void getEvents(String latitude, String longitude, Callback<List<EventDO>> callback) {
        mCollabifyService.getEvents(latitude, longitude, callback);
    }

    /**
     * Create a new event
     *
     * @param event
     * @return
     * @throws CollabifyApiException
     */
    public Event createEvent(EventRequestDO event) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        return mCollabifyService.createEvent(mCurrentUserId, event);
    }

    /**
     * Create a new event
     *
     * @param event
     * @param callback
     * @throws CollabifyApiException
     */
    public void createEvent(EventRequestDO event, Callback<Event> callback) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.createEvent(mCurrentUserId, event, callback);
    }

    /**
     * Update an events info
     *
     * @param eventId
     * @param eventInfo
     * @return
     * @throws CollabifyApiException
     */
    public EventSettings updateEvent(String eventId, EventSettings eventInfo) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        return mCollabifyService.updateEvent(eventId, eventInfo);
    }

    /**
     * Update an events info
     *
     * @param eventId
     * @param eventInfo
     * @param callback
     * @throws CollabifyApiException
     */
    public void updateEvent(String eventId, EventSettings eventInfo, Callback<EventSettings> callback) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.updateEvent(eventId, eventInfo, callback);
    }

    /**
     * Update an events name
     *
     * @param eventId
     * @param name
     * @return
     * @throws CollabifyApiException
     */
    public EventSettings updateEventName(String eventId, EventRequestDO name) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        return mCollabifyService.updateEventName(eventId, name);
    }

    /**
     * Update an events name
     *
     * @param eventId
     * @param name
     * @param callback
     * @throws CollabifyApiException
     */
    public void updateEventName(String eventId, EventRequestDO name, Callback<EventSettings> callback) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.updateEventName(eventId, name, callback);
    }

    /**
     * Delete an event
     *
     * @param eventId
     * @throws CollabifyApiException
     */
    public void deleteEvent(String eventId) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.deleteEvent(eventId);
    }

    /**
     * Delete an event
     *
     * @param eventId
     * @param callback
     * @throws CollabifyApiException
     */
    public void deleteEvent(String eventId, ResponseCallback callback) throws  CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.deleteEvent(eventId, callback);
    }


    /*--------------------------------------------------------------------------------------------*
     * EventUser ---------------------------------------------------------------------- EventUser *
     *--------------------------------------------------------------------------------------------*/

    /**
     * Get a list of users that are currently attending an event
     *
     * @param eventId
     * @return
     * @throws CollabifyApiException
     */
    public List<UserDO> getEventUsers(String eventId) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        return mCollabifyService.getEventUsers(eventId);
    }

    /**
     * Get a list of users that are currently attending an event
     *
     * @param eventId
     * @param callback
     * @throws CollabifyApiException
     */
    public void getEventUsers(String eventId, Callback<List<UserDO>> callback) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.getEventUsers(eventId, callback);
    }

    /**
     * Join an event
     *
     * @param eventId
     * @return
     * @throws CollabifyApiException
     */
    public Event joinEvent(String eventId) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        return mCollabifyService.joinEvent(mCurrentUserId, eventId);
    }

    /**
     * Join an event
     *
     * @param eventId
     * @param callback
     * @throws CollabifyApiException
     */
    public void joinEvent(String eventId, Callback<Event> callback) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.joinEvent(mCurrentUserId, eventId, callback);
    }

    /**
     * Leave an event
     *
     * @param eventId
     * @throws CollabifyApiException
     */
    public void leaveEvent(String eventId) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.leaveEvent(eventId, mCurrentUserId);
    }

    /**
     * Leave an event
     *
     * @param eventId
     * @param callback
     * @throws CollabifyApiException
     */
    public void leaveEvent(String eventId, ResponseCallback callback) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.leaveEvent(eventId, mCurrentUserId, callback);
    }

    /**
     * Change a user's role in an event
     *
     * @param eventId
     * @param userId
     * @param role
     * @return
     * @throws CollabifyApiException
     */
    public Role changeUserRole(String eventId, String userId, String role) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        return mCollabifyService.changeUserRole(eventId, userId, role);
    }

    /**
     * Change a user's role in an event
     *
     * @param eventId
     * @param userId
     * @param role
     * @param callback
     * @throws CollabifyApiException
     */
    public void changeUserRole(String eventId, String userId, RoleDO role, Callback<Role> callback) throws CollabifyApiException {
        if (mCurrentUserId == null || "".equals(mCurrentUserId)) {
            throw new CollabifyApiException();
        }

        mCollabifyService.changeUserRole(eventId, userId, role, callback);
    }
}

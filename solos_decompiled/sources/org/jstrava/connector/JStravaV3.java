package org.jstrava.connector;

import com.google.gson.Gson;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.jstrava.entities.VCTrakFile;
import org.jstrava.entities.activity.Activity;
import org.jstrava.entities.activity.Comment;
import org.jstrava.entities.activity.LapEffort;
import org.jstrava.entities.activity.Photo;
import org.jstrava.entities.activity.UploadStatus;
import org.jstrava.entities.activity.Zone;
import org.jstrava.entities.athlete.Athlete;
import org.jstrava.entities.club.Club;
import org.jstrava.entities.gear.Gear;
import org.jstrava.entities.segment.Bound;
import org.jstrava.entities.segment.Segment;
import org.jstrava.entities.segment.SegmentEffort;
import org.jstrava.entities.segment.SegmentLeaderBoard;
import org.jstrava.entities.stream.Stream;
import org.jstrava.utility.MultipartUtility;

/* JADX INFO: loaded from: classes68.dex */
public class JStravaV3 implements JStrava {
    private static final String BOUNDARY = "*****";
    private static final String CRLF = "\r\n";
    private static final String TWO_HYPHENS = "--";
    private String accessToken;
    private Athlete currentAthlete;

    public String getAccessToken() {
        return this.accessToken;
    }

    @Override // org.jstrava.connector.JStrava
    public Athlete getCurrentAthlete() {
        return this.currentAthlete;
    }

    @Override // org.jstrava.connector.JStrava
    public Athlete updateAthlete(HashMap optionalParameters) {
        String result = putResult("https://www.strava.com/api/v3/athlete", optionalParameters);
        Gson gson = new Gson();
        Athlete athlete = (Athlete) gson.fromJson(result, Athlete.class);
        return athlete;
    }

    @Override // org.jstrava.connector.JStrava
    public Athlete findAthlete(int id) {
        String URL = "https://www.strava.com/api/v3/athletes/" + id;
        String result = getResult(URL);
        Gson gson = new Gson();
        Athlete athlete = (Athlete) gson.fromJson(result, Athlete.class);
        return athlete;
    }

    @Override // org.jstrava.connector.JStrava
    public List<SegmentEffort> findAthleteKOMs(int athleteId) {
        String URL = "https://www.strava.com/api/v3/athletes/" + athleteId + "/koms";
        String result = getResult(URL);
        Gson gson = new Gson();
        SegmentEffort[] segmentEffortArray = (SegmentEffort[]) gson.fromJson(result, SegmentEffort[].class);
        List<SegmentEffort> segmentEfforts = Arrays.asList(segmentEffortArray);
        return segmentEfforts;
    }

    @Override // org.jstrava.connector.JStrava
    public List<SegmentEffort> findAthleteKOMs(int athleteId, int page, int per_page) {
        String URL = "https://www.strava.com/api/v3/athletes/" + athleteId + "/koms?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);
        Gson gson = new Gson();
        SegmentEffort[] segmentEffortArray = (SegmentEffort[]) gson.fromJson(result, SegmentEffort[].class);
        List<SegmentEffort> segmentEfforts = Arrays.asList(segmentEffortArray);
        return segmentEfforts;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Athlete> getCurrentAthleteFriends() {
        String result = getResult("https://www.strava.com/api/v3/athlete/friends");
        Gson gson = new Gson();
        Athlete[] athletesArray = (Athlete[]) gson.fromJson(result, Athlete[].class);
        List<Athlete> athletes = Arrays.asList(athletesArray);
        return athletes;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Athlete> getCurrentAthleteFriends(int page, int per_page) {
        String URL = "https://www.strava.com/api/v3/athlete/friends?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);
        Gson gson = new Gson();
        Athlete[] athletesArray = (Athlete[]) gson.fromJson(result, Athlete[].class);
        List<Athlete> athletes = Arrays.asList(athletesArray);
        return athletes;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Athlete> findAthleteFriends(int id) {
        String URL = "https://www.strava.com/api/v3/athletes/" + id + "/friends";
        String result = getResult(URL);
        Gson gson = new Gson();
        Athlete[] athletesArray = (Athlete[]) gson.fromJson(result, Athlete[].class);
        List<Athlete> athletes = Arrays.asList(athletesArray);
        return athletes;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Athlete> findAthleteFriends(int id, int page, int per_page) {
        String URL = "https://www.strava.com/api/v3/athletes/" + id + "/friends?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);
        Gson gson = new Gson();
        Athlete[] athletesArray = (Athlete[]) gson.fromJson(result, Athlete[].class);
        List<Athlete> athletes = Arrays.asList(athletesArray);
        return athletes;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Athlete> getCurrentAthleteFollowers() {
        String result = getResult("https://www.strava.com/api/v3/athlete/followers");
        Gson gson = new Gson();
        Athlete[] athletesArray = (Athlete[]) gson.fromJson(result, Athlete[].class);
        List<Athlete> athletes = Arrays.asList(athletesArray);
        return athletes;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Athlete> getCurrentAthleteFollowers(int page, int per_page) {
        String URL = "https://www.strava.com/api/v3/athlete/followers?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);
        Gson gson = new Gson();
        Athlete[] athletesArray = (Athlete[]) gson.fromJson(result, Athlete[].class);
        List<Athlete> athletes = Arrays.asList(athletesArray);
        return athletes;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Athlete> findAthleteFollowers(int id) {
        String URL = "https://www.strava.com/api/v3/athletes/" + id + "/followers";
        String result = getResult(URL);
        Gson gson = new Gson();
        Athlete[] athletesArray = (Athlete[]) gson.fromJson(result, Athlete[].class);
        List<Athlete> athletes = Arrays.asList(athletesArray);
        return athletes;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Athlete> findAthleteFollowers(int id, int page, int per_page) {
        String URL = "https://www.strava.com/api/v3/athletes/" + id + "/followers?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);
        Gson gson = new Gson();
        Athlete[] athletesArray = (Athlete[]) gson.fromJson(result, Athlete[].class);
        List<Athlete> athletes = Arrays.asList(athletesArray);
        return athletes;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Athlete> findAthleteBothFollowing(int id) {
        String URL = "https://www.strava.com/api/v3/athletes/" + id + "/both-following";
        String result = getResult(URL);
        Gson gson = new Gson();
        Athlete[] athletesArray = (Athlete[]) gson.fromJson(result, Athlete[].class);
        List<Athlete> athletes = Arrays.asList(athletesArray);
        return athletes;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Athlete> findAthleteBothFollowing(int id, int page, int per_page) {
        String URL = "https://www.strava.com/api/v3/athletes/" + id + "/both-following?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);
        Gson gson = new Gson();
        Athlete[] athletesArray = (Athlete[]) gson.fromJson(result, Athlete[].class);
        List<Athlete> athletes = Arrays.asList(athletesArray);
        return athletes;
    }

    @Override // org.jstrava.connector.JStrava
    public Activity createActivity(String name, String type, String start_date_local, int elapsed_time, float distance, VCTrakFile file) {
        Activity activity;
        try {
            MultipartUtility utility = new MultipartUtility("https://www.strava.com/api/v3/uploads", "UTF-8", getAccessToken());
            utility.addFormField("name", name);
            utility.addFormField("activity_type", type);
            utility.addFormField("data_type", file.getDataType());
            utility.addFilePart("file", file);
            StringBuilder result = new StringBuilder();
            MultipartUtility.ResponseType response = utility.finish(result);
            String resultString = result.toString();
            System.out.println(resultString);
            if (!resultString.trim().isEmpty()) {
                Gson gson = new Gson();
                activity = (Activity) gson.fromJson(result.toString(), Activity.class);
                activity.setResponseType(response);
            } else {
                activity = new Activity();
                activity.setResponseType(response);
            }
            return activity;
        } catch (Exception e) {
            e.printStackTrace();
            Activity activity2 = new Activity();
            activity2.setResponseType(MultipartUtility.ResponseType.UNKNOWN);
            return activity2;
        }
    }

    @Override // org.jstrava.connector.JStrava
    public Activity createActivity(String name, String type, String start_date_local, int elapsed_time, String description, float distance, VCTrakFile file) {
        String URL = "https://www.strava.com/api/v3/activities?name=" + name + "&type=" + type + "&start_date_local=" + start_date_local + "&elapsed_time=" + elapsed_time + "&description=" + description + "&distance=" + distance;
        String result = postResult(URL, file);
        Gson gson = new Gson();
        return (Activity) gson.fromJson(result, Activity.class);
    }

    @Override // org.jstrava.connector.JStrava
    public void deleteActivity(int activityId) {
        String URL = "https://www.strava.com/api/v3/activities/" + activityId;
        String result = deleteResult(URL);
        Gson gson = new Gson();
        gson.fromJson(result, String.class);
    }

    @Override // org.jstrava.connector.JStrava
    public Activity findActivity(int id) {
        String URL = "https://www.strava.com/api/v3/activities/" + id;
        String result = getResult(URL);
        Gson gson = new Gson();
        Activity activity = (Activity) gson.fromJson(result, Activity.class);
        return activity;
    }

    @Override // org.jstrava.connector.JStrava
    public Activity findActivity(int id, boolean include_all_efforts) {
        String URL = "https://www.strava.com/api/v3/activities/" + id + "?include_all_efforts=" + include_all_efforts;
        String result = getResult(URL);
        Gson gson = new Gson();
        Activity activity = (Activity) gson.fromJson(result, Activity.class);
        return activity;
    }

    @Override // org.jstrava.connector.JStrava
    public Activity updateActivity(int activityId, HashMap optionalParameters) {
        String URL = "https://www.strava.com/api/v3/activities/" + activityId;
        String result = putResult(URL, optionalParameters);
        Gson gson = new Gson();
        Activity activity = (Activity) gson.fromJson(result, Activity.class);
        return activity;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Activity> getCurrentAthleteActivities() {
        String result = getResult("https://www.strava.com/api/v3/athlete/activities");
        Gson gson = new Gson();
        Activity[] activitiesArray = (Activity[]) gson.fromJson(result, Activity[].class);
        List<Activity> currentActivities = Arrays.asList(activitiesArray);
        return currentActivities;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Activity> getCurrentAthleteActivities(int page, int per_page) {
        String URL = "https://www.strava.com/api/v3/athlete/activities?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);
        Gson gson = new Gson();
        Activity[] activitiesArray = (Activity[]) gson.fromJson(result, Activity[].class);
        List<Activity> currentActivities = Arrays.asList(activitiesArray);
        return currentActivities;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Activity> getCurrentAthleteActivitiesBeforeDate(long before) {
        String URL = "https://www.strava.com/api/v3/athlete/activities?before=" + before;
        String result = getResult(URL);
        Gson gson = new Gson();
        Activity[] activitiesArray = (Activity[]) gson.fromJson(result, Activity[].class);
        List<Activity> currentActivities = Arrays.asList(activitiesArray);
        return currentActivities;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Activity> getCurrentAthleteActivitiesAfterDate(long after) {
        String URL = "https://www.strava.com/api/v3/athlete/activities?after=" + after;
        String result = getResult(URL);
        Gson gson = new Gson();
        Activity[] activitiesArray = (Activity[]) gson.fromJson(result, Activity[].class);
        List<Activity> currentActivities = Arrays.asList(activitiesArray);
        return currentActivities;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Activity> getCurrentFriendsActivities() {
        String result = getResult("https://www.strava.com/api/v3/activities/following");
        Gson gson = new Gson();
        Activity[] activitiesArray = (Activity[]) gson.fromJson(result, Activity[].class);
        List<Activity> currentFriendsActivities = Arrays.asList(activitiesArray);
        return currentFriendsActivities;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Activity> getCurrentFriendsActivities(int page, int per_page) {
        String URL = "https://www.strava.com/api/v3/activities/following?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);
        Gson gson = new Gson();
        Activity[] activitiesArray = (Activity[]) gson.fromJson(result, Activity[].class);
        List<Activity> currentFriendsActivities = Arrays.asList(activitiesArray);
        return currentFriendsActivities;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Zone> getActivityZones(int activityId) {
        String URL = "https://www.strava.com/api/v3/activities/" + activityId + "/zones";
        String result = getResult(URL);
        Gson gson = new Gson();
        Zone[] zonesArray = (Zone[]) gson.fromJson(result, Zone[].class);
        List<Zone> zones = Arrays.asList(zonesArray);
        return zones;
    }

    @Override // org.jstrava.connector.JStrava
    public List<LapEffort> findActivityLaps(int activityId) {
        String URL = "https://www.strava.com/api/v3/activities/" + activityId + "/laps";
        String result = getResult(URL);
        Gson gson = new Gson();
        LapEffort[] lapEffortsArray = (LapEffort[]) gson.fromJson(result, LapEffort[].class);
        List<LapEffort> lapEfforts = Arrays.asList(lapEffortsArray);
        return lapEfforts;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Comment> findActivityComments(int activityId) {
        String URL = "https://www.strava.com/api/v3/activities/" + activityId + "/comments";
        String result = getResult(URL);
        Gson gson = new Gson();
        Comment[] commentsArray = (Comment[]) gson.fromJson(result, Comment[].class);
        List<Comment> comments = Arrays.asList(commentsArray);
        return comments;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Comment> findActivityComments(int activityId, boolean markdown, int page, int per_page) {
        String URL = "https://www.strava.com/api/v3/activities/" + activityId + "/comments?markdown=" + markdown + "&page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);
        Gson gson = new Gson();
        Comment[] commentsArray = (Comment[]) gson.fromJson(result, Comment[].class);
        List<Comment> comments = Arrays.asList(commentsArray);
        return comments;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Athlete> findActivityKudos(int activityId) {
        String URL = "https://www.strava.com/api/v3/activities/" + activityId + "/kudos";
        String result = getResult(URL);
        Gson gson = new Gson();
        Athlete[] athletesArray = (Athlete[]) gson.fromJson(result, Athlete[].class);
        List<Athlete> athletes = Arrays.asList(athletesArray);
        return athletes;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Athlete> findActivityKudos(int activityId, int page, int per_page) {
        String URL = "https://www.strava.com/api/v3/activities/" + activityId + "/kudos?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);
        Gson gson = new Gson();
        Athlete[] athletesArray = (Athlete[]) gson.fromJson(result, Athlete[].class);
        List<Athlete> athletes = Arrays.asList(athletesArray);
        return athletes;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Athlete> findClubMembers(int clubId) {
        String URL = "https://www.strava.com/api/v3/clubs/" + clubId + "/members";
        String result = getResult(URL);
        Gson gson = new Gson();
        Athlete[] athletesArray = (Athlete[]) gson.fromJson(result, Athlete[].class);
        List<Athlete> athletes = Arrays.asList(athletesArray);
        return athletes;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Athlete> findClubMembers(int clubId, int page, int per_page) {
        String URL = "https://www.strava.com/api/v3/clubs/" + clubId + "/members?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);
        Gson gson = new Gson();
        Athlete[] athletesArray = (Athlete[]) gson.fromJson(result, Athlete[].class);
        List<Athlete> athletes = Arrays.asList(athletesArray);
        return athletes;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Activity> findClubActivities(int clubId) {
        String URL = "https://www.strava.com/api/v3/clubs/" + clubId + "/activities";
        String result = getResult(URL);
        Gson gson = new Gson();
        Activity[] activitiesArray = (Activity[]) gson.fromJson(result, Activity[].class);
        List<Activity> clubActivities = Arrays.asList(activitiesArray);
        return clubActivities;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Activity> findClubActivities(int clubId, int page, int per_page) {
        String URL = "https://www.strava.com/api/v3/clubs/" + clubId + "/activities?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);
        Gson gson = new Gson();
        Activity[] activitiesArray = (Activity[]) gson.fromJson(result, Activity[].class);
        List<Activity> clubActivities = Arrays.asList(activitiesArray);
        return clubActivities;
    }

    @Override // org.jstrava.connector.JStrava
    public Club findClub(int id) {
        String URL = "https://www.strava.com/api/v3/clubs/" + id;
        String result = getResult(URL);
        Gson gson = new Gson();
        Club club = (Club) gson.fromJson(result, Club.class);
        return club;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Club> getCurrentAthleteClubs() {
        String result = getResult("https://www.strava.com/api/v3/athlete/clubs");
        Gson gson = new Gson();
        Club[] clubsArray = (Club[]) gson.fromJson(result, Club[].class);
        List<Club> clubs = Arrays.asList(clubsArray);
        return clubs;
    }

    @Override // org.jstrava.connector.JStrava
    public Gear findGear(String id) {
        String URL = "https://www.strava.com/api/v3/gear/" + id;
        String result = getResult(URL);
        Gson gson = new Gson();
        Gear gear = (Gear) gson.fromJson(result, Gear.class);
        return gear;
    }

    @Override // org.jstrava.connector.JStrava
    public Segment findSegment(long segmentId) {
        String URL = "https://www.strava.com/api/v3/segments/" + segmentId;
        String result = getResult(URL);
        Gson gson = new Gson();
        Segment segment = (Segment) gson.fromJson(result, Segment.class);
        return segment;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Segment> getCurrentStarredSegment() {
        String result = getResult("https://www.strava.com/api/v3/segments/starred");
        Gson gson = new Gson();
        Segment[] segmentsArray = (Segment[]) gson.fromJson(result, Segment[].class);
        List<Segment> segments = Arrays.asList(segmentsArray);
        return segments;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Photo> findActivityPhotos(int activityId) {
        String URL = "https://www.strava.com/api/v3/activities/" + activityId + "/photos";
        String result = getResult(URL);
        Gson gson = new Gson();
        Photo[] photosArray = (Photo[]) gson.fromJson(result, Photo[].class);
        List<Photo> photos = Arrays.asList(photosArray);
        return photos;
    }

    @Override // org.jstrava.connector.JStrava
    public SegmentLeaderBoard findSegmentLeaderBoard(long segmentId) {
        String URL = "https://www.strava.com/api/v3/segments/" + segmentId + "/leaderboard";
        String result = getResult(URL);
        Gson gson = new Gson();
        SegmentLeaderBoard segmentLeaderBoard = (SegmentLeaderBoard) gson.fromJson(result, SegmentLeaderBoard.class);
        return segmentLeaderBoard;
    }

    @Override // org.jstrava.connector.JStrava
    public SegmentLeaderBoard findSegmentLeaderBoard(long segmentId, int page, int per_page) {
        String URL = "https://www.strava.com/api/v3/segments/" + segmentId + "/leaderboard?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);
        Gson gson = new Gson();
        SegmentLeaderBoard segmentLeaderBoard = (SegmentLeaderBoard) gson.fromJson(result, SegmentLeaderBoard.class);
        return segmentLeaderBoard;
    }

    @Override // org.jstrava.connector.JStrava
    public SegmentLeaderBoard findSegmentLeaderBoard(long segmentId, HashMap optionalParameters) {
        String URL = "https://www.strava.com/api/v3/segments/" + segmentId + "/leaderboard";
        String result = getResult(URL, optionalParameters);
        Gson gson = new Gson();
        SegmentLeaderBoard segmentLeaderBoard = (SegmentLeaderBoard) gson.fromJson(result, SegmentLeaderBoard.class);
        return segmentLeaderBoard;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Segment> findSegments(Bound bound) {
        String URL = "https://www.strava.com/api/v3/segments/explore?bounds=" + bound.toString();
        String result = getResult(URL).replaceFirst("\\{\"segments\":", "");
        String result2 = result.substring(0, result.lastIndexOf("}"));
        Gson gson = new Gson();
        Segment[] segmentsArray = (Segment[]) gson.fromJson(result2, Segment[].class);
        List<Segment> segments = Arrays.asList(segmentsArray);
        return segments;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Segment> findSegments(Bound bound, HashMap optionalParameters) {
        String URL = "https://www.strava.com/api/v3/segments/explore?bounds=" + bound.toString();
        String result = getResult(URL, optionalParameters);
        if (result.contains("\\{\"segments\":")) {
            String result2 = result.replaceFirst("\\{\"segments\":", "");
            result = result2.substring(0, result2.lastIndexOf("}"));
        }
        Gson gson = new Gson();
        Segment[] segmentsArray = (Segment[]) gson.fromJson(result, Segment[].class);
        List<Segment> segments = Arrays.asList(segmentsArray);
        return segments;
    }

    @Override // org.jstrava.connector.JStrava
    public SegmentEffort findSegmentEffort(int id) {
        String URL = "https://www.strava.com/api/v3/segment_efforts/" + id;
        String result = getResult(URL);
        Gson gson = new Gson();
        SegmentEffort segmentEffort = (SegmentEffort) gson.fromJson(result, SegmentEffort.class);
        return segmentEffort;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Stream> findActivityStreams(long activityId, String[] types) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }
        String URL = "https://www.strava.com/api/v3/activities/" + Long.toString(activityId) + "/streams/" + builder.toString();
        String result = getResult(URL);
        Gson gson = new Gson();
        Stream[] streamsArray = (Stream[]) gson.fromJson(result, Stream[].class);
        List<Stream> streams = Arrays.asList(streamsArray);
        return streams;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Stream> findActivityStreams(int activityId, String[] types, String resolution, String series_type) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }
        String URL = "https://www.strava.com/api/v3/activities/" + activityId + "/streams/" + builder.toString() + "?resolution=" + resolution;
        if (series_type != null && !series_type.isEmpty()) {
            URL = URL + "&series_type=" + series_type;
        }
        String result = getResult(URL);
        Gson gson = new Gson();
        Stream[] streamsArray = (Stream[]) gson.fromJson(result, Stream[].class);
        List<Stream> streams = Arrays.asList(streamsArray);
        return streams;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Stream> findEffortStreams(int id, String[] types) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }
        String URL = "https://www.strava.com/api/v3/segment_efforts/" + id + "/streams/" + builder.toString();
        String result = getResult(URL);
        Gson gson = new Gson();
        Stream[] streamsArray = (Stream[]) gson.fromJson(result, Stream[].class);
        List<Stream> streams = Arrays.asList(streamsArray);
        return streams;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Stream> findEffortStreams(int id, String[] types, String resolution, String series_type) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }
        String URL = "https://www.strava.com/api/v3/segment_efforts/" + id + "/streams/" + builder.toString() + "?resolution=" + resolution;
        if (series_type != null && !series_type.isEmpty()) {
            URL = URL + "&series_type=" + series_type;
        }
        String result = getResult(URL);
        Gson gson = new Gson();
        Stream[] streamsArray = (Stream[]) gson.fromJson(result, Stream[].class);
        List<Stream> streams = Arrays.asList(streamsArray);
        return streams;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Stream> findSegmentStreams(int id, String[] types) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }
        String URL = "https://www.strava.com/api/v3/segments/" + id + "/streams/" + builder.toString();
        String result = getResult(URL);
        Gson gson = new Gson();
        Stream[] streamsArray = (Stream[]) gson.fromJson(result, Stream[].class);
        List<Stream> streams = Arrays.asList(streamsArray);
        return streams;
    }

    @Override // org.jstrava.connector.JStrava
    public List<Stream> findSegmentStreams(int id, String[] types, String resolution, String series_type) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }
        String URL = "https://www.strava.com/api/v3/segments/" + id + "/streams/" + builder.toString() + "?resolution=" + resolution;
        if (series_type != null && !series_type.isEmpty()) {
            URL = URL + "&series_type=" + series_type;
        }
        String result = getResult(URL);
        Gson gson = new Gson();
        Stream[] streamsArray = (Stream[]) gson.fromJson(result, Stream[].class);
        List<Stream> streams = Arrays.asList(streamsArray);
        return streams;
    }

    @Override // org.jstrava.connector.JStrava
    public UploadStatus uploadActivity(String data_type, File file) {
        return null;
    }

    @Override // org.jstrava.connector.JStrava
    public UploadStatus uploadActivity(String activity_type, String name, String description, int is_private, int trainer, String data_type, String external_id, File file) {
        return null;
    }

    @Override // org.jstrava.connector.JStrava
    public UploadStatus checkUploadStatus(long uploadId) {
        try {
            String URL = "https://www.strava.com/api/v3/uploads/" + uploadId;
            String result = getResult(URL);
            Gson gson = new Gson();
            UploadStatus status = (UploadStatus) gson.fromJson(result, UploadStatus.class);
            status.json = result;
            return status;
        } catch (Exception e) {
            return null;
        }
    }

    public JStravaV3(String access_token) {
        this.accessToken = access_token;
        String result = getResult("https://www.strava.com/api/v3/athlete");
        Gson gson = new Gson();
        this.currentAthlete = (Athlete) gson.fromJson(result, Athlete.class);
    }

    private String getResult(String URL) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(HttpRequest.METHOD_GET);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while (true) {
                String output = br.readLine();
                if (output != null) {
                    sb.append(output);
                } else {
                    conn.disconnect();
                    return sb.toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private String getResult(String URL, HashMap optionalParameters) {
        StringBuilder sb = new StringBuilder();
        sb.append(URL);
        try {
            int index = 0;
            for (String key : optionalParameters.keySet()) {
                if (index == 0) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(URLEncoder.encode(optionalParameters.get(key).toString(), "UTF-8"));
                index++;
            }
            URI uri = new URI(String.format(sb.toString(), new Object[0]));
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(HttpRequest.METHOD_GET);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb2 = new StringBuilder();
            while (true) {
                try {
                    String output = br.readLine();
                    if (output != null) {
                        sb2.append(output);
                    } else {
                        conn.disconnect();
                        return sb2.toString();
                    }
                } catch (IOException e) {
                    e = e;
                    e.printStackTrace();
                    return null;
                } catch (URISyntaxException e2) {
                    e = e2;
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (IOException e3) {
            e = e3;
        } catch (URISyntaxException e4) {
            e = e4;
        }
    }

    private String postResult(String URL, VCTrakFile file) {
        BufferedReader in;
        StringBuilder sb = new StringBuilder();
        try {
            String[] parsedUrl = (URL + "&data_type=" + file.getDataType()).split("\\?");
            String params = URLEncoder.encode(parsedUrl[1], "UTF-8").replace("%3D", "=").replace("%26", "&");
            URL url = new URL(parsedUrl[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod(HttpRequest.METHOD_POST);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****");
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(params);
            wr.writeBytes("--*****\r\n");
            wr.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + file.getFileName() + "\"\r\n");
            wr.writeBytes("\r\n");
            file.write(wr);
            wr.writeBytes("\r\n");
            wr.writeBytes("--*****--\r\n");
            wr.flush();
            wr.close();
            boolean redirect = false;
            int status = conn.getResponseCode();
            if (status != 200 && (status == 302 || status == 301 || status == 303)) {
                redirect = true;
            }
            if (redirect) {
                String newUrl = conn.getHeaderField(HttpRequest.HEADER_LOCATION);
                conn = (HttpURLConnection) new URL(newUrl).openConnection();
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            }
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            String inputLine = in.readLine();
            if (inputLine == null) {
                break;
            }
            sb.append(inputLine);
            return sb.toString();
        }
        in.close();
        return sb.toString();
    }

    private String putResult(String URL) {
        String finalUrl;
        new StringBuilder();
        try {
            if (URL.contains("?")) {
                String[] parsedUrl = URL.split("\\?");
                String params = URLEncoder.encode(parsedUrl[1], "UTF-8");
                finalUrl = parsedUrl[0] + "?" + params;
            } else {
                finalUrl = URL;
            }
            URL url = new URL(finalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(HttpRequest.METHOD_PUT);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            if ((conn.getResponseCode() != 201) | (conn.getResponseCode() != 200)) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            while (true) {
                try {
                    String output = br.readLine();
                    if (output != null) {
                        sb.append(output);
                    } else {
                        conn.disconnect();
                        return sb.toString();
                    }
                } catch (IOException e) {
                    e = e;
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (IOException e2) {
            e = e2;
        }
    }

    private String putResult(String URL, HashMap optionalParameters) {
        StringBuilder sb = new StringBuilder();
        sb.append(URL);
        try {
            int index = 0;
            for (String key : optionalParameters.keySet()) {
                if (index == 0) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(URLEncoder.encode(optionalParameters.get(key).toString(), "UTF-8"));
                index++;
            }
            URI uri = new URI(sb.toString());
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(HttpRequest.METHOD_PUT);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb2 = new StringBuilder();
            while (true) {
                try {
                    String output = br.readLine();
                    if (output != null) {
                        sb2.append(output);
                    } else {
                        conn.disconnect();
                        return sb2.toString();
                    }
                } catch (IOException e) {
                    e = e;
                    e.printStackTrace();
                    return null;
                } catch (URISyntaxException e2) {
                    e = e2;
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (IOException e3) {
            e = e3;
        } catch (URISyntaxException e4) {
            e = e4;
        }
    }

    private String deleteResult(String URL) {
        try {
            URI uri = new URI(String.format(URL, new Object[0]));
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(HttpRequest.METHOD_DELETE);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            if (conn.getResponseCode() != 204) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            while (true) {
                try {
                    String output = br.readLine();
                    if (output != null) {
                        sb.append(output);
                    } else {
                        conn.disconnect();
                        return sb.toString();
                    }
                } catch (IOException e) {
                    e = e;
                    e.printStackTrace();
                    return null;
                } catch (URISyntaxException e2) {
                    e = e2;
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (IOException e3) {
            e = e3;
        } catch (URISyntaxException e4) {
            e = e4;
        }
    }
}

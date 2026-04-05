package org.jstrava.connector;

import java.io.File;
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

/* JADX INFO: loaded from: classes68.dex */
public interface JStrava {
    UploadStatus checkUploadStatus(long j);

    Activity createActivity(String str, String str2, String str3, int i, float f, VCTrakFile vCTrakFile);

    Activity createActivity(String str, String str2, String str3, int i, String str4, float f, VCTrakFile vCTrakFile);

    void deleteActivity(int i);

    Activity findActivity(int i);

    Activity findActivity(int i, boolean z);

    List<Comment> findActivityComments(int i);

    List<Comment> findActivityComments(int i, boolean z, int i2, int i3);

    List<Athlete> findActivityKudos(int i);

    List<Athlete> findActivityKudos(int i, int i2, int i3);

    List<LapEffort> findActivityLaps(int i);

    List<Photo> findActivityPhotos(int i);

    List<Stream> findActivityStreams(int i, String[] strArr, String str, String str2);

    List<Stream> findActivityStreams(long j, String[] strArr);

    Athlete findAthlete(int i);

    List<Athlete> findAthleteBothFollowing(int i);

    List<Athlete> findAthleteBothFollowing(int i, int i2, int i3);

    List<Athlete> findAthleteFollowers(int i);

    List<Athlete> findAthleteFollowers(int i, int i2, int i3);

    List<Athlete> findAthleteFriends(int i);

    List<Athlete> findAthleteFriends(int i, int i2, int i3);

    List<SegmentEffort> findAthleteKOMs(int i);

    List<SegmentEffort> findAthleteKOMs(int i, int i2, int i3);

    Club findClub(int i);

    List<Activity> findClubActivities(int i);

    List<Activity> findClubActivities(int i, int i2, int i3);

    List<Athlete> findClubMembers(int i);

    List<Athlete> findClubMembers(int i, int i2, int i3);

    List<Stream> findEffortStreams(int i, String[] strArr);

    List<Stream> findEffortStreams(int i, String[] strArr, String str, String str2);

    Gear findGear(String str);

    Segment findSegment(long j);

    SegmentEffort findSegmentEffort(int i);

    SegmentLeaderBoard findSegmentLeaderBoard(long j);

    SegmentLeaderBoard findSegmentLeaderBoard(long j, int i, int i2);

    SegmentLeaderBoard findSegmentLeaderBoard(long j, HashMap map);

    List<Stream> findSegmentStreams(int i, String[] strArr);

    List<Stream> findSegmentStreams(int i, String[] strArr, String str, String str2);

    List<Segment> findSegments(Bound bound);

    List<Segment> findSegments(Bound bound, HashMap map);

    List<Zone> getActivityZones(int i);

    Athlete getCurrentAthlete();

    List<Activity> getCurrentAthleteActivities();

    List<Activity> getCurrentAthleteActivities(int i, int i2);

    List<Activity> getCurrentAthleteActivitiesAfterDate(long j);

    List<Activity> getCurrentAthleteActivitiesBeforeDate(long j);

    List<Club> getCurrentAthleteClubs();

    List<Athlete> getCurrentAthleteFollowers();

    List<Athlete> getCurrentAthleteFollowers(int i, int i2);

    List<Athlete> getCurrentAthleteFriends();

    List<Athlete> getCurrentAthleteFriends(int i, int i2);

    List<Activity> getCurrentFriendsActivities();

    List<Activity> getCurrentFriendsActivities(int i, int i2);

    List<Segment> getCurrentStarredSegment();

    Activity updateActivity(int i, HashMap map);

    Athlete updateAthlete(HashMap map);

    UploadStatus uploadActivity(String str, File file);

    UploadStatus uploadActivity(String str, String str2, String str3, int i, int i2, String str4, String str5, File file);
}

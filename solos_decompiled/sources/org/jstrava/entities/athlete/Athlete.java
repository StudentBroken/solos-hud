package org.jstrava.entities.athlete;

import java.util.List;
import org.jstrava.entities.club.Club;
import org.jstrava.entities.gear.Gear;

/* JADX INFO: loaded from: classes68.dex */
public class Athlete {
    private boolean approve_followers;
    private List<Gear> bikes;
    private String city;
    private List<Club> clubs;
    private String created_at;
    private String date_preference;
    private String email;
    private String firstname;
    private String follower;
    private int follower_count;
    private String friend;
    private int friend_count;
    private int ftp;
    private int id;
    private String lastname;
    private String measurement_preference;
    private int mutual_friend_count;
    private boolean premium;
    private String profile;
    private String profile_medium;
    private String resource_state;
    private String sex;
    private List<Gear> shoes;
    private String state;
    private String updated_at;

    public String toString() {
        return this.firstname + " " + this.lastname;
    }

    public Athlete(int id) {
        this.id = id;
    }

    public Athlete() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResource_state() {
        return this.resource_state;
    }

    public void setResource_state(String resource_state) {
        this.resource_state = resource_state;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getProfile_medium() {
        return this.profile_medium;
    }

    public void setProfile_medium(String profile_medium) {
        this.profile_medium = profile_medium;
    }

    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFriend() {
        return this.friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getFollower() {
        return this.follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public boolean getPremium() {
        return this.premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return this.updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean getApprove_followers() {
        return this.approve_followers;
    }

    public void setApprove_followers(boolean approve_followers) {
        this.approve_followers = approve_followers;
    }

    public int getFollower_count() {
        return this.follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }

    public int getFriend_count() {
        return this.friend_count;
    }

    public void setFriend_count(int friend_count) {
        this.friend_count = friend_count;
    }

    public int getMutual_friend_count() {
        return this.mutual_friend_count;
    }

    public void setMutual_friend_count(int mutual_friend_count) {
        this.mutual_friend_count = mutual_friend_count;
    }

    public String getDate_preference() {
        return this.date_preference;
    }

    public void setDate_preference(String date_preference) {
        this.date_preference = date_preference;
    }

    public String getMeasurement_preference() {
        return this.measurement_preference;
    }

    public void setMeasurement_preference(String measurement_preference) {
        this.measurement_preference = measurement_preference;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFtp() {
        return this.ftp;
    }

    public void setFtp(int ftp) {
        this.ftp = ftp;
    }

    public List<Club> getClubs() {
        return this.clubs;
    }

    public void setClubs(List<Club> clubs) {
        this.clubs = clubs;
    }

    public List<Gear> getBikes() {
        return this.bikes;
    }

    public void setBikes(List<Gear> bikes) {
        this.bikes = bikes;
    }

    public List<Gear> getShoes() {
        return this.shoes;
    }

    public void setShoes(List<Gear> shoes) {
        this.shoes = shoes;
    }
}

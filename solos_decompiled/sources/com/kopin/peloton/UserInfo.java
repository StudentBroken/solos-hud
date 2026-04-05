package com.kopin.peloton;

import com.google.gson.Gson;

/* JADX INFO: loaded from: classes61.dex */
public class UserInfo {
    public long DOB;
    public String DefaultUserName;
    public String Email;
    public String ExternalIdentity;
    public String Gender;
    public String Login;
    public String Name;
    public String Password;
    public String Username;
    public double Weight;

    public enum GenderType {
        Unknown,
        Male,
        Female;

        @Override // java.lang.Enum
        public String toString() {
            return "" + name().charAt(0);
        }
    }

    public UserInfo(String username, String email, String password) {
        this.Username = "";
        this.Email = "";
        this.Password = "";
        this.Name = "";
        this.DOB = 0L;
        this.Weight = 0.0d;
        this.Gender = GenderType.Unknown.toString();
        this.Login = "";
        this.DefaultUserName = "";
        this.ExternalIdentity = "";
        this.Username = username;
        this.Email = email;
        this.Password = password;
    }

    public UserInfo(String key, String email) {
        this.Username = "";
        this.Email = "";
        this.Password = "";
        this.Name = "";
        this.DOB = 0L;
        this.Weight = 0.0d;
        this.Gender = GenderType.Unknown.toString();
        this.Login = "";
        this.DefaultUserName = "";
        this.ExternalIdentity = "";
        this.Login = String.format("{\"LoginProvider\" : \"Facebook\", \"ProviderKey\" : \"$s\"}", key);
        this.Email = email;
    }

    public void setGender(GenderType gender) {
        this.Gender = gender.toString();
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}

package com.facebook;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import com.facebook.share.internal.ShareConstants;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
public class TestUserManager {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final String LOG_TAG = "TestUserManager";
    private Map<String, JSONObject> appTestAccounts;
    private String testApplicationId;
    private String testApplicationSecret;

    private enum Mode {
        PRIVATE,
        SHARED
    }

    static {
        $assertionsDisabled = !TestUserManager.class.desiredAssertionStatus();
    }

    public TestUserManager(String testApplicationSecret, String testApplicationId) {
        if (Utility.isNullOrEmpty(testApplicationId) || Utility.isNullOrEmpty(testApplicationSecret)) {
            throw new FacebookException("Must provide app ID and secret");
        }
        this.testApplicationSecret = testApplicationSecret;
        this.testApplicationId = testApplicationId;
    }

    public AccessToken getAccessTokenForPrivateUser(List<String> permissions) {
        return getAccessTokenForUser(permissions, Mode.PRIVATE, null);
    }

    public AccessToken getAccessTokenForSharedUser(List<String> permissions) {
        return getAccessTokenForSharedUser(permissions, null);
    }

    public AccessToken getAccessTokenForSharedUser(List<String> permissions, String uniqueUserTag) {
        return getAccessTokenForUser(permissions, Mode.SHARED, uniqueUserTag);
    }

    public synchronized String getTestApplicationId() {
        return this.testApplicationId;
    }

    public synchronized String getTestApplicationSecret() {
        return this.testApplicationSecret;
    }

    private AccessToken getAccessTokenForUser(List<String> permissions, Mode mode, String uniqueUserTag) {
        JSONObject testAccount;
        retrieveTestAccountsForAppIfNeeded();
        if (Utility.isNullOrEmpty(permissions)) {
            permissions = Arrays.asList("email", "publish_actions");
        }
        if (mode == Mode.PRIVATE) {
            testAccount = createTestAccount(permissions, mode, uniqueUserTag);
        } else {
            testAccount = findOrCreateSharedTestAccount(permissions, mode, uniqueUserTag);
        }
        return new AccessToken(testAccount.optString("access_token"), this.testApplicationId, testAccount.optString("id"), permissions, null, AccessTokenSource.TEST_USER, null, null);
    }

    private synchronized void retrieveTestAccountsForAppIfNeeded() {
        if (this.appTestAccounts == null) {
            this.appTestAccounts = new HashMap();
            GraphRequest.setDefaultBatchApplicationId(this.testApplicationId);
            Bundle parameters = new Bundle();
            parameters.putString("access_token", getAppAccessToken());
            GraphRequest requestTestUsers = new GraphRequest(null, "app/accounts/test-users", parameters, null);
            requestTestUsers.setBatchEntryName("testUsers");
            requestTestUsers.setBatchEntryOmitResultOnSuccess(false);
            Bundle testUserNamesParam = new Bundle();
            testUserNamesParam.putString("access_token", getAppAccessToken());
            testUserNamesParam.putString("ids", "{result=testUsers:$.data.*.id}");
            testUserNamesParam.putString(GraphRequest.FIELDS_PARAM, "name");
            GraphRequest requestTestUserNames = new GraphRequest(null, "", testUserNamesParam, null);
            requestTestUserNames.setBatchEntryDependsOn("testUsers");
            List<GraphResponse> responses = GraphRequest.executeBatchAndWait(requestTestUsers, requestTestUserNames);
            if (responses == null || responses.size() != 2) {
                throw new FacebookException("Unexpected number of results from TestUsers batch query");
            }
            JSONObject testAccountsResponse = responses.get(0).getJSONObject();
            JSONArray testAccounts = testAccountsResponse.optJSONArray(ShareConstants.WEB_DIALOG_PARAM_DATA);
            JSONObject userAccountsMap = responses.get(1).getJSONObject();
            populateTestAccounts(testAccounts, userAccountsMap);
        }
    }

    private synchronized void populateTestAccounts(JSONArray testAccounts, JSONObject userAccountsMap) {
        for (int i = 0; i < testAccounts.length(); i++) {
            JSONObject testAccount = testAccounts.optJSONObject(i);
            JSONObject testUser = userAccountsMap.optJSONObject(testAccount.optString("id"));
            try {
                testAccount.put("name", testUser.optString("name"));
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Could not set name", e);
            }
            storeTestAccount(testAccount);
        }
    }

    private synchronized void storeTestAccount(JSONObject testAccount) {
        this.appTestAccounts.put(testAccount.optString("id"), testAccount);
    }

    private synchronized JSONObject findTestAccountMatchingIdentifier(String identifier) {
        JSONObject testAccount;
        Iterator<JSONObject> it = this.appTestAccounts.values().iterator();
        while (true) {
            if (!it.hasNext()) {
                testAccount = null;
                break;
            }
            testAccount = it.next();
            if (testAccount.optString("name").contains(identifier)) {
                break;
            }
        }
        return testAccount;
    }

    final String getAppAccessToken() {
        return this.testApplicationId + "|" + this.testApplicationSecret;
    }

    private JSONObject findOrCreateSharedTestAccount(List<String> permissions, Mode mode, String uniqueUserTag) {
        JSONObject testAccount = findTestAccountMatchingIdentifier(getSharedTestAccountIdentifier(permissions, uniqueUserTag));
        return testAccount != null ? testAccount : createTestAccount(permissions, mode, uniqueUserTag);
    }

    private String getSharedTestAccountIdentifier(List<String> permissions, String uniqueUserTag) {
        long permissionsHash = ((long) getPermissionsString(permissions).hashCode()) & 4294967295L;
        long userTagHash = uniqueUserTag != null ? ((long) uniqueUserTag.hashCode()) & 4294967295L : 0L;
        long combinedHash = permissionsHash ^ userTagHash;
        return validNameStringFromInteger(combinedHash);
    }

    private String validNameStringFromInteger(long i) {
        String s = Long.toString(i);
        StringBuilder result = new StringBuilder("Perm");
        char lastChar = 0;
        char[] arr$ = s.toCharArray();
        for (char c : arr$) {
            if (c == lastChar) {
                c = (char) (c + '\n');
            }
            result.append((char) ((c + 'a') - 48));
            lastChar = c;
        }
        return result.toString();
    }

    private JSONObject createTestAccount(List<String> permissions, Mode mode, String uniqueUserTag) {
        Bundle parameters = new Bundle();
        parameters.putString("installed", ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
        parameters.putString(NativeProtocol.RESULT_ARGS_PERMISSIONS, getPermissionsString(permissions));
        parameters.putString("access_token", getAppAccessToken());
        if (mode == Mode.SHARED) {
            parameters.putString("name", String.format("Shared %s Testuser", getSharedTestAccountIdentifier(permissions, uniqueUserTag)));
        }
        String graphPath = String.format("%s/accounts/test-users", this.testApplicationId);
        GraphRequest createUserRequest = new GraphRequest(null, graphPath, parameters, HttpMethod.POST);
        GraphResponse response = createUserRequest.executeAndWait();
        FacebookRequestError error = response.getError();
        JSONObject testAccount = response.getJSONObject();
        if (error != null) {
            return null;
        }
        if (!$assertionsDisabled && testAccount == null) {
            throw new AssertionError();
        }
        if (mode == Mode.SHARED) {
            try {
                testAccount.put("name", parameters.getString("name"));
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Could not set name", e);
            }
            storeTestAccount(testAccount);
            return testAccount;
        }
        return testAccount;
    }

    private String getPermissionsString(List<String> permissions) {
        return TextUtils.join(",", permissions);
    }
}

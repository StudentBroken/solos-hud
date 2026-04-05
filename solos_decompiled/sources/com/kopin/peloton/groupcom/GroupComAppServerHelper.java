package com.kopin.peloton.groupcom;

import android.content.Intent;
import android.os.AsyncTask;
import com.kopin.peloton.Cloud;
import com.kopin.peloton.Failure;
import com.kopin.peloton.PelotonResponse;
import com.ua.sdk.recorder.datasource.sensor.location.MockLocationClient;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes61.dex */
public class GroupComAppServerHelper {
    public static int CONFIG_CONNECTION_TIMEOUT = MockLocationClient.CONNECTION_TIMEOUT;
    private static FetchMyChatGroupsTask fetchChatGroupsTask;
    private static JoinSessionTask joinChatSessionTask;
    private static LeaveSessionTask leaveChatSessionTask;

    private interface AppServerResponse {
        void onFailure(Failure failure, int i, String str);
    }

    public interface FetchMyChatGroupsResponse extends AppServerResponse {
        void onFetchMyGroupsResponse(List<ChatGroup> list);
    }

    public interface JoinSessionResponse extends AppServerResponse {
        void onJoinSessionResponse(SessionInfo sessionInfo);
    }

    public interface LeaveSessionResponse extends AppServerResponse {
        void onLeaveSessionResponse();
    }

    protected static class FetchMyChatGroupsTask extends AsyncTask<Void, Void, PelotonResponse> {
        final FetchMyChatGroupsResponse mResponse;

        FetchMyChatGroupsTask(FetchMyChatGroupsResponse response) {
            this.mResponse = response;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.fetchMyChatGroups(GroupComAppServerHelper.CONFIG_CONNECTION_TIMEOUT);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            if (this.mResponse != null) {
                if (response.isServerSuccess()) {
                    this.mResponse.onFetchMyGroupsResponse((List) response.result);
                } else {
                    this.mResponse.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
                }
            }
        }
    }

    protected static class JoinSessionTask extends AsyncTask<Void, Void, PelotonResponse> {
        final String mGroupId;
        final JoinSessionResponse mResponse;

        JoinSessionTask(String groupId, JoinSessionResponse response) {
            this.mGroupId = groupId;
            this.mResponse = response;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.joinChatSession(this.mGroupId, GroupComAppServerHelper.CONFIG_CONNECTION_TIMEOUT);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            if (this.mResponse != null) {
                if (response.isServerSuccess()) {
                    this.mResponse.onJoinSessionResponse((SessionInfo) response.result);
                } else {
                    this.mResponse.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
                }
            }
        }
    }

    protected static class LeaveSessionTask extends AsyncTask<Void, Void, PelotonResponse> {
        final String mGroupId;
        final LeaveSessionResponse mResponse;

        LeaveSessionTask(String groupId, LeaveSessionResponse response) {
            this.mGroupId = groupId;
            this.mResponse = response;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.leaveChatSession(this.mGroupId, GroupComAppServerHelper.CONFIG_CONNECTION_TIMEOUT);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            if (this.mResponse != null) {
                if (response.isServerSuccess()) {
                    this.mResponse.onLeaveSessionResponse();
                } else {
                    this.mResponse.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
                }
            }
        }
    }

    public static void fetchMyChatGroups(FetchMyChatGroupsResponse response) {
        fetchChatGroupsTask = new FetchMyChatGroupsTask(response);
        fetchChatGroupsTask.execute(new Void[0]);
    }

    public static void joinChatSession(String groupId, JoinSessionResponse response) {
        joinChatSessionTask = new JoinSessionTask(groupId, response);
        joinChatSessionTask.execute(new Void[0]);
    }

    public static void leaveChatSession(String groupId, LeaveSessionResponse response) {
        leaveChatSessionTask = new LeaveSessionTask(groupId, response);
        leaveChatSessionTask.execute(new Void[0]);
    }

    public static String getGroupManagementUrl() {
        return Cloud.getGroupManagementUrl();
    }

    public static HashMap<String, String> getHttpHeaders() {
        return Cloud.getGroupManagementHttpHeaders();
    }

    public static Intent getGroupManagementUrlIntent() {
        return Cloud.getGroupManagementOpenUrlIntent();
    }
}

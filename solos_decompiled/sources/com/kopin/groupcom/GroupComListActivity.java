package com.kopin.groupcom;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.kopin.groupcom.GroupCom;
import com.kopin.peloton.groupcom.ChatGroup;
import com.kopin.peloton.groupcom.GroupComAppServerHelper;
import com.kopin.solos.common.BaseListActivity;
import com.kopin.solos.common.config.Config;
import com.kopin.solos.common.permission.Permission;
import com.kopin.solos.share.peloton.TermsActivity;
import com.kopin.solos.storage.util.Utility;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes64.dex */
public class GroupComListActivity extends BaseListActivity implements GroupCom.GroupComListener {
    public static String INTENT_EXTRA_USER_NAME = "com.kopin.groupcom.USERNAME";
    private Button mBtnPTT;
    ChatGroupListAdapter mChatGroupListAdapter;
    private List<ChatGroup> mChatGroups = new ArrayList();
    private ProgressBar mProgressBar;
    private TextView mTextNoData1;
    private TextView mTextNoData2;
    private View mViewNoData;

    @Override // com.kopin.solos.common.BaseListActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        initPermissions(Permission.RECORD_AUDIO);
        this.mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.mViewNoData = findViewById(R.id.viewNoData);
        this.mTextNoData1 = (TextView) findViewById(R.id.textNoData1);
        this.mTextNoData2 = (TextView) findViewById(R.id.textNoData2);
        this.mBtnPTT = (Button) findViewById(R.id.btnPTT);
        this.mBtnPTT.setVisibility(4);
        getActionBar().setTitle(R.string.title_chat_group_list);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        this.mChatGroupListAdapter = new ChatGroupListAdapter(this);
        setListAdapter(this.mChatGroupListAdapter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNoDataView() {
        this.mViewNoData.setVisibility(0);
        if (!GroupCom.isNetworkAvailable()) {
            this.mTextNoData1.setText(R.string.no_internet_message);
            this.mTextNoData2.setVisibility(4);
            return;
        }
        this.mTextNoData1.setText(R.string.no_groups_message_1);
        ImageSpan imageSpan = new ImageSpan(this, R.drawable.chat_settings_ic);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append((CharSequence) getString(R.string.no_groups_message_2_part_1));
        builder.setSpan(imageSpan, builder.length() - 1, builder.length(), 0);
        builder.append((CharSequence) getString(R.string.no_groups_message_2_part_2));
        this.mTextNoData2.setVisibility(0);
        this.mTextNoData2.setText(builder);
    }

    @Override // com.kopin.solos.common.BaseListActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.mViewNoData.setVisibility(8);
        GroupCom.addListener(this);
        GroupCom.fetchMyChatGroups();
    }

    @Override // com.kopin.solos.common.BaseListActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        GroupCom.removeListener(this);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_groupcom, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override // com.kopin.solos.common.BaseListActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.menuSettings) {
            openGroupManagementPage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override // android.app.ListActivity
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ChatGroup selectedChatGroup = this.mChatGroups.get(position);
        if (GroupCom.isInSession()) {
            GroupCom.disconnectSession();
        } else {
            connectToSession(selectedChatGroup);
        }
    }

    public void onPTTClick(View view) {
        boolean publishAudio = GroupCom.isSpeaking();
        if (publishAudio) {
            GroupCom.stopSpeaking();
            this.mBtnPTT.setText(R.string.push_to_talk);
        } else {
            GroupCom.startSpeaking();
            this.mBtnPTT.setText(R.string.push_to_silence);
        }
    }

    private void connectToSession(ChatGroup group) {
        String userName = "NO NAME";
        Intent intent = getIntent();
        if (intent != null) {
            userName = intent.getStringExtra(INTENT_EXTRA_USER_NAME);
        }
        GroupCom.connectSession(group, userName);
    }

    private void updateSessionState(final ChatGroup group) {
        runOnUiThread(new Runnable() { // from class: com.kopin.groupcom.GroupComListActivity.1
            @Override // java.lang.Runnable
            public void run() {
                if (group != null) {
                    GroupComListActivity.this.mChatGroupListAdapter.notifySessionStateChanged(group);
                    switch (AnonymousClass5.$SwitchMap$com$kopin$peloton$groupcom$ChatGroup$SessionState[group.sessionState.ordinal()]) {
                        case 1:
                            boolean publishAudio = GroupCom.isSpeaking();
                            if (Config.DEBUG) {
                                GroupComListActivity.this.mBtnPTT.setText(publishAudio ? R.string.push_to_silence : R.string.push_to_talk);
                                GroupComListActivity.this.mBtnPTT.setVisibility(0);
                            }
                            GroupComListActivity.this.getListView().setEnabled(true);
                            break;
                        case 2:
                            GroupComListActivity.this.getListView().setEnabled(false);
                            break;
                        case 3:
                            GroupComListActivity.this.getListView().setEnabled(true);
                            GroupComListActivity.this.mBtnPTT.setVisibility(4);
                            break;
                    }
                }
            }
        });
    }

    @Override // com.kopin.groupcom.GroupCom.GroupComListener
    public void onGroupComError(GroupCom.GroupComError error) {
    }

    @Override // com.kopin.groupcom.GroupCom.GroupComListener
    public void onSessionStateChange(ChatGroup chatGroup) {
        updateSessionState(chatGroup);
    }

    @Override // com.kopin.groupcom.GroupCom.GroupComListener
    public void onSessionTimerTick(ChatGroup chatGroup) {
        updateSessionState(chatGroup);
    }

    @Override // com.kopin.groupcom.GroupCom.GroupComListener
    public void onSubscriberConnected(String userName, int count) {
        runOnUiThread(new Runnable() { // from class: com.kopin.groupcom.GroupComListActivity.2
            @Override // java.lang.Runnable
            public void run() {
                GroupComListActivity.this.mChatGroupListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override // com.kopin.groupcom.GroupCom.GroupComListener
    public void onSubscriberDropped(String userName, int count) {
        runOnUiThread(new Runnable() { // from class: com.kopin.groupcom.GroupComListActivity.3
            @Override // java.lang.Runnable
            public void run() {
                GroupComListActivity.this.mChatGroupListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override // com.kopin.groupcom.GroupCom.GroupComListener
    public void onChatGroupsChanged(final List<ChatGroup> chatGroups) {
        runOnUiThread(new Runnable() { // from class: com.kopin.groupcom.GroupComListActivity.4
            @Override // java.lang.Runnable
            public void run() {
                if (chatGroups.isEmpty()) {
                    GroupComListActivity.this.updateNoDataView();
                }
                GroupComListActivity.this.mProgressBar.setVisibility(8);
                GroupComListActivity.this.mChatGroups = chatGroups;
                GroupComListActivity.this.mChatGroupListAdapter.notifyDataSetChanged();
            }
        });
    }

    private class ChatGroupListAdapter extends ArrayAdapter<ChatGroup> {
        public ChatGroupListAdapter(Context context) {
            super(context, 0);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public int getCount() {
            return GroupComListActivity.this.mChatGroups.size();
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public ChatGroup getItem(int position) {
            return (ChatGroup) GroupComListActivity.this.mChatGroups.get(position);
        }

        public void notifySessionStateChanged(ChatGroup group) {
            notifyDataSetChanged();
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.group_list_item, null);
            }
            ChatGroup group = getItem(position);
            TextView textName = (TextView) convertView.findViewById(android.R.id.text1);
            textName.setText(group.GroupName);
            TextView textStatus = (TextView) convertView.findViewById(android.R.id.text2);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageConnectionState);
            TextView textChatTime = (TextView) convertView.findViewById(R.id.textChatTime);
            switch (group.sessionState) {
                case CONNECTED:
                    String message = MessageFormat.format(GroupComListActivity.this.getString(R.string.members_in_session), Integer.valueOf(GroupCom.getOtherParticipantCount() + 1));
                    textStatus.setTextColor(GroupComListActivity.this.getResources().getColor(R.color.sessionConnected));
                    textStatus.setText(message);
                    imageView.setBackgroundResource(R.drawable.group_call_3_ic);
                    imageView.getBackground().setColorFilter(GroupComListActivity.this.getResources().getColor(R.color.sessionConnected), PorterDuff.Mode.SRC_ATOP);
                    textChatTime.setText(Utility.formatTime(group.sessionTime));
                    return convertView;
                case CONNECTING:
                case RECONNECTING:
                    imageView.setBackgroundResource(R.drawable.anim_connecting);
                    imageView.getBackground().setColorFilter(GroupComListActivity.this.getResources().getColor(R.color.sessionConnecting), PorterDuff.Mode.SRC_ATOP);
                    textChatTime.setText("");
                    textStatus.setTextColor(GroupComListActivity.this.getResources().getColor(R.color.sessionConnecting));
                    textStatus.setText(group.sessionState == ChatGroup.SessionState.CONNECTING ? R.string.session_connecting : R.string.session_reconnecting);
                    AnimationDrawable drawable = (AnimationDrawable) imageView.getBackground();
                    if (!drawable.isRunning()) {
                        drawable.start();
                    }
                    return convertView;
                case DISCONNECTED:
                    imageView.setBackgroundResource(R.drawable.anim_disconnecting);
                    imageView.getBackground().setColorFilter(GroupComListActivity.this.getResources().getColor(R.color.sessionDisconnected), PorterDuff.Mode.SRC_ATOP);
                    AnimationDrawable drawable1 = (AnimationDrawable) imageView.getBackground();
                    if (!drawable1.isRunning()) {
                        drawable1.start();
                    }
                    textStatus.setText("");
                    textChatTime.setText("");
                    return convertView;
                default:
                    textChatTime.setText("");
                    textStatus.setText("");
                    imageView.setBackground(null);
                    return convertView;
            }
        }
    }

    private void openGroupManagementPage() {
        Intent intent = new Intent(this, (Class<?>) TermsActivity.class);
        intent.putExtra(TermsActivity.PAGE_TITLE_RESOURCE, R.string.manage_groups);
        intent.putExtra(TermsActivity.LOAD_URL, GroupComAppServerHelper.getGroupManagementUrl());
        intent.putExtra(TermsActivity.HTTP_HEADERS, GroupComAppServerHelper.getHttpHeaders());
        startActivity(intent);
    }
}

package com.youxing.duola.chat;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.chat.views.GroupNoticeView;

import java.util.Locale;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Jun Deng on 16/1/21.
 */
public class ConversationActivity extends SGActivity {

    private static final int MENU_ID_NOTICE = 1;
    private static final int MENU_ID_MEMBER = 2;

    /**
     * 目标 Id
     */
    private String mTargetId;

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Intent intent = getIntent();

        getIntentDate(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mConversationType == Conversation.ConversationType.GROUP) {
            MenuItem item1 = menu.add(1, MENU_ID_NOTICE, 0, "公告");
            item1.setIcon(R.drawable.ic_action_notice);
            item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            MenuItem item2 = menu.add(1, MENU_ID_MEMBER, 1, "成员");
            item2.setIcon(R.drawable.ic_action_group);
            item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_ID_NOTICE) {
            AlertDialog dialog = new AlertDialog.Builder(ConversationActivity.this).create();
            dialog.show();
            GroupNoticeView noticeView = GroupNoticeView.create(ConversationActivity.this);
            noticeView.setData(RongCloudEvent.instance().getGroupCache().get(mTargetId));
            dialog.getWindow().setContentView(noticeView);
            return true;

        } else if (item.getItemId() == MENU_ID_MEMBER) {
            startActivity("duola://groupmember?id=" + mTargetId);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {

        mTargetId = intent.getData().getQueryParameter("targetId");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        String title = intent.getData().getQueryParameter("title");
        setTitle(title);

        enterFragment(mConversationType, mTargetId);
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType 会话类型
     * @param mTargetId 目标 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }

}

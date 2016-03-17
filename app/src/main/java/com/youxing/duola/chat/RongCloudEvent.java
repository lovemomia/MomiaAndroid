package com.youxing.duola.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.duola.model.IMGroup;
import com.youxing.duola.model.IMGroupModel;
import com.youxing.duola.model.IMUserModel;
import com.youxing.duola.model.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.LocationInputProvider;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;

/**
 * 融云SDK事件监听
 * <p/>
 * Created by Jun Deng on 16/1/22.
 */
public class RongCloudEvent implements RongIM.UserInfoProvider, RongIM.GroupInfoProvider, RongIM.ConversationBehaviorListener {

    private static RongCloudEvent instance;

    public static RongCloudEvent instance() {
        if (instance == null) {
            synchronized (RongCloudEvent.class) {
                if (instance == null) {
                    instance = new RongCloudEvent();
                }
            }
        }
        return instance;
    }

    public static void init() {
        if (instance == null) {
            synchronized (RongCloudEvent.class) {
                if (instance == null) {
                    instance = new RongCloudEvent();
                }
            }
        }
    }

    public RongCloudEvent() {
        RongIM.setUserInfoProvider(this, true);//设置用户信息提供者。
        RongIM.setGroupInfoProvider(this, true);//设置群组信息提供者。
        RongIM.setConversationBehaviorListener(this);

        //扩展功能自定义
        InputProvider.ExtendProvider[] provider = {
                new ImageInputProvider(RongContext.getInstance()),//图片
                new CameraInputProvider(RongContext.getInstance()),//相机
//                new LocationInputProvider(RongContext.getInstance()),//地理位置
        };
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.GROUP, provider);
    }

    private Map<String, User> userCache = new HashMap<>();
    private Map<String, IMGroup> groupCache = new HashMap<>();

    public Map<String, User> getUserCache() {
        return userCache;
    }

    public Map<String, IMGroup> getGroupCache() {
        return groupCache;
    }

    @Override

    public Group getGroupInfo(final String groupId) {
        IMGroup group = groupCache.get(groupId);
        if (group != null) {
            Group rcGroup = new Group(String.valueOf(group.getGroupId()), group.getGroupName(), Uri.parse(""));
            return rcGroup;

        } else {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", groupId));
            HttpService.get(Constants.domain() + "/im/group", params, CacheType.DISABLE, IMGroupModel.class, new RequestHandler() {
                @Override
                public void onRequestFinish(Object response) {
                    IMGroupModel model = (IMGroupModel) response;

                    Group rcGroup = new Group(String.valueOf(model.getData().getGroupId()), model.getData().getGroupName(), Uri.parse(""));
                    RongIM.getInstance().refreshGroupInfoCache(rcGroup);

                    groupCache.put(groupId, model.getData());
                }

                @Override
                public void onRequestFailed(BaseModel error) {
                }
            });
        }
        return null;
    }

    @Override
    public UserInfo getUserInfo(final String userId) {
        User user = userCache.get(userId);
        if (user != null) {
            UserInfo rcUser = new UserInfo(String.valueOf(user.getId()), user.getNickName(), Uri.parse(user.getAvatar()));
            return rcUser;

        } else {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("uid", userId));
            HttpService.get(Constants.domain() + "/im/user", params, CacheType.DISABLE, IMUserModel.class, new RequestHandler() {
                @Override
                public void onRequestFinish(Object response) {
                    IMUserModel model = (IMUserModel) response;

                    UserInfo rcUser = new UserInfo(String.valueOf(model.getData().getId()), model.getData().getNickName(), Uri.parse(model.getData().getAvatar()));
                    RongIM.getInstance().refreshUserInfoCache(rcUser);

                    userCache.put(userId, model.getData());
                }

                @Override
                public void onRequestFailed(BaseModel error) {
                }
            });
        }
        return null;
    }

    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        if (message.getContent() instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message.getContent();
            if (!TextUtils.isEmpty(textMessage.getExtra()) && textMessage.getExtra().startsWith("duola")) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(textMessage.getExtra())));
            }

        } else if (message.getContent() instanceof ImageMessage) {
            ImageMessage imageMessage = (ImageMessage) message.getContent();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("duola://photo"));
            intent.putExtra("photo", imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri());
            if (imageMessage.getThumUri() != null)
                intent.putExtra("thumbnail", imageMessage.getThumUri());

            context.startActivity(intent);
        }
        return false;
    }

    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }
}

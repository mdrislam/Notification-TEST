package com.mristudio.notification_fcm.service;

import androidx.annotation.NonNull;

import com.mristudio.notification_fcm.utils.NoficationHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessageingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        String body = remoteMessage.getNotification().getBody();
//        String title = remoteMessage.getNotification().getTitle();
//        String image = remoteMessage.getNotification().getImageUrl().toString();
//        String clickAction = remoteMessage.getNotification().getClickAction();



        String key1 = remoteMessage.getData().get("key1");
        String key2 = remoteMessage.getData().get("key2");

        String body = remoteMessage.getData().get("body");
        String title = remoteMessage.getData().get("title");
        String image = remoteMessage.getData().get("image");
        String text = remoteMessage.getData().get("text");
        String clickAction = remoteMessage.getData().get("click_action");


        NoficationHelper helper = new NoficationHelper(this);
        //helper.tiggerNotification(title,body);
        //helper.tiggerNotification(title,body,image);
        //helper.tiggerNotification(title,body,image,clickAction);
        helper.tiggerNotification(title,body,image,clickAction,key1,key2,text);

    }

}

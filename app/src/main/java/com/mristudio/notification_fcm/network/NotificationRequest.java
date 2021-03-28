package com.mristudio.notification_fcm.network;

import com.mristudio.notification_fcm.model.NotificationReq;
import com.mristudio.notification_fcm.model.NotificationResponce;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationRequest {

    @Headers({"Content-Type:application/json","Authorization:key=AAAAkNqQa8U:APA91bHBmumUwpjtVKnUAHu2kLaItfV8qeSMZdTfTBsNZppCezNjTnXdCZvhmLVemrgs1kjbpboRcM94mDGu4oIioMXcmDdyE7LDYwxH5xOKSYqkKiokUiWLURWgtgyXHvztJwE4TVow"})
    @POST("send")
    Call<NotificationResponce> sent(@Body NotificationReq req);
}

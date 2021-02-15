package com.example.namaste.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
      "Content-Type:application/json",
        "Authorization:key=AAAAzYEdgxM:APA91bHTczxs_8RjO4Uk65LKXP0XpOS2HtmzXlRqaU7LMB8Y2qWkje7s_XvtmAUV9lXPrx01Wh3V8yXz0HvUG3L15EdGyPScrUeJA31Gfztd4z738GDg-Qme8XscqLTKOQIjIaJOVNTA"
    })


    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);


}

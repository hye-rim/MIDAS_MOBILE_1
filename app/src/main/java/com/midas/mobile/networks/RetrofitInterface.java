package com.midas.mobile.networks;

import com.google.gson.JsonObject;
import com.midas.mobile.networks.model.FCMResponse;
import com.midas.mobile.entity.MenuData;
import com.midas.mobile.entity.UserData;
import com.midas.mobile.networks.model.PurchaseResponse;
import com.midas.mobile.networks.model.ReservationResponse;
import com.midas.mobile.service_customer.model.OrderRequestBody;
import com.midas.mobile.networks.model.CustomerListResponse;
import com.midas.mobile.networks.model.MenuListResponse;
import com.midas.mobile.utils.DefaultResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import static com.midas.mobile.entity.UserData.USER;
import static com.midas.mobile.networks.RetrofitManager.FCM;
import static com.midas.mobile.networks.RetrofitManager.LOGIN;
import static com.midas.mobile.networks.RetrofitManager.MENU;
import static com.midas.mobile.networks.RetrofitManager.PURCHASEHISTORYADMIN;
import static com.midas.mobile.networks.RetrofitManager.RESERVATION;
import static com.midas.mobile.networks.RetrofitManager.TEST;
import static com.midas.mobile.networks.RetrofitManager.USER_DETAIL;


public interface RetrofitInterface {
    @GET(TEST)
    Observable<DefaultResponse> getTest(@Header("x-access-token") String token);

    @POST(LOGIN)
    Observable<DefaultResponse> getToken(@Body UserData userData);

    @GET(USER)
    Observable<CustomerListResponse> getUser(@Header("x-access-token") String token);

    @POST(USER)
    Observable<DefaultResponse> postUser(@Body UserData userData);

    @PUT(USER + "/{email}")
    Observable<DefaultResponse> editUser(@Header("x-access-token") String token, @Path("email") String email, @Body JsonObject jsonObject);

    @DELETE(USER + "/{email}")
    Observable<DefaultResponse> deleteUser(@Header("x-access-token") String token, @Path("email") String email);

    @GET(MENU)
    Observable<MenuListResponse> getMenu(@Header("x-access-token") String token);

    @POST(MENU)
    Observable<DefaultResponse> postMenu(@Header("x-access-token") String token, @Body MenuData menuData);

    @PUT(MENU + "/{menuName}")
    Observable<DefaultResponse> putMenu(@Header("x-access-token") String token, @Path("menuName") String menuName, @Body MenuData menuData);

    @DELETE(MENU + "/{menuName}")
    Observable<DefaultResponse> deleteMenu(@Header("x-access-token") String token, @Path("menuName") String menuName);

    @POST(RESERVATION)
    Observable<DefaultResponse> postReservation(@Header("x-access-token") String token,  @Body OrderRequestBody body);

    @GET(RESERVATION)
    Observable<ReservationResponse> getReservationList(@Header("x-access-token") String token);

    @PUT(RESERVATION + "/{reservationId}")
    Observable<DefaultResponse> setReservationStatus(@Header("x-access-token") String token, @Path("reservationId") int reservationId, @Body RequestBody params);

    @PUT(FCM)
    Observable<DefaultResponse> putFCMToken(@Header("x-access-token") String token, @Body FCMResponse body);

    @GET(PURCHASEHISTORYADMIN)
    Observable<PurchaseResponse> getPurchaseHistoryAdminList(@Header("x-access-token") String token);

    @GET(USER_DETAIL)
    Observable<DefaultResponse> getUserDetail(@Header("x-access-token") String token);

}

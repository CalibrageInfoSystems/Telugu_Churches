package in.calibrage.teluguchurches.services;


import com.google.gson.JsonObject;

import in.calibrage.teluguchurches.views.model.AddUpdateNotificationsResponeModel;
import in.calibrage.teluguchurches.views.model.AddressResponseModel;
import in.calibrage.teluguchurches.views.model.AllItemsResponseModel;
import in.calibrage.teluguchurches.views.model.BibleReadModel;
import in.calibrage.teluguchurches.views.model.CartResponseModel;
import in.calibrage.teluguchurches.views.model.ChangePasswordResponseModel;
import in.calibrage.teluguchurches.views.model.ChapterReadModel;
import in.calibrage.teluguchurches.views.model.ChurchesSubscribeResponseModel;
import in.calibrage.teluguchurches.views.model.CommentDeleteResponseModel;
import in.calibrage.teluguchurches.views.model.CommentResponseModel;
import in.calibrage.teluguchurches.views.model.EditProfileResponseModel;
import in.calibrage.teluguchurches.views.model.EventCommentDeleteResponseModel;
import in.calibrage.teluguchurches.views.model.EventViewAllRepliesResponseModel;
import in.calibrage.teluguchurches.views.model.EventsCommentResponseModel;
import in.calibrage.teluguchurches.views.model.GetAddUpdateApplicantResponseModel;
import in.calibrage.teluguchurches.views.model.GetAllAdminResponseModel;
import in.calibrage.teluguchurches.views.model.GetAllBannersId;
import in.calibrage.teluguchurches.views.model.GetAllChurchesResponseModel;
import in.calibrage.teluguchurches.views.model.GetAllEventsResponeModel;
import in.calibrage.teluguchurches.views.model.GetAllJobDetailsResponseModel;
import in.calibrage.teluguchurches.views.model.GetAuthorEventsResponeModel;
import in.calibrage.teluguchurches.views.model.GetAuthorsResponse;
import in.calibrage.teluguchurches.views.model.GetCategory;
import in.calibrage.teluguchurches.views.model.GetChurchResponse;
import in.calibrage.teluguchurches.views.model.GetContactDetails;
import in.calibrage.teluguchurches.views.model.GetEventByDateAndChurchIdResponseModel;
import in.calibrage.teluguchurches.views.model.GetEventByDateAndUserIdResponseModel;
import in.calibrage.teluguchurches.views.model.GetEventDetailsInfoByChurchIdMonthYear;
import in.calibrage.teluguchurches.views.model.GetEventInFoByUserIdMonthYear;
import in.calibrage.teluguchurches.views.model.GetEventsForTodayResponseModel;
import in.calibrage.teluguchurches.views.model.GetJobResponseModel;
import in.calibrage.teluguchurches.views.model.GetLoginPageResponseModel;
import in.calibrage.teluguchurches.views.model.GetNotificationResponseModel;
import in.calibrage.teluguchurches.views.model.GetPostByCategoryIdResponseModel;
import in.calibrage.teluguchurches.views.model.GetPostByIdResponseModel;
import in.calibrage.teluguchurches.views.model.GetRefreshTokenResponseModel;
import in.calibrage.teluguchurches.views.model.GetResponceEvents;
import in.calibrage.teluguchurches.views.model.GetSplashMessage;
import in.calibrage.teluguchurches.views.model.GetUpComingEvents;
import in.calibrage.teluguchurches.views.model.GetUpComingEventsModel;
import in.calibrage.teluguchurches.views.model.GetUpdateViewCountByPostIdResponseModel;
import in.calibrage.teluguchurches.views.model.GetUserInfoResponseModel;
import in.calibrage.teluguchurches.views.model.GetUserManualPdfResponeModel;
import in.calibrage.teluguchurches.views.model.LikeDislikeResponeModel;
import in.calibrage.teluguchurches.views.model.LikeResponseModel;
import in.calibrage.teluguchurches.views.model.NotificationResponseModel;
import in.calibrage.teluguchurches.views.model.PostImageResponseModel;
import in.calibrage.teluguchurches.views.model.StateResponseModel;
import in.calibrage.teluguchurches.views.model.SubscribeResponseModel;
import in.calibrage.teluguchurches.views.model.UserRegisterResponseModel;
import in.calibrage.teluguchurches.views.model.ViewAllRepliesResponseModel;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;


public interface ChurchService {


    //Sign up
    @POST(APIConstants.SIGN_UP)
    Observable<UserRegisterResponseModel> UpdateUserInfo(@Body JsonObject data);

    //change Password
    @POST(APIConstants.CHANGE_PASSWORD)
    Observable<ChangePasswordResponseModel> ChangePassword(@Body JsonObject data);

    //EDIT Profile
    @POST(APIConstants.EDIT_PROFILE)
    Observable<EditProfileResponseModel> EditProfile(@Body JsonObject data, @Header("Authorization") String Authorization);

    //GET Profile Information
    @GET
    Observable<GetUserInfoResponseModel> getUserInfo(@Url String url);

    //Splash Message
    @GET
    Observable<GetSplashMessage> getSplashMessage(@Url String url);

    // Login
    @POST(APIConstants.LOGINPage)
    Observable<GetLoginPageResponseModel> getLoginPage(@Body JsonObject data);

    // RefreshToken
    @POST(APIConstants.RefreshToken)
    Observable<GetRefreshTokenResponseModel> getRefreshToken(@Body JsonObject data);

    // Churche's list
    @POST(APIConstants.GetAllChurches)
    Observable<GetAllChurchesResponseModel> getAllChurches(@Body JsonObject data);


    // Pastor's List
    @POST(APIConstants.GetAllChurchAdmins)
    Observable<GetAllAdminResponseModel> getAllAdmins(@Body JsonObject data);

    // To subscribe church/pastor
    @POST(APIConstants.GetSubscribe)
    Observable<SubscribeResponseModel> getSubscribe(@Body JsonObject data, @Header("Authorization") String Authorization);

    // To subscribe church/pastor
    @POST(APIConstants.GetSubscribe)
    Observable<ChurchesSubscribeResponseModel> getchurchSubscribe(@Body JsonObject data, @Header("Authorization") String Authorization);


    // Particular Pastor Evnets
    @GET
    Observable<GetAuthorEventsResponeModel> GetAuthorEventByUserIdMonthYear(@Url String url);

    // Particular Church Evnets
    @GET
    Observable<GetEventsForTodayResponseModel> GetEventByChurchIdMonthYear(@Url String url);

    // Particular Church Evnets List
    @GET
    Observable<GetEventDetailsInfoByChurchIdMonthYear> GetEventDetailsInfoByChurchIdMonthYear(@Url String url);


    // events list
    @POST(APIConstants.GetEventInfoByMonthYear)
    Observable<GetAllEventsResponeModel> GetEventInfoByMonthYear(@Body JsonObject data);

    // Particular Pastor Evnet details on calendar view
    // particular events details for required date
    @GET
    Observable<GetEventByDateAndChurchIdResponseModel> GetEventDateAndChurchId(@Url String url);

    // particular church information by churchID
    @GET
    Observable<GetChurchResponse> GetChurchById(@Url String url);

    // Upcoming Events list
    @POST(APIConstants.GetUpcomingEventsInfo)
    Observable<GetUpComingEvents> GetUpcomingEventsInfo(@Body JsonObject data);

    // Get Banners
    @GET
    Observable<GetAllBannersId> GetAllBannerId(@Url String url);

    // particular pastor information by pastorID
    @GET
    Observable<GetAuthorsResponse> GetAuthorId(@Url String url);


    // Particular Pastor Evnets List
    @GET
    Observable<GetEventInFoByUserIdMonthYear> GetEventInFoByUserIdMonthYear(@Url String url);

    // Categories List
    @POST(APIConstants.GetCategoryInfo)
    Observable<GetCategory> GetCategoryInfo(@Body JsonObject data);

    // Like or Disk-like all kind's of posts
    @POST(APIConstants.LikeOrDislikePost)
    Observable<LikeResponseModel> likeOrDislikePost(@Body JsonObject data, @Header("Authorization") String Authorization);

    // add comments
    @POST(APIConstants.AddUpdateComments)
    Observable<CommentResponseModel> AddUpdateComments(@Body JsonObject data, @Header("Authorization") String Authorization);

    // delete commnets
    @POST(APIConstants.DeleteComments)
    Observable<CommentDeleteResponseModel> DeleteComments(@Body JsonObject data);

    // delete events commnets
    @POST(APIConstants.DeleteEventComment)
    Observable<EventCommentDeleteResponseModel> DeleteEventComment(@Body JsonObject data);

    // Like or Disk-like all kind's of events posts
    @POST(APIConstants.LikeOrDislikePostEvent)
    Observable<LikeDislikeResponeModel> likeOrDislikePostEvents(@Body JsonObject data, @Header("Authorization") String Authorization);

    //  add events comments
    @POST(APIConstants.EventAddUpdateComments)
    Observable<EventsCommentResponseModel> EventAddUpdateComments(@Body JsonObject data, @Header("Authorization") String Authorization);

    // Particular Category posts
    // particular church events post
    @GET
    Observable<GetPostByCategoryIdResponseModel> GetPostbyCategoryId(@Url String url);

    // get All kind's of posts
    @GET
    Observable<GetPostByIdResponseModel> GetPostById(@Url String url);

    // get the View's count
    @GET
    Observable<GetUpdateViewCountByPostIdResponseModel> GetUpdateViewCountByPostId(@Url String url);

    // particular event details by eventID
    @GET
    Observable<GetUpComingEventsModel> GetUpComingEventsId(@Url String url);

    // For Bible chapters telugu
    // For Bible chapters english
    @GET
    Observable<BibleReadModel> GetBibleChapter(@Url String url);

    @GET
    Observable<ChapterReadModel> GetChapter(@Url String url);

    // View all replies
    @GET
    Observable<ViewAllRepliesResponseModel> GetPostCommentReplies(@Url String url, @Header("Authorization") String Authorization);

    // View all replies for events
    @GET
    Observable<EventViewAllRepliesResponseModel> GetEventCommentReplies(@Url String url, @Header("Authorization") String Authorization);

    // particular pastor posts
    @POST(APIConstants.GetPostByAuthorId)
    Observable<PostImageResponseModel> GetPostByAuthorId(@Body JsonObject data);

    // particular church posts
    @POST(APIConstants.GetPostByChurchId)
    Observable<PostImageResponseModel> GetPostByChurchId(@Body JsonObject data);

    //forgot Password
    @POST(APIConstants.ForgotPassword)
    Observable<ChangePasswordResponseModel> ForgotPassword(@Body JsonObject data);

    // careers list
    @POST(APIConstants.GetAllJobDetails)
    Observable<GetAllJobDetailsResponseModel> getAllJobDetails(@Body JsonObject data, @Header("Authorization") String Authorization);

    // shopping items list
    @POST(APIConstants.GetAllItems)
    Observable<AllItemsResponseModel> getAllItems(@Body JsonObject data, @Header("Authorization") String Authorization);

    // adding item to cart
    @POST(APIConstants.AddToCart)
    Observable<CartResponseModel> AddToCart(@Body JsonObject data, @Header("Authorization") String Authorization);

    // particular shopping item details
    @GET
    Observable<AllItemsResponseModel> getItemById(@Url String url);

    // particular career details
    @GET
    Observable<GetJobResponseModel> GetJobById(@Url String url);

    // added cart items count
    // cart items list
    @GET
    Observable<CartResponseModel> CartItemsCount(@Url String url, @Header("Authorization") String Authorization);

    // Apply for job
    @POST(APIConstants.AddUpdateApplicant)
    Observable<GetAddUpdateApplicantResponseModel> getAddUpdateApplicant(@Body JsonObject data, @Header("Authorization") String Authorization);

    // delete item from cart
    @GET
    Observable<CartResponseModel> DeleteFromCart(@Url String url);

    // states list for Address
    @GET
    Observable<StateResponseModel> getAllStates(@Url String url);

    // countries list for Address
    @GET
    Observable<StateResponseModel> getAllCountries(@Url String url);

    //particular adderess details
    @GET
    Observable<AddressResponseModel> getAddressDeliveryById(@Url String url);

    // Add/update delivery address of online shopping
    @POST(APIConstants.AddUpdateDeliveryAddress)
    Observable<UserRegisterResponseModel> addUpdateDeliveryAddress(@Body JsonObject data, @Header("Authorization") String Authorization);

    // added addresses list
    @POST(APIConstants.GetAllDeliveryAddress)
    Observable<AddressResponseModel> getAllDeliveryAddress(@Body JsonObject data);

    // delete address
    @POST(APIConstants.DeleteDeliveryAddress)
    Observable<UserRegisterResponseModel> deleteDeliveryAddress(@Body JsonObject data);


    //Notification list
    @POST(APIConstants.GetWebNotification)
    Observable<GetNotificationResponseModel> WebNotification(@Body JsonObject data, @Header("Authorization") String Authorization);

    // read notifications list
    @POST(APIConstants.GetReadNotification)
    Observable<GetNotificationResponseModel> GetReadNotification(@Body JsonObject data, @Header("Authorization") String Authorization);

    // Unread notifications list
    @POST(APIConstants.GetUnReadNotification)
    Observable<GetNotificationResponseModel> GetUnReadNotification(@Body JsonObject data, @Header("Authorization") String Authorization);

    // update online shopping item quantity in cart
    @POST(APIConstants.UpdateToCart)
    Observable<CartResponseModel> UpdateToCart(@Body JsonObject data, @Header("Authorization") String Authorization);

    // changing notification status from new to read
    @POST(APIConstants.AddUpdateNotifications)
    Observable<AddUpdateNotificationsResponeModel> getAddUpdateNotifications(@Body JsonObject data);

    // User manual
    @GET
    Observable<GetUserManualPdfResponeModel> getUserManualPdf(@Url String url);

    // contact US
    @GET
    Observable<GetContactDetails> getContactus(@Url String url);
}

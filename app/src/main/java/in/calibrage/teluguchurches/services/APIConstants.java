package in.calibrage.teluguchurches.services;

public interface APIConstants {

    //Global Base URL
    // String BASE_URL = "http://183.82.111.111/TeluguChurches/API/api/";
    String BASE_URL = "http://183.82.111.111/TChurchesLive/API/api/";

    //Login
    String LOGINPage = BASE_URL + "Account/Login";

    //Sign up
    String SIGN_UP = BASE_URL + "Account/Register";

    //forgot Password
    String ForgotPassword = BASE_URL + "UserInfo/ForgotPassword";

    //change Password
    String CHANGE_PASSWORD = BASE_URL + "Account/ChangePassword";

    //EDIT Profile
    String EDIT_PROFILE = BASE_URL + "UserInfo/UpdateUserInfo";

    //GET Profile Information
    String Get_UserInfo = BASE_URL + "UserInfo/GetUserInfo/";

    //Splash Message
    String Get_Splash_message = BASE_URL + "SplashMessage/GetSplashMessageByDate";

    // Churche's list
    String GetAllChurches = BASE_URL + "Church/GetAllActiveChurches";

    // Pastor's List
    String GetAllChurchAdmins = BASE_URL + "Church/GetAllChurchAdmins";

    // Particular Pastor Evnets
    String GetEventByUserIdMonthYear = BASE_URL + "Events/GetEventByUserIdMonthYear/";

    // Particular Church Evnets
    String GetEventByChurchIdMonthYear = BASE_URL + "Events/GetEventByChurchIdMonthYear/";

    // Particular Church Evnets List
    String GetEventDetailsInfoByChurchIdMonthYear = BASE_URL + "Events/GetEventDetailsInfoByChurchIdMonthYear/";

    // Particular Pastor Evnet details on calendar view
    String GetEventByDateAndUserId = BASE_URL + "Events/GetEventByDateAndUserId/";

    // particular church information by churchID
    String GetChurchbyId = BASE_URL + "Church/GetChurchbyId/";

    // Upcoming Events list
    String GetUpcomingEventsInfo = BASE_URL + "Events/GetUpcomingEventsInfo";

    // Get Banners
    String GetAllBannersById = BASE_URL + "Banners/GetAllBannersById/null";

    // particular pastor information by pastorID
    String GetAuthorId = BASE_URL + "Church/GetAuthorbyId/";

    // Particular Pastor Evnets List
    String GetEventInfoByUserIdMonthYear = BASE_URL + "Events/GetEventInfoDetailsByUserIdMonthYear/";

    // Categories List
    String GetCategoryInfo = BASE_URL + "Category/GetAllCategories";

    // Particular Category posts
    String GetPostbyCategoryId = BASE_URL + "Post/GetPostbyCategoryId/";

    // particular church events post
    String GetPostbyEventId = BASE_URL + "Post/GetPostbyEventId/";

    // particular event details by eventID
    String GetUpComingEventsId = BASE_URL + "Events/GetEventByEventId/";

    // To subscribe church/pastor
    String GetSubscribe = BASE_URL + "Church/ChurchAuthorSubscription";

    // For Bible chapters telugu
    String GetBibleChapter = BASE_URL + "BibleChapter/GetBibleChapter/12";

    // For Bible chapters english
    String GetBibleChapterEnglish = BASE_URL + "BibleChapter/GetBibleChapter/11";

    // Like or Disk-like all kind's of posts
    String LikeOrDislikePost = BASE_URL + "Post/LikeOrDislikePost";

    // Like or Disk-like all kind's of events posts
    String LikeOrDislikePostEvent = BASE_URL + "Events/EventLikeOrDisLike";

    // get All kind's of posts
    String GetPostById = BASE_URL + "Post/GetPostByPostId/";

    // get the View's count
    String UpdateViewCountByPostId = BASE_URL + "Post/UpdateViewCountByPostId/";

    // add comments
    String AddUpdateComments = BASE_URL + "Post/AddUpdateComments";

    // delete commnets
    String DeleteComments = BASE_URL + "Post/DeleteComments";

    // delete events commnets
    String DeleteEventComment = BASE_URL + "Events/DeleteEventComment";

    //  add events comments
    String EventAddUpdateComments = BASE_URL + "Events/AddUpdateEventComments";

    // particular events details for required date
    String GetEventByDateAndChurchId = BASE_URL + "Events/GetEventByDateAndChurchId";

    // View all replies
    String GetPostCommentReplies = BASE_URL + "Post/GetPostCommentReplies/";

    // View all replies for events
    String GetEventCommentReplies = BASE_URL + "Events/GetEventCommentReplies/";

    // particular pastor posts
    String GetPostByAuthorId = BASE_URL + "Post/GetPostByAuthorId";

    // particular church posts
    String GetPostByChurchId = BASE_URL + "Post/GetPostByChurchId";

    // careers list
    String GetAllJobDetails = BASE_URL + "JobDetails/GetAllJobDetails";

    // particular career details
    String GetJobById = BASE_URL + "JobDetails/GetJobById/";

    // events list
    String GetEventInfoByMonthYear = BASE_URL + "Events/GetEventInfoByMonthYear";

    // shopping items list
    String GetAllItems = BASE_URL + "ItemDetails/GetAllItems";

    // particular shopping item details
    String GetItemById = BASE_URL + "ItemDetails/GetItemById/";

    // adding item to cart
    String AddToCart = BASE_URL + "CartInfo/AddToCart";

    // added cart items count
    String CartItemsCount = BASE_URL + "CartInfo/CartItemsCount/";

    // cart items list
    String GetCartInfo = BASE_URL + "CartInfo/GetCartInfo/";

    // Apply for job
    String AddUpdateApplicant = BASE_URL + "ApplicantInfo/AddUpdateApplicant";

    // delete item from cart
    String DeleteFromCart = BASE_URL + "CartInfo/DeleteFromCart/";

    // update online shopping item quantity in cart
    String UpdateToCart = BASE_URL + "CartInfo/UpdateToCart";

    //Notification list
    String GetWebNotification = BASE_URL + "Notification/GetWebNotification";

    // changing notification status from new to read
    String AddUpdateNotifications = BASE_URL + "Notification/AddUpdateNotifications";

    // read notifications list
    String GetReadNotification = BASE_URL + "Notification/GetReadNotification";

    // Unread notifications list
    String GetUnReadNotification = BASE_URL + "Notification/GetUnReadNotification";

    // states list for Address
    String GetAllStates = BASE_URL + "States/GetAllStates";

    // countries list for Address
    String GetAllCountries = BASE_URL + "Countries/GetAllCountries";

    // Add/update delivery address of online shopping
    String AddUpdateDeliveryAddress = BASE_URL + "DeliveryAddress/AddUpdateDeliveryAddress";

    // added addresses list
    String GetAllDeliveryAddress = BASE_URL + "DeliveryAddress/GetAllDeliveryAddress";

    //particular adderess details
    String GetDeliveryAddressById = BASE_URL + "DeliveryAddress/GetAddressById/";

    // delete address
    String DeleteDeliveryAddress = BASE_URL + "DeliveryAddress/DeleteDeliveryAddress";

    // RefreshToken
    String RefreshToken = BASE_URL + "Account/RefreshToken";

    // User manual
    String GetUserManual = BASE_URL + "UserManuals/GetUserManual/";

    // contact US
    String GetContactDetails = BASE_URL + "UserInfoo/GetContactDetails";

}

package com.thelawhouse.Utils;

import com.thelawhouse.Model.AddAppointmentModel;
import com.thelawhouse.Model.AddSegementLawModel;
import com.thelawhouse.Model.AddUsefullLinkModel;
import com.thelawhouse.Model.AppointmentListModel;
import com.thelawhouse.Model.BroadcastMessageModel;
import com.thelawhouse.Model.CaseListModel;
import com.thelawhouse.Model.CompletedAppointmentListModel;
import com.thelawhouse.Model.DateCountModel;
import com.thelawhouse.Model.LoginModel;
import com.thelawhouse.Model.NewCaseModel;
import com.thelawhouse.Model.NewMemberModel;
import com.thelawhouse.Model.PhonebookModel;
import com.thelawhouse.Model.RegisterModel;
import com.thelawhouse.Model.SegmentLawListModel;
import com.thelawhouse.Model.TodayAppointmentListModel;
import com.thelawhouse.Model.UsefullLinkListModel;
import com.thelawhouse.Model.VerifyModel;
import com.thelawhouse.Model.VerifyOTPModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface WebServices {

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=send_otp_login")
    Call<VerifyModel> SendOtpLogin(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=verify_otp_login")
    Call<VerifyModel> VerifyOTPLogin(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=employee_login")
    Call<LoginModel> EmployeeLogin(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=employee_forgot_password")
    Call<RegisterModel> ForgotPassword(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=employee_forgot_verify_otp")
    Call<VerifyModel> ForgotVeryOTP(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=employee_change_password")
    Call<VerifyModel> ChangePassword(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=register")
    Call<RegisterModel> Register(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=employee_register")
    Call<RegisterModel> AdminRegister(@FieldMap Map<String, String> Map);

    @POST("the_law_house_apis.php?action=get_broadcast_message")
    Call<BroadcastMessageModel> BroadCastMessage();

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=get_month_case_count")
    Call<DateCountModel> ShowCountOnMonth(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=add_appointment")
    Call<AddAppointmentModel> AddAppointment(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=get_appointment_list")
    Call<AppointmentListModel> AppointMentList(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=get_complate_appointment_list")
    Call<CompletedAppointmentListModel> CompletedAppointMentList(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=get_today_appointment_list")
    Call<TodayAppointmentListModel> TodayAppointMentList(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=change_appointment_status")
    Call<VerifyModel> ChangeAppointmentStatus(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=get_appointment_details")
    Call<TodayAppointmentListModel> getAppointmentDetails(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=update_appointment")
    Call<AddAppointmentModel> UpdateAppointment(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=get_use_full_link_list")
    Call<UsefullLinkListModel> UsefullLinkList(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=add_use_full_link")
    Call<AddUsefullLinkModel> AddUsefullLink(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=update_use_full_link")
    Call<VerifyModel> UpdateUsefullLink(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=delete_use_full_link")
    Call<VerifyModel> DeleteUsefullLink(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=get_use_full_link_details")
    Call<UsefullLinkListModel> UsefullLinkDetails(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=get_phone_book_list")
    Call<PhonebookModel> PhoneBookDetail(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=get_segment_of_law_list")
    Call<SegmentLawListModel> SegmentLawList(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=get_segment_of_law_details")
    Call<SegmentLawListModel> SegmentLawDetails(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=add_segment_fo_law")
    Call<AddSegementLawModel> AddSegemetnLaw(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=update_segment_of_law")
    Call<VerifyModel> UpdateSegementLaw(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=delete_segment_of_law")
    Call<VerifyModel> DeleteSegmentLaw(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=get_all_complete_case_list")
    Call<CaseListModel> CompletedCases(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=change_case_status")
    Call<VerifyModel> ChangeCaseStatus(@FieldMap Map<String, String> Map);


    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=verify_otp")
    Call<VerifyOTPModel> VerifyOTP(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=resend_otp")
    Call<VerifyOTPModel> ResendOtp(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=get_all_case_list")
    Call<CaseListModel> AllCaseList(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=create_new_case")
    Call<NewCaseModel> NewCase(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=get_case_details")
    Call<CaseListModel> CaseData(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=update_case")
    Call<NewCaseModel> UpdateCase(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=get_date_case_list")
    Call<CaseListModel> FindCaseByDate(@FieldMap Map<String, String> Map);


    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=create_new_member")
    Call<NewMemberModel> NewMember(@FieldMap Map<String, String> Map);

    @FormUrlEncoded
    @POST("the_law_house_apis.php?action=find_case_by_mobile")
    Call<CaseListModel> FindCaseByMobile(@FieldMap Map<String, String> Map);
}

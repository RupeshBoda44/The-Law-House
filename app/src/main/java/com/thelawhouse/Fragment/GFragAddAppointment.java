package com.thelawhouse.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.thelawhouse.Activity.MainActivity;
import com.thelawhouse.Model.AddAppointmentModel;
import com.thelawhouse.Model.TodayAppointmentListModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.FragAddAppointmentBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;

public class GFragAddAppointment extends Fragment {
    private FragAddAppointmentBinding mBinding;
    private String countryCode = "";
    private MainActivity mainActivity;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private String appointmentId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frag_add_appointment, container, false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.ClickcableTrue();
        Typeface typeFace = ResourcesCompat.getFont(getActivity(), R.font.font_regular);
        mBinding.ccpCountryCode.setTypeFace(typeFace);
        countryCode = mBinding.ccpCountryCode.getSelectedCountryCode();
        onClickListener();
        dateSelection();
        timeSelection();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            appointmentId = bundle.getString("appointmentId");
            appointmentData(appointmentId);
            mBinding.llSubmit.setVisibility(View.GONE);
            mBinding.llUpdate.setVisibility(View.VISIBLE);
            mainActivity.editAppointment();
        } else {
            mBinding.llSubmit.setVisibility(View.VISIBLE);
            mBinding.llUpdate.setVisibility(View.GONE);
            mainActivity.addAppointment();
        }
        return mBinding.getRoot();
    }

    private void appointmentData(String appointmentId) {
        if (isInternetAvailable(getActivity())) {
            ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), true, true, false, null);
            WebApiClient.getInstance().getAppointmentDetails(paramAppointmentDetail(appointmentId)).enqueue(new Callback<TodayAppointmentListModel>() {
                @Override
                public void onResponse(Call<TodayAppointmentListModel> call, Response<TodayAppointmentListModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        if (response.body().message.equalsIgnoreCase("success")) {
                            mBinding.edtFullName.setText(response.body().appointment_data.get(0).name);
                            mBinding.edtEmail.setText(response.body().appointment_data.get(0).email);
                            mBinding.edtMobileNo.setText(response.body().appointment_data.get(0).mobile);
                            mBinding.edtReason.setText(response.body().appointment_data.get(0).appointment_reason);
                            String nextDate = response.body().appointment_data.get(0).appointment_date;
                            String inputPattern = "yyyy-MM-dd";
                            String outputPattern = "dd-MM-yyyy";
                            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                            Date date;
                            String finalNectDate = null;

                            try {
                                date = inputFormat.parse(nextDate);
                                finalNectDate = outputFormat.format(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            mBinding.tvDate.setText(finalNectDate);
                            mBinding.tvTime.setText(response.body().appointment_data.get(0).appointment_time);
                            mBinding.ccpCountryCode.setCountryForPhoneCode(Integer.parseInt(response.body().appointment_data.get(0).c_code));
                        } else {
                            Utils.showDialog(getActivity(), response.body().message + "");
                        }
                    }
                }

                @Override
                public void onFailure(Call<TodayAppointmentListModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(getActivity(), "Check Your Internet.");
        }
    }

    private Map<String, String> paramAppointmentDetail(String appointmentId) {
        Map<String, String> params = new HashMap<>();
        params.put("appointment_id", appointmentId);
        return params;
    }

    private void timeSelection() {
        mBinding.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String timeSet = "";
                        int hour = selectedHour;
                        int minutes = selectedMinute;
                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12) {
                            timeSet = "PM";
                        } else {
                            timeSet = "AM";
                        }
                        String min = "";
                        if (minutes < 10)
                            min = "0" + minutes;
                        else
                            min = String.valueOf(minutes);
                        String aTime = new StringBuilder().append(hour).append(':')
                                .append(min).append(" ").append(timeSet).toString();
                        mBinding.tvTime.setText(aTime);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }

    private void dateSelection() {
        datePickerDialog();
        mBinding.tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    private void datePickerDialog() {
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mBinding.tvDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void onClickListener() {
        mBinding.ccpCountryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                countryCode = selectedCountry.getPhoneCode();
            }
        });
        mBinding.tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.edtFullName.setText("");
                mBinding.edtEmail.setText("");
                mBinding.edtMobileNo.setText("");
                mBinding.edtReason.setText("");
                mBinding.tvDate.setText("");
                mBinding.tvTime.setText("");
                mBinding.ccpCountryCode.resetToDefaultCountry();
            }
        });
        mBinding.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (mBinding.edtFullName.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(getActivity(), "Please Enter Name");
                } else if (!mBinding.edtEmail.getText().toString().matches(emailPattern)) {
                    Utils.showDialog(getActivity(), "Please Enter valid Email Address");
                } else if (mBinding.edtMobileNo.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(getActivity(), "Please Enter Mobile Number");
                } else if (mBinding.edtMobileNo.getText().toString().length() < 10) {
                    Utils.showDialog(getActivity(), "Please Enter Valid Mobile Number");
                } else if (mBinding.edtReason.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(getActivity(), "Please Enter Reason");
                } else if (mBinding.tvDate.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(getActivity(), "Please Enter Date");
                } else if (mBinding.tvTime.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(getActivity(), "Please Enter Time");
                } else {
                    submitNewMember();
                }
            }
        });
        mBinding.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (mBinding.edtFullName.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(getActivity(), "Please Enter Name");
                } else if (!mBinding.edtEmail.getText().toString().matches(emailPattern)) {
                    Utils.showDialog(getActivity(), "Please Enter valid Email Address");
                } else if (mBinding.edtMobileNo.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(getActivity(), "Please Enter Mobile Number");
                } else if (mBinding.edtMobileNo.getText().toString().length() < 10) {
                    Utils.showDialog(getActivity(), "Please Enter Valid Mobile Number");
                } else if (mBinding.edtReason.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(getActivity(), "Please Enter Reason");
                } else if (mBinding.tvDate.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(getActivity(), "Please Enter Date");
                } else if (mBinding.tvTime.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(getActivity(), "Please Enter Time");
                } else {
                    updateMember();
                }
            }
        });

    }

    private void submitNewMember() {
        if (isInternetAvailable(getActivity())) {
            ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), true, true, false, null);
            WebApiClient.getInstance().AddAppointment(paramNewMember()).enqueue(new Callback<AddAppointmentModel>() {
                @Override
                public void onResponse(Call<AddAppointmentModel> call, Response<AddAppointmentModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        if (response.body().message.equalsIgnoreCase("success")) {
                            mainActivity.viewAppointment();

                        } else {
                            Utils.showDialog(getActivity(), response.body().message + "");
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddAppointmentModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(getActivity(), "Check Your Internet.");
        }
    }

    private Map<String, String> paramNewMember() {
        Map<String, String> params = new HashMap<>();
        String nextDate = mBinding.tvDate.getText().toString();
        String inputPattern = "dd-MM-yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date;
        String finalNectDate = null;

        try {
            date = inputFormat.parse(nextDate);
            finalNectDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        params.put("user_id", PreferenceHelper.getString(Constants.USER_ID, ""));
        params.put("name", mBinding.edtFullName.getText().toString());
        params.put("mobile", mBinding.edtMobileNo.getText().toString());
        params.put("c_code", countryCode);
        params.put("email", mBinding.edtEmail.getText().toString());
        params.put("appointment_reason", mBinding.edtReason.getText().toString());
        params.put("appointment_date", finalNectDate);
        params.put("appointment_time", mBinding.tvTime.getText().toString());
        return params;
    }

    private void updateMember() {
        if (isInternetAvailable(getActivity())) {
            ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), true, true, false, null);
            WebApiClient.getInstance().UpdateAppointment(paramUpdateMember()).enqueue(new Callback<AddAppointmentModel>() {
                @Override
                public void onResponse(Call<AddAppointmentModel> call, Response<AddAppointmentModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        if (response.body().message.equalsIgnoreCase("success")) {
                            mainActivity.viewAppointment();
                        } else {
                            Utils.showDialog(getActivity(), response.body().message + "");
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddAppointmentModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(getActivity(), "Check Your Internet.");
        }
    }

    private Map<String, String> paramUpdateMember() {
        Map<String, String> params = new HashMap<>();
        String nextDate = mBinding.tvDate.getText().toString();
        String inputPattern = "dd-MM-yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date;
        String finalNectDate = null;

        try {
            date = inputFormat.parse(nextDate);
            finalNectDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        params.put("appointment_id", appointmentId);
        params.put("user_id", PreferenceHelper.getString(Constants.USER_ID, ""));
        params.put("name", mBinding.edtFullName.getText().toString());
        params.put("mobile", mBinding.edtMobileNo.getText().toString());
        params.put("c_code", countryCode);
        params.put("email", mBinding.edtEmail.getText().toString());
        params.put("appointment_reason", mBinding.edtReason.getText().toString());
        params.put("appointment_date", finalNectDate);
        params.put("appointment_time", mBinding.tvTime.getText().toString());
        return params;
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    mainActivity.hideSoftKeyboard(getActivity(), v);
                    onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

    public void onBackPressed() {
//        Utils.onBackPressed(getActivity(), getResources().getString(R.string.exit_dialog_text));
        mainActivity.selectFirstItemAsDefault();
    }
}

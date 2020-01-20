package com.thelawhouse.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.thelawhouse.Model.CaseListModel;
import com.thelawhouse.Model.NewCaseModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.ActivityEditCaseBinding;

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

public class EditCaseActivity extends AppCompatActivity {
    EditCaseActivity mContext = EditCaseActivity.this;
    ActivityEditCaseBinding mBinding;
    private String caseId = "";
    private String countryCode = "";
    private String countryCodeW = "";
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private String click = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.activity_edit_case);
        mBinding.toolbar.ivBack.setVisibility(View.VISIBLE);
        Typeface typeFace = ResourcesCompat.getFont(mContext, R.font.font_regular);
        mBinding.ccpCountryCode.setTypeFace(typeFace);
        mBinding.ccpCountryCodeW.setTypeFace(typeFace);
        countryCode = mBinding.ccpCountryCode.getSelectedCountryCode();
        countryCodeW = mBinding.ccpCountryCodeW.getSelectedCountryCode();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            caseId = bundle.getString("caseId");
            caseData(caseId);
        }
        onClickListener();
        dateSelection();
    }

    private void caseData(String caseId) {
        if (isInternetAvailable(mContext)) {
            ProgressHUD mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().CaseData(paramCaseDate(caseId)).enqueue(new Callback<CaseListModel>() {
                @Override
                public void onResponse(Call<CaseListModel> call, Response<CaseListModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        CaseListModel.Collection_data mData = response.body().collection_data.get(0);
                        assert response.body() != null;
                        if (response.body().message.equalsIgnoreCase("success")) {
                            Map<String, String> params = new HashMap<>();
                            String nextDate = mData.case_next_date;
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
                            mBinding.tvNextDate.setText(finalNectDate);
                            String lastDate = mData.case_last_date;
                            String inputPattern1 = "yyyy-MM-dd";
                            String outputPattern1 = "dd-MM-yyyy";
                            SimpleDateFormat inputFormat1 = new SimpleDateFormat(inputPattern1);
                            SimpleDateFormat outputFormat1 = new SimpleDateFormat(outputPattern1);

                            Date date1;
                            String finalLastDate = null;

                            try {
                                date1 = inputFormat1.parse(lastDate);
                                finalLastDate = outputFormat1.format(date1);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            mBinding.tvLastDate.setText(finalLastDate);
                            mBinding.edtCaseNo.setText(mData.case_type);
                            mBinding.edtCourtNo.setText(mData.court_room_no);
                            mBinding.edtCourt.setText(mData.court);
                            mBinding.edtPartyName1.setText(mData.party_name_1);
                            mBinding.edtPartyName2.setText(mData.party_name_2);
                            mBinding.edtAdvocateParty1.setText(mData.advocate_party_1);
                            mBinding.edtAdvocateParty2.setText(mData.advocate_party_2);
                            mBinding.edtStages.setText(mData.stages);
                            mBinding.edtMobileNo.setText(mData.mobile_no);
                            mBinding.edtWhatsAppMobileNo.setText(mData.whatsapp_number);
                            mBinding.edtEmail.setText(mData.email);
                            mBinding.ccpCountryCode.setCountryForPhoneCode(Integer.parseInt(mData.m_country_code));
                            mBinding.ccpCountryCodeW.setCountryForPhoneCode(Integer.parseInt(mData.w_country_code));
                        } else {
                            Utils.showDialog(mContext, response.body().message + "");
                        }
                    }
                }

                @Override
                public void onFailure(Call<CaseListModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(mContext, "Check Your Internet.");
        }
    }

    private Map<String, String> paramCaseDate(String caseId) {
        Map<String, String> params = new HashMap<>();
        params.put("case_id", caseId);
        return params;
    }

    private void onClickListener() {
        mBinding.toolbar.tvTitle.setText("Edit Case");
        mBinding.toolbar.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBinding.ccpCountryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                countryCode = selectedCountry.getPhoneCode();
            }
        });
        mBinding.ccpCountryCodeW.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                countryCodeW = selectedCountry.getPhoneCode();
            }
        });
        mBinding.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (mBinding.tvLastDate.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please Enter Last Date");
                } else if (mBinding.edtCaseNo.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please Enter Case Type / No");
                } else if (mBinding.edtCourtNo.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please Enter Court / Room No");
                } else if (mBinding.edtCourt.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please Enter Court");
                } else if (mBinding.edtPartyName1.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please Enter Party Name 1");
                } else if (mBinding.edtAdvocateParty1.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please Enter Advocate Party 1");
                } else if (mBinding.edtPartyName2.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please Enter Party Name 2");
                } else if (mBinding.edtAdvocateParty2.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please Enter Advocate Party 2");
                } else if (mBinding.edtStages.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please Enter Stages");
                } else if (mBinding.edtMobileNo.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please Enter Mobile Number");
                } else if (mBinding.edtMobileNo.getText().toString().length() < 10) {
                    Utils.showDialog(mContext, "Please Enter Valid Mobile Number");
                } else if (mBinding.edtWhatsAppMobileNo.getText().toString().length() < 10) {
                    Utils.showDialog(mContext, "Please Enter Valid WhatsApp Mobile Number");
                } else if (!mBinding.edtEmail.getText().toString().matches(emailPattern)) {
                    Utils.showDialog(mContext, "Please Enter valid Email Address");
                } else {
                    updateCaseDetail();
                }
            }
        });
    }

    private void updateCaseDetail() {
        if (isInternetAvailable(mContext)) {
            ProgressHUD mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().UpdateCase(paramNewCase()).enqueue(new Callback<NewCaseModel>() {
                @Override
                public void onResponse(Call<NewCaseModel> call, Response<NewCaseModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        if (response.body().status) {
                            Intent intent = new Intent();
                            setResult(101, intent);
                            finish();
                        } else {
                            Utils.showDialog(mContext, response.body().message + "");
                        }
                    }
                }

                @Override
                public void onFailure(Call<NewCaseModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(mContext, "Check Your Internet.");
        }
    }

    private Map<String, String> paramNewCase() {
        Map<String, String> params = new HashMap<>();
        String nextDate = mBinding.tvNextDate.getText().toString();
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

        String lastDate = mBinding.tvLastDate.getText().toString();
        String inputPattern1 = "dd-MM-yyyy";
        String outputPattern1 = "yyyy-MM-dd";
        SimpleDateFormat inputFormat1 = new SimpleDateFormat(inputPattern1);
        SimpleDateFormat outputFormat1 = new SimpleDateFormat(outputPattern1);

        Date date1;
        String finalLastDate = null;

        try {
            date1 = inputFormat1.parse(lastDate);
            finalLastDate = outputFormat1.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (caseId != null && !caseId.equalsIgnoreCase("")) {
            params.put("case_id", caseId);
        }
        params.put("case_last_date", finalLastDate);
        params.put("case_next_date", finalNectDate);
        params.put("m_country_code", countryCode);
        params.put("w_country_code", countryCodeW);
        params.put("case_type", mBinding.edtCaseNo.getText().toString());
        params.put("court_room_no", mBinding.edtCourtNo.getText().toString());
        params.put("court", mBinding.edtCourt.getText().toString());
        params.put("party_name_1", mBinding.edtPartyName1.getText().toString());
        params.put("party_name_2", mBinding.edtPartyName2.getText().toString());
        params.put("advocate_party_1", mBinding.edtAdvocateParty1.getText().toString());
        params.put("advocate_party_2", mBinding.edtAdvocateParty2.getText().toString());
        params.put("stages", mBinding.edtStages.getText().toString());
        params.put("mobile_no", mBinding.edtMobileNo.getText().toString());
        params.put("whatsapp_number", mBinding.edtWhatsAppMobileNo.getText().toString());
        params.put("email", mBinding.edtEmail.getText().toString());
        params.put("created_by", PreferenceHelper.getString(Constants.USER_ID, ""));
        return params;
    }

    private void dateSelection() {
        datePickerDialog();
        mBinding.tvLastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = "lastDate";
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        mBinding.tvNextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = "nextDate";
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, date, myCalendar
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
        if (click.equalsIgnoreCase("lastDate"))
            mBinding.tvLastDate.setText(sdf.format(myCalendar.getTime()));
        else
            mBinding.tvNextDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

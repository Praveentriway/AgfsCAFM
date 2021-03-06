package com.daemon.emco_android.repository.remote;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.listeners.ContractUserListner;
import com.daemon.emco_android.model.response.ContractUserResponse;
import com.daemon.emco_android.repository.db.entity.ContractEntity;
import com.daemon.emco_android.repository.db.entity.ZoneEntity;
import com.daemon.emco_android.repository.remote.restapi.Api;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.listeners.BuildingDetailsListener;
import com.daemon.emco_android.listeners.CategoryListener;
import com.daemon.emco_android.listeners.JobNoListener;
import com.daemon.emco_android.listeners.LCUserInputListener;
import com.daemon.emco_android.listeners.PriorityListListener;
import com.daemon.emco_android.listeners.ReportTypesListener;
import com.daemon.emco_android.listeners.SiteListener;
import com.daemon.emco_android.listeners.WorkTypeListListener;
import com.daemon.emco_android.listeners.ZoneListener;
import com.daemon.emco_android.model.request.BuildingDetailsRequest;
import com.daemon.emco_android.model.request.EmployeeIdRequest;
import com.daemon.emco_android.model.response.BuildingDetailsResponse;
import com.daemon.emco_android.model.response.CategoryResponse;
import com.daemon.emco_android.model.response.ContractResponse;
import com.daemon.emco_android.model.response.LCUserInputResponse;
import com.daemon.emco_android.model.response.PriorityResponse;
import com.daemon.emco_android.model.response.ReportTypesResponse;
import com.daemon.emco_android.model.response.SiteResponse;
import com.daemon.emco_android.model.response.WorkTypeResponse;
import com.daemon.emco_android.model.response.ZoneResponse;
import com.daemon.emco_android.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Created by vikram on 19/7/17. */
public class GetLogComplaintPopupRepository {
  private static final String TAG = GetLogComplaintPopupRepository.class.getSimpleName();
  private AppCompatActivity mActivity;

  private EmployeeIdRequest employeeIdRequest;
  private SiteListener mCallbackSite;
  private PriorityListListener mCallbackPriority;
  private ReportTypesListener mCallbackReportTypes;
  private WorkTypeListListener workTypeListListener;
  private JobNoListener mCallbackContract;
  private ContractUserListner mCallBackContractUser;
  private ZoneListener mCallbackZone;
  private CategoryListener mCallbackCategory;
  private BuildingDetailsListener mbuildingDetails;
  private LCUserInputListener mCallbackUserLogComplaint;
  private ApiInterface mInterface;

  public GetLogComplaintPopupRepository(
      AppCompatActivity mActivity, EmployeeIdRequest employeeIdRequest) {
    this.mActivity = mActivity;
    this.employeeIdRequest = employeeIdRequest;
  //  mInterface = ApiClient.getClient(mActivity).create(ApiInterface.class);
      mInterface = Api.dataClient().create(ApiInterface.class);
  }

  public void getSiteAreaData(SiteListener listener) {
    Log.d(TAG, "getSiteAreaData");
    this.mCallbackSite = listener;
    Call<SiteResponse> callSiteArea = mInterface.getSiteAreaResult(employeeIdRequest);
    try {
      callSiteArea.enqueue(
          new Callback<SiteResponse>() {
            @Override
            public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
              Log.d(TAG, "onResponse");
              if (response.isSuccessful()) {
                if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                  mCallbackSite.onLogComplaintSiteReceivedSuccess(
                      response.body().getmSiteAread(), AppUtils.MODE_SERVER);
                } else
                  mCallbackSite.onLogComplaintSiteReceivedFailure(
                      response.body().getMessage(), AppUtils.MODE_SERVER);

              } else
                mCallbackSite.onLogComplaintSiteReceivedFailure(
                    response.message(), AppUtils.MODE_SERVER);
            }

            @Override
            public void onFailure(Call<SiteResponse> call, Throwable t) {
              Log.d(TAG, "onFailure");
              mCallbackSite.onLogComplaintSiteReceivedFailure(
                  mActivity.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
            }
          });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }




    public void getPriorityListData(PriorityListListener listener) {
    Log.d(TAG, "getPriorityListData");
    this.mCallbackPriority = listener;
    Call<PriorityResponse> callPriorityList = mInterface.getPriorityListResult();
    try {
      callPriorityList.enqueue(
          new Callback<PriorityResponse>() {
            @Override
            public void onResponse(
                Call<PriorityResponse> call, Response<PriorityResponse> response) {
              Log.d(TAG, "onResponse");
              if (response.isSuccessful()) {
                if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                  mCallbackPriority.onPriorityReceivedSuccess(
                      response.body().getObject(), AppUtils.MODE_SERVER);
                } else
                  mCallbackPriority.onPriorityReceivedFailure(
                      response.body().getMessage(), AppUtils.MODE_SERVER);

              } else
                mCallbackPriority.onPriorityReceivedFailure(
                    response.message(), AppUtils.MODE_SERVER);
            }

            @Override
            public void onFailure(Call<PriorityResponse> call, Throwable t) {
              Log.d(TAG, "onFailure");
              mCallbackPriority.onPriorityReceivedFailure(
                  mActivity.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
            }
          });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getWorkTypeListData(WorkTypeListListener listener) {
    Log.d(TAG, "getPriorityListData");
    this.workTypeListListener = listener;
    Call<WorkTypeResponse> callWorkTypeList = mInterface.getWorkTypeListResult();
    try {
      callWorkTypeList.enqueue(
          new Callback<WorkTypeResponse>() {
            @Override
            public void onResponse(
                Call<WorkTypeResponse> call, Response<WorkTypeResponse> response) {
              Log.d(TAG, "onResponse");
              if (response.isSuccessful()) {
                if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                  workTypeListListener.onWorkTypeReceivedSuccess(
                      response.body().getObject(), AppUtils.MODE_SERVER);
                } else
                  workTypeListListener.onWorkTypeReceivedFailure(
                      response.body().getMessage(), AppUtils.MODE_SERVER);

              } else
                workTypeListListener.onWorkTypeReceivedFailure(
                    response.message(), AppUtils.MODE_SERVER);
            }

            @Override
            public void onFailure(Call<WorkTypeResponse> call, Throwable t) {
              Log.d(TAG, "onFailure");
              workTypeListListener.onWorkTypeReceivedFailure(
                  mActivity.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
            }
          });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getContractListData(JobNoListener listener) {
    Log.d(TAG, "getContractListData");
    this.mCallbackContract = listener;
    try {
      mInterface.getContractListResult(employeeIdRequest)
          .enqueue(
              new Callback<ContractResponse>() {
                @Override
                public void onResponse(
                    Call<ContractResponse> call, Response<ContractResponse> response) {
                  Log.d(TAG, "onResponse");
                  if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                      mCallbackContract.onContractReceivedSuccess(
                          response.body().getObject(), AppUtils.MODE_SERVER);
                    } else
                      mCallbackContract.onContractReceivedFailure(
                          response.body().getMessage(), AppUtils.MODE_SERVER);

                  } else
                    mCallbackContract.onContractReceivedFailure(
                        response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<ContractResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure");
                  mCallbackContract.onContractReceivedFailure(
                      mActivity.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }




  public void getZoneListData(ZoneEntity zoneEntity,final ZoneListener mCallbackZone) {
    Log.d(TAG, "getZoneListData");
    this.mCallbackZone = mCallbackZone;
    try {
      mInterface
          .getZoneListResult(zoneEntity)
          .enqueue(
              new Callback<ZoneResponse>() {
                @Override
                public void onResponse(Call<ZoneResponse> call, Response<ZoneResponse> response) {
                  Log.d(TAG, "onResponse");
                  if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                      mCallbackZone.onZoneReceivedSuccess(
                          response.body().getObject(), AppUtils.MODE_SERVER);
                    } else
                      mCallbackZone.onZoneReceivedFailure(
                          response.body().getMessage(), AppUtils.MODE_SERVER);

                  } else
                    mCallbackZone.onZoneReceivedFailure(response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<ZoneResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure");
                  mCallbackZone.onZoneReceivedFailure(
                      mActivity.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


    public void getZoneFromContractUser(String[] data,final ZoneListener mCallbackZone) {
        Log.d(TAG, "getZoneListData");
        this.mCallbackZone = mCallbackZone;
      //  ZoneEntity zone =new ZoneEntity();{mOpco ,mStrEmployeeId ,tags, mStrSiteCode};
        Log.d("getZoneFromContractUser","getZoneFromContractUser"+ data[2]);
        String opco = data[0];
        String employeeId = data[1];
        String serviceGroup = data[2];
        String siteCode = data[3];
        try {
            mInterface
                    .getZoneFromContractUser(employeeId,serviceGroup,siteCode,opco)
                    .enqueue(
                            new Callback<ZoneResponse>() {
                                @Override
                                public void onResponse(Call<ZoneResponse> call, Response<ZoneResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                                            mCallbackZone.onZoneReceivedSuccess(
                                                    response.body().getObject(), AppUtils.MODE_SERVER);
                                        } else
                                            mCallbackZone.onZoneReceivedFailure(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);

                                    } else
                                        mCallbackZone.onZoneReceivedFailure(response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<ZoneResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure");
                                    mCallbackZone.onZoneReceivedFailure(
                                            mActivity.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


  public void getCategoryData(CategoryListener listener) {
    Log.d(TAG, "getCategoryData");
    this.mCallbackCategory = listener;
    try {
      mInterface
          .getCategoryResult(employeeIdRequest)
          .enqueue(
              new Callback<CategoryResponse>() {
                @Override
                public void onResponse(
                    Call<CategoryResponse> call, Response<CategoryResponse> response) {
                  Log.d(TAG, "onResponse");
                  if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                      mCallbackCategory.onCategoryReceivedSuccess(
                          response.body().getObject(), AppUtils.MODE_SERVER);
                    } else
                      mCallbackCategory.onCategoryReceivedFailure(
                          response.body().getMessage(), AppUtils.MODE_SERVER);

                  } else
                    mCallbackCategory.onCategoryReceivedFailure(
                        response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<CategoryResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure");
                  mCallbackCategory.onCategoryReceivedFailure(
                      mActivity.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getBuildingDetailsData(
      BuildingDetailsListener listener, BuildingDetailsRequest buildingDetailsRequest) {
    Log.d(TAG, "getBuildingDetailsData");
    this.mbuildingDetails = listener;
    Call<BuildingDetailsResponse> callBuildingDetails =
        mInterface.getBuildingDetailsResult(buildingDetailsRequest);
    try {
      callBuildingDetails.enqueue(
          new Callback<BuildingDetailsResponse>() {
            @Override
            public void onResponse(
                Call<BuildingDetailsResponse> call, Response<BuildingDetailsResponse> response) {
              Log.d(TAG, "onResponse");
              if (response.isSuccessful()) {
                if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                  mbuildingDetails.onBuildingDetailsReceivedSuccess(
                      response.body().getObject(), AppUtils.MODE_SERVER);
                } else
                  mbuildingDetails.onBuildingDetailsReceivedFailure(
                      response.body().getMessage(), AppUtils.MODE_SERVER);

              } else
                mbuildingDetails.onBuildingDetailsReceivedFailure(
                    response.message(), AppUtils.MODE_SERVER);
            }

            @Override
            public void onFailure(Call<BuildingDetailsResponse> call, Throwable t) {
              Log.d(TAG, "onFailure");
              mbuildingDetails.onBuildingDetailsReceivedFailure(
                  mActivity.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
            }
          });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getAllReportTypesListData(ReportTypesListener listener) {
    Log.d(TAG, "getAllReportTypesListData");
    this.mCallbackReportTypes = listener;
    Call<ReportTypesResponse> callReportTypesList = mInterface.getAllReportTypesListResult();
    try {
      callReportTypesList.enqueue(
          new Callback<ReportTypesResponse>() {
            @Override
            public void onResponse(
                Call<ReportTypesResponse> call, Response<ReportTypesResponse> response) {
              Log.d(TAG, "onResponse");
              if (response.isSuccessful()) {
                if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                  mCallbackReportTypes.onReportTypesReceivedSuccess(
                      response.body().getObject(), AppUtils.MODE_SERVER);
                } else
                  mCallbackReportTypes.onReportTypesReceivedFailure(
                      response.body().getMessage(), AppUtils.MODE_SERVER);

              } else
                mCallbackReportTypes.onReportTypesReceivedFailure(
                    response.message(), AppUtils.MODE_SERVER);
            }

            @Override
            public void onFailure(Call<ReportTypesResponse> call, Throwable t) {
              Log.d(TAG, "onFailure");
              mCallbackReportTypes.onReportTypesReceivedFailure(
                  mActivity.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
            }
          });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getLCUserInputData(LCUserInputListener listener) {
    Log.d(TAG, "getAllReportTypesListData");
    this.mCallbackUserLogComplaint = listener;
    try {
      mInterface
          .getLCUserInputResult(employeeIdRequest)
          .enqueue(
              new Callback<LCUserInputResponse>() {
                @Override
                public void onResponse(
                    Call<LCUserInputResponse> call, Response<LCUserInputResponse> response) {
                  Log.d(TAG, "onResponse");
                  if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                      mCallbackUserLogComplaint.onLCUserInputReceivedSuccess(
                          response.body().getObject(), AppUtils.MODE_SERVER);
                    } else
                      mCallbackUserLogComplaint.onLCUserInputReceivedFailure(
                          response.body().getMessage(), AppUtils.MODE_SERVER);

                  } else
                    mCallbackUserLogComplaint.onLCUserInputReceivedFailure(
                        response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<LCUserInputResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure");
                  mCallbackUserLogComplaint.onLCUserInputReceivedFailure(
                      mActivity.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}

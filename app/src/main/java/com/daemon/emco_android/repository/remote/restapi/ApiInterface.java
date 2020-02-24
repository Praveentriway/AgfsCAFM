package com.daemon.emco_android.repository.remote.restapi;

import com.daemon.emco_android.model.common.EmployeeTrackingDetail;
import com.daemon.emco_android.model.request.DocumentTransaction;
import com.daemon.emco_android.model.request.LocationDetail;
import com.daemon.emco_android.model.response.DocumentDownloadResponse;
import com.daemon.emco_android.model.response.DocumentTypeResponse;
import com.daemon.emco_android.model.response.EmployeeGpsReponse;
import com.daemon.emco_android.model.response.LocationDetailResponse;
import com.daemon.emco_android.model.response.LocationResponse;
import com.daemon.emco_android.model.response.OpcoResponse;
import com.daemon.emco_android.repository.db.entity.DFoundWDoneImageEntity;
import com.daemon.emco_android.repository.db.entity.EmployeeDetailsEntity;
import com.daemon.emco_android.repository.db.entity.LogComplaintEntity;
import com.daemon.emco_android.repository.db.entity.PPEFetchSaveEntity;
import com.daemon.emco_android.repository.db.entity.RCViewRemarksEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintRespondEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;
import com.daemon.emco_android.repository.db.entity.SaveFeedbackEntity;
import com.daemon.emco_android.repository.db.entity.SaveFeedbackEntityNew;
import com.daemon.emco_android.repository.db.entity.SaveMaterialEntity;
import com.daemon.emco_android.repository.db.entity.SaveRatedServiceEntity;
import com.daemon.emco_android.repository.db.entity.SurveyContractResponse;
import com.daemon.emco_android.repository.db.entity.SurveyCustomerResponse;
import com.daemon.emco_android.repository.db.entity.SurveyLocationResponse;
import com.daemon.emco_android.repository.db.entity.SurveyQuestionnaireResponse;
import com.daemon.emco_android.repository.db.entity.SurveyReferenceReponse;
import com.daemon.emco_android.repository.db.entity.SurveyTransaction;
import com.daemon.emco_android.repository.db.entity.UserToken;
import com.daemon.emco_android.repository.db.entity.ZoneEntity;
import com.daemon.emco_android.model.common.FetchPpmScheduleDetails;
import com.daemon.emco_android.model.common.FetchPpmScheduleDocBy;
import com.daemon.emco_android.model.response.PPMDetailsResponse;
import com.daemon.emco_android.model.common.PpmScheduleDocBy;
import com.daemon.emco_android.model.request.AddNewUser;
import com.daemon.emco_android.model.request.AssetDetailsRequest;
import com.daemon.emco_android.model.request.BuildingDetailsRequest;
import com.daemon.emco_android.model.request.ChangePasswordRequest;
import com.daemon.emco_android.model.request.EmployeeIdRequest;
import com.daemon.emco_android.model.request.FetchFeedbackRequest;
import com.daemon.emco_android.model.request.FetchPpeForPpmReq;
import com.daemon.emco_android.model.request.FetchPpmScheduleDetailsRequest;
import com.daemon.emco_android.model.request.FileSizeRequest;
import com.daemon.emco_android.model.request.GetMaterialRequest;
import com.daemon.emco_android.model.request.HardSoftRequest;
import com.daemon.emco_android.model.request.LoginRequest;
import com.daemon.emco_android.model.request.MultiSearchRequest;
import com.daemon.emco_android.model.request.PPMCCCReq;
import com.daemon.emco_android.model.request.PPMFilterRequest;
import com.daemon.emco_android.model.request.PPMFindingRequest;
import com.daemon.emco_android.model.request.PpmScheduleDocByRequest;
import com.daemon.emco_android.model.request.PpmfeedbackemployeeReq;
import com.daemon.emco_android.model.request.RCDownloadImageRequest;
import com.daemon.emco_android.model.request.ReceiveComplainCCRequest;
import com.daemon.emco_android.model.request.ReceiveComplaintViewRequest;
import com.daemon.emco_android.model.request.RiskAssListRequest;
import com.daemon.emco_android.model.request.SaveAssesEntity;
import com.daemon.emco_android.model.request.SavePPMFindingRequest;
import com.daemon.emco_android.model.request.SaveRatedServiceRequest;
import com.daemon.emco_android.model.request.SearchSingleComplaintPreventiveRequest;
import com.daemon.emco_android.model.request.SearchSingleComplaintRequest;
import com.daemon.emco_android.model.request.WorkDefectRequest;
import com.daemon.emco_android.model.request.WorkDoneRequest;
import com.daemon.emco_android.model.response.AllFeedbackDetailsResponse;
import com.daemon.emco_android.model.response.AssetDetailsResponse;
import com.daemon.emco_android.model.response.BuildingDetailsResponse;
import com.daemon.emco_android.model.response.CategoryResponse;
import com.daemon.emco_android.model.response.CheckListMonthlyResponse;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.model.response.ContractDetailResponse;
import com.daemon.emco_android.model.response.ContractResponse;
import com.daemon.emco_android.model.response.CustomerFeedBackResponse;
import com.daemon.emco_android.model.response.CustomerRemarksResponse;
import com.daemon.emco_android.model.response.DashboardPieResponse;
import com.daemon.emco_android.model.response.DocComplaintListResponse;
import com.daemon.emco_android.model.response.FeedbackDetailsResponse;
import com.daemon.emco_android.model.response.FeedbackEmployeeDetailsResponse;
import com.daemon.emco_android.model.response.FetchPPENameResponse;
import com.daemon.emco_android.model.response.GetMaterialResponse;
import com.daemon.emco_android.model.response.GetPPMRecomResponse;
import com.daemon.emco_android.model.response.GetPpmParamValue;
import com.daemon.emco_android.model.response.HardSoftViewResponse;
import com.daemon.emco_android.model.response.LCUserInputResponse;
import com.daemon.emco_android.model.response.LogComplaintResponse;
import com.daemon.emco_android.model.response.LoginResponse;
import com.daemon.emco_android.model.response.MaterialRequiredResponse;
import com.daemon.emco_android.model.response.MultiSearchComplaintResponse;
import com.daemon.emco_android.model.response.NatureDescResponse;
import com.daemon.emco_android.model.response.PendingReasonsResponse;
import com.daemon.emco_android.model.response.PpeAfterSaveResponse;
import com.daemon.emco_android.model.response.PpmEmployeeFeedResponse;
import com.daemon.emco_android.model.response.PpmFeedBackResponse;
import com.daemon.emco_android.model.response.PpmStatusResponse;
import com.daemon.emco_android.model.response.PriorityResponse;
import com.daemon.emco_android.model.response.RCImageDownloadResponse;
import com.daemon.emco_android.model.response.RCImageUploadResponse;
import com.daemon.emco_android.model.response.RCRespondResponse;
import com.daemon.emco_android.model.response.ReceiveComplaintResponse;
import com.daemon.emco_android.model.response.ReceiveComplaintViewResponse;
import com.daemon.emco_android.model.response.ReportTypesResponse;
import com.daemon.emco_android.model.response.RiskAssListResponse;
import com.daemon.emco_android.model.response.SingleSearchComplaintResponse;
import com.daemon.emco_android.model.response.SiteResponse;
import com.daemon.emco_android.model.response.TechnicalRemarksResponse;
import com.daemon.emco_android.model.response.WorkDefectResponse;
import com.daemon.emco_android.model.response.WorkDoneResponse;
import com.daemon.emco_android.model.response.WorkPendingReasonResponse;
import com.daemon.emco_android.model.response.WorkStatusResponse;
import com.daemon.emco_android.model.response.WorkTypeResponse;
import com.daemon.emco_android.model.response.ZoneResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/** Created on 17/7/17. */
public interface ApiInterface {
  @POST(ApiConstant.LOGIN)
  Call<LoginResponse> getLoginResult(@Body LoginRequest loginRequest);

  @POST(ApiConstant.CHANGEPASSWORD)
  Call<CommonResponse> getChangePasswordResult(@Body ChangePasswordRequest loginRequest);

  @POST(ApiConstant.REGISTER)
  Call<CommonResponse> getRegResult(@Body AddNewUser regRequest);


  @POST(ApiConstant.USERTOKEN)
  Call<CommonResponse> addUserToken(@Body UserToken regRequest);

  @POST(ApiConstant.DELETEUSERTOKEN)
  Call<CommonResponse> deleteUserToken(@Body UserToken regRequest);


  @GET(ApiConstant.URLVALID)
  Call<CommonResponse> getURLResult();

  @GET(ApiConstant.RECEIVECOMPLAINTLIST)
  Call<ReceiveComplaintResponse> getReceiveComplaintListResult(
      @Query("employeeId") String strEmpId,
      @Query("status") String status,
      @Query("startIndex") int startIndex,
      @Query("limit") int limit);

  @GET(ApiConstant.RECEIVECOMPLAINTUNSIGNEDCOMPLAINT)
  Call<ReceiveComplaintResponse> getReceiveComplaintUnSignedResult(
      @Query("employeeId") String strEmpId,
      @Query("startIndex") int startIndex,
      @Query("limit") int limit);

  @POST(ApiConstant.PPMDETAILSLIST)
  Call<PPMDetailsResponse> getppmdetailsResult(
      @Query("employeeId") String strEmpId,
      @Query("startIndex") int startIndex,
      @Query("limit") int limit,
      @Body PPMFilterRequest ppmFilterRequest);


  @POST(ApiConstant.SUPPORTDOCLIST)
  Call<DocComplaintListResponse> getDocCmplListResult(
          @Query("employeeId") String strEmpId,
          @Query("startIndex") int startIndex,
          @Query("limit") int limit,
          @Body PPMFilterRequest ppmFilterRequest,
          @Query("type") String type);


  @GET(ApiConstant.CONTRACTDETAILS)
  Call<ContractDetailResponse> getContractDetails(
          @Query("employeeId") String employeeId,@Query("type") String type);

  @GET(ApiConstant.DOCUMENTTYPE)
  Call<DocumentTypeResponse> getDocumentType( @Query("type") String type);



  @GET(ApiConstant.NATUREDESC)
  Call<NatureDescResponse> getNatureDescription(  @Query("opco") String opco,
                                                  @Query("jobno") String jobno);

  @GET(ApiConstant.PENDINGREASONS)
  Call<PendingReasonsResponse> getPendingReasons();


  @POST(ApiConstant.FETCHPPMSCHEDULEDETAILS)
  Call<FetchPpmScheduleDetails> fetchPpmScheduleDetails(
      @Query("startIndex") int startIndex,
      @Query("limit") int limit,
      @Body FetchPpmScheduleDetailsRequest fetchPpmScheduleDetailsRequest);

  @POST(ApiConstant.FETCHPPMSCHEDULEDOCBYWORKORDERNOAPI)
  Call<FetchPpmScheduleDocBy> fetchPpmScheduleDocByWorkOrderNoAPI(
      @Body PpmScheduleDocByRequest ppmScheduleDocByRequest);

  @POST(ApiConstant.GETALLFORWARDEDCOMPLAINTBYEMPLOYEEID)
  Call<CommonResponse> getallforwardedcomplaintbyemployeeid(
      @Body EmployeeIdRequest employeeIdRequest);

  @POST(ApiConstant.RECEIVECOMPLAINTVIEW)
  Call<ReceiveComplaintViewResponse> getReceiveComplaintViewResult(
      @Body ReceiveComplaintViewRequest receiveComplaintViewRequest);

  @GET(ApiConstant.FETCHALLCOMPLAINTDETAILS)
  Call<ReceiveComplaintViewResponse> getAllReceiveComplaintViewResult();

  @GET(ApiConstant.FETCHALLCOMPLAINTDETAILS_NEW)
  Call<ReceiveComplaintViewResponse> getAllReceiveComplaintView_Result(
      @Query("userName") String userName);

 /* @GET(ApiConstant.FETCHALLCOMPLAINTDETAILS_CHANGE)
  Call<ReceiveComplaintViewResponse> getAllReceiveComplaintView_ResultNew(
          @Query("userName") String userName,@Query("startIndex") String startIndex,@Query("limit") int limit);*/

  @POST(ApiConstant.RECEIVECOMPLAINT_CCC)
  Call<CommonResponse> getReceiveComplaintCCCResult(
      @Body List<ReceiveComplainCCRequest> receiveComplainCCRequest);

  @POST(ApiConstant.PPM_CCC)
  Call<CommonResponse> getPPMCCCResult(@Body List<PPMCCCReq> ppmCCRequest);

  @POST(ApiConstant.RECEIVECOMPLAINTVIEWASSETDETAILS)
  Call<AssetDetailsResponse> getReceiveComplaintViewAssetDetailsResult(
      @Body AssetDetailsRequest assetDetailsRequest);

  @POST(ApiConstant.SAVEASSETBARCODEPPM)
  Call<CommonResponse> getAssestBarCodePPM(@Body AssetDetailsRequest username);

  @GET(ApiConstant.FETCHALLASSETBARCODEDETAILS)
  Call<AssetDetailsResponse> getAllAssetBarcodeDetailsResult();

  @POST(ApiConstant.FORGOTPASSWORD)
  Call<CommonResponse> getForgotPasswordResult(@Body LoginRequest username);

  @POST(ApiConstant.SITE)
  Call<SiteResponse> getSiteAreaResult(@Body EmployeeIdRequest employeeIdRequest);

  @GET(ApiConstant.WORKTYPE)
  Call<WorkTypeResponse> getWorkTypeListResult();

  @GET(ApiConstant.PRIORITY)
  Call<PriorityResponse> getPriorityListResult();

  @GET(ApiConstant.GETALLREPORTTYPES)
  Call<ReportTypesResponse> getAllReportTypesListResult();

  @POST(ApiConstant.CONTRACT)
  Call<ContractResponse> getContractListResult(@Body EmployeeIdRequest employeeIdRequest);

  @POST(ApiConstant.ZONE)
  Call<ZoneResponse> getZoneListResult(@Body ZoneEntity employeeIdRequest);

  @GET(ApiConstant.ALLZONE)
  Call<ZoneResponse> getAllZoneListResult(@Query("userName") String userName);

  @POST(ApiConstant.CATEGORY)
  Call<CategoryResponse> getCategoryResult(@Body EmployeeIdRequest employeeIdRequest);

  @POST(ApiConstant.BUILDING)
  Call<BuildingDetailsResponse> getBuildingDetailsResult(
      @Body BuildingDetailsRequest buildingDetailsRequest);

  @POST(ApiConstant.LOGCOMPLAINT)
  Call<LogComplaintResponse> getLogComplaintResult(@Body LogComplaintEntity logComplaintRequest);

  @POST(ApiConstant.CUSTOMERLOGCOMPLAINT)
  Call<LCUserInputResponse> getLCUserInputResult(@Body EmployeeIdRequest idRequest);

  @POST(ApiConstant.SINGLESEARCHCOMPLAINT)
  Call<SingleSearchComplaintResponse> getSingleSearchComplaintData(
      @Body SearchSingleComplaintRequest singleComplaintRequest);

  @POST(ApiConstant.SINGLESEARCHPREVENTIVE)
  Call<PPMDetailsResponse> getSingleSearchPreventiveData(@Query("employeeId") String employeeId,
      @Body SearchSingleComplaintPreventiveRequest singleComplaintRequest);

  @POST(ApiConstant.MULTI_SEARCHCOMPLAINT)
  Call<MultiSearchComplaintResponse> getMultiSearchComplaintData(
      @Body MultiSearchRequest multiSearchRequest);

  @GET(ApiConstant.WORKSTATUS)
  Call<WorkStatusResponse> getWorkStatusData();

  @GET(ApiConstant.WORKPENDINGREASON)
  Call<WorkPendingReasonResponse> getWorkPendingReasonData();

  @POST(ApiConstant.DEFECTFOUND)
  Call<WorkDefectResponse> getWorkDefectsData(@Body WorkDefectRequest workDefectRequest);

  @POST(ApiConstant.WORKDONE)
  Call<WorkDoneResponse> getWorkDoneData(@Body WorkDoneRequest workDoneRequest);

  @POST(ApiConstant.GETRISKASSESMENT)
  Call<RiskAssListResponse> getRiskAssesList(@Body RiskAssListRequest workDoneRequest);

  @POST(ApiConstant.SAVERAETPPM)
  Call<PpeAfterSaveResponse> savePpePPm(@Body RiskAssListRequest workDoneRequest);

  @POST(ApiConstant.CHECKCUSTOMERSIGNATURE)
  Call<CommonResponse> checkCustomerSignature(@Body SaveRatedServiceRequest workDoneRequest);

  @POST(ApiConstant.GETALLPPMRESULTS)
  Call<RiskAssListResponse> getEquipemList(@Body RiskAssListRequest workDoneRequest);

  @POST(ApiConstant.FETCHFEDBACKPPM)
  Call<PpmFeedBackResponse> fetchFedbackPpm(@Body RiskAssListRequest request);

  @POST(ApiConstant.SAVE_RISK_ASSES)
  Call<RiskAssListResponse> saveRiskAsses(@Body List<SaveAssesEntity> workDoneRequest);

  @POST(ApiConstant.SAVE_EQUIPMENT_TOOLS)
  Call<RiskAssListResponse> saveEquipmentTool(@Body List<SaveAssesEntity> workDoneRequest);

  @POST(ApiConstant.GETPPM_FINDING)
  Call<GetPPMRecomResponse> getPPMFinding(@Body PPMFindingRequest workDoneRequest);

  @POST(ApiConstant.SAVE_PPM_FINDING)
  Call<GetPPMRecomResponse> savePPMFinding(@Body SavePPMFindingRequest workDoneRequest);

  @GET(ApiConstant.ALLDEFECTFOUND)
  Call<WorkDefectResponse> getAllWorkDefectsData();

  @GET(ApiConstant.ALLWORKDONE)
  Call<WorkDoneResponse> getAllWorkDoneData();

  @GET(ApiConstant.GETFETCHPPMSTATUS)
  Call<PpmStatusResponse> getPpmWorkStatus();

  @GET(ApiConstant.CUSTOMERREMARKS)
  Call<CustomerRemarksResponse> getCustomerRemarksData();

  @GET(ApiConstant.TECHREMARKS)
  Call<TechnicalRemarksResponse> getTechRemarksData();

  @POST(ApiConstant.SAVERATEANDSHARE)
  Call<CommonResponse> saveRatedServiceRM(@Body SaveRatedServiceEntity request);

  @GET(ApiConstant.FETCHCUSTOMERFEEDBACK)
  Call<CustomerFeedBackResponse> getRatedService(@Query("opco") String opco, @Query("complaintNo") String complaintNo);

  @POST(ApiConstant.SAVERATEANDSHARE)
  Call<CommonResponse> saveRatedService(@Body SaveRatedServiceRequest request);

  @POST(ApiConstant.FETCHRATESERVICE)
  Call<CommonResponse> fetchRatedService(@Body SaveRatedServiceRequest request);

  @POST(ApiConstant.GETSAVEDPPMCHECKPARAMVALUE)
  Call<CheckListMonthlyResponse> getSavePpmCheckList(@Body SaveRatedServiceRequest request);

  @POST(ApiConstant.GETSAVEDIMPPMCHECKHARD)
  Call<CheckListMonthlyResponse> getIMPpmCheckListHard(@Body SaveRatedServiceRequest request);

  @POST(ApiConstant.GETSAVEDIMPPMCHECKSOFT)
  Call<CheckListMonthlyResponse> getIMPpmCheckListSoft(@Body SaveRatedServiceRequest request);

  @POST(ApiConstant.GETPPMCHECKPARAMVALUE)
  Call<GetPpmParamValue> getPpmCheckParamValue(@Body SaveRatedServiceRequest request);

  @POST(ApiConstant.SAVEASSETBARCODE)
  Call<CommonResponse> saveAssetBarCode(@Body SaveRatedServiceRequest request);

  @POST(ApiConstant.SAVEPPMCHECKPARAMVALUE)
  Call<CommonResponse> savePpmParamCheckValue(@Body List<PpmScheduleDocBy> request);

  @POST(ApiConstant.FEEDBACKEMPLOYEEDETAILS)
  Call<FeedbackEmployeeDetailsResponse> getFeedbackEmployeeDetailsResult(
      @Body EmployeeDetailsEntity request);

  @POST(ApiConstant.FEEDBACKEMPLOYEEDETAILSPPM)
  Call<PpmEmployeeFeedResponse> getFeedbackEmployeeDetailsResult(
      @Body PpmfeedbackemployeeReq request);

  @GET(ApiConstant.FETCHALLFEEDBACKDETAILS)
  Call<AllFeedbackDetailsResponse> getAllSavedFeedbackDetailsResult(
      @Query("userName") String userName);

  @GET(ApiConstant.FETCHALLEMPLOYEE)
  Call<FeedbackEmployeeDetailsResponse> getAllFeedbackEmployeeDetailsResult();

  @POST(ApiConstant.SAVEFEEDBACKDETAILS)
  Call<CommonResponse> getSaveFeedbackDetailsResult(@Body SaveFeedbackEntity request);

  @POST(ApiConstant.SAVEPPMFEEDBACKDETAILS)
  Call<CommonResponse> getSaveFeedbackDetailsResultNew(@Body SaveFeedbackEntityNew request);

  @POST(ApiConstant.FETCHFEEDBACKDETAILS)
  Call<FeedbackDetailsResponse> getFatchFeedbackDetailsResult(@Body FetchFeedbackRequest request);

  @POST(ApiConstant.RECEIVECOMPLAINTRESPOND)
  Call<CommonResponse> getReceiveComplaintRespondResult(
      @Body ReceiveComplaintRespondEntity respondRequest);

  @POST(ApiConstant.RECEIVECOMPLAINTRESPONDSAVE)
  Call<RCRespondResponse> getReceiveComplaintRespondSaveResult(
      @Body ReceiveComplaintViewEntity saveRequest);

  @POST(ApiConstant.RECEIVECOMPLAINTSAVEMATERIALREQUIRED)
  Call<CommonResponse> getReceiveComplaintMaterialRequiredSaveResult(
      @Body List<SaveMaterialEntity> saveMaterialRequests);

  @POST(ApiConstant.PPMSAVEMATERIALREQUIRED)
  Call<CommonResponse> getPPMMaterialRequiredSaveResult(
      @Body List<SaveMaterialEntity> saveMaterialRequests);

  @POST(ApiConstant.RECEIVECOMPLAINTGETMATERIALREQUIRED)
  Call<GetMaterialResponse> getReceiveComplaintMaterialGetResult(
      @Body GetMaterialRequest getMaterialRequest,
      @Query("startIndex") String startIndex,
      @Query("limit") String limit);

  @GET(ApiConstant.ALLRCMATERIALSAVED)
  Call<GetMaterialResponse> getALLRCMaterialSaved(@Query("userName") String userName);

  @POST(ApiConstant.PPMGETMATERIALREQUIRED)
  Call<GetMaterialResponse> getPPMMaterialGetResult(
      @Body GetMaterialRequest getMaterialRequest,
      @Query("startIndex") String startIndex,
      @Query("limit") String limit);

  @POST(ApiConstant.RECEIVECOMPLAINTMATERIALREQUIRED)
  Call<MaterialRequiredResponse> getReceiveComplaintMaterialRequiredResult(
      @Query("opco") String opco,
      @Query("description") String description,
      @Query("startIndex") String startIndex,
      @Query("limit") String limit);

  @GET(ApiConstant.FETCHPPENAME)
  Call<FetchPPENameResponse> getPPENameData();

  @POST(ApiConstant.SAVEPPE)
  Call<CommonResponse> savePPEData(@Body List<PPEFetchSaveEntity> request);

  @POST(ApiConstant.SAVEPPEPPM)
  Call<CommonResponse> savePPEDataPPM(@Body List<PPEFetchSaveEntity> request);

  @POST(ApiConstant.AFTERSAVEPPEPPM)
  Call<PpeAfterSaveResponse> getAfterSavePPEDataPPM(@Body FetchPpeForPpmReq fetchPpeForPpmReq);

  @POST(ApiConstant.AFTERSAVEPPE)
  Call<PpeAfterSaveResponse> getAfterSavePPEData(@Query("complainnumber") String complaintNo);

  @GET(ApiConstant.ALLAFTERSAVEPPE)
  Call<PpeAfterSaveResponse> getAfterAllSavePPEData(@Query("userName") String userName);

  @POST(ApiConstant.GETDASHBOARDDETAILS)
  Call<DashboardPieResponse> GetDashboardData(@Body MultiSearchRequest pieChartRequest);

  @POST(ApiConstant.GETDASHBOARDPIECHARTDETAILS)
  Call<MultiSearchComplaintResponse> getDashboardPiechartDetails(
      @Body MultiSearchRequest pieChartRequest);

  @POST(ApiConstant.UPLOADIMAGE)
  Call<RCImageUploadResponse> saveRCRespondImageResult(@Body DFoundWDoneImageEntity saveRequest);

  @POST(ApiConstant.UPLOADIMAGE)
  Call<RCImageDownloadResponse> saveRCRespondImageResult1(@Body DFoundWDoneImageEntity saveRequest);

  @POST(ApiConstant.DOWNLOADIMAGE)
  Call<RCImageDownloadResponse> getRespondImageResult(@Body RCDownloadImageRequest imageRequest);

  @POST(ApiConstant.UPLOADDOCUMENT)
  Call<CommonResponse> saveDocument(@Body DocumentTransaction saveRequest);

  @POST(ApiConstant.DOWNLOADDOCUMENT)
  Call<DocumentDownloadResponse> getDocumentResult(@Body DocumentTransaction saveRequest);

  @GET(ApiConstant.GETALLRESPONSEDETAILS)
  Call<RCViewRemarksEntity> getAllResponseDetailsResult();

  @POST(ApiConstant.GETSOFTSERVICECOMPLAINTDETAILS)
  Call<ReceiveComplaintResponse> getSoftServiceComplaintDetails(
      @Body HardSoftRequest hardSoftRequest,
      @Query("startIndex") int startIndex,
      @Query("limit") int limit);

  @POST(ApiConstant.GETALLSOFTSERVICEPPMDETAILS)
  Call<FetchPpmScheduleDetails> getSoftServicePPMDetails(
      @Body HardSoftRequest hardSoftRequest,
      @Query("startIndex") int startIndex,
      @Query("limit") int limit);

  @POST(ApiConstant.GETALLHARDSERVICEPPMDETAILS)
  Call<FetchPpmScheduleDetails> getHardServicePPMDetails(
      @Body HardSoftRequest hardSoftRequest,
      @Query("startIndex") int startIndex,
      @Query("limit") int limit);

  @POST(ApiConstant.GETHARDSERVICECOMPLAINTDETAILS)
  Call<ReceiveComplaintResponse> getHardServiceComplaintDetails(
      @Body HardSoftRequest hardSoftRequest,
      @Query("startIndex") int startIndex,
      @Query("limit") int limit);

  @POST(ApiConstant.GETSOFTSERVICECOMPLAINTBYCOMPLAINTID)
  Call<HardSoftViewResponse> getSoftServiceComplaintByComplaintId(
      @Body ReceiveComplaintViewRequest receiveComplaintViewRequest);

  @POST(ApiConstant.GETHARDSERVICECOMPLAINTBYCOMPLAINTID)
  Call<HardSoftViewResponse> getHardServiceComplaintByComplaintId(
      @Body ReceiveComplaintViewRequest receiveComplaintViewRequest);

  @POST(ApiConstant.SAVEHARDSERVICEREACT)
  Call<HardSoftViewResponse> savedHardServiceReact(@Body HardSoftRequest loginRequest);

  @POST(ApiConstant.SAVEHARDSERVICEPPM)
  Call<HardSoftViewResponse> savedHardServicePpm(@Body HardSoftRequest loginRequest);

  @POST(ApiConstant.SAVESOFTSERVICEPPM)
  Call<HardSoftViewResponse> savedSoftServicePpm(@Body HardSoftRequest loginRequest);

  @POST(ApiConstant.FETCHHARDSERVICEPPM)
  Call<HardSoftViewResponse> fetchHardServicePpm(@Body HardSoftRequest loginRequest);

  @POST(ApiConstant.FETCHSOFTSERVICEPPM)
  Call<HardSoftViewResponse> fetchSoftServicePpm(@Body HardSoftRequest loginRequest);

  @POST(ApiConstant.SAVESOFTSERVICEREACT)
  Call<HardSoftViewResponse> savedSoftServiceReact(@Body HardSoftRequest loginRequest);

  @POST(ApiConstant.FETCHHARDSERVICEREACT)
  Call<HardSoftViewResponse> fetchHardServiceReact(@Body HardSoftRequest loginRequest);

  @POST(ApiConstant.FETCHSOFTSERVICEREACT)
  Call<HardSoftViewResponse> fetchSoftServiceReact(@Body HardSoftRequest loginRequest);

  /*  @GET(ApiConstant.MATERIALMASTER)
  Call<CommonResponse> getMaterialMaster();*/

  @POST(ApiConstant.MATERIALMASTERSQLFileSIZENEW)
  Call<com.daemon.emco_android.model.common.CommonResponse> getMaterialMasterNew(
      @Body FileSizeRequest loginRequest);

  @GET(ApiConstant.MATERIALMASTERSQLFile)
  @Streaming
  Call<ResponseBody> downloadFile();

  @GET(ApiConstant.MATERIALMASTERFILESIZENEW)
  Call<com.daemon.emco_android.model.common.CommonResponse> GetFileSizeData();


/* API call for Customer Survey  */


  @GET(ApiConstant.SURVEYCUSTOMER)
  Call<SurveyCustomerResponse> getSurveyCustomer();


  @GET(ApiConstant.SURVEYLOCATION)
  Call<SurveyLocationResponse> getSurveyLocation( @Query("contractno") String contractno);


  @GET(ApiConstant.SURVEYCONTRACT)
  Call<SurveyContractResponse> getSurveyContract(
          @Query("customercode") String customercode);


  @GET(ApiConstant.SURVEYREFERENCE)
  Call<SurveyReferenceReponse> getSurveyReference(
          @Query("customercode") String customercode);


  @GET(ApiConstant.SURVEYQUESTIONNAIRE)
  Call<SurveyQuestionnaireResponse> getSurveyQuestionnaire(
          @Query("opco") String opco, @Query("customercode") String customercode, @Query("surveyref") String surveyref,@Query("surveyType") String surveyType);


  @POST(ApiConstant.SURVEYSAVE)
  Call<CommonResponse> saveSurvey
         (@Body SurveyTransaction survey);


  /* API call for Location Finder  */

  @GET(ApiConstant.LOCATIONOPCO)
  Call<OpcoResponse> getLocationOpco(@Query("userid") String userid);

  @POST(ApiConstant.LOCATIONBUILDING)
  Call<LocationResponse> getLocationBuilding
          (@Body LocationDetail loc);

  @POST(ApiConstant.LOCATIONZONE)
  Call<LocationResponse> getLocationZone
          (@Body LocationDetail loc);

  @POST(ApiConstant.LOCATIONJOBNO)
  Call<LocationResponse> getLocationJobNo
          (@Body LocationDetail loc);

  @POST(ApiConstant.LOCATIONDETAIL)
  Call<LocationDetailResponse> getLocationDetail
          (@Body LocationDetail loc);


  @POST(ApiConstant.UPDATEEMPLOYEEGPS)
  Call<EmployeeGpsReponse> updateEmployeeGps
          (@Body  List<EmployeeTrackingDetail> emp);


}

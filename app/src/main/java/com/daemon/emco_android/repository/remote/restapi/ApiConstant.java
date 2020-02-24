package com.daemon.emco_android.repository.remote.restapi;

import android.content.Context;

public class ApiConstant {

    public static final String SUCCESS = "True";
    public static final String FAILURE = "False";
    // dev url
  /* public static final String BASE_URL = "http://125.17.102.143:8080/mobileapi/webapi/";
  public static final String BASE_URL_FILE = "http://125.17.102.143:8080/mobileapi/";*/

    // client url
  /*  public static final String BASE_URL = "http://my.agfacilities.com:8585/mobileapi/webapi/";
  public static final String BASE_URL_FILE = "http://my.agfacilities.com:8585/mobileapi/";*/
    public static final String LOGIN = "login/validateUser";
    public static final String CHANGEPASSWORD = "changePasswordRequest/changePassword";

    // http://10.111.111.41:8080/mobileapi

  /* // Production url
  public static final String BASE_URL = "http://10.111.111.41:8080/mobileapi/webapi/";
  public static final String BASE_URL_FILE = "http://10.111.111.41:8080/mobileapi/";*/

    /*public static final String BASE_URL = "http://10.111.111.37:8585/mobileapi/webapi/";
    public static final String BASE_URL_FILE = "http://10.111.111.37:8585/mobileapi/";*/
  //  public static final String URLVALID = "/mobileapi/webapi/";

     // public static final String PREFIXURL = "mobileapi";

      public static final String PREFIXURL = "mobileapi";

      public static final String URLVALID = "/"+PREFIXURL+"/webapi/";

    public static final String REGISTER = "userRegistration/addNewUser";

    public static final String USERTOKEN = "userRegistration/addUserToken";
    public static final String DELETEUSERTOKEN = "userRegistration/deleteUserToken";

    public static final String FORGOTPASSWORD = "forgetPassword/getNewPassword";
    public static final String LOGCOMPLAINT = "logcomplain/registerlogcomplain";
    public static final String CUSTOMERLOGCOMPLAINT = "logcomplain/customerLogComplaint";
    public static final String CATEGORY = "categoryMaster/getCategoryMaster";
    public static final String BUILDING = "buildingdetails/getBuildingDetails";
    public static final String ZONE = "zoneMaster/getZoneMaster";
    public static final String ALLZONE = "zoneMaster/getAllZonesByUserName";
    public static final String PRIORITY = "priorityMaster/getPriorityMaster";
    public static final String GETALLREPORTTYPES = "reporttype/getAllReportTypes";
    public static final String SITE = "siteMaster/getSiteMaster";
    public static final String WORKTYPE = "worktype/getAllWorkType";
    public static final String CONTRACT = "contractMaster/getContractMaster";
    public static final String SINGLESEARCHCOMPLAINT =
            "complainRecieve/getComplaintRecieveByComplainRefrenceNumber";
    public static final String SINGLESEARCHPREVENTIVE =
            "ppmWorkOrders/searchWorkOrderBasedOnPpmRefNo";
    public static final String MULTI_SEARCHCOMPLAINT = "complainRecieve/getAllComplaintByCondition";
    public static final String GETALLFORWARDEDCOMPLAINTBYEMPLOYEEID =
            "complainRecieve/getAllForwardedComplaintByEmployeeId";
    public static final String RECEIVECOMPLAINTLIST = "complainRecieve/getAllcomplain";
    public static final String RECEIVECOMPLAINTUNSIGNEDCOMPLAINT =
            "complainRecieve/getAllUnSignedComplaint";
    public static final String RECEIVECOMPLAINTVIEW = "complaintEntry/getComplaintEntryByComplaintId";
    public static final String FETCHALLCOMPLAINTDETAILS = "complaintEntry/getAllComplaintEntry";
    //old one maharaja used
  public static final String FETCHALLCOMPLAINTDETAILS_NEW =
      "complaintEntry/getAllComplaintByUserId";

    public static final String FETCHALLCOMPLAINTDETAILS_CHANGE =
            "complaintEntry/getAllComplaintByUserIdWithLimit";

//changed url regards laksman told
    /*public static final String FETCHALLCOMPLAINTDETAILS_NEW =
            "complainRecieve/getAllComplaintByUserId";*/

    public static final String RECEIVECOMPLAINT_CCC = "complaintEntry/reAssignToCCC";
    public static final String PPM_CCC = "ppmWorkOrders/reassignToCCC_Ppm";
    public static final String RECEIVECOMPLAINTVIEWASSETDETAILS = "assetMaster/getAssetDetails";
    public static final String SAVEASSETBARCODEPPM = "ppmScheduleDoc/saveAssetBarCodeForPpm";
    public static final String FETCHALLASSETBARCODEDETAILS = "assetMaster/getAllAssetMasterDetails";
    public static final String RECEIVECOMPLAINTRESPOND = "complaintEntry/respond";
    public static final String RECEIVECOMPLAINTRESPONDSAVE =
            "complaintEntry/saveReceivedComplaintEntry";
    public static final String RECEIVECOMPLAINTMATERIALREQUIRED =
            "materialrequired/getmaterialrequired";
    public static final String RECEIVECOMPLAINTSAVEMATERIALREQUIRED = "material/saveMaterial";
    public static final String PPMSAVEMATERIALREQUIRED = "ppmMaterialEntry/savePpmMaterialEntry";
    public static final String RECEIVECOMPLAINTGETMATERIALREQUIRED = "material/getMaterial";
    public static final String ALLRCMATERIALSAVED = "material/getAllMaterialsByUserId";
    public static final String PPMGETMATERIALREQUIRED = "ppmMaterialEntry/getPpmMaterialEntry";
    public static final String WORKSTATUS = "workstatus/getallworkstatus";
    public static final String ALLDEFECTFOUND = "defectFound/getAllDefectFound";

    public static final String SAVEHARDSERVICEREACT = "complaintEntry/saveHardServiceComment";
    public static final String SAVESOFTSERVICEREACT = "complaintEntry/saveSoftServiceComment";
    public static final String FETCHHARDSERVICEREACT =
            "complaintEntry/fetchHardServiceCommentsByComplaintNo";
    public static final String FETCHSOFTSERVICEREACT =
            "complaintEntry/fetchSoftServiceCommentsByComplaintNo";

    public static final String SAVEHARDSERVICEPPM = "ppmScheduleDoc/saveHardServicePpmComments";
    public static final String SAVESOFTSERVICEPPM = "ppmScheduleDoc/saveSoftServicePpmComments";
    public static final String FETCHSOFTSERVICEPPM = "ppmScheduleDoc/FetchSoftServicePpmComment";
    public static final String FETCHHARDSERVICEPPM = "ppmScheduleDoc/FetchHardServicePpmComment";

    public static final String ALLWORKDONE = "workdone/getAllWorkDone";
    public static final String DEFECTFOUND = "defectFound/getDefectfound";
    public static final String GETRISKASSESMENT = "ppmCheckList/getPpmRiskAssessmentDetails";
    public static final String GETALLPPMRESULTS = "ppmCheckList/getPpmEquipmentToolDetails";
    public static final String GETFETCHPPMSTATUS = "ppmStatus/getAllPpmStatus";
    public static final String WORKDONE = "workdone/getworkdone";
    public static final String WORKPENDINGREASON = "pendingreason/getallpendingreason";
    public static final String TECHREMARKS = "rateService/fetchCustomerSignature";
    public static final String SAVE_RISK_ASSES = "ppmCheckList/savePpmRiskAssessment";
    public static final String SAVE_EQUIPMENT_TOOLS = "ppmCheckList/saveEquipmentToolsDetails";
    public static final String GETPPM_FINDING = "ppmFindings/getPpmFindingAndRecommendation";
    public static final String SAVE_PPM_FINDING = "ppmFindings/savePpmFindingAndRecommendation";
    public static final String CUSTOMERREMARKS = "rateService/fetchCustomerSurvey";
    public static final String SAVERATEANDSHARE = "rateService/saveRatedService";
    public static final String FETCHRATESERVICE = "rateService/fetchRatedService";
    public static final String FETCHCUSTOMERFEEDBACK = "rateService/fetchCustomerFeedback";
    public static final String GETSAVEDPPMCHECKPARAMVALUE =
            "ppmCheckList/getSavedPpmCheckListMonthltService";
    public static final String GETSAVEDIMPPMCHECKHARD = "ppmCheckList/getHardServicePpmCheckList";
    public static final String GETSAVEDIMPPMCHECKSOFT = "ppmCheckList/getSoftServicePpmCheckList";
    public static final String GETPPMCHECKPARAMVALUE =
            "ppmCheckList/getPpmParameterValuesForPpmCheckList";
    public static final String SAVEPPMCHECKPARAMVALUE =
            " ppmCheckList/savePpmCheckListMonthlyService";

    public static final String SAVEASSETBARCODE = "complaintEntry/saveAssetBarCodeByComplaintId";

    public static final String FETCHPPENAME = "ppelist/getppelist";
    public static final String SAVEPPE = "ppe/saveppe";
    public static final String FETCHFEDBACKPPM = "feedback/getPpmFeedBackDetails";
    public static final String SAVEPPEPPM = "ppmCheckList/savePpeForPpm";
    public static final String SAVERAETPPM = "ppmCheckList/getSavedPpeRaetForPpm";
    public static final String AFTERSAVEPPE = "ppe/getppe";
    public static final String ALLAFTERSAVEPPE = "ppe/getAllPpeByUserId";

    public static final String CHECKCUSTOMERSIGNATURE =
            "ppmSchedule/checkPpmStatusForCustomerSignature";
    public static final String AFTERSAVEPPEPPM = "ppmCheckList/getAllPpeRaetForPpm";
    public static final String GETDASHBOARDDETAILS = "dashboard/getDashBoardDetails";
    public static final String GETDASHBOARDPIECHARTDETAILS = "dashboard/getDashBoardPieChartDetails";
    public static final String FEEDBACKEMPLOYEEDETAILS = "feedback/fetchContractEmployeeDetails";
    public static final String FEEDBACKEMPLOYEEDETAILSPPM =
            "feedback/getEmployeeDetailsForPpmFeedBack";
    public static final String SAVEFEEDBACKDETAILS = "feedback/saveContractEmployeeFeedBack";
    public static final String SAVEPPMFEEDBACKDETAILS = "  feedback/savePpmFeedBackDetails";

    public static final String FETCHFEEDBACKDETAILS = "feedback/fetchContractEmployeeFeedBack";
    public static final String FETCHALLEMPLOYEE = "feedback/getAllContractEmployeeDetails";
    public static final String FETCHALLFEEDBACKDETAILS = "feedback/getAllFeedbackByUserId";
    public static final String UPLOADIMAGE = "CMISHelper/UploadImage";
    public static final String DOWNLOADIMAGE = "CMISHelper/downloadImage";
    public static final String GETALLRESPONSEDETAILS = "responseDetails/getAllResponseDetails";
    public static final String GETSOFTSERVICECOMPLAINTDETAILS =
            "complainRecieve/getSoftServiceComplaintDetails";
    public static final String GETSOFTSERVICEPPMDETAILS =
            "ppmSchedule/getHardServicePpmScheduleDetails";
    public static final String GETALLSOFTSERVICEPPMDETAILS =
            "ppmScheduleDoc/getAllSoftServicePpmScheduleDocDetails";
    public static final String GETALLHARDSERVICEPPMDETAILS =
            "ppmScheduleDoc/getAllHardServicePpmScheduleDocDetails";
    public static final String GETHARDSERVICECOMPLAINTDETAILS =
            "complainRecieve/getHardServiceComplaintDetails";
    public static final String GETSOFTSERVICECOMPLAINTBYCOMPLAINTID =
            "complaintEntry/getSoftServiceComplaintByComplaintId";
    public static final String GETHARDSERVICECOMPLAINTBYCOMPLAINTID =
            "complaintEntry/getHardServiceComplaintByComplaintId";
    public static final String MATERIALMASTERSQLFile = "ScriptFiles/MaterialMaster.sql";
    public static final String MATERIALMASTERSQLFileSIZENEW =
            "materialrequired/getMaterialMasterSQLScriptFile";
    public static final String MATERIALMASTERFILESIZENEW =
            "materialrequired/getMaterialMasterScriptFileSize";
    public static final String PPMDETAILSLIST = "ppmWorkOrders/getPpmWorkOrderDetails";

    public static final String CONTRACTDETAILS = "ppmWorkOrders/getContractDetails";

    public static final String DOCUMENTTYPE = "documentUpload/getDocType";

    public static final String NATUREDESC = "ppmWorkOrders/getNatureDescription";

    public static final String PENDINGREASONS = "feedback/getPendingReasons";

    // public static final String MATERIALMASTER = "materialrequired/getAllMaterialsRequiredInBatch";
    public static final String FETCHPPMSCHEDULEDETAILS = "ppmSchedule/getPpmScheduleDetails";
    public static final String FETCHPPMSCHEDULEDOCBYWORKORDERNOAPI =
            "ppmScheduleDoc/getPpmScheduleDocByWorkOrderNo";


    public static final String SURVEYCUSTOMER = "CustomerSurvey/getSurveyCustomer";

    public static final String SURVEYLOCATION = "CustomerSurvey/getSurveyLocation";

    public static final String SURVEYCONTRACT = "CustomerSurvey/getSurveyContract";

    public static final String SURVEYREFERENCE = "CustomerSurvey/getSurveyReference";

    public static final String SURVEYQUESTIONNAIRE = "CustomerSurvey/getSurveyQuestionnaire";

    public static final String SURVEYSAVE = "CustomerSurvey/saveCustomerSurvey";


    /* Support Document Upload URL's */

    public static final String SUPPORTDOCLIST = "documentUpload/getReactiveDocs";

    /* Document upload and Download URL */

    public static final String UPLOADDOCUMENT = "DocumentUpload/UploadDocument";
    public static final String DOWNLOADDOCUMENT = "DocumentUpload/downloadDocument";


    /* Location Finder URL */

    public static final String LOCATIONOPCO = "LocationFinder/getLocationOpco";
    public static final String LOCATIONBUILDING = "LocationFinder/getLocationBuildingname";
    public static final String LOCATIONZONE = "LocationFinder/getLocationZonename";
    public static final String LOCATIONJOBNO = "LocationFinder/getLocationJobNo";
    public static final String LOCATIONDETAIL = "LocationFinder/getLocationDetail";

    /* EMPLOYEE GPS TRACKING URL */

    public static final String UPDATEEMPLOYEEGPS = "EmployeeTrackingApi/updateEmployeeGps";


    private static Context context;

    public ApiConstant(Context context) {
        this.context = context;
    }
}

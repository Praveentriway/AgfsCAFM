package com.daemon.emco_android.ui.fragments.attendance

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.daemon.emco_android.R
import com.daemon.emco_android.databinding.FragmentAttendanceBinding
import com.daemon.emco_android.listeners.DefectDoneImage_Listener
import com.daemon.emco_android.model.common.EmployeeList
import com.daemon.emco_android.model.common.EmployeeTrackingDetail
import com.daemon.emco_android.model.common.Login
import com.daemon.emco_android.model.response.DefectDoneImageUploaded
import com.daemon.emco_android.model.response.RCDownloadImage
import com.daemon.emco_android.repository.db.entity.DFoundWDoneImageEntity
import com.daemon.emco_android.repository.remote.EmployeeGpsRepository
import com.daemon.emco_android.repository.remote.EmployeeGpsRepository.EmployeeGPSListener
import com.daemon.emco_android.repository.remote.ReceiveComplaintRespondService
import com.daemon.emco_android.service.GPSTracker
import com.daemon.emco_android.ui.fragments.common.MainDashboard
import com.daemon.emco_android.utils.AppUtils
import com.daemon.emco_android.utils.BarcodeCaptureActivity
import com.daemon.emco_android.utils.GpsUtils
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_attendance.*
import uk.co.senab.photoview.PhotoViewAttacher
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AttendanceFragment : Fragment(), EmployeeGPSListener, DefectDoneImage_Listener {

    private var barcodeScanned : Boolean = false
    private lateinit var scannedVal: Array<String>
    private lateinit var mActivity: AppCompatActivity
    private lateinit var receiveComplaintRespond_service: ReceiveComplaintRespondService
    var thumbnail: Bitmap? = null
    private lateinit var mImagePathToBeAttached: String
    private var mImageToBeAttachedDefectFound: Bitmap? = null
    private lateinit var binding: FragmentAttendanceBinding
    private val THUMBNAIL_SIZE = 75
    private var opco="999"
    private var jobNo:String?=null
    private lateinit var refNo:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }

    private lateinit var viewl: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mActivity = (activity as AppCompatActivity?)!!
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_attendance, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (checkPermission()) {

        } else {
            requestPermission();
        }

        viewl=view

        if (AppUtils.getIn()) {
            setCheckedIn()
        } else {
            setCheckedOut()
        }

        binding.switcher.setOnCheckedChangeListener { checked ->
            if (checked) {
                setCheckedOut()
            }
            else{
                setCheckedIn()
            }
        }

        binding.btnGetDetails.setOnClickListener {

         fetchEmpDetails()

        }

        binding.imgPhoto.setOnClickListener {

            if(mImageToBeAttachedDefectFound==null){
                dispatchTakePhotoIntent()
            }
            else{
                showImage(AppUtils.getEncodedString(mImageToBeAttachedDefectFound))
            }

        }

        binding.btnCancel.setOnClickListener {

            binding.linearEnterEmployee.visibility=View.VISIBLE
            binding.linearEmployeeDetail.visibility=View.GONE
            binding.linearBottomBtn.visibility=View.GONE
            binding.linearEnterEmployee.visibility=View.VISIBLE
            binding.btnGetDetails.visibility=View.VISIBLE

            binding.tieEmployeeId.setText("")
            barcodeScanned=false
            scannedVal=arrayOf<String>()
        }

        binding.btnCancel.setOnClickListener {

            clearDetail()

        }

        binding.imgScan.setOnClickListener {
            scan()
        }

        binding.btnUpload.setOnClickListener {

            if(mImageToBeAttachedDefectFound==null){

                AppUtils.showErrorToast(mActivity, "Add image before saving the details.")
            }

            else{
                upload()
            }

        }

        binding.tieEmployeeId.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

               fetchEmpDetails()

                true
            }
            false
        }

        receiveComplaintRespond_service = ReceiveComplaintRespondService(mActivity)
        receiveComplaintRespond_service.setmCallbackImages(this)

    }

    private fun fetchEmpDetails(){
        if(binding.tieEmployeeId.text.length>3){
            EmployeeGpsRepository(context, this).getEmployeeList(binding.tieEmployeeId.text.toString())
        }

        else{
            AppUtils.showErrorToast(activity, "Please enter a valid employee id")
        }

    }

    private fun clearDetail(){

        binding.linearEnterEmployee.visibility=View.VISIBLE
        binding.linearEmployeeDetail.visibility=View.GONE
        binding.linearBottomBtn.visibility=View.GONE
        binding.linearEnterEmployee.visibility=View.VISIBLE
        binding.btnGetDetails.visibility=View.VISIBLE
         opco="999"
        jobNo=null
        refNo=""
        binding.tieEmployeeId.setText("")
        barcodeScanned=false
        scannedVal=arrayOf<String>()
        mImageToBeAttachedDefectFound=null
        binding.imgPhoto.setImageResource(R.drawable.photo)
        binding.switcher.visibility=View.GONE
        binding.txtStatus.visibility=View.GONE

    }

    private fun setCheckedIn() {
        binding.txtStatus.text = "IN"
        binding.switcher.setChecked(false, true)
    }

    private fun setCheckedOut() {
        binding.txtStatus.text = "OUT"
        binding.switcher.setChecked(true, true)

    }

    private val AS_BARCODE_CAPTURE = 9001

    private fun scan() {
        val requestId = AppUtils.getIdForRequestedCamera(AppUtils.CAMERA_FACING_BACK)
        if (requestId == -1) AppUtils.showDialog(context, "Camera not available") else {
            val intent = Intent(context, BarcodeCaptureActivity::class.java)
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, true)
            startActivityForResult(intent, AS_BARCODE_CAPTURE)
        }

    }


    private fun upload() {

        if (GpsUtils.isNetworkConnected(context)) {

           val gps = GPSTracker(context)

            val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
            val currentDate = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())
            val currentDateandTime = sdf.format(Date())
            val emp = EmployeeTrackingDetail()
            emp.compCode = opco
            emp.employeeId= binding.txtEmployeeId.text.toString()
            emp.createdBy=getUseID()
            refNo=binding.txtEmployeeId.text.toString()+"-"+currentDateandTime
            emp.refNo = refNo
            emp.transType = "TNA"
            emp.actionType= binding.txtStatus.text.toString()
            emp.lat = gps.latitude.toString()
            emp.lng = gps.longitude.toString()
            emp.trans_date = currentDate
            emp.deviceID = GpsUtils.getDeviceID(context)
            emp.deviceName = GpsUtils.getSerialNumber()
            emp.generalRefno=jobNo

            AppUtils.showProgressDialog(mActivity, "", false)

            EmployeeGpsRepository(context, this).updateEmployeeGps(emp, AppUtils.MODE_REMOTE)

        }
        else{

            AppUtils.showErrorToast(activity, "No Internet Connection")

        }

    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AS_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    val barcode: Barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject)
                 //   showDialog(context, barcode.displayValue)

                    scannedVal = barcode.displayValue.split("$$").toTypedArray()
             // binding.tieEmployeeId.setText(strs[0])
                    barcodeScanned=true
                    EmployeeGpsRepository(context, this).getEmployeeList(scannedVal[0])


                } else {
                    Log.d(ContentValues.TAG, "No barcode captured, intent data is null")
                }
            } else {
                Log.d(ContentValues.TAG, "Error")
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            val file = File(mImagePathToBeAttached)
            if (file.exists()) {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(mImagePathToBeAttached, options)
                options.inJustDecodeBounds = false
                mImageToBeAttachedDefectFound = BitmapFactory.decodeFile(mImagePathToBeAttached, options)
                thumbnail = ThumbnailUtils.extractThumbnail(
                        mImageToBeAttachedDefectFound, THUMBNAIL_SIZE, THUMBNAIL_SIZE)
                file.delete()

            }
            mImagePathToBeAttached = ""

            binding.imgPhoto.setImageBitmap(thumbnail)

        }
    }

    private val REQUEST_TAKE_PHOTO = 1

    private fun dispatchTakePhotoIntent() {
        try {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (e: IOException) {
                }
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                    takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = timeStamp + "_"
        val storageDir: File? = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(fileName, ".jpg", storageDir)
        mImagePathToBeAttached = image.absolutePath
        return image
    }




    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_home).isVisible = true
        menu.findItem(R.id.action_logout).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                val fm = activity!!.supportFragmentManager
                var i = 0
                while (i < fm.backStackEntryCount) {
                    fm.popBackStack()
                    ++i
                }
                val _fragment: Fragment = MainDashboard()
                val _transaction = fm.beginTransaction()
                _transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                _transaction.replace(R.id.frame_container, _fragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showImage(base64: String){

        val display = mActivity.windowManager.defaultDisplay
        val width = display.width
        val height = display.height

        loadPhoto(base64, width, height)

    }

    private fun loadPhoto(base64: String, width: Int, height: Int) {
        val dialog = Dialog(mActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val inflater = mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                viewl.findViewById(R.id.layout_root) as? ViewGroup)
        val image = layout.findViewById<View>(R.id.fullimage) as ImageView
        image.setImageBitmap(AppUtils.getDecodedString(base64))
        image.layoutParams.height = height
        image.layoutParams.width = width
        val mAttacher = PhotoViewAttacher(image)
        image.requestLayout()
        dialog.setContentView(layout)
        dialog.show()
    }

    override fun onSuccessGpsUpdate(strMsg: String?, mode: Int) {
        uploadImage()
    }

    override fun onFailureGpsUpdate(strErr: String?, mode: Int) {
        AppUtils.hideProgressDialog()
        AppUtils.showErrorToast(mActivity, strErr)
    }

    override fun onReceiveEmployeeList(obj: MutableList<EmployeeList>?) {

        opco= obj?.get(0)?.divison.toString()
        jobNo= obj?.get(0)?.jobNo.toString()
        binding.txtEmployeeId.text = obj?.get(0)?.employeeId
        binding.txtEmployeeName.text = obj?.get(0)?.employeeName
        binding.txtEmployeeDesignation.text = obj?.get(0)?.designation
        binding.txtEmployeeDivision.text = obj?.get(0)?.divisonName
        binding.txtEmployeeJobDetail.text = obj?.get(0)?.jobNo+" - "+obj?.get(0)?.jobName

      showDetails()

    }


    private fun showDetails(){

        binding.linearEmployeeDetail.visibility=View.VISIBLE
        binding.btnGetDetails.visibility=View.GONE
        binding.linearEnterEmployee.visibility=View.GONE
        binding.linearBottomBtn.visibility=View.VISIBLE
        binding.switcher.visibility=View.VISIBLE
        binding.txtStatus.visibility=View.VISIBLE

    }


    override fun onReceiveFailureEmployeeList(err: String?) {

        if(barcodeScanned){
            binding.txtEmployeeId.text = scannedVal[0]

            if(scannedVal.size>1){

                binding.txtEmployeeName.text = scannedVal[1]
                binding.txtEmployeeDesignation.text = scannedVal[2]
                binding.txtEmployeeDivision.text = "-"
                binding.txtEmployeeJobDetail.text = "-"

            }
            else{

                binding.txtEmployeeName.text = "-"
                binding.txtEmployeeDesignation.text = "-"
                binding.txtEmployeeDivision.text = "-"
                binding.txtEmployeeJobDetail.text = "-"

            }

        }
        else{

            binding.txtEmployeeId.text = tie_employee_id.text
            binding.txtEmployeeName.text = "-"
            binding.txtEmployeeDesignation.text = "-"
            binding.txtEmployeeDivision.text = "-"
            binding.txtEmployeeJobDetail.text = "-"

        }

        showDetails()

    }

    private fun getUseID(): String? {
        var mStrEmpId = ""
        val gson = Gson()
        val mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE)
       val mEditor = mPreferences.edit()
        val mStringJson = mPreferences.getString(AppUtils.SHARED_LOGIN, null)
        if (mStringJson != null) {
            val user = gson.fromJson(mStringJson, Login::class.java)
            mStrEmpId = user.employeeId
        }
        return mStrEmpId
    }

    private fun uploadImage(){

        val imageEntity = DFoundWDoneImageEntity(binding.txtStatus.text.toString())
        imageEntity.opco = opco
        imageEntity.fileType = "jpg"
        imageEntity.transactionType = "TNA"
        imageEntity.docType = binding.txtStatus.text.toString()
        imageEntity.refDocNo = refNo
        imageEntity.createdBy = getUseID()
        imageEntity.modifiedBy = getUseID()
        imageEntity.generalRefNo = ""
        imageEntity.base64Image = AppUtils.getEncodedString(mImageToBeAttachedDefectFound)

        if (AppUtils.checkInternet(context)) {
            receiveComplaintRespond_service.saveComplaintRespondImageData1(imageEntity, activity)
        }

    }

    override fun onImageSaveReceivedSuccess1(imageEntity: RCDownloadImage?, mode: Int) {

        AppUtils.hideProgressDialog()
        clearDetail()
        AppUtils.showToast(mActivity, "Record saved Successfully.")

    }

    override fun onImageSaveReceivedSuccess(mImageEntity: DefectDoneImageUploaded?, mode: Int) {
        TODO("Not yet implemented")
    }

    override fun onImageReceivedSuccess(imageEntity: RCDownloadImage?, mode: Int) {
        TODO("Not yet implemented")
    }

    override fun onImageSaveReceivedFailure(strErr: String?, mode: Int) {
        AppUtils.hideProgressDialog()
        AppUtils.showDialog(mActivity, strErr)
    }

    override fun onImageReceivedFailure(strErr: String?, mode: Int) {
        TODO("Not yet implemented")
    }

    override fun onAllImagesReceived(mImageEntities: MutableList<DFoundWDoneImageEntity>?, mode: Int) {
        TODO("Not yet implemented")
    }


    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private val PERMISSION_REQUEST_CODE = 200


    private fun requestPermission() {
        ActivityCompat.requestPermissions(mActivity, arrayOf(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray){
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                // main logic
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(mActivity, "You need to allow access permissions", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
    }


}
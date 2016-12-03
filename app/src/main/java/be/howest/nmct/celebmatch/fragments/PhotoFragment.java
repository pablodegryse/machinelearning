package be.howest.nmct.celebmatch.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import be.howest.nmct.celebmatch.MainActivity;
import be.howest.nmct.celebmatch.R;
import be.howest.nmct.celebmatch.service.IUploadService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class PhotoFragment extends Fragment {
    private Retrofit mUploadHandler;
    private IUploadService mUploadService;
    private String mCurrentPhotoPath;
    private Uri mPhotoURI;
    private String[] mCamIds;
    private CameraManager mCamManager;
    private iPhotoFragmentListener mListener;
    private MainActivity mActivity;
    private Context mContext;
    private ImageView mImageView;
    private Button mBtnRetake;
    private Button mBtnPredict;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 621;
    private static final int REQUEST_PHOTO=123;

    public PhotoFragment() {

    }

    public static PhotoFragment newInstance() {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView=inflater.inflate(R.layout.fragment_photo, container, false);
        mImageView=(ImageView) myView.findViewById(R.id.imagePhotoTaken);
        mBtnRetake=(Button) myView.findViewById(R.id.btnRetake);
        mBtnPredict=(Button) myView.findViewById(R.id.btnPredict);
        initRetrofit();
        setListeners();
        //camera shizzle initialiseren
        try {
            initCamera();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return myView;
    }

    private void setListeners(){
        mBtnRetake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RETAKE","bldpelpdeld");
                checkPermission();
            }
        });
        mBtnPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PREDICT","LETS GOOGOooGOOGOoOGOoGo");
                if(mPhotoURI!=null){
                    uploadImageToPredict();
                }else {
                    Snackbar.make(mActivity.findViewById(R.id.frameLayoutMain), "Please retake the photo.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity=(MainActivity)context;
        this.mContext=context;
        if (context instanceof iPhotoFragmentListener) {
            mListener = (iPhotoFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement iPhotoFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    private void initCamera() throws CameraAccessException {
        Log.e("INIT","camera");
        mCamManager=(CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        mCamIds=mCamManager.getCameraIdList();
        if(mCamIds!=null){
            checkPermission();
        }
    }

    private void checkPermission(){
        Log.e("CHECKING","permission");
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }else{
            openCamerWithIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.e("FEEDBACK","entered feedback cb");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    Log.e("FEEDBACK","THE USERS AGREED TO ALLOW CAM ACCESS");
                    //camera intent
                    openCamerWithIntent();
                }else {
                    Snackbar.make(mActivity.findViewById(R.id.frameLayoutMain), "Permission denied :(", Snackbar.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void openCamerWithIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mPhotoURI = FileProvider.getUriForFile(mActivity,"be.howest.nmct.celebmatch",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoURI);
                startActivityForResult(takePictureIntent,REQUEST_PHOTO);
                Log.d("URI",mCurrentPhotoPath);
            }
        }else {
            Snackbar.make(mActivity.findViewById(R.id.frameLayoutMain), "No camera app found :'(", Snackbar.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("ddMMyy_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_CELEBMATCH";
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void uploadImageToPredict(){
        File uploadFile = new File(mPhotoURI.getPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),uploadFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", uploadFile.getName(), requestFile);
        Call<ResponseBody> uploadCall=mUploadService.uploadImageToPredict(body);
        uploadCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("success","YAAAAAAAY");
                mListener.ShowResult(response.body().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error","BOOOOOOOO");
                Log.e("UPLOAD_ERROR",t.getLocalizedMessage());
            }
        });
    }

    private void initRetrofit(){
        mUploadHandler=new Retrofit.Builder().baseUrl("http://www.testserver.be/").build();
        mUploadService=mUploadHandler.create(IUploadService.class);
    }

    public String getPath(Uri uri) {
        File myFile = new File(uri.getPath());
        return myFile.getAbsolutePath();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHOTO && resultCode == RESULT_OK) {
            mImageView.setImageURI(mPhotoURI);
        }
    }

    public interface iPhotoFragmentListener {
        void ShowResult(String result);
    }
}

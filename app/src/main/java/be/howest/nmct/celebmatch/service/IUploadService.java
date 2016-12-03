package be.howest.nmct.celebmatch.service;


import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IUploadService {
    @Multipart
    @POST("/predictClient")
    Call<ResponseBody> uploadImageToPredict(@Part MultipartBody.Part file);
}

package ussrfantom.com.example.myscanner.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import ussrfantom.com.example.myscanner.pojo.ProductResponse;

public interface ApiService {
    @GET("project_info.json")
    Observable<ProductResponse> getProducts();

}

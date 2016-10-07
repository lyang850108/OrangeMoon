package com.orangemoo.com.beta.network;


import com.orangemoo.com.beta.model.ProgramItem;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

public interface RetrofitService {
    @GET("/data/{startNum}")
    List<ProgramItem> getProgramList(@Path("startNum") int num);
}

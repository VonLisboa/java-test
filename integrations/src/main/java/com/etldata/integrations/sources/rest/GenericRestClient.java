package com.etldata.integrations.sources.rest;

import java.io.IOException;
import java.util.Map;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GenericRestClient {
  private final Dotenv dotenv = Dotenv.configure()
      .ignoreIfMalformed()
      .ignoreIfMissing()
      .load();

  private final OkHttpClient client = new OkHttpClient();
  private final Moshi moshi = new Moshi.Builder().build();
  private final JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
  private final Character SEP = '/';
  
  private String url;


  public GenericRestClient(String url){
    this.url = url;
  }

  public GenericRestClient(String scheme, String host){
    new GenericRestClient(scheme, host, "");
  }

  public GenericRestClient(String scheme, String host, String version){
    this.url = scheme + SEP + host;
    if (!version.isBlank()){
      this.url += SEP + version;
    }
  }

  public Map<String, ?> get(String endpoint, String params) throws Exception {
    this.url += SEP + endpoint;
    this.url+= "?" + params;
    
    Request request = new Request.Builder().url(this.url).build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

      @SuppressWarnings("unchecked")
      Map<String, ?> json = jsonAdapter.fromJson(response.body().source());
      
      if(dotenv.get("DEBUG") == "true"){
        System.out.println(json.toString());
      }

      return json;
    }
  }
  
    
}

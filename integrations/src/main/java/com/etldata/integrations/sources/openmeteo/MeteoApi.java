package com.etldata.integrations.sources.openmeteo;

import java.util.Map;

import com.etldata.integrations.sources.rest.GenericRestClient;

public class MeteoApi {
    private final String url = "https://api.open-meteo.com/v1";

    public MeteoApi() {

    }


    public Map<String, ?> forecast(String params) throws Exception {
        GenericRestClient rest = new GenericRestClient(url);
        return rest.get("forecast", params);
    }
}
    /*
    static class Gist {
      double latitude;
      double longitude;
      double generationtime_ms;
      double utc_offset_seconds;
      String timezone;
      String timezone_abbreviation;
      double elevation; 
      Map<String, Double> current_weather;
    }
    */

package com.etldata.app;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.etldata.integrations.destinations.DynamicCsv;
import com.etldata.integrations.destinations.LocalStorage;
import com.etldata.integrations.sources.openmeteo.MeteoApi;
import com.etldata.integrations.sources.rest.GenericRestClient;

import io.github.cdimascio.dotenv.Dotenv;

public class App {
    Dotenv dotenv = Dotenv.load();
    static MeteoApi meteo = new MeteoApi();
    static GenericRestClient rest = new GenericRestClient("https://", "api.open-meteo.com", "v1");
    static DynamicCsv csv = new DynamicCsv();

    public static void main(String[] args) throws IOException {
        try {
            LocalDateTime now = LocalDateTime.now();  
            LocalStorage store = new LocalStorage("meteo"+now+".json");

            Map json1 = rest.get("forecast", "latitude=52.52&longitude=13.41&current_weather=true");
            Map json2 = meteo.forecast("latitude=52.52&longitude=13.41&hourly=temperature_2m,relativehumidity_2m,windspeed_10m");
            
            store.saveJson(json1);
            csv.saveToTable(json2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

// https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m,relativehumidity_2m,windspeed_10m
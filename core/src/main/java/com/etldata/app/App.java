package com.etldata.app;

import java.io.IOException;
import java.util.Map;

import com.etldata.integrations.destinations.DynamicMysql;
import com.etldata.integrations.destinations.LocalStorage;
import com.etldata.integrations.sources.openmeteo.MeteoApi;
import com.etldata.integrations.sources.rest.GenericRestClient;

import io.github.cdimascio.dotenv.Dotenv;

public class App {
    Dotenv dotenv = Dotenv.load();
    static MeteoApi meteo = new MeteoApi();
    static GenericRestClient rest = new GenericRestClient("https://", "api.open-meteo.com", "v1");
    static DynamicMysql mysql = new DynamicMysql();

    public static void main(String[] args) throws IOException {
        try {
            LocalStorage store = new LocalStorage("test.json");

            Map json = rest.get("forecast", "latitude=52.52&longitude=13.41&current_weather=true");

            //Map json = meteo.forecast("latitude=52.52&longitude=13.41&hourly=temperature_2m,relativehumidity_2m,windspeed_10m");
            store.saveJson(json);
            
            mysql.saveToTable(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

// https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m,relativehumidity_2m,windspeed_10m
package com.etldata.integrations.destinations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

public class LocalStorage {
    private final BufferedWriter out;
    private final Moshi moshi = new Moshi.Builder().build();
    private final JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);

    public LocalStorage(String destination) throws FileNotFoundException, IOException{
        File file = new File(destination);
        out = new BufferedWriter(new FileWriter(file));

    }

    public void save(Map<String, ?> json) throws IOException {
        jsonAdapter.toJson(json);
        out.write(json.toString());
        out.flush();
        out.close();
    }

    public void saveJson(Map<String, ?> json) throws IOException {
        out.write(jsonAdapter.toJson(json));
        out.flush();
        out.close();
    }
}

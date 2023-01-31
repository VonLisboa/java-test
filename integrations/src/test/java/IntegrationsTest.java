import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.etldata.integrations.destinations.LocalStorage;
import com.etldata.integrations.sources.openmeteo.MeteoApi;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

public class IntegrationsTest {
    
    @Test 
    void testLocalStorage() throws Exception {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
        LocalStorage store = new LocalStorage("test.json");
        MeteoApi meteo = new MeteoApi();
        Map json = meteo.forecast("latitude=52.52&longitude=13.41&current_weather=true");
        store.saveJson(json);

        File file = new File("test.json");
        assumeTrue(file.exists());

        String strFile = Files.readString(Path.of("test.json"));
        assertEquals(strFile, jsonAdapter.toJson(json));
    }

}

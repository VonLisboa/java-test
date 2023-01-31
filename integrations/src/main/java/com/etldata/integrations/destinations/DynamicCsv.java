package com.etldata.integrations.destinations;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;

public class DynamicCsv {
    final LocalDateTime NOW = LocalDateTime.now();  
    ArrayList<String> otherTable = new ArrayList<String>();
    
    public void saveToTable(Map<String, Object> jsonIteration) throws StreamWriteException, DatabindException, IOException {     
        Builder csvSchemaBuilder = CsvSchema.builder();
        Map<String, Object> json = new HashMap<String, Object>();

        for (Map.Entry<String, Object> entry : jsonIteration.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }

        for (String key : jsonIteration.keySet()) {         
            if  (json.get(key) instanceof Map) {
                Map<String, ?> map = (Map<String, ?>) json.remove(key);

                for (String key2 : map.keySet()) {
                    if  (map.get(key2) instanceof ArrayList) {
                        // provavel recursividade
                        createParentTable(key2, (ArrayList) map.get(key2));
                        continue;
                    }

                    json.put(key2, map.get(key2));
                    csvSchemaBuilder.addColumn(key2);
                }
                continue;
            }
            csvSchemaBuilder.addColumn(key);
        }

        CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.writerFor(Map.class)
          .with(csvSchema)
          .writeValue(new File("meteo"+ NOW + ".csv"), json);        
    }

    private void createParentTable(String tableName, ArrayList<?> subTableIteration) throws StreamWriteException, DatabindException, IOException {     
        /* first search for map first

        subTableIteration.forEach(it -> {
            if (it instanceof Map) {
                createParentTableMap(tableName, (Map<String, Object>) it);
            }
        });
        */
        Builder csvSchemaBuilder = CsvSchema.builder();
        csvSchemaBuilder.addColumn(tableName); 
        CsvSchema csvSchema = csvSchemaBuilder.build().withHeader().withArrayElementSeparator(",");
        CsvMapper csvMapper = new CsvMapper();

        // how to print by line ?
        csvMapper.writerFor(ArrayList.class).with(csvSchema)
        .writeValue(new File("meteo_"+ tableName + "_" +NOW + ".csv"), subTableIteration);        
          
    }

    private void createParentTableMap(String tableName, Map<String, Object> subTableIteration) throws StreamWriteException, DatabindException, IOException {     
        Builder csvSchemaBuilder = CsvSchema.builder();
        Map<String, Object> json = new HashMap<String, Object>();

        for (Map.Entry<String, Object> entry : subTableIteration.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }

        for (String key : subTableIteration.keySet()) {         
            /*if  (json.get(key) instanceof Map) {
                Map<String, ?> map = (Map<String, ?>) json.remove(key);

                for (String key2 : map.keySet()) {
                    if  (map.get(key2) instanceof ArrayList) {
                        // recursividade
                        // createParentTable()
                        continue;
                    }

                    json.put(key2, map.get(key2));
                    csvSchemaBuilder.addColumn(key2);
                }
                continue;
            }*/
            csvSchemaBuilder.addColumn(key);
        }

        CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.writerFor(Map.class)
          .with(csvSchema)
          .writeValue(new File("meteo_"+ tableName + "_" +NOW + ".csv"), json);        
    }
    
}

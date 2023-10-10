package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        try (var fileStream = ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            var objectMapper = new ObjectMapper();
            var measurementListType = objectMapper.getTypeFactory().constructCollectionType(List.class, Measurement.class);
            return objectMapper.readValue(fileStream, measurementListType);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}

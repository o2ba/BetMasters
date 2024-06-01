package util.spring;

import object.security.SensitiveData;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToSensitiveDataConverterUtil implements Converter<String, SensitiveData> {
    @Override
    public SensitiveData convert(@NotNull String source) {
        return new SensitiveData(source);
    }
}
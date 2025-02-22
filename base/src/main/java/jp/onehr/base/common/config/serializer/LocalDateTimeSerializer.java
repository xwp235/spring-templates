package jp.onehr.base.common.config.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import jp.onehr.base.common.config.properties.SystemProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
    
    private final DateTimeFormatter formatter;
    private final String zoneId;

    public LocalDateTimeSerializer(SystemProperties systemProperties) {
        super(LocalDateTime.class);
        this.zoneId = systemProperties.getDefaultTimezone();
        String datetimePattern = systemProperties.getDefaultDatetimePattern();
        this.formatter = DateTimeFormatter
                .ofPattern(datetimePattern)
                .withZone(ZoneId.of(this.zoneId));
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String formattedDate = value.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of(zoneId))
                .format(formatter);
        gen.writeString(formattedDate);
    }

}

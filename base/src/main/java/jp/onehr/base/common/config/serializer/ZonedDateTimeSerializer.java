package jp.onehr.base.common.config.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import jp.onehr.base.common.config.properties.SystemProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {

    private final DateTimeFormatter formatter;

    public ZonedDateTimeSerializer(SystemProperties systemProperties) {
        super(ZonedDateTime.class);
        this.formatter = DateTimeFormatter
                .ofPattern(systemProperties.getDefaultDatetimePattern())
                .withZone(ZoneId.of(systemProperties.getDefaultTimezone()));
    }

    @Override
    public void serialize(ZonedDateTime zonedDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(zonedDateTime.format(formatter));
    }
}

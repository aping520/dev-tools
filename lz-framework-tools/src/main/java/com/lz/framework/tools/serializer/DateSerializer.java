package com.lz.framework.tools.serializer;

import java.io.IOException;
import java.util.Date;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.lz.framework.tools.constants.Constants;

public class DateSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		if (value != null) {
			jsonGenerator.writeString(new DateTime(value).toString(Constants.DATE_FORMAT.DATE_FORMAT0));
		}
	}
}

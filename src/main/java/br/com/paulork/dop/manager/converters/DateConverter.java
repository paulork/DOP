package br.com.paulork.dop.manager.converters;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.converter.ConversionException;
import br.com.caelum.vraptor.converter.ConversionMessage;
import br.com.caelum.vraptor.converter.Converter;
import java.sql.Time;
import java.text.SimpleDateFormat;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
@Alternative
@Priority(Interceptor.Priority.APPLICATION + 100)
@Convert(Date.class)
public class DateConverter implements Converter<Date> {

    public static final String INVALID_MESSAGE_KEY = "is_not_a_valid_date";

    @Override
    public Date convert(String value, Class<? extends Date> type) {
        if (isNullOrEmpty(value)) {
            return null;
        }
        try {
            Date date = getDateFormat().parse(value);
            return (type.getCanonicalName().equals("java.sql.Time") ? new Time(date.getTime()) : date);
        } catch (ParseException pe) {
            pe.printStackTrace();
            throw new ConversionException(new ConversionMessage(INVALID_MESSAGE_KEY, value));
        }
    }

    protected DateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    }
}

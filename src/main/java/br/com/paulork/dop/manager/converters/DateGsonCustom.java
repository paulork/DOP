package br.com.paulork.dop.manager.converters;

import br.com.caelum.vraptor.serialization.gson.DateGsonConverter;
import br.com.paulork.dop.utils.UtilDate;
import static com.google.common.base.Strings.isNullOrEmpty;
import com.google.gson.JsonDeserializationContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.enterprise.context.Dependent;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
@Dependent
@Alternative
@Priority(Interceptor.Priority.APPLICATION + 100)
public class DateGsonCustom extends DateGsonConverter implements JsonDeserializer<Date>, JsonSerializer<Date> {

    @Override
    public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
        String dateString = getFormat().format(date);
        
        if (dateString.substring(0, 4).equals("1970")) {
            String nowString = getFormat().format(new Date());
            String data = nowString.split("T")[0];
            String time = dateString.split("T")[1];
            
            String[] sDate = data.split("-");
            String[] sTime = time.split(":");
            
            LocalDateTime ldt = LocalDateTime.of(Integer.parseInt(sDate[0]), Integer.parseInt(sDate[1]), Integer.parseInt(sDate[2]), Integer.parseInt(sTime[0]), Integer.parseInt(sTime[1]), 0, 0);
            Date localDateToDate = UtilDate.localDateTimeToDate(ldt);
            
            dateString = getFormat().format(localDateToDate);
            return new JsonPrimitive(dateString);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            int seconds = calendar.get(Calendar.SECOND);
            if (hours == 0 && minutes == 0 && seconds == 0) {
                String[] split = new SimpleDateFormat("yyyy-MM-dd").format(date).split("-");
                LocalDateTime dtS = LocalDateTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), 12, 12, 12);
                Instant instant = dtS.atZone(ZoneId.systemDefault()).toInstant();
                Date res = Date.from(instant);
                return new JsonPrimitive(getFormat().format(res));
            } else {
                return new JsonPrimitive(getFormat().format(date));
            }
        }
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            if (isNullOrEmpty(json.getAsString())) {
                return null;
            }
            Date data = getFormat().parse(json.getAsString());
            return (typeOfT.getTypeName().equals("java.sql.Time")) ? new Time(data.getTime()) : data;
        } catch (ParseException e) {
            throw new JsonSyntaxException(json.getAsString(), e);
        }
    }

    @Override
    protected DateFormat getFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    }

}

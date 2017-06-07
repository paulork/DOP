package br.com.paulork.dop.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
public class UtilDate {

    public static final ZoneId ZONEID_BR = ZoneId.systemDefault();
    public static final Locale LOCALE_BR = new Locale("pt", "BR");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Deprecated
    private UtilDate() {
    }

    public static LocalDate dateToLocalDate(Date data) {
        if (data == null) {
            return null;
        }
        Instant instant = Instant.ofEpochMilli(data.getTime());
        return LocalDateTime.ofInstant(instant, ZONEID_BR).toLocalDate();
    }

    public static Date localDateToDate(LocalDate data) {
        return Date.from(data.atStartOfDay(ZONEID_BR).toInstant());
    }

    public static Date localDateTimeToDate(LocalDateTime data) {
        return Date.from(data.atZone(ZONEID_BR).toInstant());
    }

    public static LocalTime dateToLocalTime(Date data) {
        Instant instant = Instant.ofEpochMilli(data.getTime());
        return LocalDateTime.ofInstant(instant, ZONEID_BR).toLocalTime();
    }

    public static LocalDateTime dateToLocalDateTime(Date data) {
        Instant instant = Instant.ofEpochMilli(data.getTime());
        return LocalDateTime.ofInstant(instant, ZONEID_BR);
    }

    public static String getNomeMesAbrev(int mes) {
        return Month.of(mes).getDisplayName(TextStyle.SHORT, LOCALE_BR);
    }

    public static String getNomeMes(int mes) {
        return Month.of(mes).getDisplayName(TextStyle.FULL, LOCALE_BR);
    }

    /**
     * Segunda = 1, Domingo = 7
     */
    public static String getNomeDiaSemanaISO(int dia) {
        return DayOfWeek.of(dia).getDisplayName(TextStyle.SHORT, LOCALE_BR);
    }

    public static String getNomeDiaSemana(int dia) {
        if (dia == 1) {
            return "Domingo";
        }
        if (dia == 2) {
            return "Segunda-Feira";
        }
        if (dia == 3) {
            return "Terça-Feira";
        }
        if (dia == 4) {
            return "Quarta-Feira";
        }
        if (dia == 5) {
            return "Quinta-Feira";
        }
        if (dia == 6) {
            return "Sexta-Feira";
        }
        if (dia == 7) {
            return "Sábado";
        }
        return "";
    }

    public static long diferencaEmHoras(Date dtIni, Date dtFim) {
        return Duration.between(dateToLocalDateTime(dtIni), dateToLocalDateTime(dtFim)).toHours();
    }

    public static long diferencaEmHoras(LocalDateTime dtIni, LocalDateTime dtFim) {
        return Duration.between(dtIni, dtFim).toHours();
    }

    public static double diferencaHoras(LocalDateTime dtIni, LocalDateTime dtFim) {
        long mins = Duration.between(dtIni, dtFim).toMinutes();
        return new BigDecimal("" + mins).divide(new BigDecimal("60"), 2, RoundingMode.HALF_UP).doubleValue();
    }

    public static double diferencaHoras(Date dtIni, Date dtFim) {
        return diferencaHoras(dateToLocalDateTime(dtIni), dateToLocalDateTime(dtFim));
    }

    public static long diferencaEmDias(LocalDate dtIni, LocalDate dtFim) {
        return Duration.between(dtIni.atStartOfDay(), dtFim.atStartOfDay()).toDays();
    }

    public static long diferencaEmDias(Date dtIni, Date dtFim) {
        return diferencaEmDias(dateToLocalDate(dtIni), dateToLocalDate(dtFim));
    }

    public static String diferencaDatasParaHoje(Date data, Time hora) {
        long between = Duration.between(LocalDateTime.of(dateToLocalDate(data), hora.toLocalTime()), LocalDateTime.now()).toDays();
        return (between > 0 ? "Há " + between + " dia(s)" : " Há menos de 1 dia");
    }

    public static String diferencaEntreDatas(Date dataInicio, Time horaInicio, Date dataFim, Time horaFim) {
        LocalDateTime dataInicioLocalDate = LocalDateTime.of(dateToLocalDate(dataInicio), horaInicio.toLocalTime());
        LocalDateTime dataFimLocalDate = LocalDateTime.of(dateToLocalDate(dataFim), horaFim.toLocalTime());
        long between = Duration.between(dataInicioLocalDate, dataFimLocalDate).toDays();
        return (between > 0 ? "Em " + between + " dia(s)" : " Em menos de 1 dia");
    }

    public static boolean comparaDataHora(Date dataInicio, Time horaInicio, Date dataFim, Time horaFim) {
        LocalDateTime dataInicioLocalDate = LocalDateTime.of(dateToLocalDate(dataInicio), horaInicio.toLocalTime());
        LocalDateTime dataFimLocalDate = LocalDateTime.of(dateToLocalDate(dataFim), horaFim.toLocalTime());
        return (dataInicioLocalDate.compareTo(dataFimLocalDate) == 0);
    }

    public static LocalDate primeiroDiaMes(LocalDate data) {
        return data.withDayOfMonth(1);
    }

    public static LocalDate primeiroDiaMes(int mes, int ano) {
        return LocalDate.of(ano, mes, 1);
    }

    public static LocalDate ultimoDiaDoMes(int mes, int ano) {
        LocalDate l = LocalDate.of(ano, mes, 1);
        return l.withDayOfMonth(l.lengthOfMonth());
    }

    public static int ultimoDiaMes(Date data) {
        return dateToLocalDate(data).lengthOfMonth();
    }

    public static LocalDate ultimoDiaMes(LocalDate data) {
        return data.withDayOfMonth(data.lengthOfMonth());
    }

    public static int ultimoDiaMes(int mes, int ano) {
        LocalDate l = LocalDate.of(ano, mes, 1);
        return l.lengthOfMonth();
    }

    public static LocalDate incDia(LocalDate data, int dias) {
        return data.plusDays(dias);
    }

    public static Date incDia(Date data, int dias) {
        return localDateToDate(dateToLocalDate(data).plusDays(dias));
    }

    public static Date incDia(Date data) {
        return localDateToDate(dateToLocalDate(data).plusDays(1));
    }

    public static LocalDate incDia(LocalDate data) {
        return data.plusDays(1);
    }

    public static LocalDate incSemana(LocalDate data) {
        return data.plusWeeks(1);
    }

    public static LocalDate incMes(LocalDate data) {
        return data.plusMonths(1);
    }

    public static LocalDate incAno(LocalDate data) {
        return data.plusYears(1);
    }

    /**
     * @return segunda = 1, terça = 2 ... domingo = 7
     */
    public static int diaDaSemana(int dia, int mes, int ano) {
        LocalDate l = LocalDate.of(ano, mes, dia);
        return l.getDayOfWeek().getValue();
    }

    public static LocalTime timeToLocalTime(Time time) {
        return dateToLocalTime(time);
    }

    public static Date toDate(int dia, int mes, int ano) {
        LocalDate l = LocalDate.of(ano, mes, dia);
        return localDateToDate(l);
    }

    public static boolean comparaData(Date d1, String operador, Date d2) {
        return comparaData(dateToLocalDate(d1), operador, dateToLocalDate(d2));
    }

    public static boolean comparaData(LocalDate d1, String operador, LocalDate d2) {
        if ((d1 != null && d2 == null) || (d2 != null && d1 == null)) {
            return false;
        }

        int res = d1.compareTo(d2);
        if (res == 0) {
            return operador.equals("=") || operador.equals("==") || operador.equals(">=") || operador.equals("<=");
        } else if (res > 0) {
            return operador.equals(">") || operador.equals(">=") || operador.equals("<>") || operador.equals("!=");
        } else if (res < 0) {
            return operador.equals("<") || operador.equals("<=") || operador.equals("<>") || operador.equals("!=");
        } else {
            return false;
        }
    }

    public static boolean compara(Date d1, Date d2) {
        return compara(dateToLocalDate(d1), dateToLocalDate(d2));
    }

    public static boolean compara(LocalDate d1, LocalDate d2) {
        return d1.equals(d2);
    }

    public static String toStringDate(LocalDateTime date) {
        return date.format(DATE_FORMAT);
    }

    public static String toStringDateTime(LocalDateTime date) {
        return date.format(DATETIME_FORMAT);
    }

    public static String toStringDate(LocalDateTime date, String formato) {
        return date.format(DateTimeFormatter.ofPattern(formato));
    }

    public static String toStringDate(LocalDate date) {
        return date.format(DATE_FORMAT);
    }

    public static String toStringDate(LocalDate date, String formato) {
        return date.format(DateTimeFormatter.ofPattern(formato));
    }

    public static String toStringDate(Date date) {
        return toStringDate(dateToLocalDate(date));
    }

    public static String toStringDateTime(Date date) {
        return toStringDateTime(dateToLocalDateTime(date));
    }

    public static String toStringDate(Date date, String formato) {
        return toStringDate(dateToLocalDate(date), formato);
    }

    public static String toStringTime(LocalDateTime time) {
        return time.format(TIME_FORMAT);
    }

    public static String toStringTime(LocalDateTime time, String formato) {
        return time.format(DateTimeFormatter.ofPattern(formato));
    }

    public static String toStringTime(LocalTime time) {
        return time.format(TIME_FORMAT);
    }

    public static String toStringTime(LocalTime time, String formato) {
        return time.format(DateTimeFormatter.ofPattern(formato));
    }

    public static String toStringTime(Date date) {
        return toStringTime(dateToLocalTime(date));
    }

    public static String toStringTime(Date date, String formato) {
        return toStringTime(dateToLocalTime(date), formato);
    }

    public static LocalDateTime merge(Date data, Time tempo) {
        return LocalDateTime.of(data == null ? LocalDate.MIN : dateToLocalDate(data), tempo == null ? LocalTime.MIN : dateToLocalTime(tempo));
    }

    public static boolean verificaSabadoDomingo(int dia, int mes, int ano) {
        LocalDate now = LocalDate.of(ano, mes, dia);
        return now.getDayOfWeek().equals(DayOfWeek.SATURDAY) || now.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }

    /**
     * Transforma um valor double para horas
     *
     * @param horas Quantidade de horas em double
     * @return Retorna uma string formatada(HH:mm) com a transformação do valor
     * informado.
     */
    public static String doubleParaHora(double horas) {
        return decimalToHours(new BigDecimal("" + horas));
    }

    public static String doubleParaHora(BigDecimal horas) {
        return decimalToHours(horas);
    }

    /**
     * Transforma um valor double para horas
     *
     * @param horas Quantidade de horas em double
     * @return Retorna uma string formatada(HH:mm) com a transformação do valor
     * informado.
     */
    public static String decimalToHours(BigDecimal horas) {
        if (horas == null) {
            return "00:00";
        }
        horas = horas.setScale(2, RoundingMode.HALF_UP);
        Integer hora = horas.intValue();
        String strMin = horas.toString().substring(horas.toString().indexOf(".") + 1);
        long minutos = Long.parseLong(strMin);
        if (strMin.length() == 1) {
            minutos = minutos * 10;
        }
        minutos = (int) ((minutos / 100.0) * 60.0);
        DecimalFormat df = new DecimalFormat("00");
        return (hora < 10 ? "0" + hora : hora) + ":" + df.format(minutos);
    }

    public static Date zeraHora(Date data) {
        LocalDate l = UtilDate.dateToLocalDate(data);
        return UtilDate.localDateToDate(l);
    }

    /**
     * Baseia-se como Domingo sendo o primeiro dia da semana
     */
    public static LocalDate paraDiaDaSemana(LocalDate data, DayOfWeek dia) {
        if (data.getDayOfWeek().equals(dia)) {
            return data;
        }

        if (data.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            data = data.plusDays(1);
        }

        if (dia.equals(DayOfWeek.SUNDAY)) {
            while (data.getDayOfWeek() != DayOfWeek.SUNDAY) {
                data = data.minusDays(1);
            }
            return data;
        }

        data = data.minusDays(data.getDayOfWeek().compareTo(dia));

        return data;
    }

    public static int getMes(LocalDate data) {
        return data.getMonthValue();
    }

    public static int getMes(Date data) {
        return getMes(dateToLocalDate(data));
    }

    public static int getDia(LocalDate data) {
        return data.getDayOfMonth();
    }

    public static int getDia(Date data) {
        return getDia(dateToLocalDate(data));
    }

    public static int getAno(LocalDate data) {
        return data.getYear();
    }

    public static int getAno(Date data) {
        return getAno(dateToLocalDate(data));
    }

    public static String millisToText(long millis) {
        long seconds, minutes, hours, days;
        seconds = millis / 1000;
        minutes = seconds / 60;
        seconds = seconds % 60;
        hours = minutes / 60;
        minutes = minutes % 60;
        days = hours / 24;
        hours = hours % 24;
        return ((days > 0 ? days + " dias, " : "") + (hours > 0 ? hours + " horas, " : "") + (minutes > 0 ? minutes + " minutos, " : "") + seconds + " segundos");
    }

    // -----------[ Horário de Verão (DST - Daylight Saving Time) ]-------------
    public static boolean horarioDeVerao(ZonedDateTime t) {
        return horarioDeVerao(t.toInstant());
    }

    public static boolean horarioDeVerao(OffsetDateTime t) {
        return horarioDeVerao(t.toInstant());
    }

    public static boolean horarioDeVerao(LocalDateTime t, ZoneOffset zoneOffset) {
        return horarioDeVerao(t.toInstant(zoneOffset));
    }

    public static boolean horarioDeVerao(LocalDateTime t) {
        return horarioDeVerao(t.atZone(ZONEID_BR).toInstant());
    }

    public static boolean horarioDeVerao(Date t) {
        return horarioDeVerao(t.toInstant());
    }

    public static boolean horarioDeVerao(Instant t) {
        return ZoneId.systemDefault().getRules().isDaylightSavings(t);
    }
    //--------------------------------------------------------------------------

}

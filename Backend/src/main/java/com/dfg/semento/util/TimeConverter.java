package com.dfg.semento.util;

import jakarta.validation.constraints.NotNull;

import java.time.*;
import java.time.format.DateTimeFormatter;


public class TimeConverter {

    /** ES 질의를 위한 시간포맷을 만들어주는 컨버터
     * @author 최서현
     * @param startTime, endTime
     * @return FormattedTime
     */
    public static FormattedTime convertElasticsearchTime(LocalDateTime startTime, LocalDateTime endTime){
        // 타임존을 지정하여 ZonedDateTime으로 변환
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime zonedStartTime = startTime.atZone(zoneId);
        ZonedDateTime zonedEndTime = endTime.atZone(zoneId);

        // 원하는 포맷으로 문자열 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        String formattedStartTime = formatter.format(zonedStartTime);
        String formattedEndTime = formatter.format(zonedEndTime);

        return new FormattedTime(formattedStartTime, formattedEndTime);
    }


    /** ES 결과를 한국시간 포맷으로 바꿔주는 컨버터
     * @author 최서현
     * @param utcTime
     * @return FormattedTime
     */
    public static LocalDateTime convertUtcToAsia(String utcTime){
        String utcTimeString = utcTime;

        // UTC 시간을 ZonedDateTime 객체로 파싱
        ZonedDateTime utcDateTime = ZonedDateTime.parse(utcTimeString);

        // 서울 시간대로 변환
        ZonedDateTime seoulDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));

        // ZonedDateTime을 LocalDateTime으로 반환
        return seoulDateTime.toLocalDateTime();
    }

    /** String타입의 시간을 LocalDateTime으로 변환
     * @author 최서현
     * @param dateTimeStr 시간문자열
     * @return LocalDateTime
     */
    public static LocalDateTime convertStringToLocalDateTime(String dateTimeStr){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        OffsetDateTime odt = OffsetDateTime.parse(dateTimeStr, formatter);
        LocalDateTime ldt = odt.toLocalDateTime();
        return ldt;
    }

    public static String convertLocalDateTimeToString(@NotNull LocalDateTime startTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = startTime.format(formatter);
        return formattedDate;
    }
    
    
}

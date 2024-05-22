package com.dfg.semento.util;

/** 통계를 만드는 클래스
 */
public class CalculateData {

    /** 두 데이터의 차이 퍼센테이지를 구함
     * @author 최서현
     * @param ohtValue
     * @param overallValue
     * @return
     */
    public static double getDifferencePercentage(double ohtValue, double overallValue){
        if(overallValue == 0) return 100;
        return  ((ohtValue - overallValue) / overallValue) * 100;
    }

    /**
     * 전체 데이터에 대해 value가 몇 퍼센트인지 계산
     * @param value
     * @param overallValue
     * @return
     */
    public static double getPercentage(double value, double overallValue) {
        return value / overallValue * 100;
    }
}

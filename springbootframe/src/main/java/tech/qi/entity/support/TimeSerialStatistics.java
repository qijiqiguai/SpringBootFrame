package tech.qi.entity.support;

import org.springframework.data.annotation.PersistenceConstructor;

import java.io.Serializable;

/**
 * @author wangqi
 */
public class TimeSerialStatistics implements Serializable {
    private static final long serialVersionUID = 3683794288562215569L;
    private int year;
    private int month;
    private int day;
    private double value;

    @PersistenceConstructor
    public TimeSerialStatistics(int year, int month, int day, double value) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.value = value;
    }

    @PersistenceConstructor
    public TimeSerialStatistics(int year, int month, int day, long value) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.value = value;
    }

    public TimeSerialStatistics(){}

    public String getTime() {
        String time = year + "-" + (month<10 ? "0"+month : month);
        if(day != 0){
            time += "-" + (day<10 ? "0"+day : day);
        }
        return time;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

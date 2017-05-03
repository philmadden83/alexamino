package com.mymo.alexamino.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

/**
 * Created by Phil Madden on 1/22/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Slot {
    String AMAZON_DATE_FORMAT = "yyyy-MM-dd";
    String AMAZON_WEEK_FORMAT = "yyyy-ww";
    String AMAZON_MONTH_FORMAT = "yyyy-MM";
    String AMAZON_YEAR_FORMAT = "yyyy";
    String SHOT_DAY_FORMAT = "EEE";
    String SHOT_MONTH_FORMAT = "MMM";

    String value();
    String pattern() default "";
    DateFilter dateFilter() default DateFilter.ANY;
    Class<?> typeDeserializer() default Class.class;

    enum DateFilter {
        WEEKEND         ("^(?i)(\\d{4})\\-W(\\d{2})-WE$"),
        WEEK_OF_YEAR    ("^(?i)(\\d{4})\\-W(\\d{2})$"),
        YEAR            ("^\\d{4}$"),
        MONTH           ("^(?i)(\\d{4})\\-(\\d{2})|(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)['abcehilmoursty]*$"),
        DECADE          ("^(?i)\\d{3}X$"),
        SEASON          ("^(?i)(\\d{4})\\-(WI|SP|SU|FA)$"),
        DATE            ("(?i)^(\\d{4}\\-\\d{2}\\-\\d{2}$)|(Mon|Tue|Wed|Thu|Fri|Sat|Sun|Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)['abcdehilmnoursty]*$"),
        ANY             (".*"),
        NOW             ("(?i)^PRESENT_REF$");

        final String regex;
        DateFilter(String regex) {
            this.regex = regex;
        }

        public String regex() {
            return regex;
        }

        public boolean matches(String value) {
            return Pattern.matches(this.regex(), value);
        }
    }


}

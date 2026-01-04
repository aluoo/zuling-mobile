package com.zxtx.hummer.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelField {

    /**
     * 列名称
     *
     * @return
     */
    String name() default "";

    /**
     * 时间格式表达式
     *
     * @return
     */
    String pattern() default "";


    /**
     * 替换规则 以"_"分割原值和新值
     *
     * @return
     */
    String[] replace() default {};

}

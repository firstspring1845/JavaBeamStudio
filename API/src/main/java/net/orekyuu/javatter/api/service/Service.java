package net.orekyuu.javatter.api.service;

import java.lang.annotation.*;

/**
 * Lookupを使用して実装を検索できる事を表すインターフェイスです。
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
}

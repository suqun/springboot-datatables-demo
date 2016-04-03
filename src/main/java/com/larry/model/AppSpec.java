
package com.larry.model;

import org.springframework.data.jpa.domain.Specification;

/**
 * Created on 16/3/29
 * Message Specification
 */
public class AppSpec {
    public static Specification<App> isNotDelete() throws Exception {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("isDelete"));
    }
}

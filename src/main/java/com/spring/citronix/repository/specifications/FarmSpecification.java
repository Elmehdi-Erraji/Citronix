package com.spring.citronix.repository.specifications;

import com.spring.citronix.domain.Farm;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class FarmSpecification {

    public static Specification<Farm> nameContains(String name) {
        return (root, query, cb) ->
                name != null && !name.isEmpty()
                        ? cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%")
                        : null;
    }

    public static Specification<Farm> locationContains(String location) {
        return (root, query, cb) ->
                location != null && !location.isEmpty()
                        ? cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%")
                        : null;
    }

    public static Specification<Farm> creationDateAfter(LocalDate startDate) {
        return (root, query, cb) ->
                startDate != null
                        ? cb.greaterThanOrEqualTo(root.get("creationDate"), startDate)
                        : null;
    }
}

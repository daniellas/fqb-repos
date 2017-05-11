package com.lynx.fqb.repos.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.SingularAttribute;

import com.lynx.fqb.order.Orders;
import com.lynx.fqb.path.Paths;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Sort<R> {

    private final List<DirectionAttribute<R>> properties;

    public enum Direction {
        ASC, DESC;

        public static Direction fromString(String value) {
            return valueOf(Optional.ofNullable(value).map(String::toUpperCase).orElseThrow(NullPointerException::new));
        }
    }

    private BiFunction<CriteriaBuilder, Path<? extends R>, Order> toOrder(DirectionAttribute<R> property) {
        if (property.getDirection() == Direction.ASC) {
            return Orders.asc(Paths.get(property.getAttr()));
        }

        return Orders.desc(Paths.get(property.getAttr()));
    }

    @SuppressWarnings("unchecked")
    public BiFunction<CriteriaBuilder, Path<? extends R>, Order>[] toOrders() {
        return (BiFunction<CriteriaBuilder, Path<? extends R>, Order>[]) properties.stream().map(this::toOrder).toArray(BiFunction[]::new);
    }

    public static <R> Sort<R> by(Direction direction, SingularAttribute<? super R, ?> attr) {
        return new Sort<>(Arrays.asList(DirectionAttribute.of(direction, attr)));
    }

    public static <R> Sort<R> by(String direction, SingularAttribute<? super R, ?> attr) {
        return by(Direction.fromString(direction), attr);
    }

    public Sort<R> thenBy(Direction direction, SingularAttribute<? super R, ?> attr) {
        ArrayList<DirectionAttribute<R>> props = new ArrayList<>(properties);

        props.add(DirectionAttribute.of(direction, attr));

        return new Sort<>(new ArrayList<>(props));
    }

    @Getter
    @RequiredArgsConstructor(staticName = "of")
    private static class DirectionAttribute<R> {
        private final Direction direction;
        private final SingularAttribute<? super R, ?> attr;
    }

}

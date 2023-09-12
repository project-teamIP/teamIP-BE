package com.teamip.heyhello.domain.hourtraffic.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHourTraffic is a Querydsl query type for HourTraffic
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHourTraffic extends EntityPathBase<HourTraffic> {

    private static final long serialVersionUID = -771470798L;

    public static final QHourTraffic hourTraffic = new QHourTraffic("hourTraffic");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> time = createDateTime("time", java.time.LocalDateTime.class);

    public final NumberPath<Integer> userCount = createNumber("userCount", Integer.class);

    public QHourTraffic(String variable) {
        super(HourTraffic.class, forVariable(variable));
    }

    public QHourTraffic(Path<? extends HourTraffic> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHourTraffic(PathMetadata metadata) {
        super(HourTraffic.class, metadata);
    }

}


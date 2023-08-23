package com.teamip.heyhello.domain.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReportCount is a Querydsl query type for ReportCount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportCount extends EntityPathBase<ReportCount> {

    private static final long serialVersionUID = 9241659L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReportCount reportCount = new QReportCount("reportCount");

    public final EnumPath<ReportCategory> category = createEnum("category", ReportCategory.class);

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.teamip.heyhello.domain.user.entity.QUser user;

    public QReportCount(String variable) {
        this(ReportCount.class, forVariable(variable), INITS);
    }

    public QReportCount(Path<? extends ReportCount> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReportCount(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReportCount(PathMetadata metadata, PathInits inits) {
        this(ReportCount.class, metadata, inits);
    }

    public QReportCount(Class<? extends ReportCount> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.teamip.heyhello.domain.user.entity.QUser(forProperty("user")) : null;
    }

}


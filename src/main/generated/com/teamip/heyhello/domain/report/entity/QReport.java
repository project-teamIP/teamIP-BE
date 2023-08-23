package com.teamip.heyhello.domain.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReport is a Querydsl query type for Report
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReport extends EntityPathBase<Report> {

    private static final long serialVersionUID = 762936628L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReport report = new QReport("report");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<ReportCategory> reportCategory = createEnum("reportCategory", ReportCategory.class);

    public final com.teamip.heyhello.domain.user.entity.QUser requestUser;

    public final com.teamip.heyhello.domain.user.entity.QUser targetUser;

    public QReport(String variable) {
        this(Report.class, forVariable(variable), INITS);
    }

    public QReport(Path<? extends Report> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReport(PathMetadata metadata, PathInits inits) {
        this(Report.class, metadata, inits);
    }

    public QReport(Class<? extends Report> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.requestUser = inits.isInitialized("requestUser") ? new com.teamip.heyhello.domain.user.entity.QUser(forProperty("requestUser")) : null;
        this.targetUser = inits.isInitialized("targetUser") ? new com.teamip.heyhello.domain.user.entity.QUser(forProperty("targetUser")) : null;
    }

}


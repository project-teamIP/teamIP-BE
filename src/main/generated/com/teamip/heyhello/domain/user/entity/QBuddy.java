package com.teamip.heyhello.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBuddy is a Querydsl query type for Buddy
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBuddy extends EntityPathBase<Buddy> {

    private static final long serialVersionUID = -515847729L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBuddy buddy = new QBuddy("buddy");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QUser receiver;

    public final QUser sender;

    public QBuddy(String variable) {
        this(Buddy.class, forVariable(variable), INITS);
    }

    public QBuddy(Path<? extends Buddy> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBuddy(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBuddy(PathMetadata metadata, PathInits inits) {
        this(Buddy.class, metadata, inits);
    }

    public QBuddy(Class<? extends Buddy> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.receiver = inits.isInitialized("receiver") ? new QUser(forProperty("receiver")) : null;
        this.sender = inits.isInitialized("sender") ? new QUser(forProperty("sender")) : null;
    }

}


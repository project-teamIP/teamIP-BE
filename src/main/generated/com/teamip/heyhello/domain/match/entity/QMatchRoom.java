package com.teamip.heyhello.domain.match.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMatchRoom is a Querydsl query type for MatchRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMatchRoom extends EntityPathBase<MatchRoom> {

    private static final long serialVersionUID = -2080349235L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMatchRoom matchRoom = new QMatchRoom("matchRoom");

    public final DateTimePath<java.time.LocalDateTime> closedAt = createDateTime("closedAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final ComparablePath<java.util.UUID> roomName = createComparable("roomName", java.util.UUID.class);

    public final com.teamip.heyhello.domain.user.entity.QUser user1;

    public final ComparablePath<java.util.UUID> user1Client = createComparable("user1Client", java.util.UUID.class);

    public final com.teamip.heyhello.domain.user.entity.QUser user2;

    public final ComparablePath<java.util.UUID> user2Client = createComparable("user2Client", java.util.UUID.class);

    public QMatchRoom(String variable) {
        this(MatchRoom.class, forVariable(variable), INITS);
    }

    public QMatchRoom(Path<? extends MatchRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMatchRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMatchRoom(PathMetadata metadata, PathInits inits) {
        this(MatchRoom.class, metadata, inits);
    }

    public QMatchRoom(Class<? extends MatchRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user1 = inits.isInitialized("user1") ? new com.teamip.heyhello.domain.user.entity.QUser(forProperty("user1")) : null;
        this.user2 = inits.isInitialized("user2") ? new com.teamip.heyhello.domain.user.entity.QUser(forProperty("user2")) : null;
    }

}


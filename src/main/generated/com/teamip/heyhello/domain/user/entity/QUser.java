package com.teamip.heyhello.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -847360094L;

    public static final QUser user = new QUser("user");

    public final NumberPath<Long> cleanPoint = createNumber("cleanPoint", Long.class);

    public final StringPath country = createString("country");

    public final StringPath gender = createString("gender");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final ListPath<Interest, QInterest> interests = this.<Interest, QInterest>createList("interests", Interest.class, QInterest.class, PathInits.DIRECT2);

    public final BooleanPath isGoogle = createBoolean("isGoogle");

    public final BooleanPath isKakao = createBoolean("isKakao");

    public final BooleanPath isLocked = createBoolean("isLocked");

    public final StringPath language = createString("language");

    public final StringPath loginId = createString("loginId");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final EnumPath<UserStatus> status = createEnum("status", UserStatus.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}


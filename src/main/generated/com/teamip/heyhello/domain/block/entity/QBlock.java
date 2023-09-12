package com.teamip.heyhello.domain.block.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBlock is a Querydsl query type for Block
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlock extends EntityPathBase<Block> {

    private static final long serialVersionUID = 1269263570L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBlock block = new QBlock("block");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.teamip.heyhello.domain.user.entity.QUser requestUser;

    public final com.teamip.heyhello.domain.user.entity.QUser targetUser;

    public QBlock(String variable) {
        this(Block.class, forVariable(variable), INITS);
    }

    public QBlock(Path<? extends Block> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBlock(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBlock(PathMetadata metadata, PathInits inits) {
        this(Block.class, metadata, inits);
    }

    public QBlock(Class<? extends Block> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.requestUser = inits.isInitialized("requestUser") ? new com.teamip.heyhello.domain.user.entity.QUser(forProperty("requestUser")) : null;
        this.targetUser = inits.isInitialized("targetUser") ? new com.teamip.heyhello.domain.user.entity.QUser(forProperty("targetUser")) : null;
    }

}


package com.mironov.coursework.data.database.model.message

import androidx.room.Embedded
import androidx.room.Relation

data class MessageDbModel(
    @Embedded
    val message: MessageInfoDbModel,
    @Relation(
        parentColumn = MessageInfoDbModel.COLUMN_ID,
        entityColumn = ReactionDbModel.COLUMN_MESSAGE_ID
    )
    val reactions: List<ReactionDbModel>
)
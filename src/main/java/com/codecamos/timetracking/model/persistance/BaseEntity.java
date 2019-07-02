package com.codecamos.timetracking.model.persistance;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
public class BaseEntity {

	@BsonId
	private ObjectId id;

	@Field("updated_at")
	@LastModifiedDate
	private Date updatedAt;
}

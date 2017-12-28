package tech.qi.entity.support;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Jackson Annotation Bundle. Force related object as id when serialization.
 */

@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonIdentityReference(alwaysAsId=true)
public @interface JsonId {

}

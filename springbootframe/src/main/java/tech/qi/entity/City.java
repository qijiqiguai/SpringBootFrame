package tech.qi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tech.qi.entity.support.AbstractAuditableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * Lesson, HotLesson, LessonSKU etc. will associate with City
 *
 * @author wangqi
 */
@Entity
public class City extends AbstractAuditableEntity {
    private static final long serialVersionUID = 5437219992174061484L;

    @Column(unique = true)
    private String code;

    @Column(unique = true)
    private String name;

    private boolean enabled;
    private int sortOrder;

    /**
     * Override this to enable @JsonIgnore of this property
     *
     * @return
     */
    @JsonIgnore
    @Override
    public Date getCreatedDate() {
        return super.getCreatedDate();
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}

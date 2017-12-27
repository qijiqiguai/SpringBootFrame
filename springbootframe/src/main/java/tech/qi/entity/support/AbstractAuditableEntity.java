package tech.qi.entity.support;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Abstract Super Class for Entity that requires Auditing feature. ID is also included here.
 * Only CreatedDate is not @JsonIgnore ed
 *
 * @author wangqi
 * @JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditableEntity implements Serializable{

    private static final long serialVersionUID = -8817522182624565455L;

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @CreatedBy
    private String createdBy;

    @JsonIgnore
    @LastModifiedBy
    private String lastModifiedBy;

    @CreatedDate
    private Date createdDate;

    @JsonIgnore
    @LastModifiedDate
    private Date lastModifiedDate;

    @Version
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    protected Integer getVersion() {
        return version;
    }

    protected void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractAuditableEntity that = (AbstractAuditableEntity) o;

        return id.equals(that.id);

    }
}

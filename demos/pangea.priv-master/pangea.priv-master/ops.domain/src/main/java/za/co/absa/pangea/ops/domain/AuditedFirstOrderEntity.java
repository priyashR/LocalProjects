package za.co.absa.pangea.ops.domain;

import java.util.Date;
import java.util.Objects;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@MappedSuperclass
public abstract class AuditedFirstOrderEntity extends FirstOrderEntity
{
	@PrePersist
	void prePersist()
	{
		setCreateTimestamp(new Date());
	}
	@PreUpdate
	void preUpdate()
	{
		setLastUpdateTimestamp(new Date());
	}

	@Temporal (TemporalType.TIMESTAMP)
	private Date createTimestamp;
	@Temporal (TemporalType.TIMESTAMP)
	private Date lastUpdateTimestamp;

	public final Date getCreateTimestamp()
	{
		return createTimestamp;
	}
	public final void setCreateTimestamp(Date createTimestamp)
	{
		if (Objects.equals(this.createTimestamp, createTimestamp))
			return;
		this.createTimestamp = createTimestamp;
	}
	public final Date getLastUpdateTimestamp()
	{
		return lastUpdateTimestamp;
	}
	public final void setLastUpdateTimestamp(Date lastUpdateTimestamp)
	{
		if (Objects.equals(this.lastUpdateTimestamp, lastUpdateTimestamp))
			return;
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}
	
	public void forceUpdate()
	{
		setLastUpdateTimestamp(null);
	}
}

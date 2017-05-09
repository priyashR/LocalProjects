package za.co.absa.pangea.ops.domain;

import java.util.Objects;

import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class Entity implements Comparable<Entity>
{
	public abstract String getId();
	public abstract void setId(String id);

	public void validate() { };

	@Override
	public int compareTo(Entity that)
	{
		return this.getId().compareTo(that.getId());
	}

	@Override
	public final boolean equals(Object o)
	{
		return (o instanceof Entity && this.equals((Entity)o));
	}
	private boolean equals(Entity that)
	{
		return Objects.equals(this.getId(), that.getId()) && this.getClass().equals(that.getClass());
	}

	@Override
	public final int hashCode()
	{
		return Objects.hashCode(getId());
	}

	@Override
	public String toString()
	{
		StringBuilder buf = new StringBuilder();
		buf.append(getClass().getName());
		buf.append(" { id= ").append(getId()).append(" }");
		return buf.toString();
	}
}

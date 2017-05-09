package za.co.absa.pangea.ops.domain;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class SimpleEntity extends Entity
{
	@Id
	private String id;

	@Override
	public String getId()
	{
		return this.id;
	}
	@Override
	public void setId(String id)
	{
		this.id = id;
	}

	@Override
	public void validate() { }
}

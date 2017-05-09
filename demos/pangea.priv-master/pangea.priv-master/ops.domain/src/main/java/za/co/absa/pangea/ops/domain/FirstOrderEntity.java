package za.co.absa.pangea.ops.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;


@MappedSuperclass
public abstract class FirstOrderEntity extends Entity
{
	@Id @GeneratedValue(generator= "system-uuid")
	@GenericGenerator(name= "system-uuid", strategy= "za.sq.amse.domain.FirstOrderEntityIdGenerator")
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
}

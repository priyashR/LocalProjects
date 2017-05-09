package za.co.absa.pangea.ops.domain;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.UUIDGenerator;

public class FirstOrderEntityIdGenerator extends UUIDGenerator
{
	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException
	{
		FirstOrderEntity entity = ((FirstOrderEntity)object);
		return (entity.getId() != null ? entity.getId() : super.generate(session, object));
	}
}

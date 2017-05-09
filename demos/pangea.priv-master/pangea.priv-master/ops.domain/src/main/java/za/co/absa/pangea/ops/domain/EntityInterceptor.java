package za.co.absa.pangea.ops.domain;


import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

@SuppressWarnings("serial")
public class EntityInterceptor extends EmptyInterceptor
{
	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
	{
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types)
	{
		if (entity instanceof Entity)
		{
			((Entity)entity).validate();
		}
		return false;
	}

	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
	{
		return false;
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
	{
		if (entity instanceof Entity)
		{
			((Entity)entity).validate();
		}
		return false;
	}

	@Override
	public void afterTransactionCompletion(Transaction tx)
	{
	}
	
}

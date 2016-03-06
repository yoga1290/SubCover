/* 
**
** Copyright 2014, Jules White
**
** 
*/
package subcover.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.Query;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.apphosting.api.DatastorePb.DatastoreService;
import java.util.LinkedList;
import java.util.Map;

/**
 * This class provides a minimal interface to mimic a subset
 * of the functionality in the Spring Data Repository. This
 * example is provided solely to show how to accomplish 
 * similar types of operations using JDO. It is possible to 
 * run Spring Data on top of AppEngine's JPA implementation, 
 * which will provide an identical environment to previous
 * examples.  
 * 
 * @author jules
 *
 * @param <T> - The type of Object stored by the repository
 * @param <ID> - The type of ID used by the stored object
 */
public class JDOCrudRepository<T,ID extends Serializable> {

	private Class<T> type_;
	
	public JDOCrudRepository(Class<T> type){
		type_ = type;
	}
	
	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 * 
	 * @param entity
	 * @return the saved entity
	 */
	public <S extends T> S save(S entity){
		return PMF.get().getPersistenceManager().makePersistent(entity);
	}
	
	/**
	 * @author yoga1290
	 */
	public <S extends T> S update(S entity){
		return JDOHelper.getPersistenceManager(entity).makePersistent(entity);
	}

	/**
	 * Saves all given entities.
	 * 
	 * @param entities
	 * @return the saved entities
	 */
	public <S extends T> Iterable<S> save(Iterable<S> entities){
		List<S> saved = new ArrayList<S>();
		for(S entity : entities){
			saved.add(save(entity));
		}
		return saved;
	}

	/**
	 * Retrieves an entity by its id.
	 * 
	 * @param id must not be {@literal null}.
	 * @return the entity with the given id or {@literal null} if none found
	 */
	@SuppressWarnings("unchecked")
	public T findOne(ID id){
		return (T)PMF.get().getPersistenceManager().getObjectById(type_, id);
	}

	/**
	 * Returns whether an entity with the given id exists.
	 * 
	 * @param id must not be {@literal null}.
	 * @return true if an entity with the given id exists, {@literal false} otherwise
	 */
	public boolean exists(ID id){
		try{
			return findOne(id) != null;
		}
		catch(Exception eew){
			return false;
		}
	}

	/**
	 * Returns all instances of the type.
	 * 
	 * @return all entities
	 */
	@SuppressWarnings("unchecked")
	public Iterable<T> findAll(){
		Query query = PMF.get().getPersistenceManager().newQuery(type_);
		Object rslt = query.execute();
		return (Collection<T>)rslt;
	}
	
	/**
	 * @author yoga1290
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll(int offset,int limit){
		 com.google.appengine.api.datastore.DatastoreService 
		 		datastore = DatastoreServiceFactory.getDatastoreService();
		 com.google.appengine.api.datastore.Query 
		 		q = new com.google.appengine.api.datastore.Query(type_.getSimpleName());
		 PreparedQuery pq = datastore.prepare(q);
		return (List<T>) pq.asList(FetchOptions.Builder.withOffset(offset).limit(limit));
	}
        /**
	 * @author yoga1290
	 */
	/* // trNgGrid 
	public List<T> query(SearchObj query) {
		// TODO Auto-generated method stub
		com.google.appengine.api.datastore.DatastoreService 
 		datastore = DatastoreServiceFactory.getDatastoreService();
		com.google.appengine.api.datastore.Query 
 		q = new com.google.appengine.api.datastore.Query(type_.getSimpleName());
 
                if(query.getOrderBy()!=null && query.getOrderBy().length()>0)
                    q=q.addSort(query.getOrderBy(), query.isOrderByReverse() ? SortDirection.ASCENDING:SortDirection.DESCENDING);
                Map<String,String> map=query.getFilterByFields();
                List<Filter> filters=new LinkedList<Filter>();
                for (Map.Entry<String, String> entry : map.entrySet())
                {
                        Filter startWithFilter1=new FilterPredicate(entry.getKey(), FilterOperator.GREATER_THAN_OR_EQUAL, entry.getValue());
                        Filter startWithFilter2=new FilterPredicate(entry.getKey(), FilterOperator.LESS_THAN, entry.getValue()+ "\ufffd");
                        filters.add(CompositeFilterOperator.and(startWithFilter1,startWithFilter2));
                }
                if(filters.size()==1)
                    q.setFilter(filters.get(0));
                else if(filters.size()>1)
                    q.setFilter(CompositeFilterOperator.or(filters));
		PreparedQuery pq = datastore.prepare(q);
		return (List<T>) pq.asList(FetchOptions.Builder.withOffset(query.getCurrentPage()).limit(query.getPageItems()));
	}
	//*/
	/**
	 * @author yoga1290
	 */
	
	public List<T> find(String startWith, String selCol, boolean isAsc, int offset, int limit) {
		// TODO Auto-generated method stub
		com.google.appengine.api.datastore.DatastoreService 
 		datastore = DatastoreServiceFactory.getDatastoreService();
		com.google.appengine.api.datastore.Query 
 		q = new com.google.appengine.api.datastore.Query(type_.getSimpleName());
 
		 if(selCol.length()>0)
		 {
			 q=q.addSort(selCol, isAsc ? SortDirection.ASCENDING:SortDirection.DESCENDING);
			 
			 if(startWith.length()>0)
			 {
				 Filter startWithFilter1=new FilterPredicate(selCol, FilterOperator.GREATER_THAN_OR_EQUAL, startWith);
				 Filter startWithFilter2=new FilterPredicate(selCol, FilterOperator.LESS_THAN, startWith+ "\ufffd");
				 
				 Filter startWithFilter= CompositeFilterOperator.and(startWithFilter1,startWithFilter2);		 
				 q.setFilter(startWithFilter);
			 }
		}
		PreparedQuery pq = datastore.prepare(q);
		return (List<T>) pq.asList(FetchOptions.Builder.withOffset(offset).limit(limit));
	}
	/**
	 * @author yoga1290
	 */
	public int count() {
		com.google.appengine.api.datastore.DatastoreService 
 		ds = DatastoreServiceFactory.getDatastoreService();
	        
		com.google.appengine.api.datastore.Query 
			q = new com.google.appengine.api.datastore.Query("__Stat_Kind__");
	    List<Entity> kindStat = ds.prepare(q)
	        .asList(FetchOptions.Builder.withLimit(1000));
	        
	    for (Entity en : kindStat) {
	        String name = (String) en.getProperty("kind_name");
	        if (name.equals(type_.getSimpleName())) {
	            return ((Number)en.getProperty("count")).intValue();
	        }
	    }
	    return 0;
	        
	    //you can use the following in development environment for testing purpose
	    //q = new Query(kind).setKeysOnly();
	    //return ds.prepare(q).countEntities(FetchOptions.Builder.withLimit(1000));
	}
	
	

	/**
	 * Deletes the entity with the given id.
	 * 
	 * @param id must not be {@literal null}.
	 */
	public void delete(ID id){
		T obj = findOne(id);
		if(obj != null){
			PMF.get().getPersistenceManager().deletePersistent(obj);
		}
	}

	/**
	 * Deletes a given entity.
	 * 
	 * @param entity
	 */
	public void delete(T entity){
		PMF.get().getPersistenceManager().deletePersistent(entity);
	}

}

/**
 * This class implements the methods needed to access data related to Matches in the db.
 * 
 * NOTE: the implementation is not yet complete.
 * 
 * @author Andrew Curry
 */
package com.revature.repository.impl;

import java.util.List;

import javax.transaction.Transactional;

import com.revature.model.MatchRecord;
import com.revature.model.User;
import com.revature.model.MatchRecord.MatchStatus;
import com.revature.repository.RepositoryException;
import com.revature.repository.interfaces.MatchRepository;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("matchRepository")
@Transactional
public class MatchRepositoryImpl implements MatchRepository{

    // ---------------------
    // INSTANCE VARIABLES
    // ---------------------

    @Autowired
    private SessionFactory sessionFactory;

    // ---------------------
    // HELPER/TEST METHODS
    // ---------------------

    /**
     * Used for testing, injects a new sessionFactory.
     * 
     * @param sessionFactory
     */
    @Override
    public void useOutsideSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // ---------------------
    // DATA ACCESS METHODS
    // ---------------------

    /**
     * Persists the given match record to the database.
     * Works with new AND already-existing entries.
     * 
     * Assumes the fields of the matchRecord are valid.
     * 
     * Throws RepositoryException if there are problems communicating with the database.
     * 
     * @param mr
     * @throws RepositoryException
     */
	@Override
	public void save(MatchRecord mr) throws RepositoryException {
		try{
            Session session = sessionFactory.getCurrentSession();
            session.saveOrUpdate(mr);
            //if (mr.getId() < 1) // if 'null'
            //    session.save(mr);
            //else session.update(mr);
        } catch(HibernateException e){
            throw new RepositoryException("HibernateException: " + e.getMessage());
        }
	}

    /**
     * Returns a mr from the database that matches either the id or code of the given mr.
     * Null if no such mr found.
     * 
     * Throws exception if there is a problem with the db.
     * 
     * @param mr
     * @return
     * @throws RepositoryException
     */
    @Override
    public MatchRecord findMatchRecord(MatchRecord mr) throws RepositoryException{
        MatchRecord result = findMatchRecordById(mr.getId());
        return (result != null) ? result : findMatchRecordByCode(mr.getCode());
    }

    /**
     * Returns the match record corresponding to the given id.
     * If no such match record exists, returns null.
     * 
     * Throws RepositoryException if there is a problem with the database.
     * 
     * @param id
     * @return
     * @throws RepositoryException
     */
	@Override
	public MatchRecord findMatchRecordById(int id) throws RepositoryException {
		try{
            Session session = sessionFactory.getCurrentSession();
            return (MatchRecord)session.get(MatchRecord.class, id);
        } catch(HibernateException e){
            throw new RepositoryException("HibernateException: " + e.getMessage());
        }
	}

    /**
     * Returns the match record corresponding to the given code.
     * If no such match record exists, returns null.
     * 
     * Throws RepositoryException if there is a problem with the database.
     * 
     * @param id
     * @return
     * @throws RepositoryException
     */
    @Override
    @SuppressWarnings(value="unchecked")
	public MatchRecord findMatchRecordByCode(int code) throws RepositoryException {
		try{
            Session session = sessionFactory.getCurrentSession();
            Criteria crit = session.createCriteria(MatchRecord.class);
            crit.add(Restrictions.eq("code", code));
            List<MatchRecord> mrList = crit.list(); // haha, mister list
            return (mrList.isEmpty()) ? null : mrList.get(0);
        } catch(HibernateException e){
            throw new RepositoryException("HibernateException: " + e.getMessage());
        }
	}

    /**
     * Returns the match record corresponding to the given code.
     * If no such match record exists, returns null.
     * 
     * Throws RepositoryException if there is a problem with the database.
     * 
     * @param id
     * @return
     * @throws RepositoryException
     */
    @Override
    @SuppressWarnings(value="unchecked")
	public List<MatchRecord> findMatchRecordsBy(User user) throws RepositoryException {
		try{
            Session session = sessionFactory.getCurrentSession();
            Criteria crit = session.createCriteria(MatchRecord.class);
            crit.add(Restrictions.or(
                Restrictions.eq("whiteUser.id", user.getId()), 
                Restrictions.eq("blackUser.id", user.getId())));
            List<MatchRecord> mrList = crit.list(); // haha, mister list
            return mrList;
        } catch(HibernateException e){
            throw new RepositoryException("HibernateException: " + e.getMessage());
        }
	}

    /**
     * Finds all match records matching the given status filter.
     * Only supports ALL, PENDING, ONGOING, and FINISHED.
     * 
     * @param filter
     * @return
     */
    @Override
    @SuppressWarnings(value="unchecked")
	public List<MatchRecord> findMatchRecordsBy(MatchStatusFilter filter) throws RepositoryException {
		try{
            Session session = sessionFactory.getCurrentSession();
            Criteria crit = session.createCriteria(MatchRecord.class);
            switch(filter){
                case PENDING:
                    crit.add(Restrictions.eq("status", MatchStatus.PENDING));
                    break;
                case ONGOING:
                    crit.add(Restrictions.eq("status", MatchStatus.ONGOING));
                    break;
                case FINISHED:
                    crit.add(Restrictions.or(
                            Restrictions.eq("status", MatchStatus.WHITE_VICTORY), 
                            Restrictions.eq("status", MatchStatus.BLACK_VICTORY)));
                    break;
                default: // covers ALL
                    break;
            }
            List<MatchRecord> mrList = crit.list(); // haha, mister list
            return mrList;
        } catch(HibernateException e){
            throw new RepositoryException("HibernateException: " + e.getMessage());
        }
	}

    /**
     * Finds all match records where the given user is one of the players, AND the status
     * matches the given status filter.
     * Supports all filter types.
     * 
     * @param user
     * @param filter
     * @return
     * @throws RepositoryException
     */
    @Override
    @SuppressWarnings(value="unchecked")
	public List<MatchRecord> findMatchRecordsBy(User user, MatchStatusFilter filter) throws RepositoryException {
		try{
            Session session = sessionFactory.getCurrentSession();
            Criteria crit = session.createCriteria(MatchRecord.class);
            crit.add(Restrictions.or(
                    Restrictions.eq("whiteUser.id", user.getId()), 
                    Restrictions.eq("blackUser.id", user.getId())));
            switch(filter){
                case PENDING:
                    crit.add(Restrictions.eq("status", MatchStatus.PENDING));
                    break;
                case ONGOING:
                    crit.add(Restrictions.eq("status", MatchStatus.ONGOING));
                    break;
                case FINISHED:
                    crit.add(Restrictions.or(
                            Restrictions.eq("status", MatchStatus.WHITE_VICTORY), 
                            Restrictions.eq("status", MatchStatus.BLACK_VICTORY)));
                    break;
                case WON_BY_GIVEN_USER:
                    crit.add(Restrictions.or(
                        Restrictions.and(
                            Restrictions.eq("whiteUser.id", user.getId()), 
                            Restrictions.eq("status", MatchStatus.WHITE_VICTORY)),
                        Restrictions.and(
                            Restrictions.eq("blackUser.id", user.getId()),
                            Restrictions.eq("status", MatchStatus.BLACK_VICTORY))));
                    break;
                case LOST_BY_GIVEN_USER:
                    crit.add(Restrictions.or(
                        Restrictions.and(
                            Restrictions.eq("whiteUser.id", user.getId()), 
                            Restrictions.eq("status", MatchStatus.BLACK_VICTORY)),
                        Restrictions.and(
                            Restrictions.eq("blackUser.id", user.getId()),
                            Restrictions.eq("status", MatchStatus.WHITE_VICTORY))));
                    break;
                default: // covers ALL
                    break;
            }
            List<MatchRecord> mrList = crit.list(); // haha, mister list
            return mrList;
        } catch(HibernateException e){
            throw new RepositoryException("HibernateException: " + e.getMessage());
        }
	}
    
    /**
     * Determines if the db has a MatchRecord matching either the id or code of the given
     * mr.
     * 
     * @param mr
     * @return
     * @throws RepositoryException 
     */
    @Override
    public boolean checkExists(MatchRecord mr) throws RepositoryException{
        return findMatchRecord(mr) != null;
    }

    /**
     * Determines if the db has a MatchRecord matching the given id
     * 
     * @param mr
     * @return
     * @throws RepositoryException 
     */
    @Override
    public boolean checkExistsById(int id) throws RepositoryException{
        return findMatchRecordById(id) != null;
    }

    /**
     * Determines if the db has a MatchRecord matching the given code
     * 
     * @param mr
     * @return
     * @throws RepositoryException 
     */
    @Override
    public boolean checkExistsByCode(int code) throws RepositoryException{
        return findMatchRecordByCode(code) != null;
    }
}

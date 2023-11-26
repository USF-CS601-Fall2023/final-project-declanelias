package HotelData;

import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread safe implementation of reviews
 */
public class ThreadSafeReviews extends Reviews {

    private ReentrantReadWriteLock lock1;
    private ReentrantReadWriteLock lock2;

    public ThreadSafeReviews(Set<String> stopWords) {
        super(null, stopWords);
        lock1 = new ReentrantReadWriteLock();
        lock2 = new ReentrantReadWriteLock();
    }

    /**
     * Adds to map in a thread safe way
     * @param hr HotelReview object to be added
     */
    @Override
    public void addToIdMap(HotelReview hr) {
        try {
            lock1.writeLock().lock();
            super.addToIdMap(hr);
        } finally {
            lock1.writeLock().unlock();
        }
    }

    /**
     * Adds to word map in a thread safe way
     * @param hr HotelReview object to be added
     */
    @Override
    public void addToWordMap(HotelReview hr) {
        try {
            lock2.writeLock().lock();
            super.addToWordMap(hr);
        } finally {
            lock2.writeLock().unlock();
        }
    }

    /**
     * Reads from map in thread safe way
     * @param id the hotelId
     * @return set of reviews from hotel with id
     */
    @Override
    public Set<HotelReview> searchByID(int id) {
        try {
            lock1.readLock().lock();
            return super.searchByID(id);
        } finally {
            lock1.readLock().unlock();
        }
    }

    /**
     * Reads from map in thread safe way
     * @param word word in review
     * @return set of reviews containing word
     */
    @Override
    public Set<ReviewWithFrequency> searchByWord(String word) {
        try {
            lock2.readLock().lock();
            return super.searchByWord(word);
        } finally {
            lock2.readLock().unlock();
        }
    }
}

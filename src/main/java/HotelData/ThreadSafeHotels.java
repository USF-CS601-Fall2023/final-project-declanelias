package HotelData;

import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread safe implementation of Hotels
 */
public class ThreadSafeHotels extends HotelData {
    private ReentrantReadWriteLock lock;

    /**
     * Constructor method
     */
    public ThreadSafeHotels() {
        super(null);
        lock = new ReentrantReadWriteLock();
    }

    /**
     * Adds to mpa in thread safe wau
     * @param hotel hotel object
     */
    @Override
    public void addToMap(Hotel hotel) {
        try {
            lock.writeLock().lock();
            super.addToMap(hotel);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Reads from map in thread sage way
     * @param word word in hotel name
     * @return list of hotel ids with word in hotel name
     */
    @Override
    public Set<Hotel> searchByWord(String word) {
        try {
            lock.readLock().lock();
            return super.searchByWord(word);
        } finally {
            lock.readLock().unlock();
        }

    }

    /**
     * Reads from map in thread sage way
     * @param id hotelId
     * @return hotel object with id
     */
    @Override
    public Hotel searchByID(String id) {
        try {
            lock.readLock().lock();
            return super.searchByID(id);
        } finally {
            lock.readLock().unlock();
        }
    }
}

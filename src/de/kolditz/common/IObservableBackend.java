/**
 * 
 */
package de.kolditz.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A back-end implementation for {@link IObservable}s. This is intended to reduce implementation effort for front-end
 * classes by just delegating calls to an ObservableBackend object.
 * 
 * @author Till Kolditz - Till.Kolditz@GoogleMail.com
 */
public class IObservableBackend<E> implements IObservable<E> {
    private IObservable<E> frontEnd;
    private Map<IObserver<E>, Integer> observables;

    public IObservableBackend(IObservable<E> frontEnd) {
        this.frontEnd = frontEnd;
        observables = new HashMap<IObserver<E>, Integer>();
    }

    @Override
    public boolean registerObserver(IObserver<E> observer) {
        if (observer == null)
            throw new IllegalArgumentException("observer is null"); //$NON-NLS-1$
        observables.put(observer, Integer.valueOf(0));
        return true;
    }

    @Override
    public boolean unregisterObserver(IObserver<E> observer) {
        if (observer == null)
            throw new IllegalArgumentException("observer is null"); //$NON-NLS-1$
        observables.remove(observer);
        return true;
    }

    /**
     * Updates all registered {@link IObserver}s.
     * 
     * @param data
     *            the application-specific data object
     */
    public void update(E data) {
        for (Entry<IObserver<E>, Integer> e : observables.entrySet()) {
            e.getKey().update(frontEnd, data);
        }
    }
}

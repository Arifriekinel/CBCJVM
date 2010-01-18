/*
 * This file is part of CBCJVM.
 * CBCJVM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CBCJVM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CBCJVM.  If not, see <http://www.gnu.org/licenses/>.
*/

package cbccore.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * An event system based roughly on ActionScript's (Adobe Flash) and awt's event
 * dispatching system. (yes, you may now toss insults at us based on your hate
 * of FlashPlayer. They will all be redirected to /dev/null). Anything that
 * dispatches an event should subclass this. Generally maintains O(1) time
 * thanks to HashMaps! Yay HashMaps!
 *
 * @author Braden McDorman / Benjamin Woodruff
 * @see    {@link http://www.adobe.com/livedocs/flash/9.0/ActionScriptLangRefV3/flash/events/EventDispatcher.html}
 */

public class EventDispatcher {
	private HashMap<EventEmitter, HashMap<Class<Event>, ArrayList<IEventListener>>> events = new HashMap<EventEmitter, HashMap<Class<Event>, ArrayList<IEventListener>>>();
	private static EventDispatcher instance = null;
	
	public static EventDispatcher getInstance() {
		if (instance == null)
			instance = new EventDispatcher();
		return instance;
	}
	
	public void addEventListener(EventEmitter emitter, Event type,
			IEventListener listener) {
		HashMap<Class<Event>, ArrayList<IEventListener>> emit = getEmitter(emitter);
		ArrayList<IEventListener> listeners = getListeners(emit, type);
		
		listeners.add(listener);
	}
	
	
	/**
	 * Removes an event listener from all types in an emitter.
	 *
	 * @param emitter The type of emitter that IEventListener has been listening for.
	 */
	public void removeEventListener(EventEmitter emitter,
			IEventListener listener) {
		
		HashMap<Class<Event>, ArrayList<IEventListener>> types = events
				.get(emitter);
		Iterator<ArrayList<IEventListener>> it = types.values().iterator();
		while (it.hasNext()) {
			ArrayList<IEventListener> listeners = it.next();
			for (int i = 0; i < listeners.size(); ++i) {
				IEventListener working = listeners.get(i);
				if (working.equals(listener)) {
					listeners.remove(i);
				}
			}
		}
	}
	
	
	
	/**
	 * Dispatches the event to all listeners
	 *
	 * @param emitter Frankly, I'm not sure. I need to talk to catron about this.
	 * @param type Same case.
	 */
	public void emit(EventEmitter emitter, Event type) {
		HashMap<Class<Event>, ArrayList<IEventListener>> emit = getEmitter(emitter);
		ArrayList<IEventListener> listeners = getListeners(emit, type);
		for (IEventListener i : listeners) {
			i.eventDispatched(emitter, type);
		}
	}
	
	private HashMap<Class<Event>, ArrayList<IEventListener>> getEmitter(
			EventEmitter e) {
		HashMap<Class<Event>, ArrayList<IEventListener>> emit = events.get(e);
		if (emit == null) {
			emit = new HashMap<Class<Event>, ArrayList<IEventListener>>();
			events.put(e, emit);
		}
		return emit;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<IEventListener> getListeners(
			HashMap<Class<Event>, ArrayList<IEventListener>> e, Event type) {
		ArrayList<IEventListener> listeners = e.get(type.getClass());
		if (listeners == null) {
			listeners = new ArrayList<IEventListener>();
			e.put((Class<Event>) type.getClass(), listeners);
		}
		return listeners;
	}
}

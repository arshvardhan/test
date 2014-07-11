/**
 * EventManager
 * © DbyDx Software ltd. @2011 - 2011+1
 * Confidential and proprietary.
 */
package com.kelltontech.framework.EventHandler;

import java.util.Random;
import java.util.Vector;

import com.kelltontech.framework.model.Request;
import com.kelltontech.framework.model.Response;

public class EventManager {

	private int mTypeId;
	private static Vector<Client> mClients = new Vector<Client>();

	/**
	 * Class Constructor
	 */
	public EventManager() {
		mTypeId = new Random().nextInt(Integer.MAX_VALUE);
	}

	/**
	 * Called to handle the event. Default implementation does nothing.
	 * 
	 * @param int action
	 * @param int type
	 * @param AmcBuzzResponse
	 *            containing
	 *            <ul>
	 *            <li>EventArray for ICalendar data</li>
	 *            <li>NewsArray for RSS or Atom data</li>
	 *            <li>DictionaryArray for PList data</li>
	 *            <li>DirectoryArray for VCard data String for String, HTML, or
	 *            Base64 data</li>
	 *            <li>byte[] for Binary data</li>
	 *            </ul>
	 */
	protected void handle(int action, int type, Response payload) {
		payload = null;
	}

	protected void handle(int action, int type, Request payload) {
		payload = null;
	}

	/**
	 * Register to receive an event
	 */
	public void register(int action) {
		add(action, EventManager.this);
	}

	/**
	 * Unregister to receive an event
	 */
	public void unregister(int action) {
		remove(action, EventManager.this);
	}

	/**
	 * Unregister all events.
	 */
	public void unregister() {
		remove(EventManager.this);
	}

	/**
	 * Raise notification that an event occurred
	 */
	public void raise(int action, int type, Response payload) {
		send(action, type, payload);
	}

	/**
	 * Raise notification that an event occurred
	 */
	public void raise(int action, int type, Request payload) {
		payload.setRequestId(type);
		send(action, type, payload);
	}

	/**
	 * Raise notification that an event occurred
	 */
	public int raise(int action, Request payload) {
		int type = mTypeId++;
		payload.setRequestId(type);
		send(action, type, payload);
		return type;
	}

	/**
	 * Raise notification that an event occurred
	 */
	public int raise(int action) {
		int type = mTypeId++;
		Request r = new Request();
		r.setRequestId(type);
		send(action, type, r);
		return type;
	}

	/**
	 *
	 */
	private void send(final int action, final int type, final Request req) {
		for (int i = 0; i < mClients.size(); i++) {
			final Client c = (Client) mClients.elementAt(i);
			if (action == c.action) {
				try {
					c.client.handle(action, type, req);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
	}

	/**
	 *
	 */
	private void send(int action, int type, Response resp) {
		for (int i = 0; i < mClients.size(); i++) {
			Client c = (Client) mClients.elementAt(i);
			if (action == c.action) {
				try {
					c.client.handle(action, type, resp);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
	}

	private Client contains(int action, EventManager client) {
		Client rc = null;
		for (int i = 0; i < mClients.size(); i++) {
			Client c = (Client) mClients.elementAt(i);
			if (action == c.action && client.equals(c.client)) {
				rc = c;
				break;
			}
		}
		return rc;
	}

	private void add(int action, EventManager client) {
		Client c = contains(action, client);
		if (c == null) {
			mClients.addElement(new Client(action, EventManager.this));
		} else {
			c.incrementUseCount();
		}
	}

	private void remove(int action, EventManager client) {
		Client c = contains(action, client);
		if (c != null) {
			if (c.useCount <= 0) {
				mClients.removeElement(c);
			} else {
				c.decrementUseCount();
			}
		}
	}

	private void remove(EventManager client) {
		for (int i = 0; i < mClients.size(); i++) {
			Client c = (Client) mClients.elementAt(i);
			if (client.equals(c.client)) {
				mClients.removeElementAt(i);
			}
		}
	}

	private static class Client {
		int useCount;
		int action;
		EventManager client;

		public Client(int a, EventManager c) {
			action = a;
			client = c;
		}

		public void incrementUseCount() {
			useCount++;
		}

		public void decrementUseCount() {
			useCount--;
		}
	}
}

package com.jyq.qs.http;

public interface IRequestListener {

	/**
	 * notify
	 */
	public void notify(String action, String resultCode, String resultMsg, Object obj);
}

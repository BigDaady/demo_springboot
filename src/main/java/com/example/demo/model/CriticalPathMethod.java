package com.example.demo.model;

public class CriticalPathMethod {
	
	public CriticalPathMethod(String path, Integer duration) {
		super();
		this.path = path;
		this.duration = duration;
	}
	private String path;
	/**
	 * @return the path
	 */
	public synchronized String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public synchronized void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the duration
	 */
	public synchronized Integer getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public synchronized void setDuration(Integer duration) {
		this.duration = duration;
	}
	private Integer duration; 

}

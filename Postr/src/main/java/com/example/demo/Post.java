package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Post {
	@Id
	@GeneratedValue
	private Long id;

	private String posterName;

	private String postMessage;

	protected Post() {
	}

	public Post(String name, String message) {
		this.posterName = name;
		this.postMessage = message;
	}

	public Long getId() {
		return id;
	}

	public String getPosterName() {
		return posterName;
	}

	public void setPosterName(String posterName) {
		this.posterName = posterName;
	}

	public String getPostMessage() {
		return postMessage;
	}

	public void setPostMessage(String postMessage) {
		this.postMessage = postMessage;
	}

	@Override
	public String toString() {
		return String.format("Post[id=%d, posterName='%s', postMessage='%s']", id,
				posterName, postMessage);
	}
}
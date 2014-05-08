package org.mamute.model;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.joda.time.DateTime;
import org.mamute.providers.SessionFactoryCreator;

import javax.persistence.*;

@Entity
@Audited
public class UserSession {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Index(name="session_key")
	@Column(unique=true)
	private String sessionKey;
	
	@ManyToOne
	private User user;
	
	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt = new DateTime();
	
	/**
	 * @deprecated
	 */
	UserSession() {
	}

	public UserSession(User user, String sessionKey) {
		this.user = user;
		this.sessionKey = sessionKey;
	}

	public String getSessionKey() {
		return sessionKey;
	}
	
	public User getUser() {
		return user;
	}
}

package org.mamute.model;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Audited
public class Flag {

	@Id
	@GeneratedValue
	private Long id;
	
	@Type(type = "text")
	private String reason;
	
	@ManyToOne
	private User author;
	
	@Enumerated(EnumType.STRING)
	private FlagType type;

	/**
	 * @deprecated
	 */
	Flag() {
	}

	public Flag(FlagType flagType, User author) {
		this.type = flagType;
		this.author = author;
	}
	
	
	public void setReason(String reason) {
		if (!type.equals(FlagType.OTHER)) {
			throw new IllegalStateException("only " + FlagType.OTHER + "should have a reason");
		}
		this.reason = reason;
	}

	public boolean createdBy(User user) {
		return user.getId().equals(author.getId());
	}
	
	public String getReason() {
		return reason;
	}
	
	public User getAuthor() {
		return author;
	}
	
	public FlagType getType() {
		return type;
	}
	
}

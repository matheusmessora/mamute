package org.mamute.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.mamute.providers.SessionFactoryCreator;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY, region="cache")
@Entity
@Audited
public class Tag {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique = true, nullable = false)
	@NotEmpty
	private String name;
	
	private String description;
	
	@Type(type = SessionFactoryCreator.JODA_TIME_TYPE)
	private final DateTime createdAt = new DateTime();
	
	@ManyToOne
	private final User author;

	private Long usageCount = 0l;
	
	/**
	 * @deprecated hibernate eyes only
	 */
	public Tag() {
		this("", "", null);
	}
	
	public Tag(String name, String description, User author) {
		this.name = name;
		this.description = description;
		this.author = author;
	}

	public String getName() {
		return name;
	}
	
	public String getUriName() {
		try {
			return URLEncoder.encode(name, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getDescription() {
		return description;
	}

	public Long getId() {
		return id;
	}
	
	public Long getUsageCount() {
		return usageCount;
	}
	
	public void incrementUsage() {
		this.usageCount ++;
	}
	
	public void decrementUsage(){
		this.usageCount --;
	}
	
}

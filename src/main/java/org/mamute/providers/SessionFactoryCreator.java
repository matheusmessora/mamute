package org.mamute.providers;

import br.com.caelum.vraptor.environment.Environment;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.mamute.model.*;
import org.mamute.model.watch.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.validation.ValidatorFactory;
import java.net.URL;
import java.util.Map;

@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class SessionFactoryCreator {
	
	public static final String JODA_TIME_TYPE= "org.jadira.usertype.dateandtime.joda.PersistentDateTime";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SessionFactoryCreator.class);
	
	private Configuration cfg;
	private SessionFactory factory;
	private Environment env;
	private ValidatorFactory vf;
    private static ServiceRegistry serviceRegistry;

	@Deprecated
	public SessionFactoryCreator() {
	}

	@Inject
	public SessionFactoryCreator(Environment env, ValidatorFactory vf) {
		this.env = env;
		this.vf = vf;
	}

	@PostConstruct
	public void init() {
		URL xml = env.getResource("/hibernate.cfg.xml");
		LOGGER.info("Loading hibernate xml from " + xml);
		this.cfg = new Configuration().configure(xml);
		
		if (this.vf != null) {
			Map<Object, Object> properties = cfg.getProperties();
			properties.put("javax.persistence.validation.factory", this.vf);
		}
		
		String url = System.getenv("JDBC_URL");
		if (url != null) {
			String user = System.getenv("USER");
			String password = System.getenv("PASSWORD");
			
			LOGGER.info("reading database config from environment: " + url);
			cfg.setProperty("hibernate.connection.url", url);
			cfg.setProperty("hibernate.connection.username", user);
			cfg.setProperty("hibernate.connection.password", password);
		}



		cfg.addAnnotatedClass(User.class);
		cfg.addAnnotatedClass(Question.class);
		cfg.addAnnotatedClass(AnswerInformation.class);
		cfg.addAnnotatedClass(Answer.class);
		cfg.addAnnotatedClass(Tag.class);
		cfg.addAnnotatedClass(Vote.class);
		cfg.addAnnotatedClass(Comment.class);
		cfg.addAnnotatedClass(QuestionInformation.class);
		cfg.addAnnotatedClass(Flag.class);
		cfg.addAnnotatedClass(LoginMethod.class);
		cfg.addAnnotatedClass(UserSession.class);
		cfg.addAnnotatedClass(Watcher.class);
		cfg.addAnnotatedClass(ReputationEvent.class);
		cfg.addAnnotatedClass(News.class);
		cfg.addAnnotatedClass(NewsInformation.class);
		cfg.addAnnotatedClass(NewsletterSentLog.class);
		cfg.addAnnotatedClass(TagPage.class);

      // Hibernate Envers
      cfg.setProperty("hibernate.hbm2ddl.auto", "create-drop");

      cfg.setProperty("hibernate.ejb.event.post-insert",
          "org.hibernate.ejb.event.EJB3PostInsertEventListener,org.hibernate.envers.event.AuditEventListener");

      cfg.setProperty("hibernate.ejb.event.post-update",
          "org.hibernate.ejb.event.EJB3PostUpdateEventListener,org.hibernate.envers.event.AuditEventListener");

      cfg.setProperty("hibernate.ejb.event.post-delete",
          "org.hibernate.ejb.event.EJB3PostDeleteEventListener,org.hibernate.envers.event.AuditEventListener");

      cfg.setProperty("hibernate.ejb.event.pre-collection-update",
          "org.hibernate.envers.event.AuditEventListener");

      cfg.setProperty("hibernate.ejb.event.pre-collection-remove",
          "org.hibernate.envers.event.AuditEventListener");

      cfg.setProperty("hibernate.ejb.event.post-collection-recreate",
          "org.hibernate.envers.event.AuditEventListener");

      serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
      this.factory = cfg.buildSessionFactory(serviceRegistry);
//      		this.factory = cfg.buildSessionFactory();
		
	}

	@Produces
	@javax.enterprise.context.ApplicationScoped
	public SessionFactory getInstance() {
		return factory;
	}

	void destroy(@Disposes SessionFactory factory) {
		if (!factory.isClosed()) {
			factory.close();
		}
		factory = null;
	}

	public void dropAndCreate() {
		destroy(this.factory);
		new SchemaExport(cfg).drop(true, true);
		new SchemaExport(cfg).create(true, true);
		init();
	}
	public void drop() {
		factory.close();
		factory = null;
		new SchemaExport(cfg).drop(true, true);
		init();
	}
	
	public Configuration getCfg() {
		return cfg;
	}
	
}

package org.mamute.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mamute.builder.QuestionBuilder;
import org.mamute.dao.TestCase;
import org.mamute.model.LoggedUser;
import org.mamute.model.Question;
import org.mamute.model.QuestionInformation;
import org.mamute.model.UpdateStatus;
import org.mamute.model.Updater;
import org.mamute.model.User;

public class UpdaterTest extends TestCase{
    
    private User author;
    private Question myQuestion;
    private Updater updater = new Updater();
    private Long id = 0l;
    QuestionBuilder question = new QuestionBuilder();

    @Before
    public void before_test() {
        author = user("author", "author@gmail", nextId());
        myQuestion = question.withTitle("titel").withDescription("description").withAuthor(author).build();
    }

    @Test
    public void should_be_pending_if_is_not_author() {
        User user = user("chico", "chico@gmail.com", nextId());
        QuestionInformation newInformation = new QuestionInformation("title", "description", new LoggedUser(user, null));
        
		UpdateStatus update = updater.update(myQuestion, newInformation);
        
        assertEquals(UpdateStatus.PENDING, update);
        assertTrue(myQuestion.getHistory().contains(newInformation));
    }
    
    @Test
    public void should_update_if_is_moderator() {
        User moderator = user("moderator", "moderator@brutal", nextId()).asModerator();
        QuestionInformation newInformation = new QuestionInformation("title", "description", new LoggedUser(moderator, null));
        UpdateStatus update = updater.update(myQuestion, newInformation);
        
        assertEquals(UpdateStatus.NO_NEED_TO_APPROVE, update);
        assertTrue(myQuestion.getHistory().contains(newInformation));
    }
    
    @Test
    public void should_update_if_is_the_author() {
        QuestionInformation newInformation = new QuestionInformation("title", "description", new LoggedUser(author, null));
		UpdateStatus update = updater.update(myQuestion, newInformation);
        
        assertEquals(UpdateStatus.NO_NEED_TO_APPROVE, update);
        assertTrue(myQuestion.getHistory().contains(newInformation));
    }
    
    private Long nextId() {
        return id++;
    }

}

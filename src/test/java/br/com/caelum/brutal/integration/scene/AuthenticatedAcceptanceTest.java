package br.com.caelum.brutal.integration.scene;

import org.junit.After;


public abstract class AuthenticatedAcceptanceTest extends AcceptanceTestBase {
    
    protected void login() {
        home().toLoginPage().login("acceptance@caelum.com.br", "123456");
    }
    
    @After
    public final void logout() {
        if (home().isLoggedIn()) {
        	home().logOut();
        }
    }
    
    protected void loginRandomly() {
		logout();
		home().toSignUpPage()
            .signUp("acceptance test user", 
        		"acceptance"+ Math.random() +"@brutal.com", 
        		"123456", "123456");
    }
    
    protected void loginWithALotOfKarma() {
    	logout();
    	home().toLoginPage().login("karma.nigga@caelum.com.br", "123456");
    }
    
    protected void loginAsModerator() {
        logout();
        home().toLoginPage().login("moderator@caelum.com.br", "123456");
    }

}

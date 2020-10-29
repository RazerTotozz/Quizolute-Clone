/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.seeder;

import com.quizolute.jpa.RoomsJpaController;
import com.quizolute.jpa.exceptions.RollbackFailureException;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Totozz
 */
public class DatabaseSeederRunner {
    
    private final UsersSeeder userSeeder;
    private final QuestionSetsSeeder questionSetSeeder;
    private final RoomsSeeder roomSeeder;
    private final QuestionsSeeder questionSeeder;
    private final ChoicesSeeder choicesSeeder;
    private final UserInRoomsSeeder userInRoomSeeder;
    private final UserAnswersSeeder userAnswerSeeder;

    public DatabaseSeederRunner(EntityManagerFactory emf, UserTransaction utx) {
        this.userSeeder = new UsersSeeder(utx, emf);
        this.questionSetSeeder = new QuestionSetsSeeder(utx, emf);
        this.roomSeeder = new RoomsSeeder(utx, emf);
        this.questionSeeder = new QuestionsSeeder(utx, emf);
        this.choicesSeeder = new ChoicesSeeder(utx, emf);
        this.userInRoomSeeder = new UserInRoomsSeeder(utx, emf);
        this.userAnswerSeeder = new UserAnswersSeeder(utx, emf);
        
    }

    public void run() throws Exception {
        
        this.userSeeder.doSeeder();
        this.questionSetSeeder.doSeeder();
        this.roomSeeder.doSeeder();
        this.questionSeeder.doSeeder();
        this.choicesSeeder.doSeeder();
        this.userInRoomSeeder.doSeeder();
        
        
    }
    
    public void refresh() throws RollbackFailureException, Exception {
        this.userInRoomSeeder.refresh();
        this.choicesSeeder.refresh();
        this.questionSeeder.refresh();
        this.roomSeeder.refresh();
        this.questionSetSeeder.refresh();
        this.userSeeder.refresh();
        
        run();
    }
    
    public void makeScore() throws RollbackFailureException, Exception {
        this.userAnswerSeeder.doSeeder();
    }
    
    public void deleteScore() throws RollbackFailureException, Exception {
        this.userAnswerSeeder.refresh();
    }
}

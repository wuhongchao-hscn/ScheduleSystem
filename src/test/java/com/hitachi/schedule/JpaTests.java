package com.hitachi.schedule;

import com.hitachi.schedule.service.GSABSScheduleF;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaTests {
    @Autowired
    private GSABSScheduleF gsabsScheduleF;


    @Test
    @Transactional
    public void testDoubleDataSource() {
        // agreeParam  stat       articleAgree  agreeTbl
        // 1           ●〇⇒〇〇   -1            D
        // 2           ●〇⇒〇●   -1            U
        // 3           〇●⇒●〇   +1            U
        // 4           〇●⇒〇〇   ×             D
        // 5           〇〇⇒●〇   +1            I
        // 6           〇〇⇒〇●   ×             I
        System.out.println(gsabsScheduleF.updateArticleAgree("1", 2, 1));

        System.out.println(gsabsScheduleF.updateArticleAgree("1", 2, 5));
        System.out.println(gsabsScheduleF.updateArticleAgree("1", 2, 1));
        System.out.println(gsabsScheduleF.updateArticleAgree("1", 2, 6));
        System.out.println(gsabsScheduleF.updateArticleAgree("1", 2, 3));
        System.out.println(gsabsScheduleF.updateArticleAgree("1", 2, 2));
        System.out.println(gsabsScheduleF.updateArticleAgree("1", 2, 4));
    }

}
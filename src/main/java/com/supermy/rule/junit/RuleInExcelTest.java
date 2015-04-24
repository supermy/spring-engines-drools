package com.supermy.rule.junit;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.*;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class RuleInExcelTest {

	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
        execlDirectTest();
        execlSpringTest();
	}

    @Test
    public static void execlDirectTest() {

        DecisionTableConfiguration dtableconfiguration = KnowledgeBuilderFactory
				.newDecisionTableConfiguration();
		dtableconfiguration.setInputType(DecisionTableInputType.XLS);
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory
				.newClassPathResource("rules/RuleInExcel.xls",
						RuleInExcelTest.class),
				ResourceType.DTABLE, dtableconfiguration);

		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        ksession.fireAllRules();
        ksession.dispose();
    }
    @Test
    public static void execlSpringTest() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("drools/spring-drools.xml");
        StatefulKnowledgeSession ksession = (StatefulKnowledgeSession) context.getBean( "ksession-excel" );
        ksession.fireAllRules();
        ksession.dispose();
    }

}
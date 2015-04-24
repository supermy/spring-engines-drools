package com.supermy.rule.junit;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.*;
import org.drools.definition.KnowledgePackage;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Collection;

public class MessagesTest {


	@Test
	public void test() {
		try {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("drools/spring-drools.xml");
        StatefulKnowledgeSession ksession = (StatefulKnowledgeSession) context.getBean( "kbase1" );

			KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory
					.newFileLogger(ksession, "test");
			// go !
			Message message = new Message();
			message.setMessage("Hello World");
			message.setStatus(Message.HELLO);
			ksession.insert(message);
			ksession.fireAllRules();
			ksession.dispose();  
			logger.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static class Message {

		public static final int HELLO = 0;
		public static final int GOODBYE = 1;

		private String message;

		private int status;

		public String getMessage() {
			return this.message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public int getStatus() {
			return this.status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

	}

	private static void compile(final String srcFile, final String destFile)
			throws IOException {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();

		// Resource newClassPathResource =
		// ResourceFactory.newClassPathResource("Message.drl");

		URL src = MessagesTest.class.getResource(srcFile);// FIXME

		Resource r = ResourceFactory.newInputStreamResource(src.openStream());

		kbuilder.add(r, ResourceType.DRL);
		if (kbuilder.hasErrors()) {
			throw new IllegalStateException("Can not initialize Drools: "
					+ kbuilder.getErrors().toString());
		}
		Collection<KnowledgePackage> kpackages = kbuilder
				.getKnowledgePackages();

		File dest = new File(destFile);
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
				dest));
		out.writeObject(kpackages);
		out.close();
	}
	
	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("rules/Message.drl"),
				ResourceType.DRL);
		// kbuilder.add(ResourceFactory.newUrlResource("http://localhost:8080/drools-guvnor/org.drools.guvnor.Guvnor/package/com.chen.rules/LATEST"),
		// ResourceType.PKG);

		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

		// 采用Agent的方式
		/*
		 * KnowledgeAgent
		 * kagent=KnowledgeAgentFactory.newKnowledgeAgent("/deploy.properties");
		 * KnowledgeBase kbase=kagent.getKnowledgeBase();
		 */

		return kbase;
	}

	public static final void main(String[] args) {
		try {
			// load up the knowledge base
			// String
			// f="D:\\workspace\\bonc-drools-my\\src\\main\\rules\\Message.drl";
			// compile(f,"Sample.drlc");

			KnowledgeBase kbase = readKnowledgeBase();
			StatefulKnowledgeSession ksession1 = kbase
					.newStatefulKnowledgeSession();

			KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory
					.newFileLogger(ksession1, "test");
			// go !
			Message message = new Message();
			message.setMessage("Hello World");
			message.setStatus(Message.HELLO);
			ksession1.insert(message);
			ksession1.fireAllRules();
			logger.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}

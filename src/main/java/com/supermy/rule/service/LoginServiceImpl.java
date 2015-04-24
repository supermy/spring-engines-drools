package com.supermy.rule.service;

import com.supermy.rule.pojo.Vip;
import org.drools.runtime.StatefulKnowledgeSession;

public class LoginServiceImpl {
        private Vip vip;

		public Vip getVip() {
			return vip;
		}

		public void setVip(Vip vip) {
			this.vip = vip;
		}
           
		public void checkLogin(StatefulKnowledgeSession kstateless ){
			System.out.println("s");
			kstateless.insert(vip);
			kstateless.fireAllRules();
			kstateless.dispose();
			System.out.println("e");
		}
         
		public static boolean checkDB(String name,String password){
			return name.trim().equals("jack")&&password.trim().equals("123");
		}
		
}

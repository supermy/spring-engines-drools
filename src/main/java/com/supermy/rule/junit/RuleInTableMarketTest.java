package com.supermy.rule.junit;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.*;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RunWith(Parameterized.class)
public class RuleInTableMarketTest {

        public void testExcel(String fileName, Map<String, Object> params)
                throws Exception {
            System.out.println("---------------begin------------------------");

            DecisionTableConfiguration dtableconfiguration = KnowledgeBuilderFactory
                    .newDecisionTableConfiguration();
            dtableconfiguration.setInputType(DecisionTableInputType.XLS);
            final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
                    .newKnowledgeBuilder();
            File file = new File( fileName);
            InputStream is = new FileInputStream(file);

            //InputStream is = new ClassPathResource(fileName).getInputStream();
            kbuilder.add(ResourceFactory.newInputStreamResource(is,"UTF-8"),
                    ResourceType.DTABLE);
            if (kbuilder.hasErrors()) {
                System.out.println(kbuilder.getErrors().toString());
            }
            Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();
            KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
            kbase.addKnowledgePackages(pkgs);

            StatelessKnowledgeSession ksession = kbase
                    .newStatelessKnowledgeSession();

            ksession.execute(Arrays.asList(new Object[] {params}));

            System.out.println("---------------end------------------------");
        }

        private int sum;
        private KADest param;
        private String extected;

        public RuleInTableMarketTest(KADest param,String extected) {
            this.param = param;
            this.extected = extected;
        }

        public static class KADest {
            private String orderTime;
            private String orderTotal;

            public KADest(String orderTime, String orderTotal) {
                this.orderTime = orderTime;
                this.orderTotal = orderTotal;
            }

            public Map<String,Object> getParams(){
                Map<String,Object> params = new HashMap<String,Object>();


                params.put("orderTime", orderTime);
                params.put("orderTotal", orderTotal);
                params.put("discountTotal", 0);

                return params;
            }
        }

        @Parameterized.Parameters
        public static Collection<?> contructData(){
            return Arrays.asList(new Object[][]{
                    {new KADest("2013-01-02 12:30:31","6"),"3"},
                    {new KADest("2013-01-02 12:30:31","1"),"0"},
                    {new KADest("2013-01-02 12:30:31","2"),"1"},
                    {new KADest("2013-01-01 12:30:31","2"),"1"},
                    {new KADest("2013-01-01 12:30:31","2"),"1"},
                    {new KADest("2013-01-01 12:30:31","2"),"1"},
                    {new KADest("2013-01-04 12:30:31","2"),"0"},
                    {new KADest("2013-01-04 12:30:31","2"),"0"},
                    {new KADest("2013-01-04 12:30:31","2"),"0"},
            });
        }

        @Test
        public void testKaDest() throws Exception{
            Map<String,Object> params = new HashMap<String,Object>();
            params.putAll(param.getParams());
            System.out.println("******params:"+params);

            testExcel("/Users/moyong/project/env-myopensource/1-spring/13-jamesmo/spring-engines-drools/src/main/resources/rules/Market.xls", params);

            //execlSpringTest(params);

            System.out.println("******result:"+params);

            Assert.assertEquals(extected, "" + params.get("discountTotal"));

        }


    //@Test
    public  void execlSpringTest(Map<String, Object> params) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("drools/spring-drools.xml");
        StatelessKnowledgeSession ksession = (StatelessKnowledgeSession) context.getBean( "ksession-market" );

        System.out.println(params);

        ksession.execute(Arrays.asList(new Object[]{params}));


    }

}
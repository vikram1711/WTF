/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core;

import org.wtf.core.WTFTestConfig;


/**
 * Base test account manager.
 * @author venkatesan.sundramurthy@gmail.com (Venkatesan Sundramurthy)
 */
public class WTFTestAccountManager {

  public static enum Accounts {
    MOTORS_TEST_ACCOUNT1("motors_qa_test_acc_1", "password", "motors_prod_test_acc_1",
        "testing123"),
    SRP_TEST_ACCOUNT1("usseller1","password","",""),
    UK_SRP_TEST_ACCOUNT1("keyword_uk","password","","");;

    private String qaUserName;
    private String qaPassword;
    private String prodUserName;
    private String prodPassword;

    private Accounts(String qaUserName, String qaPassword, String prodUserName,
        String prodPassword) {
      this.qaUserName = qaUserName;
      this.qaPassword = qaPassword;
      this.prodUserName = prodUserName;
      this.prodPassword = prodPassword;
    }

    public String getUserName() {
    	String server=WTFTestConfig.getServerURL();
    	return server. 
        	equals(WTFTestConfig.QA_BASE_SERVER_URL) ? qaUserName : prodUserName;
    }

    public String getPassword() {
    	String server=WTFTestConfig.getServerURL();
    	return server. 
    		equals(WTFTestConfig.QA_BASE_SERVER_URL) ? qaPassword : prodPassword;    }
  }
}

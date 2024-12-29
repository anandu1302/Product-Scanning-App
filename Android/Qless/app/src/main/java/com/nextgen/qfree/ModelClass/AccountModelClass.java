package com.nextgen.qfree.ModelClass;

public class AccountModelClass {

    String id;
    String accname;
    String accountno;
    String bank;
    String balance;

    public AccountModelClass(String id,String accname,String accountno,String bank,String balance) {
        this.id = id;
        this.accname = accname;
        this.accountno = accountno;
        this.bank = bank;
        this.balance = balance;
    }

    public String getId(){
        return id;
    }

    public String getAccname(){
        return accname;
    }

    public String getAccountno(){
        return accountno;
    }

    public String getBank(){
        return bank;
    }

    public String getBalance(){
        return balance;
    }
}

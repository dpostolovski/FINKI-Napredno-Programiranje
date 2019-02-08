import java.util.*;
import java.util.stream.Collectors;
import java.text.DecimalFormat;

class Account implements Cloneable
{
    private String name, balance;
    private Long id;
    
    public Account()
    {
        
    }
    
    public Account(String name, String balance)
    {
        this();
        Random rng=new Random();
        this.name=name;
        this.balance=balance;
        this.id=rng.nextLong();
    }
    
    public Object clone() 
        	throws CloneNotSupportedException
    {
        Account result=(Account)super.clone();
        result.name=this.name;
        result.id=this.id;
        result.balance=this.balance;
        
        return result;
    }
        				
    
    public String getBalance()
    {
        return this.balance;
    }
    
    public void setBalance(String balance)
    {
        this.balance=balance;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public long getId()
    {
        return this.id;
    }
    
    public String toString()
    {
        StringBuilder s=new StringBuilder();
        s.append("Name: ");
        s.append(this.name);
        s.append("\n");
        s.append("Balance: ");
        s.append(this.balance);
        s.append("\n");
        
        return s.toString();
    }
    
    public boolean equals(Object o)
    {
        if(o instanceof Account)
        {
            Account rhs=(Account)o;
            return((this.name.equals(rhs.name))&&(this.id==rhs.id)&&(this.balance.equals(rhs.balance)));
        }
        else 
            return false;
    }
    
    public int hashCode()
    {
        return this.name.hashCode()*this.id.hashCode()*this.balance.hashCode();
    }
    
}

abstract class Transaction
{
    protected  Long fromId, toId;
    protected  String description, amount;
    
    public Transaction()
    {
        
    }
    
    public Transaction(long fromId, long toId, String description, String amount)
    {
        this.fromId=fromId;
        this.toId=toId;
        this.description=description;
        this.amount=amount;
    }
    
    public long getFromId()
    {
        return this.fromId;
    }
    
    public long getToId()
    {
        return this.toId;
    }
    
    public String getAmount()
    {
        return this.amount;
    }
    
    public String getDescription()
    {
        return this.description;
    }
    
    public boolean equals(Object o)
    {
        if(o instanceof Transaction)
        {
          Transaction rhs=(Transaction)o;
          return((this.fromId==rhs.fromId)&&(this.toId==rhs.toId) && (this.description.equals(rhs.description) && (this.amount.equals(rhs.amount))));
        
        }
        else
            return false;
    }
               
    public int hashCode()
    {
        return this.fromId.hashCode()*this.toId.hashCode()*this.description.hashCode()*this.amount.hashCode();
    }
}

class FlatAmountProvisionTransaction extends Transaction
{
    private String flatAmount;
    
    public FlatAmountProvisionTransaction()
    {
        super();
    }
    
    public FlatAmountProvisionTransaction(long fromId, long toId, String amount, String flatProvision)
    {
        super(fromId, toId, "FlatAmount", amount);
        this.flatAmount=flatProvision;
    }
    
    public String getFlatAmount()
    {
        return this.flatAmount;
    }
    
    public boolean equals(Object o)
    {
        if(o instanceof FlatAmountProvisionTransaction)
        {
            FlatAmountProvisionTransaction rhs=(FlatAmountProvisionTransaction)o;
        	return ( super.equals(rhs) && (this.flatAmount.equals(rhs.flatAmount)));
    	}
        else 
            return false;
    }
    
    public int hashCode()
    {
        return super.hashCode()*flatAmount.hashCode();
    }
}

class FlatPercentProvisionTransaction extends Transaction
{
    private Integer percent;
    
    public FlatPercentProvisionTransaction()
    {
        super();
    }
    
    public FlatPercentProvisionTransaction(long fromId, long toId, String amount, int centsPerDolar)
    {
        super(fromId, toId, "FlatPercent", amount);
        this.percent=centsPerDolar;
    }
    
    public int getPercent()
    {
        return this.percent;
    }
    
    public boolean equals(Object o)
    {
        if(o instanceof FlatPercentProvisionTransaction)
        {
            FlatPercentProvisionTransaction rhs=(FlatPercentProvisionTransaction)o;
       		return(super.equals(rhs) && (this.percent==rhs.percent));
        }
        else
            return false;
    }
    
    public int hashCode()
    {
        return (super.hashCode()*this.percent.hashCode());
    }
}

class Bank
{
    private String name;
    private Account[] accounts;
    private Double transfered, provisions;
    
    public Bank()
    {
        transfered=0.0;
        provisions=0.0;
    }
    
    public Bank(String name, Account[] accounts)
    {
        this();
        this.name=name;
        this.accounts=new Account[accounts.length];
        for(int i=0; i<accounts.length; i++)
        {
            //this.accounts[i]=new Account();
            try
            {
            	this.accounts[i]=(Account)accounts[i].clone();
            }
            
            catch(CloneNotSupportedException e)
            {
                
            }
        }
    }
    
    public Account[] getAccounts()
    {
        return this.accounts;
    }
    
    public boolean makeTransaction(Transaction t)
    {
        Account from=null, to=null;
        for(int i=0; i<accounts.length; i++)
        {
            if(accounts[i].getId()==t.getFromId())
            {
                from=accounts[i];
            }
            
            if(accounts[i].getId()==t.getToId())
            {
                to=accounts[i];
            }
                
        }
        
        if(to==null || from==null)
        {
            return false;
        }
        
        String tempFromBalance=from.getBalance();
        String tempToBalance=to.getBalance();
        String tempAmount=t.getAmount();
        Double fromBalance=Double.parseDouble(tempFromBalance.substring(0, tempFromBalance.length()-1));
        Double toBalance=Double.parseDouble(tempToBalance.substring(0, tempToBalance.length()-1));
        Double amount=Double.parseDouble(tempAmount.substring(0, tempAmount.length()-1));
        Double provision=0.0;
        
        if(t.getDescription().equals("FlatAmount"))
        {
            FlatAmountProvisionTransaction temp= (FlatAmountProvisionTransaction)t;
            String tempProvision=temp.getFlatAmount();
            provision=Double.parseDouble(tempProvision.substring(0, tempProvision.length()-1));
        }
        else if(t.getDescription().equals("FlatPercent"))
        {
            FlatPercentProvisionTransaction temp=(FlatPercentProvisionTransaction)t;
            int percent=temp.getPercent();
           	provision=Math.floor(amount)*(double)percent/100.0;
        }
        
        if(fromBalance<amount+provision)
        {
            return false;
        }
        
        else
        {
            if(from==to)
            {
            	    fromBalance-=provision;
            }
            
            else
            {
                fromBalance-=amount+provision;
            	toBalance+=amount;
            }
            
            transfered+=amount;
            provisions+=provision;
            
            StringBuilder newToBalance=new StringBuilder();
            StringBuilder newFromBalance=new StringBuilder();
            
            DecimalFormat formatter=new DecimalFormat("#0.00");
            
            newToBalance.append(formatter.format(toBalance));
            newToBalance.append("$");
            to.setBalance(newToBalance.toString());
            
            newFromBalance.append(formatter.format(fromBalance));
            newFromBalance.append("$");
            from.setBalance(newFromBalance.toString());
            
            
            return true;
            
        }
    }
    
    public String totalTransfers()
    {
        DecimalFormat formatter=new DecimalFormat("#0.00");
        StringBuilder total=new StringBuilder();
        total.append(formatter.format(transfered));
        total.append("$");
        return total.toString();
    }
    
    public String totalProvision()
    {
        DecimalFormat formatter=new DecimalFormat("#0.00");
        StringBuilder total=new StringBuilder();
        total.append(formatter.format(provisions));
        total.append("$");
        return total.toString();
    }
    
    public String toString()
    {
        StringBuilder bank=new StringBuilder();
        bank.append("Name: ");
        bank.append(this.name);
        bank.append("\n");
        bank.append("\n");
        for(int i=0; i<this.accounts.length; i++)
        {
            bank.append(this.accounts[i].toString());
        }
        
        return bank.toString();
    }
    
    public boolean equals(Object o)
    {
        if(o instanceof Bank && o!=null)
        {
            Bank rhs=(Bank)o;
           
            if(this.accounts.length!=rhs.accounts.length)
            {
               	/*System.out.println("DEBUG SIZE: ");
               	System.out.print(this.accounts.length);
                System.out.print(" ");
                System.out.println(rhs.accounts.length);*/
               	return false;
            }
            
            else
            {
                for(int i=0; i<this.accounts.length; i++)
                {
                    if(!this.accounts[i].equals(rhs.accounts[i]))
                        
                    {
                        /*System.out.println(i);
                        System.out.println(this.accounts[i].toString());
                        System.out.println(rhs.accounts[i].toString());
                        */
                        return false;
                    }
                }
            }
            
            //System.out.println("DEBUG");
            
            /*System.out.println(this.name.equals(rhs.name));
            System.out.println(this.transfered==rhs.transfered);
            System.out.println(this.provisions==rhs.provisions);
            */
            return((this.name.equals(rhs.name)));// && (this.transfered==rhs.transfered) && (this.provisions==rhs.provisions));
        }
        else
            return false;
    }
    
    public int hashCode()
    {
        int hash=1;
        hash*=this.name.hashCode()*this.transfered.hashCode()*this.provisions.hashCode();
        for(int i=0; i<this.accounts.length; i++)
        {
            hash*=accounts[i].hashCode();
        }
        
        return hash;
    }
}


public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1)&&!a1.equals(a2)&&!a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            /*System.out.println("DEBUG START");
            System.out.println(b1.equals(b1));
            System.out.println(!b1.equals(null));
            System.out.println(!b1.equals(b2));
            System.out.println(!b2.equals(b1));
            System.out.println(!b1.equals(b3));
            System.out.println(!b3.equals(b1));
            System.out.println(!b1.equals(b4));
            System.out.println(b1.equals(b5));*/
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }


}
